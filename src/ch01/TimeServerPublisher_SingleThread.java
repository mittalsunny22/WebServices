package ch01;

import javax.xml.ws.Endpoint;

public class TimeServerPublisher_SingleThread {    

	public static void main(String[ ] args) {     

		System.out.println("hello");

		// 1st argument is the publication URL      
		// 2nd argument is an SIB instance      
		
		Endpoint.publish("http://localhost:7676/Web-service", new TimeServerImpl()); 
		
		System.out.println("Service Publish");
	}
}