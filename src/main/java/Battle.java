
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;


public class Battle {
    //List of Players for team1
    private static List<Character> team1 = new ArrayList<>();
    //List of Player for team2
    private static List<Character> team2 = new ArrayList<>();
    //List of player on the graveyard
    private static List<Character> graveyard = new ArrayList<>();
    //List which identifies from which team is each player of the graveyard
    private static List<Integer> teamOfDead = new ArrayList<>();
    //size of each team that can be choose by the user
    private static int teamSize;

    //background colour
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

    //text colour
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    //constructor
    public Battle() {
    }

    //getters and setters
    public static int getTeamSize() {
        return teamSize;
    }

    public static void setTeamSize(int teamSize) {
        Battle.teamSize = teamSize;
    }

    public static List<Character> getTeam1() {
        return team1;
    }

    public static List<Character> getTeam2() {
        return team2;
    }

    public static List<Character> getGraveyard() {
        return graveyard;
    }

    public static List<Integer> getTeamOfDead() {
        return teamOfDead;
    }

    public static void setTeam1(List<Character> team1) {
        Battle.team1 = team1;
    }

    public static void setTeam2(List<Character> team2) {
        Battle.team2 = team2;
    }

    public static void setGraveyard(List<Character> graveyard) {
        Battle.graveyard = graveyard;
    }


    //This method define the size of the teams [limit 10]
    public static int defineTeamSize(Scanner scanner){
        int teamSize = 0;
        try {
            teamSize = scanner.nextInt();
            //wrong input
            if (teamSize < 1 || teamSize > 10){
                System.err.println("The size is wrong. Please, insert a right number: ");
                teamSize = defineTeamSize(scanner);
            }
        } catch (Exception e){
            System.err.println("The size is wrong. Please, insert a right number: ");
            scanner.next();
            teamSize = defineTeamSize(scanner);
        }
        return teamSize;
    }

    //---creation of the teams---

    //Option one: randomly creation
    public static List<Character> createRandomParty(int team_size) {
        List<Character> randomCharacters = new ArrayList<>();
        String[] names = {"Paula", "Monica", "Yanira", "Esteban", "James", "Lucas", "Sebastian",
                "Carlos", "Julia", "Pilar", "Felipe", "Raymond", "Shaun"};
        for (int i = 0; i < team_size; i++){
            //random selection of the name
            String name = names[(int)(Math.random() * names.length)];
            //if is equal of one of the already selected add Jr at the end
            if (randomCharacters.size() != 0) {
                for (int j = 0; j < randomCharacters.size(); j++) {
                    if (name.equals(randomCharacters.get(j).getName())) {
                        name = name + " Jr";
                    }
                }
            }
            int hp = 0;
            //random selection of the character. If 1 Warrior, if 2 Wizard
            int random_Character = (int)(Math.random()* (3 -1) +1);
            if (random_Character == 1) {
                //randomly creation of hp
                hp = (int)(Math.random()* (201 -100) +100);
                //randomly creation of strength
                int strength = (int)(Math.random()* (11 -1) +1);
                //randomly creation of stamina
                int stamina = (int)(Math.random()* (51 -10) +10);
                Warrior player_warrior = new Warrior (name, hp, stamina, strength);
                randomCharacters.add(player_warrior);
            }else {
                //randomly creation of hp
                hp = (int)(Math.random()* (101 -50) +50);
                //randomly creation of mana
                int mana = (int)(Math.random()* (51 -10) +10);
                //randomly creation of intelligence
                int intelligence = (int)(Math.random()* (51 -1) +1);
                Wizard player_wizard = new Wizard (name, hp, mana, intelligence);
                randomCharacters.add(player_wizard);
            }
        }
        return randomCharacters;
    }

    //Option 2: creation from the CVS
    public static List<Character> createPartyFromCSV(int size) throws FileNotFoundException {
        List<Character> csvCharacters = new ArrayList<>();
        Scanner scanner = new Scanner(new File("characters.csv"));
        String header = scanner.nextLine();
        while(scanner.hasNextLine()){
            String[] attributes = scanner.nextLine().split(";");
            //int id = Integer.parseInt(attributes[0]);
            String characterClass = attributes[1];
            String name = attributes[2];
            int hp = Integer.parseInt(attributes[3]);
            int mana = Integer.parseInt(attributes[4]);
            int stamina = Integer.parseInt(attributes[5]);
            int strength = Integer.parseInt(attributes[6]);
            int intelligence = Integer.parseInt(attributes[7]);
            //int random = (int) Math.floor(Math.random()*2+1);
            if(characterClass.equals("Warrior")){
                csvCharacters.add(new Warrior(name,hp,stamina,strength));
            } else {
                csvCharacters.add(new Wizard(name,hp,mana,intelligence));
            }
        }
        scanner.close();
        List<Character> team = new ArrayList<>();
        Collections.shuffle(csvCharacters);
        for(int i = 0; i<size; i++){
            team.add(csvCharacters.get(i));
        }
        return team;
    }

    //method for the creation of the team1
    public static void createTeam1(Scanner scanner){
        if (isRandom(scanner)) {
            setTeam1(createRandomParty(getTeamSize()));
        } else {
            List<Character> caractherLeft = new ArrayList<>();
            int playerToMakeLeft = getTeamSize();
            for (int i = 0; i<getTeamSize(); i++) {
                userMakeaParty(getTeam1());
                playerToMakeLeft--;
                if (i != getTeamSize() -1) {
                    System.out.println("If you want to keep customizing press \"c\". If not, press \"r\" and the rest will be randomly created");
                    if (isRandom(scanner)) {
                        caractherLeft = createRandomParty(playerToMakeLeft);
                        break;
                    }
                }
            }
            for (Character c : getTeam1()){
                caractherLeft.add(c);
            }
            setTeam1(caractherLeft);
        }
    }

    public static void userMakeaParty(List<Character> team){
        List<String> teamNames = new ArrayList<>();
        if (team.size() != 0) {
            for (Character s : team) {
                teamNames.add(s.getName());
            }
        }
            //selection of character type
            Scanner typeOfCharacter = new Scanner(System.in);
            System.out.println("What would you like to create a Warrior or Wizard?");
            String Character_choose = typeOfCharacter.next();
            //warrior choose
            if (Character_choose.equalsIgnoreCase("warrior")){
                Scanner names = new Scanner(System.in);
                //name selection
                System.out.println("Now choose a name for your character");
                String name = chooseName(names.next(), teamNames);
                teamNames.add(name);
                //hp selection [100,200]
                int hp = userInputs("Health", 200,100);
                //stamina selection [10,50]
                int stamina =  userInputs("Stamina", 50,10);
                //strength selection [1,10]
                int strength = userInputs("Strength", 10,1);
                Warrior player = new Warrior(name, hp, stamina, strength);
                team.add(player);
                //if (i != team_size-1 ) {
                    System.out.println("Great! Let's create another character");
                //}
                //wizard choose
            }else if(Character_choose.equalsIgnoreCase("wizard")){
                //name selection
                Scanner names = new Scanner(System.in);
                System.out.println("Now choose a name for your character");
                String name = chooseName(names.next(), teamNames);
                teamNames.add(name);
                //hp selection [50,100]
                int hp = userInputs("Health", 100, 50);
                //mana selection [10,50]
                int mana = userInputs("Mana", 50, 10);
                //intelligence selection[1,50]
                int intelligence = userInputs("Intelligence", 50, 1);
                Wizard player = new Wizard(name, hp,mana, intelligence);
                team.add(player);
                //if (i != team_size-1 ) {
                    System.out.println("Great! Let's create another character");
                //}
                //invalid input
            }else{
                System.err.println("Please select a valid type. Write Warrior or Wizard");
                //i--;
            }
    }

    //Auxiliary method for the creation of a character by the user. Input of the name, if repeated, add Jr at the end
    public static String chooseName(String name, List<String> teamNames) {
        if (teamNames.size() != 0) {
            boolean repeatedName = false;
            for (String s : teamNames) {
                if (s.equalsIgnoreCase(name)) {
                    name = name + " Jr";
                    repeatedName = true;
                }
            }
            if (repeatedName) {
                System.out.println("Repeated name! Final name: " + name);
            }
        }
        return name;
    }

    //Auxiliary method for the creation of a character by the user. Input of the different characteristics between a range
    public static int userInputs (String characteristic, int max, int min){
        int input = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Now choose the " + characteristic + ". It has to be between "+ min+ " and " + max);
        try {
            input = scanner.nextInt();
            //invalid input
            while (input > max || input < min) {
                System.err.println("Invalid value. Please try again");
                input = scanner.nextInt();
            }
        }catch(Exception e){
            System.err.println("Invalid value. Please try again");
            scanner.next();
            input = userInputs(characteristic,max,min);
        }
        return input;
    }

    //method for the selection of a random or customize. Use in team creation and battle.
    public static boolean isRandom(Scanner scanner){
        boolean random = true;
        String option = scanner.nextLine();
        switch (option) {
            case "r" -> System.out.println("You have selected the random option");
            case "c" -> {
                random = false;
                System.out.println("You have to select the option to choose");
            }
            default -> {
                System.err.println("The command entered is not correct. Remember you must write \"r\" or \"c\"");
                random = isRandom(scanner);
            }
        }
        return random;
    }

    //Start battle method
    public static void startBattle(Scanner scanner, boolean random){
        boolean battlehasstart = false;
        while(!battlehasstart) {
            System.out.println("Please write \"start\" to begin the battle:");
            String start = scanner.nextLine();
            if (start.equals("start")) {
                play("piratas.wav");
                if (random){
                    randomBattle(team1, team2);
                } else {
                    battle(scanner, team1, team2);
                }
                battlehasstart = true;
            } else {
                System.err.println("You type the wrong command. Try again");
            }
        }
    }

    //Battle option 1: user select 1 player for each team to battle
    public static void battle(Scanner scanner, List<Character> team1, List<Character> team2){
        while (team1.size() > 0 && team2.size() > 0){
            System.out.println("Select the number of the player from Team 1");
            printPlayers(team1);
            Character c1 = selectPlayer(scanner, team1);

            System.out.println("Select the number of the player from Team 2");
            printPlayers(team2);
            Character c2 = selectPlayer(scanner, team2);

            characterVSCharacter(c1,c2);
        }
        if (team1.size() > 0){
            printWinner("Team 1");
        }else{
            printWinner("Team 2");
        }
    }

    //Auxiliary method for battle option 1. Selection of players
    public static Character selectPlayer(Scanner scanner, List<Character> team){
        int number;
        Character c;
        try {
            number = scanner.nextInt();
            if (number < 1 || number > team.size()){
                System.err.println("The number is not correct. Write a number between 1 and " + team.size());
                c = selectPlayer(scanner, team);
            } else {
                c = team.get(number - 1);
                System.out.println("You have selected " + c.getName());
            }
        } catch (Exception e){
            System.err.println("You have to write a number between 1 and " + team.size());
            scanner.next();
            c = selectPlayer(scanner, team);
        }

        return c;
    }


    //Battle option 2: random selection of the players to battle.
    public static void randomBattle(List<Character> team1, List<Character> team2) {
        while (team1.size() > 0 && team2.size() > 0) {
            //way of getting random numbers (min included and max excluded)
            Character c1 = team1.get((int) (Math.random() * team1.size()));
            System.out.println("From team 1 we have: " + c1.getName());
            Character c2 = team2.get((int) (Math.random() * team2.size()));
            System.out.println("From team 2 we have: " + c2.getName());
            characterVSCharacter(c1,c2);
        }
        if (team1.size() > 0){
            printWinner("Team 1");
        }else{
            printWinner("Team 2");
        }
    }
    /*
    Auxiliary method for Battle: the battle between two characters
    We have assumed always the first parameter will be the character from team 1 and the second the one from team 2
     */
    public static void characterVSCharacter(Character c1, Character c2){
        //number of rounds
        int round = 0;
        //if the players are still alive we keep the battle
        while (c1.isAlive() && c2.isAlive()){
            System.out.println("Round " + round);
            c1.attack(c2);
            c2.attack(c1);
            System.out.println("After this round " + c1.getName() + " has " + c1.getHp() + " lives");
            System.out.println("After this round " + c2.getName() + " has " + c2.getHp() + " lives");
            round++;
            System.out.println();
        }
        //depend on who character reminds alive at the end, if any, we do different things.
        if (c1.isAlive()){
            System.out.println( ANSI_GREEN + c1.getName() + " is the winner" + ANSI_RESET);
            System.out.println();
            sendToGraveyard(c2,team2);
            teamOfDead.add(2);
        } else if (c2.isAlive()){
            System.out.println( ANSI_GREEN + c2.getName() + " is the winner"+ ANSI_RESET);
            System.out.println();
            sendToGraveyard(c1,team1);
            teamOfDead.add(1);
        } else {
            System.out.println(ANSI_RED + "Both have died :(" + ANSI_RESET);
            System.out.println();
            sendToGraveyard(c1,team1);
            teamOfDead.add(1);
            sendToGraveyard(c2,team2);
            teamOfDead.add(2);
        }
    }
    //method that prints the winner
    public static void printWinner(String team){
        System.out.println(ANSI_GREEN_BACKGROUND + team + " is the winner" + ANSI_RESET);
        System.out.println();
        theend();
        play("applause.wav");
        System.out.println();
        System.out.println(ANSI_RED + "But lets pray a moment for our losses: "+ ANSI_RESET);
        showGraveyard();
    }
    //Method that send loser to the graveyard
    public static void sendToGraveyard(Character c, List<Character> team){
        team.remove(c);
        graveyard.add(c);
    }

    //Method that show the graveyard
    public static void showGraveyard(){
        cruz();
        System.out.println();
        System.out.println(ANSI_RED + "In the graveyard we have:" );
        //variable to save the players from each team
        List<Character> team1Deads = new ArrayList<>();
        List<Character> team2Deads = new ArrayList<>();
        for (int i = 0; i < graveyard.size(); i++){
            if (teamOfDead.get(i) == 1){
                team1Deads.add(graveyard.get(i));
            }else {
                team2Deads.add(graveyard.get(i));
            }
        }
        System.out.println();
        System.out.println("From Team 1:");
        printPlayers(team1Deads);
        System.out.println();
        System.out.println("From Team 2:");
        printPlayers(team2Deads);
        System.out.println(ANSI_RESET);
    }
    //method to export/save the party into a csv file
    public static void exportPartyToCSV(List<Character> partyList) throws IOException {
        // create CSVWriter object with filewriter object as parameter
        CSVWriter writer = new CSVWriter(new FileWriter("export-party.csv"),',' ,CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        // adding header to csv
        String[] header = { "Id", "Character Class", "Name","HP","Mana", "Stamina","Strength", "Intelligence"};
        writer.writeNext(header);

        // add data to csv
        for (int i=0;i<partyList.size();i++) {
            String[] data = new String[8];
            data[0] = String.valueOf(partyList.get(i).getId());
            data[1] = partyList.get(i).getClass().getSimpleName().toString();
            data[2] = partyList.get(i).getName();
            data[3] = String.valueOf(partyList.get(i).getHp());
            // need to convert Character into corresponding character subclass in order to access the subclass properties
            if(partyList.get(i) instanceof Wizard){
                Wizard w = (Wizard) partyList.get(i);
                data[4] = String.valueOf(w.getMana());
                data[5] = "0";
                data[6] = "0";
                data[7] = String.valueOf(w.getIntelligence());
            } else if(partyList.get(i) instanceof Warrior){
                Warrior w = (Warrior) partyList.get(i);
                data[4] = "0";
                data[5] = String.valueOf(w.getStamina());
                data[6] = String.valueOf(w.getStrength());
                data[7] = "0";
            }
            writer.writeNext(data);
        }
        // closing writer
        writer.close();
    }

    //method that prints the players.
    public static void printPlayers(List<Character> team){
        int i = 1;
        for (Character c : team){
            String kindOfCharacter = c instanceof Warrior ? "Warrior" : "Wizard";
            System.out.println(i + ". " + c.getName() + " \"" + kindOfCharacter + "\"");
            i++;
        }
    }

    //cross creation
    public static void cruz(){
        Scanner input = new Scanner(System.in);
        int i1, i2;
        char arr[][];
        i1 = 7;
        i2 = 5;
        arr = new char[i1][i2];
        System.out.println();
        System.out.println(ANSI_RED);
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    if (i == 2 || j == arr[0].length/2){
                        arr[i][j] = '*';
                    }else {
                        arr[i][j] = ' ';
                    }
                    System.out.print(arr[i][j]+" ");
                }
                System.out.println();
            }
        System.out.println(ANSI_RESET);

    }

    //things for the sound and the end

    public static void play(String filePath) {
        final File file = new File(filePath);

        try (final AudioInputStream in = getAudioInputStream(file)) {

            final AudioFormat outFormat = getOutFormat(in.getFormat());
            final Info info = new Info(SourceDataLine.class, outFormat);

            try (final SourceDataLine line =
                         (SourceDataLine) AudioSystem.getLine(info)) {

                if (line != null) {
                    line.open(outFormat);
                    line.start();
                    stream(getAudioInputStream(outFormat, in), line);
                    line.drain();
                    line.stop();
                }
            }

        } catch (UnsupportedAudioFileException
                 | LineUnavailableException
                 | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();

        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    public static void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[4096];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }

    public static void theend(){
        System.out.println(ANSI_GREEN +"*******    ****    **    ******");
        System.out.println("**         ** **   **    **    **");
        System.out.println("*******    **  **  **    **     **");
        System.out.println("**         **   ** **    **    **");
        System.out.println("*******    **    ****    ******"+ ANSI_RESET);
    }
}
