package sunnyREST;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//The class below is a WebServiceProvider rather than the more usual SOAP-based WebService. 
//The service implements the generic Provider interface rather than a customized SEI with designated @WebMethods. 
@WebServiceProvider

//There are two ServiceModes: PAYLOAD, the default, signals that the service wants access only to the underlying message payload (e.g., the 
// body of an HTTP POST request); MESSAGE signals that the service wants 
// access to entire message (e.g., the HTTP headers and body). 
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)

//The HTTP_BINDING as opposed, for instance, to a SOAP binding. 
@BindingType(value = HTTPBinding.HTTP_BINDING) 
public class RestfulTeams implements Provider<Source> 
{    
	@Resource    
	protected WebServiceContext ws_ctx;

	private Map<String, Team> team_map; // for easy lookups    
	private List<Team> teams;           // serialized/deserialized    
	private byte[ ] team_bytes;         // from the persistence file
	private static final String file_path = "E:\\AlpsTeam.ser";
	private static final String post_put_key = "Cargo";


	public RestfulTeams() {
		read_teams_from_file(); // read the raw bytes from teams.ser        
		deserialize();     
		// deserialize to a List<Team>    
	}
	// This method handles incoming requests and generates the response.    
	public Source invoke(Source request) {

		if (ws_ctx == null)
			throw new RuntimeException("DI Failed on ws_ctx.");

		// Grab the message context and extract the request verb.        
		MessageContext msg_ctx = ws_ctx.getMessageContext();        
		String http_verb = (String)msg_ctx.get(MessageContext.HTTP_REQUEST_METHOD);
		http_verb = http_verb.trim().toUpperCase();

		System.out.println("Request type :: " + http_verb);

		if (http_verb.equals("GET")) return doGet(msg_ctx);
		if (http_verb.equals("PUT")) return doPut(msg_ctx);
		if (http_verb.equals("POST")) return doPost(msg_ctx);
		if (http_verb.equals("DELETE")) return doDelete(msg_ctx);

		else throw new HTTPException(405); // method not allowed	    
	}

	private Source doDelete(MessageContext msg_ctx) {
		String query_string = (String) msg_ctx.get(MessageContext.QUERY_STRING);

		// Disallow the deletion of all teams at once.
		if (query_string == null)
			throw new HTTPException(403);     // illegal operation
		else {            
			String name = get_value_from_qs("name", query_string);
			if (!team_map.containsKey(name))
				throw new HTTPException(404); // not found

			// Remove team from Map and List, serialize to file.
			Team team = team_map.get(name);
			teams.remove(team);
			team_map.remove(name);
			serialize();

			// Send response.
			return response_to_client(name + " deleted.");
		} 
	}

	private Source doPost(MessageContext msg_ctx) {

		@SuppressWarnings("unchecked")
		Map<String, List<String>> request = (Map<String, List<String>>) msg_ctx.get(MessageContext.HTTP_REQUEST_HEADERS);

		List<String> cargo = request.get(post_put_key); 

		if (cargo == null) throw new HTTPException(400); // bad request

		String xml = "";
		for (String next : cargo)
		{
			xml += next.trim();

			System.out.println(xml);

		}
		ByteArrayInputStream xml_stream = new ByteArrayInputStream(xml.getBytes());
		String team_name = null;

		try {            

			// Set up the XPath object to search for the XML elements.
			DOMResult dom = new DOMResult();
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.transform(new StreamSource(xml_stream), dom);
			URI ns_URI = new URI("createTeam");
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			xp.setNamespaceContext(new NSResolver("", ns_URI.toString()));

			team_name = xp.evaluate("/createTeam/teamName", dom.getNode());

			System.out.println("Team Name to be add :: " + team_name);

			List<Employee> team_employees = new ArrayList<Employee>();

			NodeList team = (NodeList) xp.evaluate("//employee", dom.getNode(), XPathConstants.NODESET);

			System.out.println(team.getLength());

			for (int i = 0; i < team.getLength(); i++) {

				Node node = team.item(i);

				String name = xp.evaluate("name", node);

				String emailAddress = xp.evaluate("emailAddress", node);

				Employee employee = new Employee(name, emailAddress);
				team_employees.add(employee);
			}			

			// Add new team to the in-memory map and save List to file.
			Team t = new Team(team_name, team_employees);
			team_map.put(team_name, t);
			teams.add(t);
			serialize();
		}
		catch(URISyntaxException e) {
			System.err.println(e.getMessage());
			throw new HTTPException(500);   // internal server error        
		}
		catch(TransformerConfigurationException e) {
			System.err.println(e.getMessage());
			throw new HTTPException(500);   // internal server error        
		}        
		catch(TransformerException e) {
			System.err.println(e.getMessage());
			throw new HTTPException(500);   // internal server error
		}
		catch(XPathExpressionException e) {
			System.err.println(e.getMessage());
			throw new HTTPException(400);   // bad request
		}

		// Send a confirmation to requester.
		return response_to_client("Team " + team_name + " created.");
	}

	private Source response_to_client(String msg) {

		/*HttpResponse response = new HttpResponse();
		 response.setResponse(msg);*/
		ByteArrayInputStream stream = encode_to_stream(msg);
		return new StreamSource(stream); 
	}

	private void serialize() {
		try {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file_path));

			XMLEncoder enc = new XMLEncoder(out);
			enc.writeObject(teams);
			enc.close();
			out.close();
		}
		catch(IOException e) { System.err.println(e); }    
	}

	private Source doPut(MessageContext msg_ctx) {
		// Parse the query string. 
		String query_string = (String) msg_ctx.get(MessageContext.QUERY_STRING);
		String name = null;
		String new_name = null;

		// Get all teams.
		if (query_string == null)
			throw new HTTPException(403); // illegal operation

		// Get a named team.
		else {            
			// Split query string into name= and new_name= sections
			String[ ] parts = query_string.split("&");

			if (parts[0] == null || parts[1] == null)
				throw new HTTPException(403);

			name = get_value_from_qs("name", parts[0]);
			new_name = get_value_from_qs("new_name", parts[1]);

			if (name == null || new_name == null)
				throw new HTTPException(403);

			Team team = team_map.get(name);
			if (team == null)
				throw new HTTPException(404);

			team.setName(new_name);
			team_map.put(new_name, team);

			/*
			 * If we want to remove data to get using old name then un comment below part
            teams.remove(team);
			team_map.remove(name);
			 */
			serialize();
		}
		// Send a confirmation to requester.
		return response_to_client("Team " + name + " changed to " + new_name);
	}

	private Source doGet(MessageContext msg_ctx) {        
		// Parse the query string.

		System.out.println("Inside doGet method");

		String query_string = (String) msg_ctx.get(MessageContext.QUERY_STRING);

		System.out.println("Query :: " + query_string);

		// Get all teams.
		if (query_string == null)
			return new StreamSource(new ByteArrayInputStream(team_bytes));
		else {
			// Get a named team.
			String name = get_value_from_qs("name", query_string);

			// Check if named team exists.
			Team team = team_map.get(name);
			if (team == null) 
				throw new HTTPException(404); // not found

			System.out.println("Team Name :: " +team.getName());

			// Otherwise, generate XML and return.
			ByteArrayInputStream stream = encode_to_stream(team);
			return new StreamSource(stream); 
		}	 
	}

	private ByteArrayInputStream encode_to_stream(Object obj) {        
		// Serialize object to XML and return
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		XMLEncoder enc = new XMLEncoder(stream);
		enc.writeObject(obj);
		enc.close();

		return new ByteArrayInputStream(stream.toByteArray());    
	}

	private String get_value_from_qs(String key, String qs) {

		String[ ] parts = qs.split("=");
		// Check if query string has form: name=<team name>        
		if (!parts[0].equalsIgnoreCase(key))
			throw new HTTPException(400); // bad request
		return parts[1].trim();
	}


	@SuppressWarnings("resource")
	private void read_teams_from_file() {
		try {
			int len = (int) new File(file_path).length();
			team_bytes = new byte[len];
			new FileInputStream(file_path).read(team_bytes);
		}
		catch(IOException e) {
			System.err.println("Error :: " + e.getMessage()); 
		}	
	}

	private void deserialize() {
		// De Serialize the bytes into Teams

		XMLDecoder dec = new XMLDecoder(new ByteArrayInputStream(team_bytes));
		@SuppressWarnings("unchecked")
		List<Team> teamsObj = (List<Team>) dec.readObject();

		teams = teamsObj;

		// Create a map for quick lookups of teams.
		team_map = Collections.synchronizedMap(new HashMap<String, Team>());

		for (Team team : teams) {

			System.out.println("team name :: "+ team.getName());

			team_map.put(team.getName(), team); 
		}
	}
}