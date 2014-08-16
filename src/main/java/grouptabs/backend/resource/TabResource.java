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
	
	
	// XXX needs authorization
	@GET
	@Path("/{tabKey}")
	public Response getTab(@PathParam("tabKey") String tabKey) {
		Tab tab = tabDao.getTab(tabKey);
		
		List<TabUser> tabUsers = tabDao.getTabUsers(tab.getId());
		Map<Integer, String> userMap = new TreeMap<Integer, String>();
		for (TabUser tabUser : tabUsers) {
			userMap.put(tabUser.getUserId(), tabUser.getLocalName());
		}
		tab.setUsers(userMap);
		
		return Response.ok(tab).build();
	}
	
	@GET
	@Path("/{tabKey}/transactions")
	public Response getLastTransactions(@PathParam("tabKey") String tabKey, @QueryParam("since") String since, @DefaultValue("20") @QueryParam("n") Integer n) {
		// TODO should this (additionally/alternatively) support parameters "from"/"to"?
		
		List<Transaction> transactions;
		if (since != null) {
			transactions = tabDao.getLastTransactionsForTab(tabKey, since, n);
		}
		else {
			transactions = tabDao.getLastTransactionsForTab(tabKey, n);
		}
		return Response.ok(transactions).build();
	}
	
	@POST
	@Path("/{tabKey}/transactions")
	public Response writeTransaction(@PathParam("tabKey") String tabKey, Transaction transaction) throws URISyntaxException {
		
		if (tabDao.getTab(tabKey).getId() != transaction.getTabId()) {
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
	@Path("/{tabKey}/transactions/{transactionId}")
	public Response getTransaction(@PathParam("tabKey") String tabKey, @PathParam("transactionId") Long transactionId) {
		Transaction transaction = tabDao.getTransaction(transactionId);
		transaction.setParticipants(tabDao.getParticipantsForTransaction(transaction.getId()));
		return Response.ok(transaction).build();
	}
	
	@PUT
	@Path("/{tabKey}/transactions/{transactionId}")
	public Response updateCategory(@PathParam("tabKey") String tabKey, @PathParam("transactionId") Long transactionId, Transaction transaction) {
		
		if (!tabDao.transactionExists(transactionId)) {
			return Response.status(Status.NOT_FOUND).entity("There is no transaction with ID " + transactionId).build();
		}
		else if (transactionId != transaction.getId()) {
			return Response.status(Status.BAD_REQUEST).entity("Transaction ID must match URI").build();
		}
		else if (tabDao.getTab(tabKey).getId() != transaction.getTabId()) {
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
	@Path("/{tabKey}/transactions/{transactionId}")
	public Response deleteTransaction(@PathParam("tabKey") String tabKey, @PathParam("transactionId") Long transactionId) {
		if (!tabDao.transactionExists(transactionId)) {
			return Response.status(Status.NOT_FOUND).entity("There is no transaction with ID " + transactionId).build();
		}
		tabDao.deleteTransaction(transactionId);
		return Response.noContent().build();
	}


}
