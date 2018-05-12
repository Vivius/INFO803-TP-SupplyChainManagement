package univ_smb.m1.info803.ui;

import univ_smb.m1.info803.Application;
import univ_smb.m1.info803.ApplicationListener;
import univ_smb.m1.info803.model.Specification;

import javax.swing.*;
import java.io.IOException;

public class Home implements ApplicationListener {

    private final Application app;

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
    private JPanel proposition1;
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
    private JPanel proposition2;
    private JPanel proposition3;

    private void init(){
        appel2.setVisible(false);
        appel3.setVisible(false);
        proposition1.setVisible(false);
        proposition2.setVisible(false);
        proposition3.setVisible(false);
    }

    public Home() throws IOException {
        init();
        this.app = new Application();
        app.addApplicationListener(this);
        new Thread(app).start();
    }

    @Override
    public void specificationProcessed(Specification spec) {
        System.err.println(spec);
    }

    public static void main(String args[]) throws IOException {
        JFrame frame = new JFrame("Supply Chain Management");
        frame.setContentPane(new Home().window);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
