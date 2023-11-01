import java.util.Scanner;

public class test {
    private static char[][] board = new char[3][3];
    private static char currentPlayer = 'X';

    public static void main(String[] args) {
        initializeBoard();
        displayBoard();
        playGame();
    }

    public static void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    public static void displayBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    public static void playGame() {
        boolean gameEnded = false;
        while (!gameEnded) {
            System.out.println("It's " + currentPlayer + "'s turn. Enter row (1-3) and column (1-3):");
            Scanner scanner = new Scanner(System.in);
            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;
            if (board[row][col] == '-') {
                board[row][col] = currentPlayer;
                displayBoard();
                if (checkForWin()) {
                    System.out.println(currentPlayer + " has won the game!");
                    gameEnded = true;
                } else if (checkForDraw()) {
                    System.out.println("It's a tie!");
                    gameEnded = true;
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            } else {
                System.out.println("That spot is already taken. Try again!");
            }
        }
    }

    public static boolean checkForWin() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != '-') {
                return true;
            }
        }
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == board[1][j] && board[1][j] == board[2][j] && board[0][j] != '-') {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != '-') {
            return true;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != '-') {
            return true;
        }
        return false;
    }

    public static boolean checkForDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }
}
