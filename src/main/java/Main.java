import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean play = true;
        while (play) {
            Scanner scanner = new Scanner(System.in);
            Battle b = new Battle();

            System.out.println("Welcome to the Tournament of Terror. Before starting the battle you have to define the team size.");
            System.out.println("Teams cannot be longer than 10. Please insert the size: ");

            b.setTeamSize(b.defineTeamSize(scanner));

            System.out.println("Your team has " + b.getTeamSize() + " players.");
            try {
                b.setTeam2(b.createPartyFromCSV(b.getTeamSize()));
            }catch(Exception e){
                System.out.println("something went wrong");
            }

            System.out.println("Now you have to select your team members");
            System.out.println("Select the players. Type \"r\" to randomize or \"c\" to customize your players");
            scanner.nextLine();
            if (b.isRandom(scanner)) {
                b.setTeam1(b.createRandomParty(b.getTeamSize()));
            } else {
                b.setTeam1(b.userMakeaParty(b.getTeamSize()));
            }

            System.out.println("Your team has the following players: ");
            b.printPlayers(b.getTeam1());
            System.out.println();
            System.out.println("Your team has been saved for future battles in a CSV file");
            System.out.println();

            try {
                b.exportPartyToCSV(b.getTeam1());
            } catch (Exception ex) {
                System.out.println("Up!!!");
            }

            System.out.println("Your team is going to battle against: ");
            b.printPlayers(b.getTeam2());
            System.out.println();

            System.out.println("The fight will start soon !!!");
            System.out.println("Now, type \"r\" for a random battle or \"c\" " +
                    "to choose the players that will face each other");

            b.startBattle(scanner, b.isRandom(scanner));

            //keep playing or not
            System.out.println("Would you like to run another battle?[yes/no]");
            String answer = scanner.next();
            boolean answer_notvalid = true;
            while (answer_notvalid) {
                if (answer.equalsIgnoreCase("yes")) {
                    System.out.println("Leeeet´s go");
                    answer_notvalid = false;
                } else if (answer.equalsIgnoreCase("no")) {
                    System.out.println("No worry! We will miss you :( But thanks for playing <3");
                    play = false;
                    answer_notvalid = false;
                } else {
                    System.err.println("WRONG! Write only YES or NO");
                    answer = scanner.next();
                }
            }
        }
    }
}
