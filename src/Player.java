/**
 * Created by Weston Ford on 2/12/2016.
 */


public class Player {
    //Takes row m, column n, max connections r,
    //Along with int color which can be either 1 or 2 representing red or black
    /*private int[][] board;
    private int[] friendlyvals;
    private int[] enemyvals;
    private int[] height;
    */
    private int color;
    private Node board;
    private Node backup;
    private int depth;
    DecisionTree strategist;

    private int m, n, r;

    public Player(int m, int n, int r, int color, int depth){
        /*int[][] board = new int[m][n];
        int[] friendlyvals = new int[r];
        int[] enemyvals = new int[r];
        int[] height = new int[n];*/
        this.m = m;
        this.n = n;
        this.r = r;
        this.color = color;
        board = new Node (m, n, r, 0);
        backup = new Node (m, n, r, 0);
        this.depth = depth;
        strategist = new DecisionTree(2, 0, .01, m, n, r, depth);

    }

    //should eventually return an int
    public int move(int column){
        //backup.updateBoard(board);
        strategist.miniMax(board, column);
        return board.getLastMove();
    }
    //public void undo(){
    //  board = backup;
    //}
    public int firstMove(){
        board.friendlyMove(n/2);
        strategist.evaluateConnections(board);
        board.setScore(strategist.score(board));
        return board.getLastMove();
    }
    public void manualMove(int column){
        board.friendlyMove(column);
        strategist.evaluateConnections(board);
        System.out.println("Values of friendly connections: ");
        for(int i = 0; i < r; i++)
            System.out.print(" " + board.getFVals(i));
        board.setScore(strategist.score(board));
    }
    public void enemyMove(int column){
        board.enemyMove(column);
        strategist.evaluateConnections(board);
        board.setScore(strategist.score(board));
    }
    public boolean winState(){
        return board.getWinState();
    }
    public int getColor(){
        return color;
    }
    public boolean validMove(int column){
        boolean valid;
        int height[] = board.getHeight();
        for (int i = 0; i < n; i++)
        System.out.print("height" + i + ": " + height[i] + "_ ");
        System.out.println();
        System.out.println("Value of m " + m);
        if(height[column] < m-1)
            valid = true;
        else
            valid = false;
        return valid;
    }
    public void printBoard(){
        int[][] display = board.getBoard();
        for(int i = m-1; i >= 0; i--){
            for(int j = 0; j < n; j++){
                System.out.print("|"+display[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~");
        System.out.println();
    }

}
