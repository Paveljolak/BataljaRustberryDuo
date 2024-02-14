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

	/*
	 * GAME DATA
	 */

	public static Map<String, List<Map<String, Object>>> planets = new HashMap<>();
	// public static Map<String, Object> planet = new HashMap<>();

	public static List<Map<String, Object>> myPlanetsList = new ArrayList<>();
	public static List<Map<String, Object>> enemyPlanetsList = new ArrayList<>();

	public static int universeWidth;
	public static int universeHeight;
	public static String myColor;

	// public static Map<String, Object> myPlanets = new HashMap<>();
	public static int globalCounter = 0;

	public static int currentTurnNumber = 0;
	public static int currentTurnNumberAfterNeutral = 0;

	public static void main(String[] args) throws Exception {

		try {
			while (true) {
				planets.put("blue", new ArrayList<>());
				planets.put("cyan", new ArrayList<>());
				planets.put("green", new ArrayList<>());
				planets.put("yellow", new ArrayList<>());
				planets.put("neutral", new ArrayList<>());

				// logToFile("begore getgamestate: " + planets);

				getGameState();

				// logToFile("AFTER getgamestate: " + planets);

				// based on my color, find my planets and set enemies
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

				/*
				 * LOGIC, STRATEGY & ATTACK
				 */

				// TODO: shoot greatest neutral planets in range lets sat
				// if (globalCounter % 3 == 0) {
				if (myPlanetsList.size() > 0 && enemyPlanetsList.size() > 0) {
					// tua proverujme dali ima null planeti i dali turnot e %25==0
					if (planets.get("neutral").size() <= 0 && currentTurnNumberAfterNeutral % 20 == 0) {
						attactWithFurthestPlanet(myPlanetsList, enemyPlanetsList);
					} else {
						for (Map<String, Object> myPlanet : myPlanetsList) {
							if (checkTurn(myPlanet) == false) {
								continue;
							}

							int distance = Integer.MAX_VALUE;
							Map<String, Object> target = null;

							for (Map<String, Object> enemyPlanet : enemyPlanetsList) {
								// calculate the closest target planet
								int tempdistance = calculateDistance(myPlanet, enemyPlanet);
								if (tempdistance < distance) {
									distance = tempdistance;
									target = enemyPlanet;
								}

							}
							// ako nasa planeta ima povekje army od target planeta, pukas so tolku army
							// kolku enemy planeta + 1
							// ako ne pukaj so pola army od nasata planeta
							attack(myPlanet, target);
							// System.out.println("A " + myPlanet.get("name") + " " + target.get("name") + "
							// 30");
						}
					}
				}
				// }
				// globalCounter++;
				currentTurnNumber++;

				// ideas
				/*
				 * zavisi planet size, tolku da cekame
				 * make functionn for this one
				 * 
				 * 
				 * napadni so anjdalecni golemi planeti emeny planeti
				 * 
				 * 
				 * funkcija so kje ni najde najgolema
				 * 
				 * 
				 * 
				 * znaci naogjabe najbliska enemy planeta od site nasi planeti, i a odbirame
				 * planetata so najgolem distance
				 */

				System.out.println("M Hello I survived my attack (sent from " + myColor + ")");
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

	// ======================= ATTACK WITH FURTHEST PLANET ========================
	public static void attactWithFurthestPlanet(List<Map<String, Object>> myPlanetsF,
			List<Map<String, Object>> enemyPlanetsF) {
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
		int maxMinDistance = 0;
		for (int i = 1; i < distances.length; i++) {
			if (distances[i] > maxMinDistance)
				maxMinDistance = i;
		}
		Map<String, Object> targetFurthestPlanet = null;
		int furthestDistance = Integer.MIN_VALUE;
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

	// =============================== ATTACK =====================================
	public static void attack(Map<String, Object> myPlanet, Map<String, Object> target) {
		int myArmy = Integer.parseInt(myPlanet.get("armySize").toString());
		int targetArmy = Integer.parseInt(target.get("armySize").toString());
		if (myArmy > targetArmy) {
			System.out.println("A " + myPlanet.get("name") + " " + target.get("name") + " " + (targetArmy + 1));
		} else {
			System.out.println("A " + myPlanet.get("name") + " " + target.get("name") + " " + (targetArmy + 1));
		}
	}
	// ============================================================================

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
			if (currentTurnNumber % 2 == 0)
				return true;
		}
		return false;
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

	// ============================ CALCULATE DISTANCE ============================
	public static int calculateDistance(Map<String, Object> myPlanet, Map<String, Object> enemyPlanet) {
		int distance = 0;
		// calculate distance between two planets
		// distance = sqrt((x2 - x1)^2 + (y2 - y1)^2)
		int myPlanetX = Integer.parseInt(myPlanet.get("x").toString());
		int myPlanetY = Integer.parseInt(myPlanet.get("y").toString());
		int enemyPlanetX = Integer.parseInt(enemyPlanet.get("x").toString());
		int enemyPlanetY = Integer.parseInt(enemyPlanet.get("y").toString());
		distance = (int) Math.sqrt(Math.pow(myPlanetX - enemyPlanetX, 2)
				+ Math.pow(myPlanetY - enemyPlanetY, 2));
		return distance;
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

	// ============================== GET GAME STATE ==============================
	public static void getGameState() throws NumberFormatException, IOException {
		BufferedReader stdin = new BufferedReader(
				new java.io.InputStreamReader(System.in));

		String line = "";

		while (!(line = stdin.readLine()).equals("S")) {
			logToFile(line);
			// Map<String, Object> planet = new HashMap<>();

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
			// logToFile("this is end");
			// logToFile("" + planets);
			// planet.clear();
		}
		/*
		 * - override data from previous turn
		 * - convert the lists into fixed size arrays
		 */

	}
	// ============================================================================

}
