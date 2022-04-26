
import java.io.File;
import java.io.FileNotFoundException;
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
        System.out.println("Now you have to select your team members");

        //Una vez definida la cantidad de integrantes de los equipos

        //Se llama a la funcion que esta haciendo Pilar.

        //Se seleccionan los miembros del equipo contrario random:
        System.out.println(createRandomParty(teamSize));
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
                    battle(team1, team2);
                }
                battlehasstart = true;
            } else {
                System.err.println("You type the wrong command. Try again");
            }
        }
    }

    //Method that permit select one by one player from each team
    public static void battle(List<Character> team1, List<Character> team2){
        //..........
    }


    //method that creates random players
    public static List<Character> createRandomParty(int team_size) throws FileNotFoundException {
        List<Character> randomCharacters = new ArrayList<>();
        String[] names = {"Paula", "Monica", "Yanira", "Esteban", "James", "Lucas", "Sebastian",
                "Carlos", "Julia", "Pilar"};
        int randomIndex;
        String temp;
        Random r = new Random();
        // hacer el ordenamiento aleatorio del array de nombres5
        for(int i = 0; i < names.length; i++) {
            // generar numero aleatorio entre 0 y array.length
            randomIndex = r.nextInt(names.length);
            // hacer el intercambio entre el nombre en posicion i y randomIndex
            temp = names[randomIndex];
            names[randomIndex] = names[i];
            names[i] = temp;

            int id = 0+i;
            String name = names[i];
            Random ran=new Random();
            int mana=ran.nextInt(50)+10;
            int stamina = ran.nextInt(50)+10;
            int strength = ran.nextInt(10)+1;
            int intelligence = ran.nextInt(50)+1;
            int hp = ran.nextInt(200)+50;
            if (hp< 100) {
                randomCharacters.add(new Wizard(id, name, hp, mana, intelligence));
            } else if (hp > 100) {
                randomCharacters.add(new Warrior(id, name, hp, stamina, strength));
            }
            Collections.shuffle(randomCharacters);
        }
        List<Character> team1 = new ArrayList<>();

        for (int j = 0; j < team_size; j++) {
            team1.add(randomCharacters.get(j));
        }
        return team1;
    }


    //method that creates parties from a csv
    public static List<Character> createPartyFromCSV(int team_size) throws FileNotFoundException {
        List<Character> csvCharacters = new ArrayList<>();
        Scanner scanner = new Scanner(new File("characters.csv"));
        String header = scanner.nextLine();
        while(scanner.hasNextLine()){
            String[] attributes = scanner.nextLine().split(";");
            int id = Integer.parseInt(attributes[0]);
            String name = attributes[1];
            int hp = Integer.parseInt(attributes[2]);
            int mana = Integer.parseInt(attributes[3]);
            int stamina = Integer.parseInt(attributes[3]);
            int strength = Integer.parseInt(attributes[4]);
            int intelligence = Integer.parseInt(attributes[4]);
            int random = (int) Math.floor(Math.random()*2+1);
            if(random == 1){
                csvCharacters.add(new Warrior(id,name,hp,stamina,strength));
            } else {
                csvCharacters.add(new Wizard(id,name,hp,mana,intelligence));
            }
        }
        scanner.close();
        List<Character> team = new ArrayList<>();
        Collections.shuffle(csvCharacters);
        for(int i = 0; i<team_size; i++){
            team.add(csvCharacters.get(i));
        }
        return team;
    }

    //method that select to random player from each team and make them battle
    public static void randomBattle(List<Character> team1, List<Character> team2) {
        while (team1.size() > 0 && team2.size() > 0) {
            //way of getting random numbers (min included and max excluded)
            Character c1 = team1.get((int) Math.random() * (team1.size() - 0) + 0);
            System.out.println("From team 1 we have: " + c1.getName());
            Character c2 = team2.get((int) Math.random() * (team1.size() - 0) + 0);
            System.out.println("From team 2 we have: " + c2.getName());
            CharacterVSCharacter(c1,c2);
        }
        if (team1.size() > 0){
            System.out.println("Team 1 is the winner"); //Your team !!!
            System.out.println("But lets pray a moment for our losses: ");
            //maybe here we could show the graveyard but i am done for the day
            showGraveyard();
        }else{
            System.out.println("Team 2 is the winner");
            System.out.println("But lets pray a moment for our losses: ");
            //maybe here we could show the graveyard but i am done for the day
            showGraveyard();
        }
    }

    //method that makes the battle between 2 players
    //I have assumed always the first parameter will be the character from team 1 and the second the one from team 2
    public static void CharacterVSCharacter(Character c1, Character c2){
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
            System.out.println(c2.getName() + " is the winner");
            sendToGraveyard(c2,team2);
            teamOfDead.add(2);
        } else if (c2.isAlive()){
            System.out.println(c1.getName() + " is the winner");
            sendToGraveyard(c1,team1);
            teamOfDead.add(1);
        } else {
            System.out.println("Both have died :(");
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
        System.out.println("From team 1: ");
        for (int i = 0; i < team1Deads.size(); i++) {
            System.out.println(graveyard.get(i).toString());
        }
        System.out.println("From team 2: ");
        for (int i = 0; i < team2Deads.size(); i++) {
            System.out.println(graveyard.get(i).toString());
        }
    }

}
