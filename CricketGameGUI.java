import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CricketGameGUI extends JFrame {
    private JButton startButton, tossButton, playButton, resetButton;
    private JLabel tossResultLabel, gameStatusLabel;
    private JTextArea gameLog;
    private JTextField player1NameField, player2NameField;
    private String player1Name, player2Name;
    private boolean player1Batting;
    private Random random;
    private int sum1, sum2;

    public CricketGameGUI() {
        setTitle("CRICKET MANIA");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        random = new Random();

        startButton = new JButton("Start Game");
        tossButton = new JButton("Toss");
        playButton = new JButton("Play");
        resetButton = new JButton("Reset Game");

        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        tossButton.setFont(new Font("Arial", Font.BOLD, 18));
        playButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));

        tossResultLabel = new JLabel("Toss Result: ");
        tossResultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gameStatusLabel = new JLabel("Game Status: ");
        gameLog = new JTextArea(15, 40);
        gameLog.setEditable(false);
        gameLog.setBackground(Color.BLACK);
        gameLog.setForeground(Color.WHITE);

        player1NameField = new JTextField(15);
        player2NameField = new JTextField(15);

        JPanel panel = new JPanel();
        panel.setBackground(Color.GREEN);
        panel.setForeground(Color.WHITE);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel player1Label = new JLabel("Player 1 Name:");
        player1Label.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel player2Label = new JLabel("Player 2 Name:");
        player2Label.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(player1Label, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(player1NameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(player2Label, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(player2NameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(startButton, gbc);

        gbc.gridy = 3;
        panel.add(tossButton, gbc);

        gbc.gridy = 4;
        panel.add(playButton, gbc);

        gbc.gridy = 5;
        panel.add(resetButton, gbc);

        gbc.gridy = 6;
        panel.add(tossResultLabel, gbc);

        gbc.gridy = 7;
        panel.add(gameStatusLabel, gbc);

        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(gameLog), gbc);

        add(panel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1Name = player1NameField.getText();
                player2Name = player2NameField.getText();
                gameLog.append("Welcome, " + player1Name + " and " + player2Name + "!\n");
                gameStatusLabel.setText("Game Status: Ready to Toss");
            }
        });

        tossButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performToss();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player1Batting) {
                    playInnings(player1Name);
                    playInningsWithEarlyExit(player2Name);
                } else {
                    playInnings(player2Name);
                    playInningsWithEarlyExit(player1Name);
                }

                gameLog.append(player1Name + "'s total score: " + sum1 + "\n");
                gameLog.append(player2Name + "'s total score: " + sum2 + "\n");

                if (sum1 > sum2) {
                    gameLog.append("Congratulations, " + player1Name + " wins!\n");
                } else if (sum1 == sum2) {
                    gameLog.append("Match Draw!\n");
                } else {
                    gameLog.append("Congratulations, " + player2Name + " wins!\n");
                }

                gameStatusLabel.setText("Game Status: Finished");
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
    }

    private void performToss() {
        boolean coinflip = random.nextBoolean();
        player1Batting = coinflip;
        String tossResult = coinflip ? player1Name + " will bat first." : player2Name + " will bat first.";
        tossResultLabel.setText("Toss Result: " + tossResult);
        gameLog.append(tossResult + "\n");
    }

    private void playInnings(String playerName) {
        int playerScore, opponentScore;
        int sum = 0;
        int ballsFaced = 0;

        while (ballsFaced < 6) {
            String input = JOptionPane.showInputDialog(this, playerName + ", enter your run (0-6): ");
            try {
                playerScore = Integer.parseInt(input);
                if (playerScore < 0 || playerScore > 6) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number between 0 and 6");
                continue;
            }

            opponentScore = random.nextInt(7);
            gameLog.append(playerName + " = " + playerScore + ", Computer = " + opponentScore + "\n");

            if (playerScore == opponentScore) {
                gameLog.append(playerName + " is out!\n");
                break;
            } else {
                sum += playerScore;
                ballsFaced++;
            }
        }

        if (playerName.equals(player1Name)) {
            sum1 = sum;
        } else {
            sum2 = sum;
        }
    }

    private void playInningsWithEarlyExit(String playerName) {
        int playerScore, opponentScore;
        int sum = 0;
        int ballsFaced = 0;

        while (ballsFaced < 6) {
            String input = JOptionPane.showInputDialog(this, playerName + ", enter your run (0-6): ");
            try {
                playerScore = Integer.parseInt(input);
                if (playerScore < 0 || playerScore > 6) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number between 0 and 6");
                continue;
            }

            opponentScore = random.nextInt(7);
            gameLog.append(playerName + " = " + playerScore + ", Computer = " + opponentScore + "\n");

            if (playerScore == opponentScore) {
                gameLog.append(playerName + " is out!\n");
                break;
            } else {
                sum += playerScore;
                ballsFaced++;
                if (playerName.equals(player2Name) && sum > sum1) {
                    gameLog.append(playerName + " has surpassed " + player1Name + "'s score!\n");
                    sum2 = sum;
                    return;
                }
            }
        }

        if (playerName.equals(player1Name)) {
            sum1 = sum;
        } else {
            sum2 = sum;
        }
    }

    private void resetGame() {
        player1NameField.setText("");
        player2NameField.setText("");
        gameLog.setText("");
        tossResultLabel.setText("Toss Result: ");
        gameStatusLabel.setText("Game Status: ");
        sum1 = 0;
        sum2 = 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CricketGameGUI().setVisible(true);
            }
        });
    }
}
