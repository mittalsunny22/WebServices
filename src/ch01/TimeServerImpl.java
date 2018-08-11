package ch01;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.jws.WebService;

/** *  The @WebService property endpointInterface links the 
 * * SIB (this class) to the SEI (ch01.TimeServer). 
 * *  Note that the method implementations are not annotated as @WebMethods. 
 * */ 

@WebService(endpointInterface = "ch01.TimeServers") 
public class TimeServerImpl implements TimeServers {

	public String getTimeAsString()
	{
		return new Date().toString(); 
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
}