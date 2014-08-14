package grouptabs.backend.representation;

import java.math.BigDecimal;

public class TransactionContribution {
		
		private String participant;
		private BigDecimal amount;
		
		public TransactionContribution() {
		}
		
		public String getParticipant() {
			return participant;
		}

		public void setParticipant(String participant) {
			this.participant = participant;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		

}
