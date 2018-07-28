package ch01;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class TestClient {

	public static void main(String[] args) throws MalformedURLException {

		URL url = new URL("http://localhost/8089?wsdl");
		// Qualified name of the service:       
		//   1st arg is the service URI      
		//   2nd is the service name published in the WSDL       
		QName qname = new QName("http://ch01/", "TimeServerImplService");
		// Create, in effect, a factory for the service.      
		Service service = Service.create(url, qname);
		
		Iterator<QName> ports = service.getPorts();
		while (ports.hasNext()) {
			QName qName2 = (QName) ports.next();
			
			System.out.println(qName2.getLocalPart());
			
		}	
		System.out.println(service.getPorts());
		// Extract the endpoint interface, the service "port".       
		TimeServers eif = service.getPort(TimeServers.class);
		
		System.out.println(eif.getTimeAsElapsed());
		System.out.println(eif.getTimeAsString());
		System.out.println(eif.getTimeAsCalender());
		
		Calendar  calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 5);
		System.out.println(eif.getTimeDiffereceWithCurrentDate(calendar));

	}
}