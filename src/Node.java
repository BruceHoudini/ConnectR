import java.util.ArrayList;

/**
 * Created by Weston Ford on 2/12/2016.
 */
public class Node {
    private ArrayList<Node> children = new ArrayList<>();
    private int[][] board;
    private int[] friendlyvals;
    private int[] enemyvals;
    private int[] height;
    private int lastmove;
    private boolean winstate = false;
    private int depth, maximinIndex;
    private double maximin;

    private int m, n, r;

    private double score;



    public Node(int m, int n, int r, int depth){
        board = new int[m][n];
        friendlyvals = new int[r];
        enemyvals = new int[r];
        height = new int[n];
        this.m = m;
        this.n = n;
        this.r = r;
        this.depth = depth;
        zeroGameBoard(board, height, friendlyvals, enemyvals);
    }

    public void zeroGameBoard(int[][] board, int[] height, int[] friendlyvals, int[] enemyvals){
        for(int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = 0;
            }
        }

        for(int i = 0; i < n; i++)
            height[i] = 0;
        for(int i = 0; i < r; i++) {
            friendlyvals[i] = 0;
            enemyvals[i] = 0;
        }
    }
    public void clearChildren(){
        while (!children.isEmpty())
            children.remove(0);
    }

    public void addChild(Node child){
        children.add(child);
    }
    public Node getChild(int index){
        return children.get(index);
    }
    public int numChildren(){
        return children.size();
    }

    public double getMaximin(){
        return maximin;
    }
    public int getMaximinIndex(){
        return maximinIndex;
    }
    public void setMaximin(double mm){
        maximin = mm;
    }
    public void setMaximinIndex(int mi){
        maximinIndex = mi;
    }

    public int[][] getBoard(){
        int[][] newboard = new int[m][n];
        newboard = board;
        return newboard;
    }
    public int getBoard(int i, int j){
        return board[i][j];
    }
    public int[] getFVals(){
        return friendlyvals;
    }
    public int getFVals(int i){
        return friendlyvals[i];
    }
    public int[] getEVals(){
        return enemyvals;
    }
    public int getEVals(int i){
        return enemyvals[i];
    }
    public int[] getHeight(){
        return height;
    }
    public int getHeight(int i){ return height[i];};
    public int getLastMove(){
        return lastmove;
    }
    public void modifyFLib(int deltaLib) {
        friendlyvals[0] += deltaLib;
    }
    public void modifyELib(int deltaLib){
        enemyvals[0] += deltaLib;
    }
    public void plusConnect(int index, int player){
        if (player == 1)
            friendlyvals[index]++;
        else
            enemyvals[index]++;
    }
    public void friendlyMove(int column){
        board[height[column]][column] = 1;
        lastmove = column;
        height[column]++;
    }
    public void enemyMove(int column){
        board[height[column]][column] = 2;
        lastmove = column;
        height[column]++;
    }
    public void setScore(double score){
        this.score = score;
    }
    public double getScore(){
        return score;
    }
    public void updateBoard(Node newboard){

        for (int i = 0; i < m; i++){
            for (int j = 0; j < n; j++){
                board[i][j] = newboard.getBoard(i, j);
            }
        }
        for (int i = 0; i < r; i++) {
            friendlyvals[i] = newboard.getFVals(i);
        }
        for (int i = 0; i < r; i++) {
            enemyvals[i] = newboard.getEVals(i);
        }
        for (int i = 0; i < n; i++) {
            height[i] = newboard.getHeight(i);
        }
        if (newboard.getWinState() == false)
            winstate = false;
        else
            winstate = true;
    }
    public int getDepth(){
        return depth;
    }
    public void setDepth(int depth){
        this.depth = depth;
    }
    public void winMove(){
        winstate = true;
    }
    public boolean getWinState(){
        return winstate;
    }

    public void printBoard(){
        for (int i = 0; i < n; i++)
                System.out.print("HC" + i + ": " + height[i] + "_ ");
        System.out.println("");
        for(int i = 0; i < r; i++)
            System.out.print("FVals" + i + ": " + friendlyvals[i] + "_ ");
        System.out.println("");
        for(int i = 0; i < r; i++)
            System.out.print("EVals" + i + ": " + enemyvals[i] + "_ ");
        System.out.println("");


        for(int i = m-1; i >= 0; i--){
            for(int j = 0; j < n; j++){
                System.out.print("|"+board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~");
        System.out.println();
    }

}
