import java.util.Collections;
import java.util.Scanner;

public class Main {

    private static final int ROWS = 3;
    private static final int COLS = 3;
    private static final char BLANK = '_';
    private static final String FILL_STRING = String.join("", Collections.nCopies(9, String.valueOf(BLANK)));

    private static char turn = 'X';
    private static Scanner sc = new Scanner(System.in);
    private static char[][] gameBoard = new char[ROWS][COLS];
    private static GameState state = GameState.PLAYING;
    private static int xCounter = 0;
    private static int oCounter = 0;

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

            int valueX = sc.nextInt();
            int valueY = sc.nextInt();

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

    static void updateState(char[][] arr) {
        boolean xWins = false;
        boolean oWins = false;

        updateCounters(arr);

        if (horizontalEvaluation(arr, 'X')
                || verticalEvaluation(arr, 'X')
                || diagonalEvaluation(arr, 'X')) {
            xWins = true;
        }

        if (horizontalEvaluation(arr, 'O')
                || verticalEvaluation(arr, 'O')
                || diagonalEvaluation(arr, 'O')) {
            oWins = true;
        }

        if (freeCellsEvaluation(arr) && !xWins && !oWins) {
            state = GameState.PLAYING;
            resetCounters();
        } else if (xWins && oWins || Math.abs(xCounter - oCounter) > 1) {
            state = GameState.ERROR;
        } else if (xCounter + oCounter == 9 && !xWins && !oWins) {
            state = GameState.FINISHED;
        } else if (xWins) {
            state = GameState.X_WINS;
        } else if (oWins) {
            state = GameState.O_WINS;
        }
    }

    static boolean freeCellsEvaluation(char[][] arr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == BLANK) {
                    return true;
                }
            }
        }
        return false;
    }

    static void updateCounters(char[][] arr) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (arr[i][j] == 'X') {
                    xCounter++;
                } else if (arr[i][j] == 'O') {
                    oCounter++;
                }
            }
        }
    }

    static void resetCounters() {
        xCounter = 0;
        oCounter = 0;
    }

    static boolean horizontalEvaluation(char[][] arr, char player) {
        int hSum = 0;
        for (int i = 0; i < 3; i++) {
            hSum = (int) arr[i][0] + arr[i][1] +arr[i][2];
            if (hSum == 264 && player == 'X' || hSum == 237 && player == 'O') {
                return true;
            }
        }
        return false;
    }

    static boolean verticalEvaluation(char[][] arr, char player) {
        int vSum = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                vSum += (int) arr[j][i];
            }
            if (vSum == 264 && player == 'X' || vSum == 237 && player == 'O') {
                return true;
            }
            vSum = 0;
        }
        return false;
    }

    static boolean diagonalEvaluation(char[][] arr, char player) {
        int dSum1 = 0;
        int dSum2 = 0;

        dSum1 += (int) arr[0][0] + arr[1][1] + arr[2][2];
        dSum2 += (int) arr[0][2] + arr[1][1] + arr[2][0];

        if ((dSum1 == 264 || dSum2 == 264) && player == 'X'
                || (dSum1 == 237 || dSum2 == 237) && player == 'O') {
            return true;
        }
        return false;
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