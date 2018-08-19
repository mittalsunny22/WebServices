package sunny;

import javax.xml.ws.Endpoint;

public class TimeServerPublisher_MultiThread { 

	private Endpoint endpoint;

	public static void main(String[ ] args) {        
		TimeServerPublisher_MultiThread self = new TimeServerPublisher_MultiThread();        
		self.create_endpoint();        
		self.configure_endpoint();        
		self.publish();    
		
		System.out.println("Publish");
	}    
 
	private void create_endpoint() {       
		endpoint = Endpoint.create(new TimeServerImpl());    
	}  

	private void configure_endpoint() {     
		endpoint.setExecutor(new MyThreadPool());    
	}

	private void publish() 
	{            
		endpoint.publish(WSConstants.ENDPOINT_URL.value());        
		System.out.println("Publishing TimeServer on " + WSConstants.ENDPOINT_URL.value());  
	} 
}   