package sever;

import implement.SudokuImpl;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class SudokuServer {
    public static void main(String[] args) throws RemoteException {
        Registry registry = java.rmi.registry.LocateRegistry.createRegistry(1099);
        System.out.println("RMI registry ready.");
        SudokuImpl sudoku = new SudokuImpl();
        registry.rebind("sudoku", sudoku);
        System.out.println("Sudoku Server is running...");
    }
}
