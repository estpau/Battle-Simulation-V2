
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


public class Battle {
    private static List<Character> team1 = new ArrayList<>();
    private static List<Character> team2 = new ArrayList<>();
    private static List<Character> graveyard = new ArrayList<>();
    private static List<Integer> teamOfDead = new ArrayList<>();
    //private final static int team_size = 5;
    private static int teamSize;

    public static int getTeamSize() {
        return teamSize;
    }

    public static void setTeamSize(int teamSize) {
        Battle.teamSize = teamSize;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Tournament of Terror. Before starting the battle you have to define the team size.");
        System.out.println("Teams cannot be longer than 10. Please insert the size: ");

        setTeamSize(defineTeamSize(scanner));

        System.out.println("Your team has " + teamSize + " players.");
        team2 = createPartyFromCSV(teamSize);

        System.out.println("Now you have to select your team members");
        System.out.println("Select the players. Type \"r\" to randomize or \"c\" to customize your players");
        scanner.nextLine();

        if(isRandom(scanner)){
            team1 = createRandomParty(teamSize);
        } else {
            team1 = userMakeaParty(teamSize);
        }

        System.out.println("Your team has the following players: ");
        printPlayers(team1);
        System.out.println();

        System.out.println("Your team has been saved for future battles in a CSV file");
        System.out.println();

        try {
            exportPartyToCSV(team1);
        } catch (Exception ex){
            System.out.println("Up!!!");
        }

        System.out.println("Your team is going to battle against: ");
        printPlayers(team2);
        System.out.println();

        System.out.println("The fight will start soon !!!");
        System.out.println("Now, type \"r\" for a random battle or \"c\" " +
                "to choose the players that will face each other");

        startBattle(scanner, isRandom(scanner));

    }

    //============ Methods ===================

    //Start battle method
    public static void startBattle(Scanner scanner, boolean random){
        boolean battlehasstart = false;
        while(!battlehasstart) {
            System.out.println("Please write start to begin the battle:");
            String start = scanner.nextLine();
            if (start.equals("start")) {
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

    //Method that permit select one by one player from each team
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

    //method that creates random players
    public static List<Character> createRandomParty(int team_size) throws FileNotFoundException {
        List<Character> randomCharacters = new ArrayList<>();
        String[] names = {"Paula", "Monica", "Yanira", "Esteban", "James", "Lucas", "Sebastian",
                "Carlos", "Julia", "Pilar", "Felipe", "Raymond", "Shaun"};
        for (int i = 0; i < team_size; i++){
            int id = i;
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
                Warrior player_warrior = new Warrior (id, name, hp, stamina, strength);
                randomCharacters.add(player_warrior);
            }else {
                //randomly creation of hp
                hp = (int)(Math.random()* (101 -50) +50);
                //randomly creation of mana
                int mana = (int)(Math.random()* (51 -10) +10);
                //randomly creation of intelligence
                int intelligence = (int)(Math.random()* (51 -1) +1);
                Wizard player_wizard = new Wizard (id,name, hp, mana, intelligence);
                randomCharacters.add(player_wizard);
            }
        }
        return randomCharacters;
    }

    //method that creates parties from a csv
    public static List<Character> createPartyFromCSV(int size) throws FileNotFoundException {
        List<Character> csvCharacters = new ArrayList<>();
        Scanner scanner = new Scanner(new File("characters.csv"));
        String header = scanner.nextLine();
        while(scanner.hasNextLine()){
            String[] attributes = scanner.nextLine().split(";");
            int id = Integer.parseInt(attributes[0]);
            String characterClass = attributes[1];
            String name = attributes[2];
            int hp = Integer.parseInt(attributes[3]);
            int mana = Integer.parseInt(attributes[4]);
            int stamina = Integer.parseInt(attributes[5]);
            int strength = Integer.parseInt(attributes[6]);
            int intelligence = Integer.parseInt(attributes[7]);
            //int random = (int) Math.floor(Math.random()*2+1);
            if(characterClass.equals("Warrior")){
                csvCharacters.add(new Warrior(id,name,hp,stamina,strength));
            } else {
                csvCharacters.add(new Wizard(id,name,hp,mana,intelligence));
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

    //method that select to random player from each team and make them battle
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

    public static void printWinner(String team){

        System.out.println(team + " is the winner");
        System.out.println();
        System.out.println("But lets pray a moment for our losses: ");
        showGraveyard();
    }

    //method that makes the battle between 2 players
    //I have assumed always the first parameter will be the character from team 1 and the second the one from team 2
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
        if (c1.isAlive()){
            System.out.println(c1.getName() + " is the winner");
            sendToGraveyard(c2,team2);
            teamOfDead.add(2);
        } else if (c2.isAlive()){
            System.out.println(c2.getName() + " is the winner");
            sendToGraveyard(c1,team1);
            teamOfDead.add(1);
        } else {
            System.out.println("Both have died :(");
            System.out.println();
            sendToGraveyard(c1,team1);
            teamOfDead.add(1);
            sendToGraveyard(c2,team2);
            teamOfDead.add(2);
        }
    }

    //Method that send loser to the graveyard
    public static void sendToGraveyard(Character c, List<Character> team){
        team.remove(c);
        graveyard.add(c);
    }

    //This method define the size of the teams
     public static int defineTeamSize(Scanner scanner){
        int teamSize = 0;
        try {
            teamSize = scanner.nextInt();
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


    //Method that defines if the fight will be random or the players will be chosen one by one
    public static boolean ChosePlayersOrNot(Scanner scanner){
        boolean random = true;
        String option = scanner.nextLine();
        switch (option){
            case "r":
                System.out.println("You have selected the random option");
                break;
            case "p":
                random = false;
                System.out.println("You have to select the opponents from both teams");
                break;
            default:
                System.err.println("The command entered is not correct. Remember you must write \"r\" or \"p\"");
                random = ChosePlayersOrNot(scanner);
                break;
        }
        return random;
    }

    //Method that show the graveyard
    public static void showGraveyard(){
        System.out.println("In the graveyard we have: ");
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
        System.out.println("From team 1: ");
        for (int i = 0; i < team1Deads.size(); i++) {
            System.out.println(team1Deads.get(i).toString());
        }
        System.out.println();
        System.out.println("From team 2: ");
        for (int i = 0; i < team2Deads.size(); i++) {
            System.out.println(team2Deads.get(i).toString());
        }
        System.out.println();

    }

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


    //method for the user to create the players of the team 1
    public static List<Character> userMakeaParty(int team_size){
        List<Character> choosenCharacters = new ArrayList<>();
        List<String> teamNames = new ArrayList<>();
        for (int i = 0; i< team_size; i++){
            int id = i+1;
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
                //if repeated name: JR add at the end

                teamNames.add(name);
                //hp selection [100,200]
                int hp = userInputs("Health", 200,100);
                //stamina selection [10,50]
                int stamina =  userInputs("Stamina", 50,10);
                //strength selection [1,10]
                int strength = userInputs("Strength", 10,1);
                Warrior player = new Warrior(id, name, hp, stamina, strength);
                choosenCharacters.add(player);
                if (i != team_size-1 ) {
                    System.out.println("Great! Let's create another character");
                }
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
                Wizard player = new Wizard(id, name, hp,mana, intelligence);
                choosenCharacters.add(player);
                if (i != team_size-1 ) {
                    System.out.println("Great! Let's create another character");
                }
            //invalid input
            }else{
                System.err.println("Please select a valid type. Write Warrior or Wizard");
                i--;
            }
        }

        /*
        //Show the team 1 I don´t know if it was needed, but I added it just in case
        System.out.println("Process Complete! Team 1 has the following members:");
        for (int i = 0; i < choosenCharacters.size(); i++) {
            System.out.println(choosenCharacters.get(i).toString());
        }

         */
        return choosenCharacters;

    }

    public static int userInputs (String characteristic, int max, int min){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Now choose the " + characteristic + ". It has to be between "+ min+ " and " + max);
        String input =scanner.next();
        int inputInt = Integer.parseInt(input);
        while(inputInt > max || inputInt < min) {
            System.err.println("Invalid value. Please try again");
            input = scanner.next();
            inputInt = Integer.parseInt(input);
        }
        return inputInt;
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

    public static void printPlayers(List<Character> team){
        int i = 1;
        for (Character c : team){
            String kindOfCharacter = c instanceof Warrior ? "Warrior" : "Wizard";
            System.out.println(i + ". " + c.getName() + " \"" + kindOfCharacter + "\"");
            i++;
        }
    }

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


}
