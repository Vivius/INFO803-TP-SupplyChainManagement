package univ_smb.m1.info803.ui;

import univ_smb.m1.info803.Application;
import univ_smb.m1.info803.ApplicationListener;
import univ_smb.m1.info803.model.Specification;

import javax.swing.*;

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

    public JPanel getWindow() {
        return window;
    }

    public Home(Application app) {
        this.app = app;
        app.addApplicationListener(this);
    }

    @Override
    public void specificationProcessed(Specification spec) {
        System.err.println("Cahier des charges traité");
        System.err.println(spec);
    }
}
