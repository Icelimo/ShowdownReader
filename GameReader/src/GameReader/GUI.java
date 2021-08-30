package GameReader;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.Color;

public class GUI {

    private JFrame frame;
    private JTextField textField;
    private JLabel lblPlayer1_Name,lblPlayer2_Name,lblWinLoose,lblWinnerP1,lblWinnerP2,lblFehler,lblZoroark;
    private JTextPane textPane1,textPane2;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUI window = new GUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    private GUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("ShowdownReader");
        frame.setBounds(100, 100, 825, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblLinkHere = new JLabel("Link Here:");
        lblLinkHere.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblLinkHere.setBounds(10, 11, 72, 14);
        frame.getContentPane().add(lblLinkHere);

        textField = new JTextField();
        textField.setBounds(10, 30, 690, 25);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Apply");
        btnNewButton.addActionListener(arg0 -> {
            try {
                Player[] game = Spielauswertung.werteAus(textField.getText());
                frame.setTitle("ShowdownReader: '" + game[0].getNickname() + "' vs. '" + game[1].getNickname() + "'");

                lblFehler.setVisible(false);
                lblZoroark.setVisible(false);
                lblPlayer1_Name.setVisible(true);
                lblPlayer2_Name.setVisible(true);
                textPane1.setVisible(true);
                textPane2.setVisible(true);
                lblWinLoose.setVisible(true);
                lblWinnerP1.setVisible(true);
                lblWinnerP2.setVisible(true);

                lblPlayer1_Name.setText(game[0].getNickname());
                lblPlayer2_Name.setText(game[1].getNickname());

                textPane1.setText("Kills|Pokemon, dead/alive \r\n");
                textPane2.setText("Kills|Pokemon, dead/alive \r\n");

                int deadP1=0;
                int deadP2=0;
                for (Pokemon p : game[0].getMons()) {//Hallo Dieter\r\nTest\r\nDrei\r\nVier\r\nF\u00FCnf\r\nSechs
                    textPane1.setText(textPane1.getText()+p.getKills()+"|"+p.getPokemon()+", "+p.DeadToString()+"\r\n");
                    if(p.isDead())deadP1++;
                }
                for (Pokemon p : game[1].getMons()) {
                    textPane2.setText(textPane2.getText()+p.getKills()+"|"+p.getPokemon()+", "+p.DeadToString()+"\r\n");
                    if(p.isDead())deadP2++;
                }

                lblWinLoose.setText((6-deadP1)+":"+(6-deadP2));

                if(game[0].isWinner()) {
                    if(deadP2<6) {
                        lblWinnerP1.setText("Winner");
                        lblWinnerP2.setText("ffd. Looser");
                    } else {
                        lblWinnerP1.setText("Winner");
                        lblWinnerP2.setText("Looser");
                    }
                } else {
                    if(deadP1<6) {
                        lblWinnerP1.setText("Looser ffd.");
                        lblWinnerP2.setText("Winner");
                    } else {
                        lblWinnerP1.setText("Looser");
                        lblWinnerP2.setText("Winner");
                    }
                }


                for(Player pl:game) {
                    for(Pokemon p:pl.getMons()) {
                        if(p.getPokemon().equals("Zoroark")){
                            lblZoroark.setText("Watch out, Zoroark is in a Team! It doesn't work with Zoroark!");
                            lblZoroark.setVisible(true);
                        }
                        else if(p.getPokemon().equals("Zoroa")){
                            lblZoroark.setText("Watch out, Zoroa is in a Team! It doesn't work with Zoroa!");
                            lblZoroark.setVisible(true);
                        }
                    }
                }

            } catch (ArithmeticException e) {
                lblFehler.setVisible(true);
            }

        });
        btnNewButton.setBounds(710, 30, 89, 25);
        frame.getContentPane().add(btnNewButton);

        lblPlayer1_Name = new JLabel("Player 1:");
        lblPlayer1_Name.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPlayer1_Name.setBounds(10, 66, 371, 25);
        frame.getContentPane().add(lblPlayer1_Name);
        lblPlayer1_Name.setVisible(false);

        lblPlayer2_Name = new JLabel("Player 2:");
        lblPlayer2_Name.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPlayer2_Name.setBounds(428, 66, 371, 25);
        frame.getContentPane().add(lblPlayer2_Name);
        lblPlayer2_Name.setVisible(false);

        textPane1 = new JTextPane();
        textPane1.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textPane1.setText("");
        textPane1.setBounds(10, 102, 371, 158);
        frame.getContentPane().add(textPane1);
        textPane1.setVisible(false);

        textPane2 = new JTextPane();
        textPane2.setText("");
        textPane2.setFont(new Font("Tahoma", Font.PLAIN, 17));
        textPane2.setBounds(428, 102, 371, 158);
        frame.getContentPane().add(textPane2);
        textPane2.setVisible(false);

        lblWinLoose = new JLabel("0:0");
        lblWinLoose.setFont(new Font("Tahoma", Font.PLAIN, 61));
        lblWinLoose.setHorizontalAlignment(SwingConstants.CENTER);
        lblWinLoose.setBounds(333, 271, 142, 59);
        frame.getContentPane().add(lblWinLoose);
        lblWinLoose.setVisible(false);

        lblWinnerP1 = new JLabel("Looser");
        lblWinnerP1.setHorizontalAlignment(SwingConstants.LEFT);
        lblWinnerP1.setFont(new Font("Tahoma", Font.PLAIN, 61));
        lblWinnerP1.setBounds(10, 271, 348, 59);
        frame.getContentPane().add(lblWinnerP1);
        lblWinnerP1.setVisible(false);

        lblWinnerP2 = new JLabel("Looser");
        lblWinnerP2.setHorizontalAlignment(SwingConstants.RIGHT);
        lblWinnerP2.setFont(new Font("Tahoma", Font.PLAIN, 61));
        lblWinnerP2.setBounds(451, 271, 348, 59);
        frame.getContentPane().add(lblWinnerP2);
        lblWinnerP2.setVisible(false);

        lblFehler = new JLabel("Fehler!");
        lblFehler.setForeground(Color.RED);
        lblFehler.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblFehler.setBounds(88, 11, 72, 14);
        frame.getContentPane().add(lblFehler);
        lblFehler.setVisible(false);

        JLabel lblByicelimo = new JLabel("By Icelimo");
        lblByicelimo.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblByicelimo.setBounds(710, 11, 72, 14);
        frame.getContentPane().add(lblByicelimo);

        lblZoroark = new JLabel("Watch out, Zoroark is in a Team! It doesn't work with Zoroark!");
        lblZoroark.setForeground(Color.RED);
        lblZoroark.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblZoroark.setBounds(160, 11, 500, 14);
        frame.getContentPane().add(lblZoroark);
        lblZoroark.setVisible(false);
    }
}
