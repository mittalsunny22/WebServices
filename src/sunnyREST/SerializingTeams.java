package sunnyREST;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SerializingTeams {

	private static final String E_TEAMS_SER = "E:/AlpsTeam.ser";

	public static void main(String[] args) {

		seralizing();

		deserializing();
	}

	private static void deserializing() {

		Teams teams = null;

		try {
			FileInputStream fileInputStream = new FileInputStream(E_TEAMS_SER);
			XMLDecoder inputStream = new XMLDecoder(fileInputStream);

			teams = (Teams) inputStream.readObject();

			System.out.println(teams);

			inputStream.close();
			fileInputStream.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println(teams.getTeams().size());

	}

	private static void seralizing() {
		Teams teams = new Teams();

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(E_TEAMS_SER);
			XMLEncoder objectOutputStream = new XMLEncoder(fileOutputStream);
			objectOutputStream.writeObject(teams);
			objectOutputStream.close();
			fileOutputStream.close();
			System.out.println("teams object is serialized");
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}

}
