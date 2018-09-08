package sunnyREST;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TeamsClientCode {
	private static final String endpoint = "http://localhost:8087/Web-service/teams";

	private static final String POST_PARAMS = "Cargo=<create_team><teamName>Eclipse</teamName><employee><name>Dilbar</name><emailAddress>dsingh@qasource.com</emailAddress></employee><employee><name>Dilbarwqewqeqw Singh</name><emailAddress>dilsinwqeqwqweqwegh@qasource.com</emailAddress></employee></create_team>";

	public static void main(String[ ] args) {
		
		System.out.println("test client");
		new TeamsClientCode().send_requests();
	}

	private void send_requests() {
		try {
			
			putRequest();
			
			getRequest();
		
			deleteRequest();
			
			//postRequest();
					
		}
		catch(IOException e)
		{ 
			System.err.println(e);
		}        
		catch(NullPointerException e) { 
			System.err.println(e);
		}
	}

	private void sendPost() throws IOException {
		String urlParameters  = "Cargo=<create_team><teamName>Eclipse</teamName><employee><name>Dilbar</name><emailAddress>dsingh@qasource.com</emailAddress></employee><employee><name>Dilbarwqewqeqw Singh</name><emailAddress>dilsinwqeqwqweqwegh@qasource.com</emailAddress></employee></create_team>";
		byte[] postData       = urlParameters.getBytes("UTF-8");
		int    postDataLength = postData.length;
		URL    url            = new URL( endpoint );
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "POST" );
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty( "charset", "utf-8");
		conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		conn.setUseCaches( false );
		conn.getOutputStream().write(postData);
		
		System.out.println(conn.getResponseCode());
		
	}

	private void postRequest() throws IOException {
		HttpURLConnection conn = get_connection(endpoint, "POST");
		
		Map<String, String> params = new LinkedHashMap<String, String>();
	    params.put("Cargo", "<create_team><teamName>Eclipse</teamName><employee><name>Dilbar</name><emailAddress>dsingh@qasource.com</emailAddress></employee><employee><name>Dilbarwqewqeqw Singh</name><emailAddress>dilsinwqeqwqweqwegh@qasource.com</emailAddress></employee></create_team>");
	    StringBuilder postData = new StringBuilder();
	    for (Map.Entry<String, String> param : params.entrySet()) {
	        if (postData.length() != 0) postData.append('&');
	        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
	        postData.append('=');
	        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
	    }
		
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		
		conn.connect();
		conn.getOutputStream().write(postDataBytes);
		readServerResponse(conn);
	}
	
	

	private void deleteRequest() throws IOException {
		HttpURLConnection conn = get_connection(endpoint + "?name=Test12", "DELETE");
		conn.connect();
		readServerResponse(conn);
	}

	private void getRequest() throws IOException {
		HttpURLConnection conn = get_connection(endpoint + "?name=Test12", "GET");
		conn.connect();
		readServerResponse(conn);
		print_and_parse(conn, true);
	}

	private void readServerResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in = new BufferedReader(
	             new InputStreamReader(conn.getInputStream()));
	     String inputLine;
	     StringBuffer response = new StringBuffer();
	     while ((inputLine = in.readLine()) != null) {
	     	response.append(inputLine);
	     }
	     in.close();
	     conn.disconnect();
	     //print in String
	     System.out.println("Server Response :: " + response.toString());
	}

	private void putRequest() throws IOException {
		
		HttpURLConnection conn = get_connection(endpoint + "?name=Test1&new_name=Test12", "PUT");
		conn.connect();
		 readServerResponse(conn);
	}

	private HttpURLConnection get_connection(String url_string, String verb) {   
		HttpURLConnection conn = null; 
		try {   
			URL url = new URL(url_string);
			conn = (HttpURLConnection) url.openConnection();   
			
			if(verb.equalsIgnoreCase("PUT") || verb.equalsIgnoreCase("POST"))
				conn.setDoOutput(true);
			
			conn.setRequestMethod(verb); 
			
			System.out.println(conn.getResponseCode());
			
		}   
		catch(MalformedURLException e)
		{ System.err.println(e); }  
		catch(IOException e) 
		{ System.err.println(e); }        
		return conn; 
	}

	private void print_and_parse(HttpURLConnection conn, boolean parse) {  
		try {   
			String xml = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String next = null;    
			while ((next = reader.readLine()) != null)      
				xml += next;         
			System.out.println("The raw XML:\n" + xml);

			if (parse) {  
				SAXParser parser =SAXParserFactory.newInstance().newSAXParser();
				parser.parse(new ByteArrayInputStream(xml.getBytes()),new SaxParserHandler()); 
			}    
		}      
		catch(IOException e) { System.err.println(e); }    
		catch(ParserConfigurationException e) { System.err.println(e); }     
		catch(SAXException e) { System.err.println(e); }    }

	static class SaxParserHandler extends DefaultHandler {    
		char[ ] buffer = new char[1024];
		int n = 0;
		public void startElement(String uri, String lname,String qname, Attributes attributes) {  
			clear_buffer();   
		}
		public void characters(char[ ] data, int start, int length) {
	
			System.arraycopy(data, start, buffer, 0, length);
			n += length;
		}

		public void endElement(String uri, String lname, String qname) { 
			if (Character.isUpperCase(buffer[0]))
				System.out.println("New Buffer :: " +new String(buffer));

			clear_buffer();
		}

		private void clear_buffer() {  
			Arrays.fill(buffer, '\0');
			n = 0; 
		}
	}
}