import java.util.Collections;
import java.util.Scanner;

public class TicTacToe {

    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static final char BLANK = '_';
    private static final String FILL_STRING = String.join("", Collections.nCopies(9, String.valueOf(BLANK)));


    private static int valueX;
    private static int valueY;
    private static char turn = 'X';
    private static Scanner sc = new Scanner(System.in);
    private static char[][] gameBoard = new char[ROWS][COLS];
    private static GameState state = GameState.PLAYING;

    public static void main(String[] args) {
        fillMatrix(gameBoard);
        print(gameBoard);

        while (state.equals(GameState.PLAYING)) {
            getCoordinates();
            print(gameBoard);
            updateState(gameBoard);
            turn = (turn == 'X') ? 'O' : 'X';
        }
        System.out.println(state.msg);
    }

    static void print(char[][] arr) {
        System.out.println("---------");
        for(int i = 0; i < 3; i++){
            System.out.print("| ");
            for(int j = 0; j < 3; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    static void getCoordinates() {
        System.out.println("Enter the coordinates: ");
        while(true){

            while(!(sc.hasNextInt())) {
                System.out.println("You should enter numbers!");
            }

            valueX = sc.nextInt();
            valueY = sc.nextInt();

            if(valueX < 1 || valueX > 3 || valueY < 1 || valueY > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
            } else {
                rotateRight(gameBoard);
                if(isEmpty(gameBoard, valueX, valueY)){
                    fillValue(gameBoard, turn, valueX, valueY);
                    rotateLeft(gameBoard);
                    break;
                } else {
                    System.out.println("This cell is occupied! Choose another one!");
                }
                rotateLeft(gameBoard);
            }
        }
    }

    static void fillMatrix(char[][] arr) {
        int count = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                arr[i][j] = FILL_STRING.charAt(count);
                count++;
            }
        }
    }

    static void fillValue(char[][] arr, char value, int X, int Y) {
        arr[X-1][Y-1] = value;
    }

    static boolean isEmpty(char[][] arr, int X, int Y) {
        return (arr[X-1][Y-1] == BLANK);
    }

    static void rotateRight(char[][] arr) {
        char[][] carr = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                carr[i][j] = arr[i][j];
            }
        }

        int k = 2;
        for (int i = 0; i < 3; i++) {
            arr[0][i] = carr[k][0];
            arr[1][i] = carr[k][1];
            arr[2][i] = carr[k][2];
            k--;
        }
    }

    static void rotateLeft(char[][] arr) {
        for (int i = 0; i < 3; i++) {
            rotateRight(arr);
        }
    }

    private static void updateState(char[][] arr) {

        boolean xWins = false;
        boolean oWins = false;
        boolean freeCells = false;
        int hSum = 0;
        int vSum = 0;
        int dSum1 = 0;
        int dSum2 = 0;
        int xCounter = 0;
        int oCounter = 0;

        for (int i = 0; i < 3; i++) { // Horizontal and freeCells evaluation
            hSum = (int) arr[i][0] + arr[i][1] +arr[i][2];
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == BLANK) {
                    freeCells = true;
                } else if (arr[i][j] == 'X') {
                    xCounter++;
                } else if (arr[i][j] == 'O') {
                    oCounter++;
                }
            }
            if (hSum == 264) {
                xWins = true;
                hSum = 0;
            } else if (hSum == 237) {
                oWins = true;
                hSum = 0;
            }
        }

        for (int i = 0; i < 3; i++) { // Vertical evaluation
            for (int j = 0; j < 3; j++) {
                vSum += (int) arr[j][i];
            }
            if (vSum == 264) {
                xWins = true;
                vSum = 0;
            } else if (vSum == 237) {
                oWins = true;
                vSum = 0;
            } else {
                vSum = 0;
            }
        }

        dSum1 += (int) arr[0][0] + arr[1][1] + arr[2][2]; // First Diagonal evaluation

        dSum2 += (int) arr[0][2] + arr[1][1] + arr[2][0]; // Second Diagonal evaluation

        if (dSum1 == 264 || dSum2 == 264) {
            xWins = true;
        } else if (dSum1 == 237 || dSum2 == 237) {
            oWins = true;
        }

        if (Math.abs(xCounter - oCounter) > 1 || xWins && oWins) {
            state = GameState.ERROR;
        } else if (freeCells && !xWins && !oWins) {
            state = GameState.PLAYING;
        } else if (xCounter + oCounter == 9 && !xWins && !oWins) {
            state = GameState.FINISHED;
        } else if (xWins) {
            state = GameState.X_WINS;
        } else if (oWins) {
            state = GameState.O_WINS;
        }
    }

    enum GameState {
        PLAYING("Game not finished"),
        FINISHED("Draw"),
        X_WINS("X wins"),
        O_WINS("O wins"),
        ERROR("Impossible");

        private final String msg;

        GameState(String msg) { this.msg = msg; }
    }
}