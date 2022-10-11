import java.util.*;

public class Main {

    public static void main(String[] args) {
        int size = 4;
        int numOfMoves = 3;
        int numOfNodes = calculateNumOfNodes(numOfMoves, 4);
        int[][] board = new int[size][size];
        int[] initTileValues = new int[]{2, 4, 8, 16};
        int currStateNum = 1;
        String move = "";
        board = initArray(board, 8, size, initTileValues);
        board = new int[][]{ {2, 0, 2, 0},{0, 0, 4, 4}, {0, 4, 4, 4}, {0, 0, 0, 0}};
        State root = new State( currStateNum);
        root.stateBoard = board;
        root.prevBoard = board;
        LinkedList<State> queue = new LinkedList<State>();
        root.visited = 1;
        queue.add(root);
        currStateNum++;
        System.out.println(root.nodeNum);
        while (queue.size() != 0 && currStateNum <= numOfNodes){
            State currNode = queue.poll();
            for(int i=0; i<4; i++){
                if(i == 0){
                    move = "up";
                } else if (i == 1) {
                    move = "down";
                } else if (i == 2) {
                    move = "left";
                }
                else {
                    move = "right";
                }
                State state = new State(currStateNum);
                state.prevBoard = currNode.stateBoard;
                state.stateBoard = new int[4][4];
                returnMove(state, move);
                addRandomTwo(state);
                queue.add(state);
                printArray(state.stateBoard);
                System.out.println(" ");
                currStateNum+=1;

            }

        }
       System.out.println(currStateNum);
       //returnMove(root, "up");
    }

    public static void addRandomTwo(State state){
        Random random = new Random();
        ArrayList<Pair> emptyIndices = returnEmptyIndices(state.stateBoard);
        Pair pickedIndice = emptyIndices.get(random.nextInt(emptyIndices.size()));
        state.stateBoard[pickedIndice.first][pickedIndice.second] = 2;
    }

    public static ArrayList<Pair> returnEmptyIndices(int[][] board){
        ArrayList<Pair> lisOfIndices = new ArrayList<Pair>();
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                if(board[i][j] == 0){
                   Pair pair = new Pair(i, j);
                   lisOfIndices.add(pair);

                }
            }
        }
        return lisOfIndices;
    }

    public static int calculateNumOfNodes(int numOfMoves, int childNodeSize){
        double sum = Math.pow(childNodeSize, numOfMoves+1) - 1;
        sum /= (childNodeSize-1);
        return (int)Math.round(sum);
    }

    public static int[][] initArray(int[][] board, int initBoardFillNum, int size, int[] initTileValues){
        Random random = new Random();
        Set<List<Integer>> setOfIndices = new HashSet<List<Integer>>();
        int max = size;
        int min = 0;
        int initTileValuesSize = initTileValues.length;
        for(int i=0; setOfIndices.size() != initBoardFillNum; i++){
            int row = random.nextInt(max - min ) + min;
            int column = random.nextInt(max - min ) + min;
            setOfIndices.add(new ArrayList<>(Arrays.asList(row, column)));
        }
        for(List<Integer> indicePair : setOfIndices) {
            int index = random.nextInt(initTileValuesSize);
            board[indicePair.get(0)][indicePair.get(1)] = initTileValues[index];
        }
        return board;
    }

    public static String returnMove(State tempState, String move){
        int sizeOfBoard = 4;
        int[][] tempBoard = tempState.prevBoard;
        int[][] boardRight = new int[4][4];
        int[][] boardLeft = new int[4][4];
        int[][] boardUp = new int[4][4];
        int[][] boardDown = new int[4][4];
        String bestMove = "";
        int column = sizeOfBoard;
        int row = sizeOfBoard;
        List<Integer> sumList = new ArrayList<>();

        if(move.equals("right")) {
            // For a right move
            for (int i = 0; i < tempBoard[0].length; i++) {
                boardRight[i] = tempBoard[i].clone();
            }
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < boardRight[0].length; j++) {
                    int firstFilledColumn = findFilled(boardRight, i, column - 1, move);
                    if (firstFilledColumn == -1) {
                        break;
                    }
                    int firstEmptyColumn = findEmpty(boardRight, i, column - 1, move);
                    if (firstFilledColumn != -1 && firstEmptyColumn != -1 && firstFilledColumn < firstEmptyColumn) {
                        boardRight[i][firstEmptyColumn] = boardRight[i][firstFilledColumn];
                        boardRight[i][firstFilledColumn] = 0;
                    }
                    column -= 1;

                }
                column = boardRight[0].length;
            }


            sumList.add(findSum(boardRight, move));
            tempState.stateBoard = new int[tempBoard.length][tempBoard[0].length];
            for (int i = 0; i < boardRight[0].length; i++) {
                tempState.stateBoard[i] = boardRight[i].clone();
            }
           // printArray(tempState.stateBoard);
            //System.out.println("The sum is " + sumList.get(0));
        } else if (move.equals("left")) {

            // For a left move
            move = "left";
            for (int i = 0; i < tempBoard[0].length; i++) {
                boardLeft[i] = tempBoard[i].clone();
            }
            int columnTraverse = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < boardLeft[0].length; j++) {
                    int firstFilledColumn = findFilled(boardLeft, i, columnTraverse, move);
                    if (firstFilledColumn == -1) {
                        break;
                    }
                    int firstEmptyColumn = findEmpty(boardLeft, i, columnTraverse, move);
                    if (firstFilledColumn != -1 && firstEmptyColumn != -1 && firstFilledColumn > firstEmptyColumn) {
                        boardLeft[i][firstEmptyColumn] = boardLeft[i][firstFilledColumn];
                        boardLeft[i][firstFilledColumn] = 0;
                    }
                    columnTraverse += 1;

                }
                columnTraverse = 0;
            }

            sumList.add(findSum(boardLeft, move));
            for (int i = 0; i < boardLeft[0].length; i++) {
                tempState.stateBoard[i] = boardLeft[i].clone();
            }
            //printArray(boardLeft);
            //System.out.println("The sum is " + sumList.get(0));
        } else if (move.equals("up")) {

            // For a up move
            move = "up";
            for (int i = 0; i < tempBoard[0].length; i++) {
                boardUp[i] = tempBoard[i].clone();
            }

            for (int i = 0; i < boardUp.length; i++) {
                for (int j = 0; j < boardUp[0].length; j++) {
                    int firstFilledRow = findFilled(boardUp, j, i, move);
                    if (firstFilledRow == -1) {
                        break;
                    }
                    int firstEmptyRow = findEmpty(boardUp, j, i, move);
                    if (firstFilledRow != -1 && firstEmptyRow != -1 && firstFilledRow > firstEmptyRow) {
                        boardUp[firstEmptyRow][i] = boardUp[firstFilledRow][i];
                        boardUp[firstFilledRow][i] = 0;
                    }
                }
            }


            sumList.add(findSum(boardUp, move));
           // printArray(boardUp);
            for (int i = 0; i < boardUp[0].length; i++) {
                tempState.stateBoard[i] = boardUp[i].clone();
            }
          // System.out.println("The sum is " + sumList.get(0));
        }else {
            // For a down move
            move = "down";
            for (int i = 0; i < tempBoard[0].length; i++) {
                boardDown[i] = tempBoard[i].clone();
            }

            for (int i = 0; i < boardDown[0].length; i++) {
                for (int j = boardDown.length - 1; j >= 0; j--) {
                    int firstFilledRow = findFilled(boardDown, j, i, move);
                    if (firstFilledRow == -1) {
                        break;
                    }
                    int firstEmptyRow = findEmpty(boardDown, j, i, move);
                    if (firstFilledRow != -1 && firstEmptyRow != -1 && firstFilledRow < firstEmptyRow) {
                        boardDown[firstEmptyRow][i] = boardDown[firstFilledRow][i];
                        boardDown[firstFilledRow][i] = 0;
                    }
                }
            }


            sumList.add(findSum(boardDown, move));
           // printArray(boardDown);
            for (int i = 0; i < boardDown[0].length; i++) {
                tempState.stateBoard[i] = boardDown[i].clone();
            }
          // System.out.println("The sum is " + sumList.get(0));

        }
        return bestMove;
    }

    public static int findSum(int[][] board, String move){
        int sum = 0;
        if(move.equals("right")) {
            for (int i = 0; i < board.length; i++) {
                for (int j = board[0].length - 1; j > 0; j--) {
                    if (board[i][j] == board[i][j - 1] && board[i][j] != 0) {
                        board[i][j] += board[i][j];
                        sum += board[i][j];
                        board[i][j - 1] = 0;
                        moveAllToRight(board, i, j - 1);
                    }
                }
            }
        } else if (move.equals("left")) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j <board.length - 1; j++) {
                    if (board[i][j] == board[i][j + 1] && board[i][j] != 0) {
                        board[i][j] += board[i][j];
                        sum += board[i][j];
                        board[i][j + 1] = 0;
                        moveAllToLeft(board, i, j + 1);
                    }
                }
            }
        }
        else if (move.equals("up")) {
            for (int j = 0; j < board.length; j++) {
                for (int i = 0; i <board.length - 1; i++) {
                    if (board[i][j] == board[i+1][j] && board[i][j] != 0) {
                        board[i][j] += board[i][j];
                        sum += board[i][j];
                        board[i+1][j] = 0;
                        moveAllToUp(board, i+1, j);
                    }
                }
            }
        }
        else if (move.equals("down")) {
            for (int j = 0; j < board.length; j++) {
                for (int i = board.length-1; i > 0; i--) {
                    if (board[i][j] == board[i-1][j] && board[i][j] != 0) {
                        board[i][j] += board[i][j];
                        sum += board[i][j];
                        board[i-1][j] = 0;
                        moveAllToDown(board, i-1, j);
                    }
                }
            }
        }
        return sum;
    }
    public static void moveAllToDown(int[][] board, int row, int column){
        for(int i = row; i > 0; i--){
            board[i][column] = board[i-1][column];
        }
        if(board[0][column] != 0){
            board[0][column] =  0;
        }
    }
    public static void moveAllToUp(int[][] board, int row, int column){
        for(int i = row; i < board.length-1; i++){
            board[i][column] = board[i+1][column];
        }
        if(board[board.length - 1][column] != 0){
            board[board.length - 1][column] =  0;
        }
    }
    public static void moveAllToLeft(int[][] board, int row, int column){
        for(int i = column; i < board[0].length-1; i++){
            board[row][i] = board[row][i+1];
        }
        if(board[row][board[0].length-1] != 0){
            board[row][board[0].length-1] = 0;
        }
    }
    public static void moveAllToRight(int[][] board, int row, int column){
        for(int i = column; i > 0; i--){
            board[row][i] = board[row][i-1];
        }
        if(board[row][0] != 0){
            board[row][0] = 0;
        }
    }
    public static int findFilled(int[][] board, int row, int column, String move){
        if(move.equals("right")) {
            int firstFilledColumn = -1;
            if (column < 0) {
                return firstFilledColumn;
            }
            if (board[row][column] != 0) {
                firstFilledColumn = column;
                return firstFilledColumn;
            }

            return findFilled(board, row, column - 1, move);
        } else if (move.equals("left")) {
            int firstFilledColumn = -1;
            if (column >= board[0].length) {
                return firstFilledColumn;
            }
            if (board[row][column] != 0) {
                firstFilledColumn = column;
                return firstFilledColumn;
            }

            return findFilled(board, row, column + 1, move);
        }
        else if (move.equals("up")) {
            int firstFilledRow = -1;
            if (row >= board.length) {
                return firstFilledRow;
            }
            if (board[row][column] != 0) {
                firstFilledRow = row;
                return firstFilledRow;
            }

            return findFilled(board, row+1, column, move);
        }

        else if (move.equals("down")) {
            int firstFilledRow = -1;
            if (row < 0) {
                return firstFilledRow;
            }
            if (board[row][column] != 0) {
                firstFilledRow = row;
                return firstFilledRow;
            }

            return findFilled(board, row-1, column, move);
        }

        return -1;
    }

    public static int findEmpty(int[][] board, int row, int column, String move){
        if(move.equals("right")) {
            int firstEmptyColumn = -1;
            if (column < 0) {
                return firstEmptyColumn;
            }
            if (board[row][column] == 0) {
                firstEmptyColumn = column;
                return firstEmptyColumn;
            }

            return findEmpty(board, row, column - 1, move);
        } else if (move.equals("left")) {
            int firstEmptyColumn = -1;
            if (column >= board[0].length) {
                return firstEmptyColumn;
            }
            if (board[row][column] == 0) {
                firstEmptyColumn = column;
                return firstEmptyColumn;
            }

            return findEmpty(board, row, column + 1, move);
        }
        else if (move.equals("up")) {
            int firstEmptyRow = -1;
            if (row >= board.length) {
                return firstEmptyRow;
            }
            if (board[row][column] == 0) {
                firstEmptyRow = row;
                return firstEmptyRow;
            }

            return findEmpty(board, row+1, column, move);
        }

        else if (move.equals("down")) {
            int firstEmptyRow = -1;
            if (row < 0) {
                return firstEmptyRow;
            }
            if (board[row][column] == 0) {
                firstEmptyRow = row;
                return firstEmptyRow;
            }

            return findEmpty(board, row-1, column, move);
        }
        return -1;
    }

    public static void printArray(int[][] board){
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                System.out.print(board[i][j] + "    ");
            }
            System.out.println(" ");
        }
    }
}

class State{
    int[][] stateBoard;
    int[][] prevBoard;
    int nodeNum;
    int visited = 0;
    int sum = 0;
    int nextStateNum = -1;

    public State( int nodeNum){
        this.nodeNum = nodeNum;
    }
}

class Pair{
    int first;
    int second;

    public Pair(int first, int second){
        this.first = first;
        this.second = second;
    }
}