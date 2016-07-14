import java.util.Scanner;

/**
 * Created by Weston Ford on 2/12/2016.
 */
public class GameDriver {
    public static void main(String[] args) {
        int m, n, r, depth, color = 1;
        int numhumans;
        int move1, move2;
        Player player1;
        Player player2;

        //m = 8;
        //n = 7;
        //r = 4;
        //depth = 5;

        Scanner in = new Scanner(System.in);

        String option = "";

        System.out.println("How many rows?");
        option = in.nextLine();
        m = Integer.valueOf(option);

        System.out.println("How many columns?");
        option = in.nextLine();
        n = Integer.valueOf(option);

        System.out.println("What is the value of R?");
        option = in.nextLine();
        r = Integer.valueOf(option);

        //Should be odd.
        System.out.println("What is the max search depth?");
        option = in.nextLine();
        depth = Integer.valueOf(option);

        System.out.println("How many human players (0, 1, or 2)?");
        option = in.nextLine();
        numhumans = Integer.valueOf(option);

        if (numhumans == 1){
            System.out.println("Would you like to be red (first move) or black (second move)?");
            option = in.nextLine();
            if(option.equals("red")){
                player1 = new Player(m, n, r, 1, depth);
                player2 = new Player(m, n, r, 2, depth);
            }
            else{
                player1 = new Player(m, n, r, 2, depth);
                player2 = new Player(m, n, r, 1, depth);
            }
        }
        else{
            player1 = new Player(m, n, r, 1, depth);
            player2 = new Player(m, n, r, 2, depth);
        }

        if(player1.getColor() == 1){
            System.out.println("Player 1 Select Column:");
            option = in.nextLine();
            move1 = Integer.valueOf(option);
            if(move1 >= n || move1 < 0) {
                System.out.println("invalid column, moving automatically");
                move1 = n/2;
            }
            player1.manualMove(move1);
            player1.printBoard();
            player1.enemyMove(player2.move(move1));
            player2.printBoard();
        }
        else{
            player1.enemyMove(player2.firstMove());
        }

        System.out.println("player1 win state: " + player1.winState());
        System.out.println("player2 win state: " + player2.winState());

        while(!player1.winState() && !player2.winState()){
        //while(true){
            System.out.println("Player 1 Select Column:");
            option = in.nextLine();
            move1 = Integer.valueOf(option);
            if(player1.validMove(move1)) {
                player1.manualMove(move1);
            }
            else {
                while (!player1.validMove(move1)) {
                    System.out.println("The column: " + move1 + ", is invalid, select again");
                    option = in.nextLine();
                    move1 = Integer.valueOf(option);
                }
            }
            if(player1.winState() == true) {
                System.out.println("PLAYER 1 IS THE WINNER");
            }
            else {
                player1.enemyMove(player2.move(move1));
                player1.printBoard();
            }
            player2.printBoard();

            if(player2.winState() == true)
                System.out.println("PLAYER 2 IS THE WINNER");
        }

    }
}
