package ch01;

import java.util.Calendar;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
  The annotation @WebService signals that this is the 
  SEI (Service Endpoint Interface). @WebMethod signals  
  that each method is a service operation. 
  The @SOAPBinding annotation impacts the under-the-hood 
  construction of the service contract, the WSDL 
(Web Services Definition Language) document. Style.RPC
  simplifies the contract and makes deployment easier.
  */

@WebService
public interface TimeServers {

	@WebMethod
	String getTimeAsString();

	@WebMethod
	long getTimeAsElapsed();
	
	@WebMethod
	Calendar getTimeAsCalender();
	
	@WebMethod
	String getTimeDiffereceWithCurrentDate(Calendar calendar);
}
