package grouptabs.backend.resource;

import grouptabs.backend.dao.TabDAO;
import grouptabs.backend.representation.Tab;
import grouptabs.backend.representation.TabUser;
import grouptabs.backend.representation.Transaction;
import grouptabs.backend.representation.TransactionContribution;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.skife.jdbi.v2.DBI;

@Path("/tabs")
@Produces(MediaType.APPLICATION_JSON)
public class TabResource {
	
	private final TabDAO tabDao;

	public TabResource(DBI jdbi) {
		tabDao = jdbi.onDemand(TabDAO.class);
	}
	
	@GET
	public Response getTabs(@Context HttpServletResponse response) {
		List<Tab> tabs = tabDao.getTabs();
		return Response.ok(tabs).build();
	}
	
	@GET
	@Path("/{tabId}")
	public Response getTab(@PathParam("tabId") Integer tabId) {
		Tab tab = tabDao.getTab(tabId);
		
		List<TabUser> tabUsers = tabDao.getTabUsers(tabId);
		Map<Integer, String> userMap = new TreeMap<Integer, String>();
		for (TabUser tabUser : tabUsers) {
			userMap.put(tabUser.getUserId(), tabUser.getLocalName());
		}
		tab.setUsers(userMap);
		
		return Response.ok(tab).build();
	}
	
	@GET
	@Path("/{tabId}/transactions")
	public Response getLastTransactions(@PathParam("tabId") Integer tabId, @QueryParam("since") String since, @DefaultValue("20") @QueryParam("n") Integer n) {
		// TODO should this (additionally/alternatively) support parameters "from"/"to"?
		
		List<Transaction> transactions;
		if (since != null) {
			transactions = tabDao.getLastTransactionsForTab(tabId, since, n);
		}
		else {
			transactions = tabDao.getLastTransactionsForTab(tabId, n);
		}
		return Response.ok(transactions).build();
	}
	
	@POST
	@Path("/{tabId}/transactions")
	public Response writeTransaction(@PathParam("tabId") Integer tabId, Transaction transaction) throws URISyntaxException {
		if (tabId != transaction.getTabId()) {
			return Response.status(Status.BAD_REQUEST).entity("Tab ID must match URI").build();
		}
		
		tabDao.begin();
		
		// store transaction
		Long transactionId = tabDao.insertTransaction(transaction.getTabId(),
				transaction.getDate(), transaction.getTimestamp(),
				transaction.getDescription(), transaction.getType().toString());
		
		// store particiants and their expenses
		List<TransactionContribution> contributions = transaction.getParticipants();
		for (TransactionContribution contribution : contributions) {
			tabDao.insertTransactionParticipant(transactionId, contribution.getParticipant(), contribution.getAmount());
		}
		
		tabDao.commit();
		
		return Response.created(new URI(String.valueOf(transactionId))).type(MediaType.TEXT_PLAIN).build();
	}

	@GET
	@Path("/{tabId}/transactions/{transactionId}")
	public Response getTransaction(@PathParam("tabId") Integer tabId, @PathParam("transactionId") Long transactionId) {
		Transaction transaction = tabDao.getTransaction(transactionId);
		transaction.setParticipants(tabDao.getParticipantsForTransaction(transaction.getId()));
		return Response.ok(transaction).build();
	}
	
	@PUT
	@Path("/{tabId}/transactions/{transactionId}")
	public Response updateCategory(@PathParam("tabId") Integer tabId, @PathParam("transactionId") Long transactionId, Transaction transaction) {
		
		if (!tabDao.transactionExists(transactionId)) {
			return Response.status(Status.NOT_FOUND).entity("There is no transaction with ID " + transactionId).build();
		}
		else if (transactionId != transaction.getId()) {
			return Response.status(Status.BAD_REQUEST).entity("Transaction ID must match URI").build();
		}
		else if (tabId != transaction.getTabId()) {
			return Response.status(Status.BAD_REQUEST).entity("Tab ID must match URI").build();
		}
		
		// start database transaction (see http://skife.org/jdbi/java/library/sql/2011/03/16/jdbi-sql-objects.html)
		tabDao.begin();
		
		// update transaction
		tabDao.updateTransaction(transaction.getId(), transaction.getDate(), new Date(), transaction.getDescription(), transaction.getType().toString());
		
		// delete old participant records
		tabDao.deleteParticipantsForTransaction(transaction.getId());
		
		// store particiants and their expenses
		List<TransactionContribution> contributions = transaction.getParticipants();
		for (TransactionContribution contribution : contributions) {
			tabDao.insertTransactionParticipant(transactionId, contribution.getParticipant(), contribution.getAmount());
		}
		
		// commit database transaction
		tabDao.commit();
		
		return Response.ok(
				new Transaction(transaction.getId(), transaction.getTabId(), transaction.getDate(), transaction.getTimestamp(),
						transaction.getDescription(), contributions, transaction.getType())).build();
	}
	
	@DELETE
	@Path("/{tabId}/transactions/{transactionId}")
	public Response deleteTransaction(@PathParam("tabId") Integer tabId, @PathParam("transactionId") Long transactionId) {
		if (!tabDao.transactionExists(transactionId)) {
			return Response.status(Status.NOT_FOUND).entity("There is no transaction with ID " + transactionId).build();
		}
		tabDao.deleteTransaction(transactionId);
		return Response.noContent().build();
	}


}
