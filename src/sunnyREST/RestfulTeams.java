package sunnyREST;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;

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


	public RestfulTeams() {
		read_teams_from_file(); // read the raw bytes from teams.ser        
		deserialize();     
		System.out.println("test");
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

		// Act on the verb. To begin, only GET requests accepted.        
		if (http_verb.equals("GET")) 
			return doGet(msg_ctx);

		else throw new HTTPException(405); // method not allowed    
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
		Teams teamsObj = (Teams) dec.readObject();

		teams = teamsObj.getTeams();

		// Create a map for quick lookups of teams.
		team_map = Collections.synchronizedMap(new HashMap<String, Team>());

		for (Team team : teams) {

			System.out.println("team name :: "+ team.getName());

			team_map.put(team.getName(), team); 
		}
	}
}