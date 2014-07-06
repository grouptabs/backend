package grouptabs.backend.representations;

import java.math.BigDecimal;

public class TransactionParticipant {
		
		private Integer userId;
		private BigDecimal amount;
		
		public TransactionParticipant() {
		}
		
		public Integer getUserId() {
			return userId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}
		

}
