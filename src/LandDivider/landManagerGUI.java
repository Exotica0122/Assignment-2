package LandDivider;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import LandDivider.LandManager.Method;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;

public class landManagerGUI extends JPanel implements ActionListener {

    private final JButton analyze, stop;
    private final JTextField lengthInput, widthInput;
    private final JLabel lengthFieldLabel, widthFieldLabel, status, originalValue, splits;
    private final JComboBox<String> algorithms;
    private final mainPanel drawPanel;
    private final JPanel southPanel, northPanel;
    private final Timer timer;
    LandManager landManager;

    public landManagerGUI() {
        super(new BorderLayout());
        southPanel = new JPanel();
        northPanel = new JPanel();
//Buttons-----------------------------------------------------------------------
        analyze = new JButton("ANALYSE");
        analyze.addActionListener(this);
        stop = new JButton("STOP");
        stop.addActionListener(this);
        stop.setEnabled(false);
//Text Fields-------------------------------------------------------------------
        lengthInput = new JTextField(5);
        widthInput = new JTextField(5);

//Labels -----------------------------------------------------------------------
        lengthFieldLabel = new JLabel("Length: ");
        widthFieldLabel = new JLabel("Width: ");
        status = new JLabel();
        status.setPreferredSize(new Dimension(100,45));
        status.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Solution: ")
                    ,status.getBorder()));
        originalValue = new JLabel();
        originalValue.setPreferredSize(new Dimension(100,45));
        originalValue.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Original: ")
                    ,originalValue.getBorder()));
        splits = new JLabel();
        splits.setPreferredSize(new Dimension(100,45));
        splits.setBorder(new CompoundBorder(BorderFactory.createTitledBorder("Total Splits: ")
                    ,splits.getBorder()));
//Combo Box --------------------------------------------------------------------
        String[] algorithmOptions = {"Brute Force", "Dynamic Programming", "Greedy Algorithm"};
        algorithms = new JComboBox<>(algorithmOptions);

        
        northPanel.add(algorithms);
        northPanel.add(status);
        northPanel.add(splits);
        northPanel.add(originalValue);
        southPanel.add(lengthFieldLabel);
        southPanel.add(lengthInput);
        southPanel.add(widthFieldLabel);
        southPanel.add(widthInput);
        southPanel.add(analyze);
        southPanel.add(stop);

        drawPanel = new mainPanel();

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(drawPanel, BorderLayout.CENTER);

        timer = new Timer(20, this);
        timer.start();
    }

    private class mainPanel extends JPanel {

        public mainPanel() {
            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(500, 500));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (landManager != null) {
                landManager.drawLand(g);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == analyze) {
            if (algorithms.getSelectedItem() == "Brute Force") {
                analyze.setEnabled(false);
                stop.setEnabled(true);
                try {
                    landManager = new LandManager(Integer.parseInt(lengthInput.getText()), Integer.parseInt(widthInput.getText()), Method.BRUTE_FORCE);
                    originalValue.setText("$" + landManager.getLand().getValue());
                    drawPanel.repaint();
                    landManager.run();
                    JOptionPane.showMessageDialog(new JFrame(), "Size per unit area is worth $" +landManager.getLand().price
                            + ".\nSplitting cost per split-length is $"
                            + landManager.splitCost 
                            + ".", " info", JOptionPane.INFORMATION_MESSAGE);
                    splits.setText(landManager.getSplits() + " splits");
                    status.setText("$" + landManager.getSolution());
                } catch (NumberFormatException e) {
                    System.err.println(e + " is not a number!");
                    JOptionPane.showMessageDialog(new JFrame(), "Please enter numerical values for both \'Length\' and \'Width\'", "WARNING", JOptionPane.WARNING_MESSAGE);
                    stop.setEnabled(false);
                    analyze.setEnabled(true);
                }
            }
            if (algorithms.getSelectedItem() == "Dynamic Programming") {
                analyze.setEnabled(false);
                stop.setEnabled(true);
                try {
                    landManager = new LandManager(Integer.parseInt(lengthInput.getText()), Integer.parseInt(widthInput.getText()), Method.DYNAMIC_PROGRAMMING);
                    originalValue.setText("$" + landManager.getLand().getValue());
                    drawPanel.repaint();
                    landManager.run();
                    JOptionPane.showMessageDialog(new JFrame(), "Size per unit area is worth $" +landManager.getLand().price
                            + ".\nSplitting cost per split-length is $"
                            + landManager.splitCost 
                            + ".", " info", JOptionPane.INFORMATION_MESSAGE);

                    status.setText("$" + landManager.getSolution());
                    splits.setText(landManager.getSplits() + " splits");
                } catch (NumberFormatException e) {
                    System.err.println(e + " is not a number!");
                    JOptionPane.showMessageDialog(new JFrame(), "Please enter numerical values for both \'Length\' and \'Width\'", "WARNING", JOptionPane.WARNING_MESSAGE);
                    stop.setEnabled(false);
                    analyze.setEnabled(true);
                }
            }
            if(algorithms.getSelectedItem() == "Greedy Algorithm"){
                JOptionPane.showMessageDialog(new JFrame(), "Approach has not been implemented yet. Sorry :(", "WARNING", JOptionPane.WARNING_MESSAGE);
            }
            analyze.setEnabled(true);
            stop.setEnabled(false);
        }
        if (source == stop) {
            if (landManager != null) {
                if (landManager.isRunning()) {
                    landManager.setRunning(false);
                    stop.setEnabled(false);
                    analyze.setEnabled(true);
                }
            }
        }

        drawPanel.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Land Divider");
        // kill all threads when frame closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new landManagerGUI());
        frame.pack();
        frame.setResizable(false);
        // position the frame in the middle of the screen
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenDimension = tk.getScreenSize();
        Dimension frameDimension = frame.getSize(); 
        frame.setLocation((screenDimension.width - frameDimension.width) / 2,
                (screenDimension.height - frameDimension.height) / 2);
        JOptionPane.showMessageDialog(new JFrame(), """
                                                    I was so careless that I thought this assignment was due 2 weeks after 
                                                    the midsemester break. So I dedicated most of the break to my other course
                                                    assignments.
                                                    I found out 3 days before the due date (i.e., 19th Sep) that I was wrong.
                                                    Entirely my fault, many apologies for the pain that you are about to see.
                                                    Hence, this is just a warning as there may be an enourmous amount of mistakes 
                                                    in the algorithms and bugs.
                                                    Press ok whenever you are ready dear Grader :) """, "WARNING", JOptionPane.WARNING_MESSAGE);
        frame.setVisible(true);
        // now display something while the main thread is still alive
        for (int i = 0; i < 20; i++) {
            System.out.println("Main thread counting: " + i);
            try {
                Thread.sleep(500); // delay for 500ms
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Main thread about to die");
    }

}
