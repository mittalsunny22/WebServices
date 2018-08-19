package sunny;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jws.WebService;

/** *  
 * SIB - Service EndPoint Interface
 * SEI - 
 * The @WebService property endpointInterface links the 
 * * SIB (this class) to the SEI (ch01.TimeServer). 
 * *  Note that the method implementations are not annotated as @WebMethods. 
 * */ 

@WebService(endpointInterface = "sunny.TimeServers") 
public class TimeServerImpl implements TimeServers {

	public String getTimeAsString()
	{
		return new Date().toString(); 
	}     

	public int countRabbits(int n) throws Exception  {    
		
		Map<Integer, Integer> cache = Collections.synchronizedMap(new HashMap<Integer, Integer>());
		

		// Throw a fault if n is negative.     
		if (n < 0) 
			throw new Exception("Neg. arg. not allowed."+ n + " < 0");

		// Easy cases.        
		else if (n < 2) 
			return n;
		else
		{
			int fib = 1, prev = 0;       
			for (int i = 2; i <= n; i++) {         
				int temp = fib;
				fib += prev;
				prev = temp;
			}
			cache.put(n, fib); // cache value for later lookup			
			return fib; 
		}
	}

	public long getTimeAsElapsed() 
	{ 
		return new Date().getTime(); 
	}

	public Calendar getTimeAsCalender() {
		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date());
		return instance;
	}

	public String getTimeDiffereceWithCurrentDate(Calendar calendar) {

		Date date = new Date();

		long timeDiff = Math.abs(date.getTime()-calendar.getTimeInMillis());

		return String.format("%d hour(s) %d min(s)", TimeUnit.MILLISECONDS.toHours(timeDiff), TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));       
	}

	@Override
	public String getName() {

		return getClass().getName();
	} 
}