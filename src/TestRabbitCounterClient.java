import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TestRabbitCounterClient {    
	
	private static final String url = "http://localhost:8080/rc/fib";

	public static void main(String[ ] args) {  
		new TestRabbitCounterClient().send_requests();  
	}

	private void send_requests() {     
		try {  
			HttpURLConnection conn = null;

			// POST request to create some Fibonacci numbers.
			List<Integer> nums = new ArrayList<Integer>();
			for (int i = 1; i < 10; i++)
				nums.add(i);

			String payload = URLEncoder.encode("nums", "UTF-8") + "=" + URLEncoder.encode(nums.toString(), "UTF-8");

			System.out.println("**************************Sending POST request along with payload data :: \n" + payload+"\n");
			
			// Send the request   
			conn = get_connection(url, "POST");
			conn.setRequestProperty("accept", "text/xml");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());   
			out.writeBytes(payload); 
			out.flush();   
			out.close();
			get_response(conn);

			System.out.println("*****************Sending GET request to check the inserted values using POST request*******************");
			conn = get_connection(url + "?num=8", "GET");    
			conn.addRequestProperty("accept", "text/plain");
			conn.connect();            
			get_response(conn);

			
			System.out.println("******************Sending DELETE request to delete entry*****************");
			// DELETE request
			conn = get_connection(url + "?num=8", "DELETE");
			conn.addRequestProperty("accept", "text/xml");
			conn.connect(); 
			get_response(conn);

			System.out.println("********************Sending GET request to check the deleted values using DELETE request************");
			// GET request to test whether DELETE worked
			conn = get_connection(url + "?num=8", "GET");    
			conn.addRequestProperty("accept", "text/html");     
			conn.connect();   
			get_response(conn);  
		}  
		catch(IOException e) { System.err.println(e); }   
		catch(NullPointerException e) { System.err.println(e); } 
	}


	private HttpURLConnection get_connection(String url_string, String verb) {     
		HttpURLConnection conn = null; 
		try {  
			URL url = new URL(url_string);   
			conn = (HttpURLConnection) url.openConnection();  
			conn.setRequestMethod(verb); 
			conn.setDoInput(true);  
			conn.setDoOutput(true);  
			
			System.out.println("Connected to "+ url_string);
		}  
		catch(MalformedURLException e) { System.err.println(e); }  
		catch(IOException e) { System.err.println(e); }    
		return conn;    
	} 

	private void get_response(HttpURLConnection conn) { 
		try {  
			String xml = "";  
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));   
			String next = null; 
			while ((next = reader.readLine()) != null)
				xml += next;

			System.out.println("The response after "+conn.getRequestMethod()+":\n" + xml);    
		} 
		catch(IOException e) { System.err.println(e); }
		finally {
			conn.disconnect();
			System.out.println(conn.getRequestMethod() + " Connection terminated.");
		}
	}
}