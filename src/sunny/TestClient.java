package sunny;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class TestClient {

	public static void main(String[] args) throws Exception {  
		
		System.out.println("here");
		
		// URL updated
		URL url = null;
		try {
			url = new URL(WSConstants.ENDPOINT_URL.value());
		} catch (MalformedURLException e1) {
			System.out.println("Excepton :: "+ e1.getMessage());
		}
		
		// Qualified name of the service:       
		//   1st arg is the service URI      
		//   2nd is the service name published in the WSDL       
		QName qname = new QName("http://sunny/", "TimeServerImplService");
		// Create, in effect, a factory for the service.      
		Service service = Service.create(url, qname);
		// Extract the endpoint interface, the service "port".       
		TimeServers eif = service.getPort(TimeServers.class);
		
		System.out.println("Time in milliseconds ::"+eif.getTimeAsElapsed());
		System.out.println("Time in String ::"+eif.getTimeAsString());
		System.out.println(eif.getTimeAsCalender());
		
		Calendar  calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 5);
		System.out.println("Time Diff ::"+eif.getTimeDiffereceWithCurrentDate(calendar));
		System.out.print("Enter the value to calculate Fab value >> ");
		int n = new Scanner(System.in).nextInt();
		System.out.println("Fab series for "+n +" is :: "+eif.countRabbits(n));
		}
}