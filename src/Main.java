import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int alivePlayers = 10; //sätter den till ett tal högre än 1 så loopen kan börja
        Scanner input = new Scanner(System.in);
        int turnNumber = -1;//sätter den till -1 så första rundan fungerar
        System.out.println("Hur många ska spela?");
        int playerCount = input.nextInt();
        int[] playerHealth = new int[playerCount];//gör en array som är lika lång som antalet spelare
        Arrays.fill(playerHealth, 30);//fyller arrayen med antalet liv

        String[] playerNames = setupGame(input, playerCount);

        while(alivePlayers > 1) {//kollar så antalet spelare som lever är mer än 1
            if(turnNumber < 0) {//ökar turnnumber med 1 om det inte är på den första rundan
                turnNumber = 0;
            }else{
                turnNumber++;
            }
            if (turnNumber >= playerCount){//sätter den till 0 om man har övergått antalet spelare
                turnNumber = 0;
            }
            while (playerHealth[turnNumber] <= 0){//hoppar spelare som är döda
                turnNumber++;
            }
            int sum = dice(input, playerNames, turnNumber);
            int attackInt = calc(playerHealth, turnNumber, sum);
            if (sum > 30) {//hoppar attack delen om man inte fick högre än 30
                int damage = damage(attackInt);
                if (damage > 0) {//ser till att du faktiskt kommer att göra skada
                    attack(damage, input, playerNames, playerHealth);
                }
            }
            standings(playerCount, playerHealth, playerNames);

            alivePlayers = 0;
            for (int i = 0; i < playerCount; i++) {//kollar hur många som lever
                if (playerHealth[i] > 0){
                    alivePlayers++;
                }
            }
        }
        for (int i = 0; i < playerCount; i++) {//skriver ut den som vann
            if (playerHealth[i] > 0){
                System.out.println("\n\n\n" + playerNames[i] + " vann");
            }
        }
    }

    public static String[] setupGame(Scanner input, int playerCount){
        //Skriva in hur många och vilka som spelar
        boolean flag;
        String[] playerNames = new String[playerCount];//Gör en array med alla namn
        for(int i = 0; i < playerCount; i++){
            System.out.println("\nVad ska spelare " + (i + 1) + " heta?");
            do{
                flag = false;
                playerNames[i] = input.next();
                for (int j = 0; j < i; j++) {//kollar så namnet inte redan finns
                    if (Objects.equals(playerNames[i], playerNames[j])) {
                        flag = true;
                        playerNames[i] = "";
                        System.out.println("Det namnet finns redan");
                    }
                }
            }while(flag);
        }
        return playerNames;
    }

    public static int dice(Scanner input, String[] playerNames, int turnNumber) {
        System.out.println("\nDet är " + playerNames[turnNumber] + "s tur");
        int amountOfSavedDice = 0;
        int[] savedDice = new int[6];
        Integer[] dice = new Integer[6];//måste skriva Integer annars fungerar inte sorteringen i fallande ordning av någon anledning
        int amountOfDice = 6;
        int sum = 0;

        while (amountOfDice > 0) {//Slå tärningarna till man har slut
            int saveAmount;
            String printDice = "";
            Arrays.fill(dice, 0);//resetar tärningarna

            for (int i = 0; i < amountOfDice; i++) {
                dice[i] = (int) ((Math.random() * 6) + 1);//Göra sex tärning
            }
            Arrays.sort(dice, Collections.reverseOrder());//måste sortera fallande så nollan i den inte använda delen av arrayen kommer sist
            for (int i = 0; i < amountOfDice; i++) {
                printDice = printDice + " " + dice[i];//gör så printingen händer på en rad
            }

            System.out.println("\nSlag:\n" + printDice.substring(1) + "\nHur många vill du spara?");
            do {//ser till att du väljer mer än 0 tärning
                saveAmount = input.nextInt();
                if (saveAmount == 0){
                    System.out.println("\nDu måste skriva ett tal högre än 0");
                }
            }while(saveAmount <= 0);
            if (amountOfDice - saveAmount >= 0){
                for (int i = 0; i < saveAmount; i++) {//sparar de största tärningarna
                    savedDice[amountOfSavedDice] = dice[i];
                    amountOfSavedDice++;
                }
            }
            amountOfDice = amountOfDice - saveAmount;//räknar ut hur många tärningar som ska slås nästa gång

        }

        for (int i = 0; i < 6; i++) {
            sum = sum + savedDice[i];//räknar ut summan av tärningskasten
        }
        System.out.println("\nDu fick: " + sum + " poäng");
        return sum;
    }

    public static int calc(int[] playerHealth, int turnNumber, int sum){
        int attackInt = 0;

        if (sum > 30){
            attackInt = sum - 30;//räknar ut vilket nummer man vill slå
        }
        if (sum < 30){
            playerHealth[turnNumber] = playerHealth[turnNumber] - (30 - sum); //Om under 30 ta liv av en själv
            if (playerHealth[turnNumber] <= 0){//Om man får slut på liv dör man
                System.out.println("\nDu dog lmao");
            }
        }
        return attackInt;
    }

    public static int damage(int attackInt){ //Om mer än 30 slå för att ta liv av någon
        int damageMult = 0;
        int diceRemove;
        boolean doContinue = true;
        int[] dice = new int[6];
        int amountOfDice = 6;

        System.out.println("\nDu vill slå " + attackInt);

        while (doContinue) {//loopar slagen
            diceRemove = 0;
            String printDice = "";
            doContinue = false;

            for (int i = 0; i < amountOfDice; i++) {
                dice[i] = (int) ((Math.random() * 6) + 1);//slår tärningar
            }
            Arrays.sort(dice);
            for (int i = 0; i < amountOfDice; i++) {
                printDice = printDice + " " + dice[i];
            }

            System.out.println(printDice.substring(1));
            for (int i = 0; i < amountOfDice; i++) {
                if (dice[i] > attackInt) {//kollar om talet som du är på är högre än vad du vill ha och avslutar då loopen
                    break;
                }
                if (dice[i] == attackInt) {
                    damageMult++;//lägger till ett till hur många av talet du fick
                    diceRemove++;//tar bort saker
                    doContinue = true;//ser till att du slog ett av talet du ville ha och gör att den slår igen
                }
            }
            amountOfDice = amountOfDice - diceRemove;//räknar ut hur många tärningar du har kvar
            if (amountOfDice <= 0){//avslutar om man får slut på tärningar
                doContinue = false;
            }
        }
        return damageMult * attackInt;//räknar ut hur mycket skada du ska göra
    }

    public static void attack(int attack, Scanner input, String[] playerNames, int[] playerHealth){
        System.out.println("\nDu kommer göra " + attack + " skada" + "\nVem vill du attackera? (skriv namnet)");
        String target = input.next();
        int playerNumber = Arrays.asList(playerNames).indexOf(target);//tar vilket nummer i arrayen som den attackerade spelaren är
        playerHealth[playerNumber] = playerHealth[playerNumber] - attack;//tar bort livet från spelaren
        if (playerHealth[playerNumber] <= 0){//kollar om hen dog
            System.out.println("Du dog lmao");
        }
    }

    public static void standings(int playerCount, int[] playerHealth, String[] playerNames){
        System.out.println("\nStandings");//skriver ut hur läget i spelet är
        for (int i = 0; i < playerCount; i++) {
            if (playerHealth[i] > 0) {
                System.out.println(playerNames[i] + " || " + playerHealth[i]);
            } else{
                System.out.println(playerNames[i] + " || DÖD" );
            }
        }
    }
}
