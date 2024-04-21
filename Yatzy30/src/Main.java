import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int alivePlayers = 10;
        Scanner input = new Scanner(System.in);
        int turnNumber = -1;
        System.out.println("Hur många ska spela?");
        int playerCount = input.nextInt();
        int[] playerHealth = new int[playerCount];
        Arrays.fill(playerHealth, 30);
        String[] playerNames = setupGame(input, playerCount);

        while(alivePlayers > 1) {
            if(turnNumber < 0){
                turnNumber = 0;
            }else{
                turnNumber++;
            }
            if (turnNumber >= playerCount){
                turnNumber = 0;
            }
            while (playerHealth[turnNumber] <= 0){
                turnNumber++;
            }
            int sum = dice(input, playerNames, turnNumber);
            int attackInt = calc(playerHealth, turnNumber, sum);
            if (sum > 30) {
                int damage = damage(attackInt);
                if (damage > 0) {
                    attack(damage, input, playerNames, playerHealth);
                }
            }
            standings(playerCount, playerHealth, playerNames);

            alivePlayers = 0;
            for (int i = 0; i < playerCount; i++) {
                if (playerHealth[i] > 0){
                    alivePlayers++;
                }
            }
        }
        for (int i = 0; i < playerCount; i++) {
            if (playerHealth[i] >= 0){
                System.out.println(playerNames[i] + " vann");
            }
        }
    }

    public static String[] setupGame(Scanner input, int playerCount){
        //Skriva in hur många och vilka som spelar
        String[] playerNames = new String[playerCount];
        for(int i = 0; i < playerCount; i++){
            System.out.println("Vad ska spelare " + (i + 1) + " heta?");
            playerNames[i] = input.next();
        }
        return  playerNames;
    }

    public static int dice(Scanner input, String[] playerNames, int turnNumber) {
        System.out.println("Det är " + playerNames[turnNumber] + "s tur");
        int amountOfSavedDice = 0;
        int[] savedDice = new int[6];
        Integer[] dice = new Integer[6];
        int amountOfDice = 6;
        int sum = 0;

        while (amountOfDice > 0) {//Slå tärningarna till man har slut
            String printDice = "";
            Arrays.fill(dice, 0);

            for (int i = 0; i < amountOfDice; i++) {
                dice[i] = (int) ((Math.random() * 6) + 1);//Göra sex tärning
                printDice = printDice + " " + dice[i];
            }

            System.out.println(printDice.substring(1));
            Arrays.sort(dice, Collections.reverseOrder());
            System.out.println("Hur många vill du spara?");
            int saveAmount = input.nextInt();
            if (amountOfDice - saveAmount >= 0){
                for (int i = 0; i < saveAmount; i++) {
                    savedDice[amountOfSavedDice] = dice[i];
                    amountOfSavedDice++;
                }
            }
            amountOfDice = amountOfDice - saveAmount;

        }

        for (int i = 0; i < 6; i++) {//Räkna ihop och se vad som händer
            sum = sum + savedDice[i];
        }
        System.out.println("Du fick: " + sum + " poäng");
        return sum;
    }

    public static int calc(int[] playerHealth, int turnNumber, int sum){
        int attackInt = 0;

        if (sum > 30){
            attackInt = sum - 30;
        }
        if (sum < 30){
            playerHealth[turnNumber] = playerHealth[turnNumber] - (30 - sum); //Om under 30 ta liv av en själv
            if (playerHealth[turnNumber] <= 0){
                System.out.println("Du dog lmao");
            }
        }
        return attackInt;
    }

    public static int damage(int attackInt){ //Om mer än 30 slå för att ta liv av någon
        int damageMult = 0;
        int diceRemove = 0;
        boolean doContinue = true;
        int[] dice = new int[6];
        int amountOfDice = 6;

        System.out.println("Du vill slå " + attackInt);

        while (doContinue) {
            diceRemove = 0;
            String printDice = "";
            doContinue = false;

            for (int i = 0; i < amountOfDice; i++) {
                dice[i] = (int) ((Math.random() * 6) + 1);
            }
            Arrays.sort(dice);
            for (int i = 0; i < amountOfDice; i++) {
                printDice = printDice + " " + dice[i];
            }

            System.out.println(printDice.substring(1));
            for (int i = 0; i < amountOfDice; i++) {
                if (dice[i] > attackInt) {
                    break;
                }
                if (dice[i] == attackInt) {
                    damageMult++;
                    diceRemove++;
                    doContinue = true;
                }
            }
            amountOfDice = amountOfDice - diceRemove;
            if (amountOfDice <= 0){
                doContinue = false;
            }
        }
        int damage = damageMult * attackInt;
        return damage;
    }

    public static int[] attack(int attack, Scanner input, String[] playerNames, int[] playerHealth){
        System.out.println("Du kommer göra " + attack + " skada" + "\nVem vill du attackera? (skriv namnet)");
        String target = input.next();
        int playerNumber = Arrays.asList(playerNames).indexOf(target);
        playerHealth[playerNumber] = playerHealth[playerNumber] - attack;
        if (playerHealth[playerNumber] <= 0){
            System.out.println("Du dog lmao");
        }
        return playerHealth;
    }

    public static void standings(int playerCount, int[] playerHealth, String[] playerNames){
        System.out.println("Standings");
        for (int i = 0; i < playerCount; i++) {
            if (playerHealth[i] > 0) {
                System.out.println(playerNames[i] + " || " + playerHealth[i]);
            } else{
                System.out.println(playerNames[i] + " || DÖD" );
            }
        }
    }
}