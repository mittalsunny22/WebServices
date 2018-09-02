package sunnyREST;

import javax.xml.ws.Endpoint;

public class TeamsPublisher {

	public static void main(String[ ] args) {

		String url = "http://localhost:8087/Web-service/teams";
		Endpoint.publish(url, new RestfulTeams());
		System.out.println("Published Teams restfully on url " + url);
	}
}     