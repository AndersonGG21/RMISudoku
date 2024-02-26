package implement;

import interfaces.SudokuInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class SudokuImpl extends UnicastRemoteObject implements SudokuInterface {

    public SudokuImpl() throws RemoteException {
    }

    private int[][] board = new int[][]{};

    // This method generates a new Sudoku board. It returns a 2D array of integers.
    @Override
    public int[][] generateBoard(int option) {
        Random rand = new Random();
        var smallBoard = new int[][]{
                {4, 2, 0, 3},
                {2, 0, 3, 4},
                {1, 3, 0, 2},
                {3, 0, 2, 0}
        };

        var mediumBoard = new int[][]{
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        var bigBoard = new int[][]{
                {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };


        return switch (option) {
            case 1 -> generateRandomBoard(smallBoard);
            case 2 -> generateRandomBoard(mediumBoard);
            case 3 -> generateRandomBoard(bigBoard);
            default -> smallBoard;
        };
    }

    @Override
    public int[][] generateRandomBoard(int[][] board) {
        this.board = board;
        Random rand = new Random();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (rand.nextDouble() < 0.5) {
                    board[i][j] = 0;
                }
            }
        }
        return this.board;
    }

    // Este método intenta resolver el tablero de Sudoku. Toma un array 2D de enteros como entrada y devuelve un booleano indicando si el tablero se ha resuelto con éxito.
    @Override
    public boolean solveBoard(int[][] board) {
        this.board = board;
        try {
            for (int row = 0; row < this.board.length; row++) {
                for (int column = 0; column < this.board[0].length; column++) {
                    if (this.board[row][column] == 0) {
                        int flag = 0;
                        switch (this.board.length) {
                            case 4:
                                flag = 4;
                                break;
                            case 9:
                                flag = 9;
                                break;
                            case 16:
                                flag = 16;
                            default:
                                break;
                        }
                        for (int number = 1; number <= flag; number++) {
                            if (isValid(this.board, row, column)) {
                                board[row][column] = number;
                                if (isValid(this.board,row,column) && solveBoard(this.board)) {
                                    return true;
                                } else {
                                    this.board[row][column] = 0;
                                }
                            }
                        }
                        return false;
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e);
        }

        for (int[] row : this.board) {
            for (int i : row) {
                System.out.print(i + " ");
            }
            System.out.println();
        }

        return true;
    }

    // Este método comprueba si un movimiento dado es válido. Toma una matriz 2D de enteros, un número de fila y un número de columna como entrada y devuelve un booleano indicando si el movimiento es válido.
    @Override
    public boolean isValid(int[][] board, int row, int col) {
        if (board.length == 9 && board[0].length == 9) {
            return (rowCheck(board, row)
                    && colCheck(board, col)
                    && subsectionCheck(board, row, col));
        } else {
            return (rowCheck(board, row)
                    && colCheck(board, col));
        }
    }


    @Override
    public boolean subsectionCheck4x4(int[][] board, int row, int col) {
        boolean[] check = new boolean[board.length];
        int subsectionRowStart = row / 2 * 2;
        int subsectionRowEnd = subsectionRowStart + 2;

        int subsectionColStart = col / 2 * 2;
        int subsectionColEnd = subsectionColStart + 2;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColStart; c < subsectionColEnd; c++) {
                if (!checkConstraints(board, r, check, c)) {
                    return false;
                }
            }
        }
        return true;
    }


    // Este método comprueba si una fila dada es válida. Toma una matriz 2D de enteros y un número de fila como entrada y devuelve un booleano que indica si la fila es válida.
    @Override
    public boolean rowCheck(int[][] board, int row) {
        boolean[] check = new boolean[board.length];
        return IntStream.range(0, board.length)
                .allMatch(column -> checkConstraints(board, row, check, column));
    }

    // Este método comprueba si una columna dada es válida. Toma una matriz 2D de enteros y un número de columna como entrada y devuelve un booleano que indica si la columna es válida.
    @Override
    public boolean colCheck(int[][] board, int col) {
        boolean[] check = new boolean[board.length];
        return IntStream.range(0, board.length)
                .allMatch(row -> checkConstraints(board, row, check, col));
    }

    //
    @Override
    public boolean subsectionCheck(int[][] board, int row, int col) {
        boolean[] check = new boolean[board.length];
        int subsectionRowStart = row / 3 * 3;
        int subsectionRowEnd = subsectionRowStart + 3;

        int subsectionColStart = col / 3 * 3;
        int subsectionColEnd = subsectionColStart + 3;

        for (int r = subsectionRowStart; r < subsectionRowEnd; r++) {
            for (int c = subsectionColStart; c < subsectionColEnd; c++) {
                if (!checkConstraints(board, r, check, c)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Este método comprueba las restricciones del tablero de Sudoku. Toma un array 2D de enteros, un número de fila, un array booleano de restricciones y un número de columna como entrada y devuelve un booleano indicando si se cumplen las restricciones.
    @Override
    public boolean checkConstraints(int[][] board, int row, boolean[] constraint, int col) {
        if (board[row][col] != 0) {
            if (!constraint[board[row][col] - 1]) {
                constraint[board[row][col] - 1] = true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public int[][] printBoard(int[][] board) {
        return this.board;
    }

    public String checkSolvedBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            if (!rowCheck(board, i) || !colCheck(board, i) || Arrays.stream(board).anyMatch(row -> Arrays.stream(row).anyMatch(col -> col == 0))) {
                return "Has fallado, revisa de nuevo el tablero.";
            }
            if (board.length == 9 && !subsectionCheck(board, i, i)) {
                return "Has fallado, revisa de nuevo el tablero.";
            }
        }
        return "Felicitaciones, has resuelto el tablero.";
    }

    @Override
    public int[][] getBoard() {
        return this.board;
    }
}
