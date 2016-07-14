import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weston Ford on 2/12/2016.
 */
public class DecisionTree {

    private double x, y, l;
    private double[] thetas;
    private int m, n, r;
    private int loopcount = 0;
    private int depth;
    private List<Node> queue = new ArrayList<>();
    private int minimax;

    public DecisionTree(double x, double y, double l, int m, int n, int r, int depth){
        this.x = x;
        this.y = y;
        this.l = l;
        this.m = m;
        this.n = n;
        this.r = r;
        this.depth = depth;

        thetas = new double[r];
        setThetas();
    }

    private void setThetas(){
        thetas[0] = l;
        for (int i = 1; i < r; i++){
            thetas[i] = 1.0/(Math.pow(x, r - i) * Math.pow(r - 1, y));
        }
    }


    public void miniMax(Node parent, int column){
        double maximinal;
        parent.enemyMove(column);
        evaluateConnections(parent);
        parent.setScore(score(parent));
        parent.setDepth(parent.getDepth());
        buildSubtree(parent, 1);
        maximinal = depthFirstAssess(parent);
        int count = 0;
        int index;
        System.out.println("parent after depthFirstAssess");
        parent.printBoard();
        while (count < parent.numChildren()){
            if (parent.getChild(count).getWinState() == true){
                parent.updateBoard(parent.getChild(count));
            }
            else if(parent.getChild(count).getMaximin() == maximinal){
                parent.updateBoard(parent.getChild(count));
                parent.clearChildren();
            }
            count++;
        }
    }
    public double depthFirstAssess(Node parent){
        int index;
        double maximinal;
        double temp;
        int counter = parent.numChildren();
        if(parent.numChildren() == 1) {
            maximinal = depthFirstAssess(parent.getChild(0));
            parent.setMaximin(maximinal);
        }
        else if(parent.numChildren() > 1) {
            maximinal = depthFirstAssess(parent.getChild(0));
            for (int i = 1; i < counter; i++) {
                temp = depthFirstAssess(parent.getChild(i));
                if (temp < maximinal){
                    maximinal = temp;
                }
            }
            parent.setMaximin(maximinal);
        }
        else if(parent.numChildren() == 0) {
            return parent.getScore();
        }
        return parent.getMaximin();

    }
    public void buildSubtree(Node parent, int movetype){

        int[] height = parent.getHeight();
        if (parent.getDepth() < depth) {
            for (int j = 0; j < n; j++) {
                if (height[j] < m) {
                    Node child = new Node(m, n, r, parent.getDepth() + 1);
                    child.updateBoard(parent);
                    if (movetype % 2 == 1) {
                        child.friendlyMove(j);
                    } else if (movetype % 2 == 0) {
                        child.enemyMove(j);
                    }
                    evaluateConnections(child);
                    child.setScore(score(child));
                    parent.addChild(child);


                    //child.printBoard();


                    buildSubtree(child, movetype+1);
                }
            }
        }
    }
    public void evaluateConnections(Node current){
        int[][] board = current.getBoard();
        int[] height = current.getHeight();
        int lastCol = current.getLastMove();
        int lastRow = height[lastCol]-1;
        int player = board[lastRow][lastCol];
        int vert = 0, left = 0, right = 0, topleftdiag = 0, botleftdiag = 0, toprightdiag = 0, botrightdiag = 0, liberty = 0, enemyliberty = 0;
        int leftdiag = 0, rightdiag = 0, horizontal = 0, vertical = 0;

        int i = lastRow;
        int j = lastCol;
        int temp;

            vert = checkBot(board, i, j, player);

            botleftdiag = checkBotLeft(board, i, j, player);
            left = checkLeft(board, i, j, player);
            topleftdiag = checkTopLeft(board, i, j, player);

            botrightdiag = checkBotRight(board, i, j, player);
            right = checkRight(board, i, j, player);
            toprightdiag = checkTopRight(board, i, j, player);

            if(botleftdiag == -1){
                enemyliberty--;
            }
            else if(botleftdiag > 0){
                liberty--;
                rightdiag+=botleftdiag;
            }
            else if(botleftdiag == 0){
                if (vert < 1 && vert != -2) {
                    liberty++;
                }
            }

            if(left == -1){
                enemyliberty--;
            }
            else if(left > 0){
                liberty--;
                horizontal+=left;
            }
            else if(left == 0){
                if (botleftdiag < 1 && botleftdiag != -2) {
                    liberty++;
                }
            }

            if(topleftdiag == -1){
                enemyliberty--;
            }
            else if(topleftdiag > 0){
                liberty--;
                leftdiag+=topleftdiag;
            }
            else if(topleftdiag == 0){
                if (left < 1 && left != -2) {
                    liberty++;
                }
            }
            if(botrightdiag == -1){
                enemyliberty--;
            }
            else if(botrightdiag > 0){
                liberty--;
                leftdiag+=botrightdiag;
            }
            else if(botrightdiag == 0){
                if (vert < 1 && vert != -2) {
                    liberty++;
                }
            }
            if(right == -1){
                enemyliberty--;
            }
            else if(right > 0){
                liberty--;
                horizontal+=right;
            }
            else if(right == 0){
                if (botrightdiag < 1 && botrightdiag != -2) {
                    liberty++;
                }
            }
            if(toprightdiag == -1){
                enemyliberty--;
            }
            else if(toprightdiag > 0){
                liberty--;
                rightdiag+=toprightdiag;
            }
            else if(toprightdiag == 0){
                if (right < 1 && right != -2){
                    liberty++;
                }
            }
            if(vert == -1){
                enemyliberty--;
            }
            else if(vert > 0){
                liberty--;
                vertical+=vert;
            }

        if(i < m-1) {
            if(left < 1 && right < 1 && topleftdiag < 1 && toprightdiag < 1) {
                liberty++;
            }
        }

        if (player == 1) {
            current.modifyFLib(liberty);
            current.modifyELib(enemyliberty);
        }
        else{
            current.modifyFLib(enemyliberty);
            current.modifyELib(liberty);
        }

        if(vertical == 0 && horizontal == 0 && leftdiag == 0 && rightdiag == 0){
            current.plusConnect(1, player);
        }
        else{
            if (vertical > 0){
                if(vertical + 1 >= r){
                    current.winMove();
                }
                else {
                    current.plusConnect(vertical, player);
                }
            }
            if (horizontal > 0){
                if(horizontal + 1 >= r){
                    current.winMove();
                }
                else
                    current.plusConnect(horizontal, player);
            }
            if (leftdiag > 0) {
                if(leftdiag + 1 >= r)
                    current.winMove();
                else
                    current.plusConnect(leftdiag, player);
            }
            if (rightdiag > 0) {
                if(rightdiag + 1 >= r)
                    current.winMove();
                else
                    current.plusConnect(rightdiag, player);
            }
        }
    }
    private int checkBot(int[][] board, int lastRow, int lastCol, int player){
        int bot = 0;
        int i = lastRow;
        int j = lastCol;

        if (i > 0 && board[i-1][j] != 0 && board[i-1][j] != player)
            bot = -1;
        else if (i == 0)
            bot = -2;
        else {
            while (i > 0 && board[i - 1][j] == player) {
                bot++;
                i--;
            }
        }
        return bot;
    }
    private int checkBotLeft(int[][] board, int lastRow, int lastCol, int player){
        int botleft = 0;
        int i = lastRow;
        int j = lastCol;
        if (i > 0 && j > 0 && board[i - 1][j - 1] != player && board[i - 1][j - 1] != 0)
            botleft = -1;
        else if (i == 0 || j == 0)
            botleft = -2;
        else {
            while (i > 0 && j > 0 && board[i - 1][j - 1] == player) {
                botleft++;
                i--;
                j--;
            }
        }
        return botleft;
    }
    private int checkLeft(int[][] board, int lastRow, int lastCol, int player){
        int left = 0;
        int i = lastRow;
        int j = lastCol;

        if (j > 0 && board[i][j - 1] != player && board[i][j - 1] != 0)
            left = -1;
        else if (j == 0)
            left = -2;
        else {
            while (j > 0 && board[i][j - 1] == player) {
                left++;
                j--;
            }
        }
        return left;
    }
    private int checkTopLeft(int[][] board, int lastRow, int lastCol, int player){
        int topleft = 0;
        int i = lastRow;
        int j = lastCol;
        if (i < m-1 && j > 0 && board[i + 1][j - 1] != player && board[i + 1][j - 1] != 0)
            topleft = -1;
        else if (i == m-1 || j == 0)
            topleft = -2;
        else {
            while (i < m-1 && j > 0 && board[i + 1][j - 1] == player) {
                topleft++;
                i++;
                j--;
            }
        }
        return topleft;
    }
    private int checkBotRight(int[][] board, int lastRow, int lastCol, int player){
        int botright = 0;
        int i = lastRow;
        int j = lastCol;
        if (i > 0 && j < n-1 && board[i - 1][j + 1] != player && board[i - 1][j + 1] != 0)
            botright = -1;
        else if (i == 0 || j == n-1)
            botright = -2;
        else {
            while (i > 0 && j < n-1 && board[i - 1][j + 1] == player) {
                botright++;
                i--;
                j++;
            }
        }
        return botright;
    }
    private int checkRight(int[][] board, int lastRow, int lastCol, int player){
        int right = 0;
        int i = lastRow;
        int j = lastCol;
        if ( j < n - 1 && board[i][j + 1] != player && board[i][j + 1] != 0)
            right = -1;
        else if (j == n - 1)
            right = -2;
        else {
            while (j < n - 1 && board[i][j + 1] == player) {
                right++;
                j++;
            }
        }
        return right;
    }
    private int checkTopRight(int[][] board, int lastRow, int lastCol, int player) {
        int topright = 0;
        int i = lastRow;
        int j = lastCol;

        if (i < m-1 && j < n - 1 && board[i + 1][j + 1] != player && board[i + 1][j + 1] != 0)
            topright = -1;
        else if (i == m-1 || j == n-1)
            topright = - 2;
        else {
            while (i < m - 1 && j < n - 1 && board[i + 1][j + 1] == player) {
                topright++;
                i++;
                j++;
            }
        }
        return topright;
    }

    public double score(Node current){

        double total;
        int[] temp = current.getFVals();
        double friendly = temp[0] * l;
        for (int i = 1; i < r; i++){
            friendly += temp[i]*thetas[i];
        }
        temp = current.getEVals();
        double enemy = temp[0] * l;
        for (int i = 1; i < r; i++){
            enemy += temp[i]*thetas[i];
        }
        total = friendly - enemy;

        return total;
    }

}
