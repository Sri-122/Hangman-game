import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

//Nicholas Demetrick
public class Hangman {

	//Score constants
	private static final int wordFailCost = 2; private static final int attemps = 7; private static final int RemaingGeussBonus = 30; private static final int wordBonus = 100;
	
	//genral varibuls
	private int dictionarySize = 0; private int guessCount = 0;  private String display = ""; private int score = 0; private String word; private String geuss; private boolean alive  = true;
	
	//use this if you want to cheat
	private boolean trobleShooting = false;
	
	//used to store wrongs geusses
	@SuppressWarnings("unchecked")
	private ArrayList<String> wrongs = new ArrayList();
	
	//used to store our dictionary 
	private ArrayList<String> list = new ArrayList();

	/** The heart of our game where we start the rounds and set up the dictionary */
	public void game() {
		setup();
		while (alive) {
			//this resets our varibuls after ever game
			if(trobleShooting) System.out.println("TrobleShooting mode on. to turn of turn the varibul to false  \n");

			display = "";
			rounds();

		}//end of while alive

		System.out.println("Game over your final score is " + score);

	}//end of contoller
	
	/** This is where we create a dictionary in a array list */
	private void setup() {
		File dictionary = new File("src/dictionary.txt");

		//Adds everything in the dictionary to list
		try(Scanner reader = new Scanner(dictionary);){
			while(reader.hasNext()) {
				list.add( reader.nextLine() );
				dictionarySize++;
			}//while
			reader.close();
		}//try

		catch(IOException e) {
			System.out.println("File not found: Cannot find dictionary.txt"); 
		}//catch

	}//setup()

	/** This is where each hangman round is played */
	private  void rounds() {
		System.out.println("Lets play Hangman");

		//gets a random word out of our list
		word = list.get( (int) (Math.random() * dictionarySize) );

		if(trobleShooting)System.out.println("Word: " + word);

		//This creates a visual of the word size for our game
		for(int i = 0; i != word.length(); i++) {
			System.out.print("_ ");
			display += "_";
		}//for

		System.out.println('\n');

		//This is the main game loop where the user makes there geuss
		Scanner input = new Scanner(System.in);
		while(guessCount < attemps ) {

			System.out.println("Geuss a letter:");

			//user input to make a geuss
			try{

				String a = input.nextLine();

				//no geuss
				if(a.length() == 0) throw new StringIndexOutOfBoundsException("You have to make a geuss" );
				geuss =  a.toLowerCase();

				//alreay geussed
				if(wrongs.contains("" + geuss) || display.contains("" + geuss)  ) throw new StringIndexOutOfBoundsException("You already geuss that you goof" );
			}//try

			// no letters or there is no geuss
			catch(StringIndexOutOfBoundsException  e){
				System.out.println( "\n" + e.getMessage() + "\n");
			}//catch

			if(geuss.length() > 1) wordgeuss(geuss);

			//Builds the new game display 
			else display(geuss.charAt(0));

			//test to see if you won and breaks
			if(! display.contains("_") ) {
				
				//has to be here for the word bounus to work
				score += (wordBonus + (RemaingGeussBonus * ( attemps  -  guessCount    ) ) ); 
				guessCount = attemps;
				System.out.println("You Win : )" + "\n");
				System.out.println("Your score is " + score);
			}

			else guessCount++;

		}//while

		System.out.println();

		//see if you lost
		if(display.contains("_")) {
			System.out.println("You lose. The word is " + word);
			alive = false;
		}//if

		else guessCount = 0;

	}//game()

	/** This is where we update our display  
	 * @param letter this is our gussed letter */
	private void display(char letter) {
		StringBuilder builder = new StringBuilder(display);
		//if already geussed
		if(word.contains("" + letter)) guessCount--;

		//builds wrong guesses
		else if(!wrongs.contains(letter)) wrongArray( letter);

		//rebuilds display
		for(int i = 0; i != word.length(); i++) {
			if(word.charAt(i) == letter ) {
				builder.setCharAt( i, letter);
				score += 10;
			}//if
		}//for

		//prints display with spaces
		for(int i = 0; i != builder.length(); i++) System.out.print(builder.charAt(i) + " ");

		System.out.print("		Geusses Left: " +  (7 - guessCount));
		System.out.print("		Your Score is " + score);
		System.out.println( "		Worng letters: "  +"{" +  wrongString() + "}");

		display =  builder.toString();
	}//display()

	/** This is our wrong array that will store our wrong geusses and sorts* 
	 * @param letter that was geussed */
	private void wrongArray(char letter) {
		if(!wrongs.contains("" + letter) )  wrongs.add("" + letter);
		Collections.sort(wrongs);
	}//wrong

	/** @return a string of wrong geusses */
	private String wrongString() {
		if(wrongs.size() == 0) return "";
		String builder =  wrongs.get(0);
		for(int i =1; i != wrongs.size();  i++ ) builder += ", " +  wrongs.get(i); 
		return builder;
	}//end of wrong string

	/** * @param geussed word to compaire to our secrete word*/
	public void wordgeuss(String geuss) {
		geuss = geuss.trim();
		
		if(geuss.equals(word)) {
			display = word;
			System.out.println("you got the word, proud of you");
		}//if
		
		else {
			guessCount += wordFailCost;
			System.out.println(geuss + " was not the word : ' ( \nYou got " + (attemps -  guessCount) + " attemps left" );
		}//else
	}//geuss()

	public int getScore() { return score; }//getScore()

}//end of class Hangman			