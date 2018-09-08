package sunnyREST;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TeamsClientCode {
	private static final String endpoint = "http://localhost:8087/Web-service/teams";

	public static void main(String[ ] args) {
		
		System.out.println("test client");
		new TeamsClientCode().send_requests();
	}

	private void send_requests() {
		try {
			// GET requests
			HttpURLConnection conn = get_connection(endpoint + "?name=PSO", "GET");
			conn.connect();
			print_and_parse(conn, true);
		}
		catch(IOException e)
		{ 
			System.err.println(e);
		}        
		catch(NullPointerException e) { 
			System.err.println(e);
		}
	}

	private HttpURLConnection get_connection(String url_string, String verb) {   
		HttpURLConnection conn = null; 
		try {   
			URL url = new URL(url_string);
			conn = (HttpURLConnection) url.openConnection();   
			conn.setRequestMethod(verb); 
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