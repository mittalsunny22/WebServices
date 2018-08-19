package sunny;

import java.util.Calendar;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
  The annotation 
  @WebService signals that this is the SEI (Service Endpoint Interface). 
  @WebMethod signals that each method is a service operation. 
  @SOAPBinding annotation impacts the under-the-hood construction of the service contract, the WSDL 
(Web Services Definition Language) document. 
Style.RPC simplifies the contract and makes deployment easier.
  */

@WebService
@SOAPBinding(style = Style.RPC)

public interface TimeServers {

	@WebMethod
	String getTimeAsString();
	
	 @WebMethod
	int countRabbits(int n) throws Exception;
	 
	 @WebMethod
	 String getName();

	@WebMethod
	long getTimeAsElapsed();
	
	@WebMethod
	Calendar getTimeAsCalender();
	
	@WebMethod
	String getTimeDiffereceWithCurrentDate(Calendar calendar);
}
