package sunny;

import javax.xml.ws.Endpoint;

public class TimeServerPublisher_SingleThread {    

	public static void main(String[ ] args) {     

		String address = WSConstants.ENDPOINT_URL.value();

		//Arguments as publication URL,SIB instance      
		Endpoint.publish(address, new TimeServerImpl()); 
		
		System.out.println("Service Published at \n"+address);
	}
}