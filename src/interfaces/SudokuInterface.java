package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SudokuInterface extends Remote {
    public int[][] generateBoard(int option) throws RemoteException;
    public int[][] generateRandomBoard(int[][] board) throws RemoteException;
    public boolean solveBoard(int[][] board) throws RemoteException;
    public boolean isValid(int[][] board, int row, int col) throws RemoteException;
    public boolean rowCheck(int[][] board, int row) throws RemoteException; //int num para cuando el usuario ingrese datos
    public boolean colCheck(int[][] board, int col) throws RemoteException;
    public boolean subsectionCheck(int[][] board, int row, int col) throws RemoteException;
    public boolean subsectionCheck4x4(int[][] board, int row, int col) throws RemoteException;
    public boolean checkConstraints(int[][] board, int row, boolean[] constraint, int col) throws RemoteException;
    public int[][] printBoard(int[][] board) throws RemoteException;
    public int[][] getBoard() throws RemoteException;
    public String checkSolvedBoard(int[][] board) throws RemoteException;
}
