package univ_smb.m1.info803.ui;

import univ_smb.m1.info803.Application;
import univ_smb.m1.info803.ApplicationListener;
import univ_smb.m1.info803.model.Specification;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Home extends JFrame implements ActionListener,ApplicationListener {

    private final Application app;

    private JPanel window;
    private JButton validerButton;
    private JTextField requirements;
    private JTextField cost;
    private JTextField time;
    private JTextField quantity;
    private JPanel customer;
    private JButton INTELButton;
    private JButton NVIDIAButton;
    private JButton AMDButton;
    private JPanel proposition1;
    private JButton accepterP1Button;
    private JButton refuserP1Button;
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
    private JLabel noRequirements;
    private JLabel noCost;
    private JLabel noTime;
    private JLabel noQuantity;
    private JLabel cdcText;

    private void init(){
        appel2.setVisible(false);
        appel3.setVisible(false);
        proposition1.setVisible(false);
        proposition2.setVisible(false);
        proposition3.setVisible(false);
        noRequirements.setVisible(false);
        noCost.setVisible(false);
        noTime.setVisible(false);
        noQuantity.setVisible(false);
        validerButton.addActionListener(this);
        envoyerButton.addActionListener(this);
    }

    public Home() throws IOException {
        init();
        window.setPreferredSize(new Dimension(1000,800));
        this.app = new Application();
        app.addApplicationListener(this);
        new Thread(app).start();
    }

    @Override
    public void specificationProcessed(Specification spec) {
        System.err.println("Cahier des charges traité");
        System.err.println(spec);
    }

    public static void main(String args[]) throws IOException {
        JFrame frame = new JFrame("Supply Chain Management");
        frame.setContentPane(new Home().window);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == validerButton){
            if(validateCDC()){
                envoyerButton.setEnabled(true);
                requirements.setEnabled(false);
                cost.setEnabled(false);
                time.setEnabled(false);
                quantity.setEnabled(false);
                validerButton.setEnabled(false);
                ca.setEnabled(false);
                this.pack();
                cdcText.setText("Cahier des charges : reçu");
                System.err.println("Vous avez validé le CDC !");
            }
        } else if(source == envoyerButton){
            try {
                app.sendSpecification(new Specification(
                        Arrays.asList(requirements.getText().split("\\s*,\\s*")),
                        Integer.parseInt(cost.getText()),
                        Integer.parseInt(time.getText()),
                        Integer.parseInt(quantity.getText())
                ));
                appel1.setEnabled(false);
                envoyerButton.setEnabled(false);
                proposition1.setVisible(true);
                this.pack();
                System.err.println("CDC Envoyé !");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }else if(source == INTELButton) {


        }else if(source == NVIDIAButton) {


        }else if(source == AMDButton) {


        }else if(source == accepterP1Button) {


        }else if(source == refuserP1Button) {


        }
    }

    private boolean validateCDC() {
        boolean requirementsOK = false;
        boolean costOK = false;
        boolean timeOK = false;
        boolean quantityOK = false;

        if(requirements.getText().isEmpty()) {
            noRequirements.setVisible(true);
        }else{
            noRequirements.setVisible(false);
            requirementsOK = true;
        }

        if(cost.getText().isEmpty() || isNotInt(cost.getText())) {
            noCost.setVisible(true);
        }else{
            noCost.setVisible(false);
            costOK = true;
        }

        if(time.getText().isEmpty() || isNotInt(time.getText())){
            noTime.setVisible(true);
        }else{
            noTime.setVisible(false);
            timeOK = true;
        }

        if(quantity.getText().isEmpty() || isNotInt(quantity.getText())) {
            noQuantity.setVisible(true);
        }else{
            noQuantity.setVisible(false);
            quantityOK = true;
        }
        this.window.setSize(1000,2000);
        return requirementsOK && costOK && timeOK && quantityOK;
    }

    private static boolean isNotInt(String s)
    {
        try {
            Integer.parseInt(s);
            return false;
        } catch(NumberFormatException er) {
            return true;
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}