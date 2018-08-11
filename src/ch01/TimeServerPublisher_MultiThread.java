package ch01;

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
		int port = 8090;      
		String url = "http://localhost/" + port;        
		endpoint.publish(url);        
		System.out.println("Publishing TimeServer on " + url);  
	} 
}   