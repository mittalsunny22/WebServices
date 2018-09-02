package sunnyREST;

import java.util.List;
import clientREST.TeamsService;
import clientREST.Teams;
import clientREST.Team;
import clientREST.Player;

public class TeamClient {

	public static void main(String[ ] args) {

		TeamsService service = new TeamsService();

		Teams teamsPort = service.getTeamsPort();

		List<Team> teams = teamsPort.getTeams();
		for (Team team : teams) {
			System.out.println("Team name: " + team.getName() +" (roster count: " + team.getRosterCount() + ")");
			for (Player player : team.getPlayers())
				System.out.println("  Player: " + player.getNickname());
		}  
	}
}