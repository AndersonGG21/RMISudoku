package client.gui;

import client.SudokuClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class SudokuGUI {

    private static SudokuClient client;
    int[][] globalBoard = new int[][]{};
    int[][] originalBoard = new int[][]{};
    public SudokuGUI(int size) {
        JFrame frame = new JFrame("Sudoku");
        JPanel panel = new JPanel(new BorderLayout()); // Cambia a BorderLayout
        JButton[][] buttons = new JButton[size][size];
        JPanel buttonPanel = new JPanel(new GridLayout(size, size));

        //Titulo
        JLabel title = new JLabel("Sudoku", SwingConstants.CENTER);
        title.setFont(new Font("Cascadia Code", Font.BOLD, 50));
        title.setOpaque(true);
        panel.add(title, BorderLayout.NORTH);

        //Botones de acción
        JPanel bottomPanel = new JPanel();
        JButton button1 = new JButton("Generar Sudoku");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                globalBoard = SudokuClient.generateBoard(size);
                originalBoard = globalBoard;
                fillBoard(size, buttons, buttonPanel, frame, globalBoard, originalBoard);
                title.setText("Sudoku Generado");
                panel.add(buttonPanel, BorderLayout.CENTER);
            }
        });

        JButton button2 = new JButton("Solucionar Sudoku (Auto)");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Thread.sleep(3000);
                    globalBoard = SudokuClient.solveBoard(globalBoard);
                    fillBoard(size, buttons, buttonPanel, frame, globalBoard, originalBoard);
                    panel.updateUI();
                    //frame.update(frame.getGraphics());
                    System.out.println("Sudoku resuelto.");
                    for (int[] row : globalBoard) {
                        for (int i : row) {
                            System.out.print(i + " ");
                        }
                        System.out.println();
                    }
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex);
                }
            }
        });

        //Botón para validar el sudoku
        JButton button3 = new JButton("Validar Sudoku");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int[][] board = new int[size][size];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        String buttonText = buttons[i][j].getText();
                        if (buttonText.isEmpty()) {
                            board[i][j] = 0;
                        } else {
                            board[i][j] = Integer.parseInt(buttonText);
                        }
                    }
                }

                JOptionPane.showMessageDialog(frame, SudokuClient.checkSolvedBoard(board));
            }
        });
        bottomPanel.add(button1);
        bottomPanel.add(button2);
        bottomPanel.add(button3);

        // Agrega el panel de botones al panel principal en la posición BorderLayout.SOUTH
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setBackground(new Color(252,191,73));
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        int size = Integer.parseInt(JOptionPane.showInputDialog("Ingresa un tamaño:"));
        new SudokuGUI(size);
    }

    public static void fillBoard(int size, JButton[][] buttons, JPanel buttonPanel, JFrame frame, int[][] globalBoard, int[][] originalBoard) {

        if (buttonPanel.getComponentCount() > 0) {
            buttonPanel.removeAll();
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton(globalBoard[i][j] == 0 ? "" : String.valueOf(globalBoard[i][j]));
                buttons[i][j].setFont(new Font("Cascadia Code", Font.PLAIN, 40));
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setForeground(Color.BLACK);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(new Color(252,191,73), 1));
                buttons[i][j].setFocusPainted(true);
                buttons[i][j].setForeground(globalBoard[i][j] == originalBoard[i][j] ? Color.BLACK : Color.GREEN);


                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = (JButton) e.getSource();
                        button.setBackground(Color.WHITE);
                        String input = JOptionPane.showInputDialog(frame, "Ingresa un número:");
                        if (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 16) {
                            button.setBackground(Color.RED);
                            input = JOptionPane.showInputDialog(frame, "Ingresa un número:");
                        }
                        button.setBackground(Color.WHITE);
                        button.setForeground(Color.GREEN);
                        button.setText(input);
                    }
                });
                buttonPanel.add(buttons[i][j]);
            }
        }
    }
}