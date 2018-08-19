package sunny;

 	public enum WSConstants
	{
		ENDPOINT_URL("http://localhost:8085/Web-service");
 		
 		private String val;
 		
 		WSConstants(String val) {
 			
 			this.val = val;
 		}
 		
 		public String value()
 		{
 			return val;
 		}
 		
	}

 	