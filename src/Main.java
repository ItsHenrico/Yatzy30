import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //setupGame();
        //dice();
        calc();
        //Om mer än 30 slå för att ta liv av någon
        //Om under 30 ta liv av en själv
        //Skriv ut hur det ligger till nu
        //Gå till nästa person
        //När enbart en person har mer än 0 liv är det klart
    }
    public static void setupGame(){
        //Skriva in hur många och vilka som spelar
        Scanner input = new Scanner(System.in);
        System.out.println("Hur många ska spela?");
        int playerCount = input.nextInt();
        int[] playerHealth = new int[playerCount];
        String[] playerNames = new String[playerCount];
        for(int i = 0; i < playerCount; i++){
            playerHealth[i] = 30;
            System.out.println("Vad ska spelare " + (i + 1) + " heta?");
            playerNames[i] = input.next();
        }
    }
    public static void dice() {
        Scanner input = new Scanner(System.in);
        int amountOfSavedDice = 0;
        int[] savedDice = new int[6];
        int[] possibleDice = new int[6];
        int[] dice = new int[6];
        int amountOfDice = 6;
        int amount = 0;
        int sum = 0;

        while (amountOfDice > 0) {//Slå tärningarna till man har slut
            String printDice = "";
            Arrays.fill(possibleDice, 0);
            Arrays.fill(dice, 0);
            for (int i = 0; i < amountOfDice; i++) {
                dice[i] = (int) ((Math.random() * 6) + 1);//Göra sex tärning
                printDice = printDice + " " + dice[i];
            }
            System.out.println(printDice.substring(1));

            for (int i = 0; i <= amountOfDice-1; i++) {
                if (input.hasNextInt()) {
                    possibleDice[i] = input.nextInt();//Låt en välja vilka man ska spara
                    amount++;
                }
            }

            Arrays.sort(dice);
            Arrays.sort(possibleDice);
            for (int i = 0; i < 6; i++) {
                if (amountOfSavedDice == 6){
                    break;
                }
                if (dice[i] == possibleDice[i]) {
                    savedDice[amountOfSavedDice] = possibleDice[i];
                    amountOfSavedDice++;
                }
                if (dice[i] < possibleDice[i]) {
                    System.out.println("Dumfan");
                    break;
                }
                if (dice[i] > possibleDice[i] && possibleDice[i] != 0) {
                    System.out.println("bro...");
                }
            }
            for (int i = 0; i < 6; i++) {
                System.out.println(savedDice[i]);
            }

            amountOfDice = amountOfDice - amountOfSavedDice;
        }
        for (int i = 0; i < 6; i++) {//Räkna ihop och se vad som händer
            sum = sum + savedDice[i];
        }
    }
    public static void calc(){
        int attack = 0;
        int sum = (int)((Math.random() * 6) + 1);
        if (sum > 30){
            attack = sum - 30;
        }
        if (sum < 30){
            playerHealth[turnNumber]
        }
    }
}