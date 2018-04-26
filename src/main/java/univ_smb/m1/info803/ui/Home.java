package univ_smb.m1.info803.ui;

import javax.swing.*;

public class Home {
    private JPanel window;
    private JButton validerButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JPanel customer;
    private JButton INTELButton;
    private JButton NVIDIAButton;
    private JButton AMDButton;
    private JPanel proposition;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton accepterButton;
    private JButton refuserButton;
    private JPanel ca;
    private JButton envoyerButton;
    private JButton envoyerButton2;
    private JButton envoyerButton1;
    private JPanel logistics;
    private JPanel appel2;
    private JPanel appel1;
    private JPanel appel3;
    private JButton EMBALEXButton;
    private JButton LESUButton;
    private JButton FLEYButton;
    private JTextField textField8;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Supply Chain Management");
        frame.setContentPane(new Home().window);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
