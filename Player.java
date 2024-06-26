import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Player {
	static BufferedWriter fileOut = null;
	
	/*
		GAME DATA
	*/
	public static int universeWidth;
	public static int universeHeight;
	public static String myColor;
	
	public static String[] bluePlanets;
	public static String[] cyanPlanets;
	public static String[] greenPlanets;
	public static String[] yellowPlanets;
	public static String[] neutralPlanets;

	public static String[] blueCoordinatesX;
	public static String[] cyanCoordinatesX;
	public static String[] greenCoordinatesX;
	public static String[] yellowCoordinatesX;
	public static String[] neutralCoordinatesX;

	public static String[] blueCoordinatesY;
	public static String[] cyanCoordinatesY;
	public static String[] greenCoordinatesY;
	public static String[] yellowCoordinatesY;
	public static String[] neutralCoordinatesY;

	public static String[] bluePlanetSizes;
	public static String[] cyanPlanetSizes;
	public static String[] greenPlanetSizes;
	public static String[] yellowPlanetSizes;
	public static String[] neutralPlanetSizes;

	public static String[] blueFleets;
	public static String[] cyanFleets;
	public static String[] greenFleets;
	public static String[] yellowFleets;
	public static String[] neutralFleets;


// This is for finding the planet with the smallest fleet size so we can attack it.
	public static String[] joinedFleets;
	public static String[] joinedPlanetNames;








	public static void main(String[] args) throws Exception {

		try {
			Random rand = new Random(); // source of random for random moves

			/*
				**************
				Main game loop
				**************
			  	- each iteration of the loop is one turn.
			  	- this will loop until we stop playing the game
			  	- we will be stopped if we die/win or if we crash
			*/
			while (true) {
				/*
					- at the start of turn we first recieve data
					about the universe from the game.
					- data will be loaded into the static variables of
					this class
				*/
				getGameState();
				

				

				/*
				 	*********************************
					LOGIC: figure out what to do with
					your turn
					*********************************
					- current plan: attack randomly
				*/

				String[] myPlanets = new String[0];
				String[] myFleets = new String[0];
				String[] friendlyPlayerPlanets = new String[0];
				String targetPlayer = "";

				

				/*
					- get my planets based on my color
					- select a random other color as the target player 
				*/
				if (myColor.equals("blue")) {
					myPlanets = bluePlanets;
					myFleets = blueFleets;
					friendlyPlayerPlanets = cyanPlanets;
					joinedPlanetNames = joinArrays(greenPlanets, yellowPlanets, neutralPlanets);
					joinedFleets = joinArrays(greenFleets, yellowFleets, neutralFleets);
				} 

				if (myColor.equals("cyan")) {
					myPlanets = cyanPlanets;
					myFleets = cyanFleets;
					friendlyPlayerPlanets = bluePlanets;
					joinedPlanetNames = joinArrays(greenPlanets, yellowPlanets, neutralPlanets);
					joinedFleets = joinArrays(greenFleets, yellowFleets, neutralFleets);
				} 

				if (myColor.equals("green")) {
					myPlanets = greenPlanets;
					myFleets = greenFleets;
					friendlyPlayerPlanets = yellowPlanets;
					joinedPlanetNames = joinArrays(cyanPlanets, bluePlanets, neutralPlanets);
					joinedFleets = joinArrays(cyanFleets, blueFleets, neutralFleets);
				} 
				
				if (myColor.equals("yellow")) {
					myPlanets = yellowPlanets;
					myFleets = yellowFleets;
					friendlyPlayerPlanets = greenPlanets;
					joinedPlanetNames = joinArrays(cyanPlanets, bluePlanets, neutralPlanets);
					joinedFleets = joinArrays(cyanFleets, blueFleets, neutralFleets);
				}


				int enemyMaxFleetSize = Integer.parseInt(joinedPlanetNames[findMaxFleetSize(joinedFleets)]);
				//int myMaxFleetSize = findMaxFleetSize(myFleets);
				if (joinedPlanetNames.length > 0 && myPlanets.length > 0) {
					
					String targetPlanet = joinedPlanetNames[findMinFleetSize(joinedFleets)];
					

					for(int i = 0; i < myPlanets.length; i++ ){
						String myPlanet = myPlanets[i];
						if(Integer.parseInt(myFleets[i]) > (enemyMaxFleetSize/2)){
							logToFile("Attack from: " + myPlanet + " with force: " + myFleets[i]);
							logToFile("Attack this one: " + targetPlanet + " cuz bigest enemy planet has half force of: " + (enemyMaxFleetSize/2));
							System.out.println("A " + myPlanet + " " + targetPlanet + " 30");
						}
						else{
							logToFile("Don't attack with planet: " + myPlanet);
						}	
					}
				}
				
				
				
				/*
					- send a hello message to your teammate bot :)
					- it will recieve it form the game next turn (if the bot parses it)
				 */
				System.out.println("M Hello");

				/*
				  	- E will end my turn. 
				  	- you should end each turn (if you don't the game will think you timed-out)
				  	- after E you should send no more commands to the game
				 */
				System.out.println("E");
			}
		} catch (Exception e) {
			logToFile("ERROR: ");
			logToFile(e.getMessage());
			e.printStackTrace();
		}
		fileOut.close();
		
	}

	public static int findSmallestElement(int[] array) {
        // Check if the array is empty
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }

        // Assume the first element is the smallest
        int smallest = array[0];

        // Iterate through the array to find the smallest element
        for (int i = 1; i < array.length; i++) {
            if (array[i] < smallest) {
                smallest = array[i];
            }
        }

        return smallest;
    }

// ______________________________________________________________________________________________________________________________
	// FINDING YOUR OWN PLANET WITH THE HIGHEST FLEET SIZE
		public static int findMaxFleetSize(String[] fleet) {
			int currMax = 0;
			for(int i = 0; i < fleet.length; i++) {
				if (Integer.parseInt(fleet[i]) > Integer.parseInt(fleet[currMax])) {
					currMax = i;
				}
			}
			return currMax;
		}
// ______________________________________________________________________________________________________________________________


// ______________________________________________________________________________________________________________________________
	// FINDING OPOSING PLANET WITH SMALLEST FLEET SIZE
	public static int findMinFleetSize(String[] fleet) {
		int currMin = 0;
		for(int i = 0; i < fleet.length; i++) {
			if (Integer.parseInt(fleet[i]) < Integer.parseInt(fleet[currMin])) {
				currMin = i;
			}
		}
		return currMin;
	}
// ______________________________________________________________________________________________________________________________


// ______________________________________________________________________________________________________________________________
// JOINING ATTACKING PLANETS INTO ONE BIG ARRAY
	public static String[] joinArrays(String[] firstEnemy, String[] secondEnemy, String[] neutral){
		
	
		
		 // Calculate the total length of the combined array
		 int totalLength = firstEnemy.length + secondEnemy.length + neutral.length;

		 // Create a new array with the total length
		 String[] joinedArray = new String[totalLength];
 
		 // Copy each array into the combined array
		 System.arraycopy(firstEnemy, 0, joinedArray, 0, firstEnemy.length);
		 System.arraycopy(secondEnemy, 0, joinedArray, firstEnemy.length, secondEnemy.length);
		 System.arraycopy(neutral, 0, joinedArray, firstEnemy.length + secondEnemy.length, neutral.length);
 
		 // Now, 'combinedArray' contains all elements from the three arrays



		return joinedArray;
	}
// ______________________________________________________________________________________________________________________________

	/**
	 * This function should be used instead of System.out.print for 
	 * debugging, since the System.out.println is used to send 
	 * commands to the game
	 * @param line String you want to log into the log file.
	 * @throws IOException
	 */
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


	/**
	 * This function should be called at the start of each turn to obtain information about the current state of the game.
	 * The data received includes details about planets and fleets, categorized by color and type.
	 *
	 * This version of the function uses dynamic lists to store data about planets and fleets for each color,
	 * accommodating for an unknown quantity of items. At the end of data collection, these lists are converted into fixed-size
	 * arrays for consistent integration with other parts of the program.
	 *
	 * Feel free to modify and extend this function to enhance the parsing of game data to your needs.
	 *
	 * @throws NumberFormatException if parsing numeric values from the input fails.
	 * @throws IOException if an I/O error occurs while reading input.
	 */
	public static void getGameState() throws NumberFormatException, IOException {
		BufferedReader stdin = new BufferedReader(
			new java.io.InputStreamReader(System.in)
		); 
		/*
			- this is where we will store the data recieved from the game,
			- Since we don't know how many planets/fleets each player will 
			have, we are using lists.
		*/ 


		// STORE ALL THE PLANETS PER COLOR
		LinkedList<String> bluePlanetsList = new LinkedList<>();
		LinkedList<String> cyanPlanetsList = new LinkedList<>();
		LinkedList<String> greenPlanetsList = new LinkedList<>();
		LinkedList<String> yellowPlanetsList = new LinkedList<>();
		LinkedList<String> neutralPlanetsList = new LinkedList<>();

		// STORE THE X COORDINATES OF ALL PLANETS PER COLOR
		LinkedList<String> blueCoordinateXList = new LinkedList<>();
		LinkedList<String> cyanCoordinateXList = new LinkedList<>();
		LinkedList<String> greenCoordinateXList = new LinkedList<>();
		LinkedList<String> yellowCoordinateXList = new LinkedList<>();
		LinkedList<String> neutralCoordinateXList = new LinkedList<>();

		// STORE THE Y COORDINATES OF ALL PLANETS PER COLOR
		LinkedList<String> blueCoordinateYList = new LinkedList<>();
		LinkedList<String> cyanCoordinateYList = new LinkedList<>();
		LinkedList<String> greenCoordinateYList = new LinkedList<>();
		LinkedList<String> yellowCoordinateYList = new LinkedList<>();
		LinkedList<String> neutralCoordinateYList = new LinkedList<>();

		// STORE ALL THE PLANET SIZES PER COLOR
		LinkedList<String> bluePlanetSizeList = new LinkedList<>();
		LinkedList<String> cyanPlanetSizeList = new LinkedList<>();
		LinkedList<String> greenPlanetSizeList = new LinkedList<>();
		LinkedList<String> yellowPlanetSizeList = new LinkedList<>();
		LinkedList<String> neutralPlanetSizeList = new LinkedList<>();

		// STORE ALL THE FLEET SIZES PER COLOR
		LinkedList<String> blueFleetsList = new LinkedList<>();
		LinkedList<String> cyanFleetsList = new LinkedList<>();
		LinkedList<String> greenFleetsList = new LinkedList<>();
		LinkedList<String> yellowFleetsList = new LinkedList<>();
		LinkedList<String> neutralFleetsList = new LinkedList<>();


		
		/*
			********************************
			read the input from the game and
			parse it (get data from the game)
			********************************
			- game is telling us about the state of the game (who ows planets
			and what fleets/attacks are on their way). 
			- The game will give us data line by line. 
			- When the game only gives us "S", this is a sign
			that it is our turn and we can start calculating out turn.
			- NOTE: some things like parsing of fleets(attacks) is not implemented 
			and you should do it yourself
		*/
		String line = "";
		/*
			Loop until the game signals to start playing the turn with "S"
		*/ 
		while (!(line = stdin.readLine()).equals("S")) {
			/* 
				- save the data we recieve to the log file, so you can see what 
				data is recieved form the game (for debugging)
			*/ 
			logToFile(line); 
			
			String[] tokens = line.split(" ");
			char firstLetter = line.charAt(0);
			/*
			 	U <int> <int> <string> 						
				- Universe: Size (x, y) of playing field, and your color
			*/
			if (firstLetter == 'U') {
				universeWidth = Integer.parseInt(tokens[1]);
				universeHeight = Integer.parseInt(tokens[2]);
				myColor = tokens[3];
			} 
			/*
				P <int> <int> <int> <float> <int> <string> 	
				- Planet: Name (number), position x, position y, 
				planet size, army size, planet color (blue, cyan, green, yellow or null for neutral)
			*/
			if (firstLetter == 'P') {
				String plantetName = tokens[1];
				if (tokens[6].equals("blue")) {
					bluePlanetsList.add(plantetName);
					blueCoordinateXList.add(tokens[2]);
					blueCoordinateYList.add(tokens[3]);
					bluePlanetSizeList.add(tokens[4]);
					blueFleetsList.add(tokens[5]);

				} 
				if (tokens[6].equals("cyan")) {
					cyanPlanetsList.add(plantetName);
					cyanCoordinateXList.add(tokens[2]);
					cyanCoordinateYList.add(tokens[3]);
					cyanPlanetSizeList.add(tokens[4]);
					cyanFleetsList.add(tokens[5]);
				} 
				if (tokens[6].equals("green")) {
					greenPlanetsList.add(plantetName);
					greenCoordinateXList.add(tokens[2]);
					greenCoordinateYList.add(tokens[3]);
					greenPlanetSizeList.add(tokens[4]);
					greenFleetsList.add(tokens[5]);
				} 
				if (tokens[6].equals("yellow")) {
					yellowPlanetsList.add(plantetName);
					yellowCoordinateXList.add(tokens[2]);
					yellowCoordinateYList.add(tokens[3]);
					yellowPlanetSizeList.add(tokens[4]);
					yellowFleetsList.add(tokens[5]);
				} 
				if (tokens[6].equals("null")) {
					neutralPlanetsList.add(plantetName);
					neutralCoordinateXList.add(tokens[2]);
					neutralCoordinateYList.add(tokens[3]);
					neutralPlanetSizeList.add(tokens[4]);
					neutralFleetsList.add(tokens[5]);
					
				} 
			} 
		}
		/*
			- override data from previous turn
			- convert the lists into fixed size arrays
		*/ 

		// PLANET NAMES
		bluePlanets = bluePlanetsList.toArray(new String[0]);
		cyanPlanets = cyanPlanetsList.toArray(new String[0]);
		greenPlanets = greenPlanetsList.toArray(new String[0]);
		yellowPlanets = yellowPlanetsList.toArray(new String[0]);
		neutralPlanets = neutralPlanetsList.toArray(new String[0]);

		//COORDINATES X
		blueCoordinatesX = blueCoordinateXList.toArray(new String[0]);
		cyanCoordinatesX = cyanCoordinateXList.toArray(new String[0]);
		greenCoordinatesX = greenCoordinateXList.toArray(new String[0]);
		yellowCoordinatesX = yellowCoordinateXList.toArray(new String[0]);
		neutralCoordinatesX = yellowCoordinateXList.toArray(new String[0]);

		//COORDINATES Y
		blueCoordinatesY = blueCoordinateYList.toArray(new String[0]);
		cyanCoordinatesY = cyanCoordinateYList.toArray(new String[0]);
		greenCoordinatesY= greenCoordinateYList.toArray(new String[0]);
		yellowCoordinatesY = yellowCoordinateYList.toArray(new String[0]);
		neutralCoordinatesY = yellowCoordinateYList.toArray(new String[0]);		

		// PLANET_SIZE
		bluePlanetSizes = bluePlanetSizeList.toArray(new String[0]);
		cyanPlanetSizes = cyanPlanetSizeList.toArray(new String[0]);
		greenPlanetSizes = greenPlanetSizeList.toArray(new String[0]);
		yellowPlanetSizes = yellowPlanetSizeList.toArray(new String[0]);
		neutralPlanetSizes =neutralPlanetSizeList.toArray(new String[0]);

		// FLEET_SIZE
		blueFleets = blueFleetsList.toArray(new String[0]);
		cyanFleets = cyanFleetsList.toArray(new String[0]);
		greenFleets = greenFleetsList.toArray(new String[0]);
		yellowFleets = yellowFleetsList.toArray(new String[0]);
		neutralFleets =neutralFleetsList.toArray(new String[0]);
		

					// PROTOTYPES
					// A lot of ifs and elses and ANDS. fleet_size(i) and planet_name(i) and coordinats(i) 
					// for (int i = 0; i < plantetName.length; i++ ){
					// 	findNearestEnemy();
					// 	attackNearestEnemy();
					// }
	}
}

