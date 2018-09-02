package sunnyREST;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

public class TeamsUtility implements Serializable{   
	private Map<String, Team> team_map;

	public TeamsUtility() {       
		team_map = new HashMap<String, Team>();    }

	public Team getTeam(String name) { return team_map.get(name); }   

	public List<Team> getTeams() {
		List<Team> list = new ArrayList<Team>();
		Set<String> keys = team_map.keySet();
		for (String key : keys)
			list.add(team_map.get(key));
		return list;    
	}

	public void make_test_teams() {

		List<Team> teams = new ArrayList<Team>();
		Employee sunny = new Employee("Sunny Mittal", "smittal@qasource.com");       
		Employee daljeet = new Employee("Daljeet Singh", "dasingh@qasource.com");

		Employee vikram = new Employee("Vikam Sharma", "vikrsharma@qasource.com");       
		Employee himanshi = new Employee("Himanshi Goel", "hgoel@qasource.com");

		Employee gaurav = new Employee("Gaurav Singla", "gsingla@qasource.com");       

		List<Employee> alps = new ArrayList<Employee>();
		alps.add(sunny);
		alps.add(daljeet);

		Team alps_dev = new Team("Alps", alps);

		List<Employee> pso = new ArrayList<Employee>();
		pso.add(himanshi);
		pso.add(vikram);

		Team pso_dev = new Team("PSO", pso);

		List<Employee> sales = new ArrayList<Employee>();
		sales.add(gaurav);

		Team sales_dev = new Team("Sales", sales);

		teams.add(alps_dev);
		teams.add(pso_dev);
		teams.add(sales_dev);

		store_teams(teams);
	}

	private void store_teams(List<Team> teams) {

		for (Team team : teams)
		{
			System.out.println("Name :: " + team.getName());
			team_map.put(team.getName(), team);    
		} 
	}
}