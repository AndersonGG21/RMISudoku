package client;

import client.gui.SudokuGUI;
import implement.SudokuImpl;
import interfaces.SudokuInterface;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SudokuClient {
    private static boolean isLoading = true;

    public static void main(String[] args) throws RemoteException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        SudokuInterface sudoku = null;
        int[][] board = null;

        while (true) {
            System.out.println("\n" + "-".repeat(50));
            System.out.println("|\t\tBienvenido al juego de Sudoku.\t\t\t |".toUpperCase());
            System.out.println("-".repeat(50));
            System.out.println("|1. Generar Sudoku\t\t\t\t\t\t\t\t |");
            System.out.println("|2. Solucionar Sudoku\t\t\t\t\t\t\t |");
            System.out.println("|3. Ver Sudoku\t\t\t\t\t\t\t\t\t |");
            System.out.println("|4. Salir\t\t\t\t\t\t\t\t\t\t |");
            System.out.println("-".repeat(50));
            System.out.print("Elige una opción: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    try {
                        sudoku = (SudokuInterface) java.rmi.Naming.lookup("rmi://localhost/sudoku");
                        System.out.println("-".repeat(20));
                        System.out.println("|1. Sudoku 4x4\t   |");
                        System.out.println("|2. Sudoku 9x9\t   |");
                        System.out.println("|3. Sudoku 16x16   |");
                        System.out.println("-".repeat(20));
                        System.out.print("Elige una opción: ");
                        int option2 = scanner.nextInt();
                        if (option2 < 1 || option2 > 3) {
                            System.out.println("Opción no válida. Por favor, elige una opción del 1 al 3.");
                            break;
                        }else {
                            board = sudoku.generateBoard(option2);
                        }

                        System.out.println("Sudoku generado.");
                    } catch (Exception e) {
                        System.out.println("Exception: " + e);
                    }
                    break;
                case 2:
                    if (sudoku != null && board != null) {
                        try {
                            Thread loadingThread = new Thread(() -> {
                                String loadingText = "Solucionando";
                                while (isLoading) {
                                    System.out.print("\r" + loadingText);
                                    loadingText += ".";
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            });
                            isLoading = true;
                            loadingThread.start();

                            Thread.sleep(3000);
                            sudoku.solveBoard(board);
                            isLoading = false;
                            System.out.println("\nSudoku resuelto.");

                        } catch (Exception e) {

                            System.out.println("Exception: " + e);
                        }
                    } else {
                        System.out.println("Primero debes generar un Sudoku.");
                    }
                    break;
                case 3:
                    if (board != null) {
                        printBoard(sudoku.getBoard());
                        //SudokuGUI sudokuGUI = new SudokuGUI(board);
                    } else {
                        System.out.println("Primero debes generar un Sudoku.");
                    }
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Por favor, elige una opción del 1 al 4.");
            }
        }
    }

    public static void printBoard(int[][] board) {
        int size = board.length;
        int sqrtSize = (int) Math.sqrt(size);

        for (int i = 0; i < size; i++) {
            if (i % sqrtSize == 0 && i != 0) {
                System.out.println("-".repeat(size * 2 + sqrtSize - 1));
            }
            for (int j = 0; j < size; j++) {
                if (j % sqrtSize == 0 && j != 0) {
                    System.out.print("| ");
                }
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static int[][] generateBoard(int size) {
        SudokuInterface sudoku = null;
        int[][] board = new int[][]{};
        try {
            sudoku = (SudokuInterface) java.rmi.Naming.lookup("rmi://localhost/sudoku");

            switch (size) {
                case 4 -> board = sudoku.generateBoard(1);
                case 16 -> board = sudoku.generateBoard(3);
                default -> board = sudoku.generateBoard(2);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return board;
    }

    public static int[][] solveBoard(int[][] board) throws RemoteException {
        SudokuInterface sudoku = null;
        try {
            sudoku = (SudokuInterface) java.rmi.Naming.lookup("rmi://localhost/sudoku");
            sudoku.solveBoard(board);
            printBoard(sudoku.getBoard());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        assert sudoku != null;
        return sudoku.getBoard();
    }

    public static String checkSolvedBoard(int[][] board) {
        SudokuInterface sudoku = null;
        String message = "";
        try {
            sudoku = (SudokuInterface) java.rmi.Naming.lookup("rmi://localhost/sudoku");
            message = sudoku.checkSolvedBoard(board);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return message;
    }
}
