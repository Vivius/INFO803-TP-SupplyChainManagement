package univ_smb.m1.info803.ui;

import univ_smb.m1.info803.Application;
import univ_smb.m1.info803.ApplicationListener;
import univ_smb.m1.info803.model.Specification;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class Home extends JFrame implements ActionListener,ApplicationListener {

    private final Application app;
    private static JFrame frame;
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
    private JLabel requirementsP;
    private JLabel costP;
    private JLabel timeP;
    private JLabel labelRP;
    private JLabel labelCP;
    private JLabel labelTP;
    private JLabel version;
    private JLabel labelCP2;
    private JLabel labelPP2;
    private JLabel labelTP2;
    private JLabel colorP2;
    private JLabel weightP2;
    private JLabel sizeP2;
    private JLabel quantityP3;
    private JLabel speedP3;
    private JLabel labelVP3;
    private JLabel labelQP3;
    private JButton DHLButton;
    private JButton TNTButton;
    private JButton COLLISIMOButton;
    private JButton accepterP2Button;
    private JButton refuserP2Button;
    private JButton accepterP3Button;
    private JButton refuserP3Button;
    private ArrayList<Specification> specifications = new ArrayList<>();
    private String currentP1View = "";
    private String currentP2View = "";
    private String currentP3View = "";
    private Integer compteurRefusP1 = 1;
    private Integer compteurRefusP2 = 1;
    private Integer compteurRefusP3 = 1;
    private Specification currentSpec;

    private void init(){
        appel2.setVisible(false);
        appel3.setVisible(false);

        costP.setVisible(false);
        requirementsP.setVisible(false);
        timeP.setVisible(false);
        labelCP.setVisible(false);
        labelTP.setVisible(false);
        labelRP.setVisible(false);
        accepterP1Button.setVisible(false);
        refuserP1Button.setVisible(false);

        colorP2.setVisible(false);
        weightP2.setVisible(false);
        sizeP2.setVisible(false);
        labelCP2.setVisible(false);
        labelTP2.setVisible(false);
        labelPP2.setVisible(false);
        accepterP2Button.setVisible(false);
        refuserP2Button.setVisible(false);

        quantityP3.setVisible(false);
        speedP3.setVisible(false);
        labelVP3.setVisible(false);
        labelQP3.setVisible(false);
        accepterP3Button.setVisible(false);
        refuserP3Button.setVisible(false);

        INTELButton.setVisible(false);
        NVIDIAButton.setVisible(false);
        AMDButton.setVisible(false);

        //EMBALEXButton.setVisible(false);
        //FLEYButton.setVisible(false);
        //LESUButton.setVisible(false);

        //DHLButton.setVisible(false);
        //COLLISIMOButton.setVisible(false);
        //TNTButton.setVisible(false);

        proposition1.setVisible(false);
        proposition2.setVisible(false);
        proposition3.setVisible(false);

        noRequirements.setVisible(false);
        noCost.setVisible(false);
        noTime.setVisible(false);
        noQuantity.setVisible(false);

        validerButton.addActionListener(this);
        envoyerButton.addActionListener(this);
        envoyerButton1.addActionListener(this);
        envoyerButton2.addActionListener(this);
        INTELButton.addActionListener(this);
        NVIDIAButton.addActionListener(this);
        AMDButton.addActionListener(this);
        EMBALEXButton.addActionListener(this);
        FLEYButton.addActionListener(this);
        LESUButton.addActionListener(this);
        TNTButton.addActionListener(this);
        DHLButton.addActionListener(this);
        COLLISIMOButton.addActionListener(this);
        accepterP1Button.addActionListener(this);
        refuserP1Button.addActionListener(this);
        accepterP2Button.addActionListener(this);
        refuserP2Button.addActionListener(this);
        accepterP3Button.addActionListener(this);
        refuserP3Button.addActionListener(this);

    }

    private Home() throws IOException {
        init();
        this.app = new Application();
        app.addApplicationListener(this);
        new Thread(app).start();
    }

    @Override
    public void specificationProcessed(Specification spec) {
        System.err.println("Cahier des charges traité");
        specifications.add(spec);

        if(spec.getCompany().equals("NVIDIA")){
            INTELButton.setVisible(true);
        }else if(spec.getCompany().equals("INTEL")){
            NVIDIAButton.setVisible(true);
        }else if(spec.getCompany().equals("AMD")){
            AMDButton.setVisible(true);
        }
        frame.pack();
    }

    public static void main(String args[]) throws IOException {
        frame = new JFrame("Supply Chain Management");
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
                cdcText.setText("Cahier des charges : reçu");
                System.err.println("Vous avez validé le CDC !");
            }
        } else if(source == envoyerButton){
            try {
                app.sendSpecification(currentSpec = new Specification(
                    Arrays.asList(requirements.getText().split("\\s*,\\s*")),
                    Integer.parseInt(cost.getText()),
                    Integer.parseInt(time.getText()),
                    Integer.parseInt(quantity.getText())
                ));
                appel1.setEnabled(false);
                envoyerButton.setEnabled(false);
                proposition1.setVisible(true);
                System.err.println("CDC Envoyé !");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if(source == envoyerButton1){
            //TODO : SEND APPEL 2

            appel2.setEnabled(false);
            envoyerButton1.setEnabled(false);
            proposition2.setVisible(true);
            System.err.println("Appel 2 Envoyé !");
        } else if(source == envoyerButton2){
            //TODO : SEND APPEL 3

            appel3.setEnabled(false);
            envoyerButton2.setEnabled(false);
            proposition3.setVisible(true);
            System.err.println("Appel 3 Envoyé !");
        } else if(source == INTELButton || source == NVIDIAButton || source == AMDButton) {
            for (Specification specs : specifications) {
                if(source == INTELButton && specs.getCompany().equals("INTEL")){
                    currentP1View = "INTEL";
                    if(AMDButton.isEnabled()) {
                        AMDButton.setBackground(Color.decode("#EBEBEB"));
                    }
                    if(NVIDIAButton.isEnabled()) {
                        NVIDIAButton.setBackground(Color.decode("#EBEBEB"));
                    }
                    INTELButton.setBackground(Color.GRAY);
                    requirementsP.setText(specs.getAllRequirements().toString());
                    costP.setText(String.valueOf(round(specs.getLastCost(),2)));
                    timeP.setText(String.valueOf(specs.getLastTime()));
                }else if(source == NVIDIAButton && specs.getCompany().equals("NVIDIA")){
                    currentP1View = "NVIDIA";
                    if(INTELButton.isEnabled()) {
                        INTELButton.setBackground(Color.decode("#EBEBEB"));
                    }
                    if(AMDButton.isEnabled()) {
                        AMDButton.setBackground(Color.decode("#EBEBEB"));
                    }
                    NVIDIAButton.setBackground(Color.GRAY);
                    requirementsP.setText(specs.getAllRequirements().toString());
                    costP.setText(String.valueOf(round(specs.getLastCost(),2)));
                    timeP.setText(String.valueOf(specs.getLastTime()));
                }else if(source == AMDButton && specs.getCompany().equals("AMD")){
                    currentP1View = "AMD";
                    if(INTELButton.isEnabled()) {
                        INTELButton.setBackground(Color.decode("#EBEBEB"));
                    }
                    if(NVIDIAButton.isEnabled()) {
                        NVIDIAButton.setBackground(Color.decode("#EBEBEB"));
                    }
                    AMDButton.setBackground(Color.GRAY);
                    requirementsP.setText(specs.getAllRequirements().toString());
                    costP.setText(String.valueOf(round(specs.getLastCost(),2)));
                    timeP.setText(String.valueOf(specs.getLastTime()));
                }
            }
            costP.setVisible(true);
            requirementsP.setVisible(true);
            timeP.setVisible(true);
            labelCP.setVisible(true);
            labelTP.setVisible(true);
            labelRP.setVisible(true);
            accepterP1Button.setVisible(true);
            refuserP1Button.setVisible(true);
        }else if(source == EMBALEXButton || source == LESUButton || source == FLEYButton) {
            if(source == EMBALEXButton){
                currentP2View = "EMBALEX";
                if(LESUButton.isEnabled()) {
                    LESUButton.setBackground(Color.decode("#EBEBEB"));
                }
                if(FLEYButton.isEnabled()) {
                    FLEYButton.setBackground(Color.decode("#EBEBEB"));
                }
                EMBALEXButton.setBackground(Color.GRAY);
                colorP2.setText("EMBALEX value");
                weightP2.setText("EMBALEX value");
                sizeP2.setText(String.valueOf("EMBALEX value"));
            }else if(source == LESUButton){
                currentP2View = "LESU";
                if(EMBALEXButton.isEnabled()) {
                    EMBALEXButton.setBackground(Color.decode("#EBEBEB"));
                }
                if(FLEYButton.isEnabled()) {
                    FLEYButton.setBackground(Color.decode("#EBEBEB"));
                }
                LESUButton.setBackground(Color.GRAY);
                colorP2.setText("LESU value");
                weightP2.setText("LESU value");
                sizeP2.setText(String.valueOf("LESU value"));
            }else if(source == FLEYButton){
                currentP2View = "FLEY";
                if(LESUButton.isEnabled()) {
                    LESUButton.setBackground(Color.decode("#EBEBEB"));
                }
                if(EMBALEXButton.isEnabled()) {
                    EMBALEXButton.setBackground(Color.decode("#EBEBEB"));
                }
                FLEYButton.setBackground(Color.GRAY);
                colorP2.setText("FLEY value");
                weightP2.setText("FLEY value");
                sizeP2.setText(String.valueOf("FLEY value"));
            }
            colorP2.setVisible(true);
            weightP2.setVisible(true);
            sizeP2.setVisible(true);
            labelCP2.setVisible(true);
            labelTP2.setVisible(true);
            labelPP2.setVisible(true);
            if(compteurRefusP2 < 3){
                refuserP2Button.setVisible(true);
            }
            accepterP2Button.setVisible(true);
        } else if(source == TNTButton || source == COLLISIMOButton || source == DHLButton) {
            if(source == TNTButton){
                currentP3View = "TNT";
                if(COLLISIMOButton.isEnabled()) {
                    COLLISIMOButton.setBackground(Color.decode("#EBEBEB"));
                }
                if(DHLButton.isEnabled()) {
                    DHLButton.setBackground(Color.decode("#EBEBEB"));
                }
                TNTButton.setBackground(Color.GRAY);
                quantityP3.setText("TNT value");
                speedP3.setText("TNT value");
            }else if(source == COLLISIMOButton){
                currentP3View = "COLLISIMO";
                if(DHLButton.isEnabled()) {
                    DHLButton.setBackground(Color.decode("#EBEBEB"));
                }
                if(TNTButton.isEnabled()) {
                    TNTButton.setBackground(Color.decode("#EBEBEB"));
                }
                COLLISIMOButton.setBackground(Color.GRAY);
                quantityP3.setText("COLLISIMO value");
                speedP3.setText("COLLISIMO value");
            }else if(source == DHLButton){
                currentP3View = "DHL";
                if(TNTButton.isEnabled()) {
                    TNTButton.setBackground(Color.decode("#EBEBEB"));
                }
                if(COLLISIMOButton.isEnabled()) {
                    COLLISIMOButton.setBackground(Color.decode("#EBEBEB"));
                }
                DHLButton.setBackground(Color.GRAY);
                quantityP3.setText("DHL value");
                speedP3.setText("DHL value");
            }
            quantityP3.setVisible(true);
            speedP3.setVisible(true);
            labelQP3.setVisible(true);
            labelVP3.setVisible(true);
            if(compteurRefusP3 < 3){
                refuserP3Button.setVisible(true);
            }
            accepterP3Button.setVisible(true);
        } else if(source == accepterP1Button) {
            INTELButton.setEnabled(false);
            NVIDIAButton.setEnabled(false);
            AMDButton.setEnabled(false);
            proposition1.setEnabled(false);
            accepterP1Button.setVisible(false);
            refuserP1Button.setVisible(false);

            if(currentP1View.equals("NVIDIA")){
                NVIDIAButton.setBackground(Color.decode("#1F6617"));
            }else if(currentP1View.equals("AMD")){
                AMDButton.setBackground(Color.decode("#1F6617"));
            }else if(currentP1View.equals("INTEL")){
                INTELButton.setBackground(Color.decode("#1F6617"));
            }
            appel2.setVisible(true);
        } else if(source == accepterP2Button) {
            EMBALEXButton.setEnabled(false);
            FLEYButton.setEnabled(false);
            LESUButton.setEnabled(false);
            proposition2.setEnabled(false);
            accepterP2Button.setVisible(false);
            refuserP2Button.setVisible(false);

            if(currentP2View.equals("EMBALEX")){
                EMBALEXButton.setBackground(Color.decode("#1F6617"));
            }else if(currentP2View.equals("FLEY")){
                FLEYButton.setBackground(Color.decode("#1F6617"));
            }else if(currentP2View.equals("LESU")){
                LESUButton.setBackground(Color.decode("#1F6617"));
            }
            appel3.setVisible(true);
        }  else if(source == accepterP3Button) {
            TNTButton.setEnabled(false);
            DHLButton.setEnabled(false);
            COLLISIMOButton.setEnabled(false);
            proposition3.setEnabled(false);
            accepterP3Button.setVisible(false);
            refuserP3Button.setVisible(false);

            if(currentP3View.equals("TNT")){
                TNTButton.setBackground(Color.decode("#1F6617"));
            }else if(currentP3View.equals("DHL")){
                DHLButton.setBackground(Color.decode("#1F6617"));
            }else if(currentP3View.equals("COLLISIMO")){
                COLLISIMOButton.setBackground(Color.decode("#1F6617"));
            }
        } else if(source == refuserP1Button) {
            if(compteurRefusP1 == 3){
                try {
                    compteurRefusP1 = 1;
                    specifications.clear();
                    currentSpec.upgradeVersion();
                    System.err.println(currentSpec);

                    app.sendSpecification(new Specification(
                            currentSpec.getRequirements(),
                            currentSpec.getCost(),
                            currentSpec.getTime(),
                            currentSpec.getQuantity())
                    );

                    NVIDIAButton.setVisible(false);
                    NVIDIAButton.setEnabled(true);
                    NVIDIAButton.setBackground(Color.decode("#EBEBEB"));

                    AMDButton.setVisible(false);
                    AMDButton.setEnabled(true);
                    AMDButton.setBackground(Color.decode("#EBEBEB"));

                    INTELButton.setVisible(false);
                    INTELButton.setEnabled(true);
                    INTELButton.setBackground(Color.decode("#EBEBEB"));


                    accepterP1Button.setVisible(false);
                    refuserP1Button.setVisible(false);

                    costP.setVisible(false);
                    requirementsP.setVisible(false);
                    timeP.setVisible(false);
                    labelCP.setVisible(false);
                    labelTP.setVisible(false);
                    labelRP.setVisible(false);

                    version.setText("Version "+ currentSpec.getVersion());

                    System.err.println("CDC Re-Envoyé !");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }else {
                compteurRefusP1++;
                if (currentP1View.equals("NVIDIA")) {
                    NVIDIAButton.setEnabled(false);
                    NVIDIAButton.setBackground(Color.decode("#940900"));
                } else if (currentP1View.equals("AMD")) {
                    AMDButton.setEnabled(false);
                    AMDButton.setBackground(Color.decode("#940900"));
                } else if (currentP1View.equals("INTEL")) {
                    INTELButton.setEnabled(false);
                    INTELButton.setBackground(Color.decode("#940900"));
                }
                accepterP1Button.setVisible(false);
                refuserP1Button.setVisible(false);
            }
        }else if(source == refuserP2Button) {
            compteurRefusP2++;
            if (currentP2View.equals("EMBALEX")) {
                EMBALEXButton.setEnabled(false);
                EMBALEXButton.setBackground(Color.decode("#940900"));
            } else if (currentP2View.equals("FLEY")) {
                FLEYButton.setEnabled(false);
                FLEYButton.setBackground(Color.decode("#940900"));
            } else if (currentP2View.equals("LESU")) {
                LESUButton.setEnabled(false);
                LESUButton.setBackground(Color.decode("#940900"));
            }
            accepterP2Button.setVisible(false);
            refuserP2Button.setVisible(false);
        }else if(source == refuserP3Button) {
            compteurRefusP3++;
            if (currentP3View.equals("TNT")) {
                TNTButton.setEnabled(false);
                TNTButton.setBackground(Color.decode("#940900"));
            } else if (currentP3View.equals("DHL")) {
                DHLButton.setEnabled(false);
                DHLButton.setBackground(Color.decode("#940900"));
            } else if (currentP3View.equals("COLLISIMO")) {
                COLLISIMOButton.setEnabled(false);
                COLLISIMOButton.setBackground(Color.decode("#940900"));
            }
            accepterP3Button.setVisible(false);
            refuserP3Button.setVisible(false);
        }
        frame.pack();
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

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}