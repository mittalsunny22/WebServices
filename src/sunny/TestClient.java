package sunny;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class TestClient {

	public static void main(String[] args) throws Exception {  
		
		
		// URL updated
		URL url = null;
		try {
			url = new URL(WSConstants.ENDPOINT_URL.value());
		} catch (MalformedURLException e1) {
			System.out.println("Excepton :: "+ e1.getMessage());
		}
		
		System.out.println("Service Invoked at "+ WSConstants.ENDPOINT_URL.value());
		// Qualified name of the service:       
		//   1st arg is the service URI      
		//   2nd is the service name published in the WSDL       
		QName qname = new QName("http://sunny/", "TimeServerImplService");
		// Create, in effect, a factory for the service.      
		Service service = Service.create(url, qname);
		// Extract the endpoint interface, the service "port".       
		TimeServers eif = service.getPort(TimeServers.class);
		
//		writeObject(eif);
		
		System.out.println("Time in milliseconds ::"+eif.getTimeAsElapsed());
		System.out.println("Time in String ::"+eif.getTimeAsString());
		System.out.println(eif.getTimeAsCalender().getTime());
		
		Calendar  calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, 2);
		calendar.set(Calendar.MINUTE, 25);
		System.out.println("Time Diff ::"+eif.getTimeDiffereceWithCurrentDate(calendar));
		System.out.print("Enter the value to get next nth Fabonacci value >> ");
		int n = new Scanner(System.in).nextInt();
		System.out.println("Fab series for "+n +" is :: "+eif.countRabbits(n));
		}

	private static void writeObject(TimeServers eif) {

		try {
			String cwd = System.getProperty ("user.dir");
			String sep = System.getProperty ("file.separator");
			FileOutputStream fileOut = new FileOutputStream(cwd+sep+"TestClient.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(eif);
			out.close();
			fileOut.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		}
}