import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
	static BufferedWriter fileOut = null;

	/* ========== * GLOBAL VARIABLES* ========== */
	// Game data:
	public static int universeWidth;
	public static int universeHeight;
	public static String myColor;

	// Eyes hurt, but it works xD - Hash map of all planets mapped by color:
	public static Map<String, List<Map<String, Object>>> planets = new HashMap<>();

	// List of our own planets: 
	public static List<Map<String, Object>> myPlanetsList = new ArrayList<>();

	// List of all enemy planets including the neutral planets:
	public static List<Map<String, Object>> enemyPlanetsList = new ArrayList<>();

	// Global counters to count how many turns have passed:
	// From start:
	public static int currentTurnNumber = 0;
	// From the moment all neutral planets are gone:
	public static int currentTurnNumberAfterNeutral = 0;

	// List of all the neutral planets our bot has attacked:
	public static List<Map<String, Object>> attackedNeutralPlanets = new ArrayList<>();

	// List of all the fleets flying around
	public static List<Map<String, Object>> fleetsList = new ArrayList<>();
	// ============================================================================

	public static void main(String[] args) throws Exception {
		try {
			while (true) {
				planets.put("blue", new ArrayList<>());
				planets.put("cyan", new ArrayList<>());
				planets.put("green", new ArrayList<>());
				planets.put("yellow", new ArrayList<>());
				planets.put("neutral", new ArrayList<>());

				getGameState();
				setPlanetLists();

				/* ========== * LOGIC, STRATEGY & ATTACK * ========== */

				if (myPlanetsList.size() > 0 && enemyPlanetsList.size() > 0) {
					if (planets.get("neutral").size() <= 0 && currentTurnNumberAfterNeutral % 20 == 0) {
						attactWithFurthestPlanet(myPlanetsList, enemyPlanetsList);
					} else {
						for (Map<String, Object> myPlanet : myPlanetsList) {

							Map<String, Object> target = findClosestPlanet(myPlanet, enemyPlanetsList);

							// Find targets army size based on all the fleets flying in the air atm.
							int armySize = findEnemyArmysize(target, fleetsList);
							// 	continue;
							// }
							logToFile("ARMY SIZE OF PLANET: " + target.get("name").toString() + "IS: " + armySize);
							if (armySize > 0){
								attack(myPlanet, target);
							}
							
							
						}
					}
				}
				currentTurnNumber++;

				clearLists();
				System.out.println("E");
			}
		} catch (Exception e) {
			logToFile("ERROR: ");
			logToFile(e.getMessage());
			e.printStackTrace();
		}
		fileOut.close();
	}

	// TODO: tweak this whole function
	// ======================= ATTACK WITH FURTHEST PLANET ========================
	public static void attactWithFurthestPlanet(List<Map<String, Object>> myPlanetsF,
			List<Map<String, Object>> enemyPlanetsF) {
		// Go through all our planets and find the one closes to enemy planets
		int[] distances = new int[myPlanetsF.size()];
		int index = 0;
		for (Map<String, Object> myplanet : myPlanetsF) {
			int distance = Integer.MIN_VALUE;
			for (Map<String, Object> enemyplanet : enemyPlanetsF) {
				int tempDistance = calculateDistance(myplanet, enemyplanet);
				if (tempDistance < distance) {
					distance = tempDistance;
				}
			}
			distances[index] = distance;
			index++;
		}
		// Find the furthest planet from the planet specified above
		int maxMinDistance = 0;
		for (int i = 1; i < distances.length; i++) {
			if (distances[i] > maxMinDistance)
				maxMinDistance = i;
		}
		Map<String, Object> targetFurthestPlanet = myPlanetsF.get(0);
		int furthestDistance = Integer.MIN_VALUE;

		// Go through all enemy planets and attack the one that is furthest
		for (Map<String, Object> enemyPlanet : enemyPlanetsF) {
			int tempdistance = calculateDistance(myPlanetsF.get(maxMinDistance), enemyPlanet);
			if (tempdistance > furthestDistance) {
				furthestDistance = tempdistance;
				targetFurthestPlanet = enemyPlanet;
			}
		}
		System.out.println("A " + myPlanetsF.get(maxMinDistance).get("name") + " " + targetFurthestPlanet.get("name")
				+ " " + (Integer.parseInt(targetFurthestPlanet.get("armySize").toString()) + 1));
	}
	// ============================================================================

	// TODO: tweak attack function, add another parameter for army size
	// ================================== ATTACK ==================================

	// ((dist/2) * parsetoInt((10f * planet_size)) + (targetArmy + 1)) = myArmy
	public static void attack(Map<String, Object> myPlanet, Map<String, Object> target) throws IOException {
		try {
			if (checkIfNeutral(target)) {
				logToFile("my neutral planet is " + target);
				//attackedNeutralPlanets.add(target);
				System.out.println("M " + target.get("name").toString() + " " + target.get("x").toString() + " "
						+ target.get("y").toString() + " " + target.get("size").toString() + " "
						+ target.get("armySize").toString() + " " + target.get("color").toString());
				logToFile("my planet is " + myPlanet);
			}
		} catch (Exception e) {
			logToFile("crash" + e.getMessage());
		} 
		int myArmy = Integer.parseInt(myPlanet.get("armySize").toString());
		int targetArmy = Integer.parseInt(target.get("armySize").toString());
		int distance = calculateDistance(myPlanet, target);
		double targetPlanetSize = Float.parseFloat(target.get("size").toString());
		int attackSize = (int) ((distance / 2) * (10 * targetPlanetSize) + (targetArmy + 1));

		if (myArmy > targetArmy + targetArmy/2) {
			System.out.println("A " + myPlanet.get("name") + " " + target.get("name") + " " + attackSize);
			logToFile(" " + attackSize);
			for (Map<String, Object> map : enemyPlanetsList){
				if (map.equals(target)){
					logToFile("/////////////////////////////////////////////////////////////////////////////////////////////////");
					logToFile("" + map.put("armySize", Integer.parseInt(target.get("armySize").toString()) - attackSize));
					logToFile("/////////////////////////////////////////////////////////////////////////////////////////////////");
				}

				//logToFile("Attack with planet: " + myPlanet.get("name") + " attack_size of: " + attackSize + " on: " + target.get("name") + " planet with size of: " + map.get("armySize").toString());

			}
	}
}
	// ============================================================================

	// ============================== CHECK IF NEUTRAL ============================
	public static boolean checkIfNeutral(Map<String, Object> planet) {
		if (planet.get("color").equals("null")) {
			return true;
		}
		return false;
	}
	// ============================================================================

	// TODO: tweak check turn function
	// =============================== CHECK TURN =================================
	public static boolean checkTurn(Map<String, Object> myPlanet) {
		if (myPlanet.get("size").equals("0.1")) {
			if (currentTurnNumber % 10 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.2")) {
			if (currentTurnNumber % 9 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.3")) {
			if (currentTurnNumber % 8 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.4")) {
			if (currentTurnNumber % 7 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.5")) {
			if (currentTurnNumber % 6 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.6")) {
			if (currentTurnNumber % 5 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.7")) {
			if (currentTurnNumber % 4 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.8")) {
			if (currentTurnNumber % 3 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("0.9")) {
			if (currentTurnNumber % 2 == 0)
				return true;
		}
		if (myPlanet.get("size").equals("1.0")) {
			if (currentTurnNumber % 4 == 0)
				return true;
		}
		return false;
	}
	// ============================================================================

	// =========================== FIND CLOSEST PLANET ============================
	public static Map<String, Object> findClosestPlanet(Map<String, Object> myPlanet,
	List<Map<String, Object>> enemyPlanets) {

		int distance = Integer.MAX_VALUE;
		Map<String, Object> target = myPlanet;
		// Go through all enemy planets:
		for (Map<String, Object> enemyPlanet : enemyPlanetsList) {
			int tempdistance = calculateDistance(myPlanet, enemyPlanet);
			if (tempdistance < distance) {
				distance = tempdistance;
				target = enemyPlanet;
			}
		}
			// If the target planet is neutral then we check if our ally is attacking it
			// and if that is the case then we choose the second closest planet to attack.
			if (target.get("color").equals("null")) {
				if (!attackedNeutralPlanets.contains(target)) {
					return target;
				} else {
					enemyPlanets.remove(target);
					return findClosestPlanet(myPlanet, enemyPlanets);
				}
			} else {
				return target;
			}
	}
	// ============================================================================


	// =========================== FIND ENEMY ARMY SIZE ============================
	public static int findEnemyArmysize(Map<String, Object> targetPlanet, List<Map<String, Object>> fleets){
		int armySize = Integer.parseInt(targetPlanet.get("armySize").toString());

		for (Map<String, Object> fleet : fleets) {
			if ( targetPlanet.get("name").toString().equals(fleet.get("destination").toString())) {
				armySize = armySize - Integer.parseInt(targetPlanet.get("armySize").toString());
			}
		}

		return armySize;
		
	} 

	// ============================================================================

	// ============================ CALCULATE DISTANCE ============================
	public static int calculateDistance(Map<String, Object> myPlanet, Map<String, Object> enemyPlanet) {
		int distance = 0;
		int myPlanetX = Integer.parseInt(myPlanet.get("x").toString());
		int myPlanetY = Integer.parseInt(myPlanet.get("y").toString());
		int enemyPlanetX = Integer.parseInt(enemyPlanet.get("x").toString());
		int enemyPlanetY = Integer.parseInt(enemyPlanet.get("y").toString());

		// Euclidian distance between two points in a graph: 
		distance = (int) Math.sqrt(Math.pow(myPlanetX - enemyPlanetX, 2)
				+ Math.pow(myPlanetY - enemyPlanetY, 2));
		return distance;
	}
	// ============================================================================

	// =============================== LOG TO FILE ================================
	public static void logToFile(String line) throws IOException {
		if (fileOut == null) {
			FileWriter fstream = new FileWriter("Igralec.log");
			fileOut = new BufferedWriter(fstream);
		}
		if (line.charAt(line.length() - 1) != '\n') {
			line += "\n";
		}
		fileOut.write(line);
		fileOut.flush();
	}
	// ============================================================================

	// =============================== CLEAR LISTS ================================
	public static void clearLists() {
		planets.get("blue").clear();
		planets.get("cyan").clear();
		planets.get("green").clear();
		planets.get("yellow").clear();
		planets.get("neutral").clear();
		myPlanetsList.clear();
		enemyPlanetsList.clear();
	}
	// ============================================================================

	// ============================= SET PLANET LISTS =============================
	public static void setPlanetLists() {
		if (myColor.equals("blue")) {
			myPlanetsList = planets.get("blue");
			for (Map.Entry<String, List<Map<String, Object>>> entry : planets.entrySet()) {
				if (!entry.getKey().equals("blue") && !entry.getKey().equals("cyan")) {
					enemyPlanetsList.addAll(entry.getValue());
				}
			}
		} else if (myColor.equals("cyan")) {
			myPlanetsList = planets.get("cyan");
			for (Map.Entry<String, List<Map<String, Object>>> entry : planets.entrySet()) {
				if (!entry.getKey().equals("blue") && !entry.getKey().equals("cyan")) {
					enemyPlanetsList.addAll(entry.getValue());
				}
			}
		} else if (myColor.equals("green")) {
			myPlanetsList = planets.get("green");
			for (Map.Entry<String, List<Map<String, Object>>> entry : planets.entrySet()) {
				if (!entry.getKey().equals("green") && !entry.getKey().equals("yellow")) {
					enemyPlanetsList.addAll(entry.getValue());
				}
			}
		} else if (myColor.equals("yellow")) {
			myPlanetsList = planets.get("yellow");
			for (Map.Entry<String, List<Map<String, Object>>> entry : planets.entrySet()) {
				if (!entry.getKey().equals("green") && !entry.getKey().equals("yellow")) {
					enemyPlanetsList.addAll(entry.getValue());
				}
			}
		}
		if (planets.get("neutral").size() <= 0) {
			currentTurnNumberAfterNeutral++;
		}
	}
	// ============================================================================

	// ============================== GET GAME STATE ==============================
	public static void getGameState() throws NumberFormatException, IOException {
		BufferedReader stdin = new BufferedReader(
				new java.io.InputStreamReader(System.in));

		String line = "";

		while (!(line = stdin.readLine()).equals("S")) {
			logToFile(line);
			
			String[] tokens = line.split(" ");
			char firstLetter = line.charAt(0);

			if (firstLetter == 'U') {
				universeWidth = Integer.parseInt(tokens[1]);
				universeHeight = Integer.parseInt(tokens[2]);
				myColor = tokens[3];
			}

			/*
			 * P <int> <int> <int> <float> <int> <string>
			 * - Planet: Name, position x, position y, planet size, army size, color
			 */

			if (firstLetter == 'P') {
				Map<String, Object> planet = new HashMap<>();
				planet.put("name", tokens[1]);
				planet.put("x", tokens[2]);
				planet.put("y", tokens[3]);
				planet.put("size", tokens[4]);
				planet.put("armySize", tokens[5]);
				planet.put("color", tokens[6]);

				if (tokens[6].equals("blue")) {
					planets.get("blue").add(planet);
				}
				if (tokens[6].equals("cyan")) {
					planets.get("cyan").add(planet);
				}
				if (tokens[6].equals("green")) {
					planets.get("green").add(planet);
				}
				if (tokens[6].equals("yellow")) {
					planets.get("yellow").add(planet);
				}
				if (tokens[6].equals("null")) {
					planets.get("neutral").add(planet);
				}
			}

			// Taking the information sent by our ally bot about the planet he is attacking
			if (firstLetter == 'M') {
				Map<String, Object> planet = new HashMap<>();
				planet.put("name", tokens[1]);
				planet.put("x", tokens[2]);
				planet.put("y", tokens[3]);
				planet.put("size", tokens[4]);
				planet.put("armySize", tokens[5]);
				planet.put("color", tokens[6]);
				attackedNeutralPlanets.add(planet);
			}
		
			// - fleet name (number),
			// - fleet size
			// - origin planet
			// - destination planet
			// - current turn
			// - number of needed turns
			// - planet color (owner - may be null for neutral)

		
			if (firstLetter == 'F'){
				Map<String, Object> fleet = new HashMap<>();
				fleet.put("name", tokens[1]);
				fleet.put("size", tokens[2]);
				fleet.put("origin", tokens[3]);
				fleet.put("destination", tokens[4]);
				fleet.put("turn", tokens[5]);
				fleet.put("neededTurns", tokens[6]);
				fleet.put("planetColor", tokens[7	]);
				fleetsList.add(fleet);


			}
		}


		/*
		 * - override data from previous turn
		 * - convert the lists into fixed size arrays
		 */

	}
	// ============================================================================
}
