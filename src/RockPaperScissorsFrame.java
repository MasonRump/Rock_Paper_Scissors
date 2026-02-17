import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class RockPaperScissorsFrame extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RockPaperScissorsFrame::new);
    }

    //stats
    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;

    //history
    private int rockCount = 0;
    private int paperCount = 0;
    private int scissorsCount = 0;
    private String lastMove = null;

    //GUI components
    private JTextField playerWinsField;
    private JTextField computerWinsField;
    private JTextField tiesField;
    private JTextArea resultsArea;

    //strategies
    private Strategy randomStrategy = new RandomStrategy();
    private Strategy cheatStrategy = new Cheat();

    private Random rand = new Random();

    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLayout(new BorderLayout());

        add(createButtonPanel(), BorderLayout.NORTH);
        add(createStatsPanel(), BorderLayout.WEST);
        add(createResultsPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Choose Your Move"));

        JButton rockButton = new JButton(new ImageIcon(getClass().getResource("/images/rock.png")));
        JButton paperButton = new JButton(new ImageIcon(getClass().getResource("/images/paper.png")));
        JButton scissorsButton = new JButton(new ImageIcon(getClass().getResource("/images/scissors.png")));
        JButton quitButton = new JButton("Quit");

        ActionListener handler = e -> {
            if (e.getSource() == rockButton) handleMove("R");
            else if (e.getSource() == paperButton) handleMove("P");
            else if (e.getSource() == scissorsButton) handleMove("S");
            else System.exit(0);
        };

        rockButton.addActionListener(handler);
        paperButton.addActionListener(handler);
        scissorsButton.addActionListener(handler);

        quitButton.addActionListener(e -> System.exit(0));

        panel.add(rockButton);
        panel.add(paperButton);
        panel.add(scissorsButton);
        panel.add(quitButton);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Stats"));

        playerWinsField = new JTextField("0");
        computerWinsField = new JTextField("0");
        tiesField = new JTextField("0");

        playerWinsField.setEditable(false);
        computerWinsField.setEditable(false);
        tiesField.setEditable(false);

        panel.add(new JLabel("Player Wins:"));
        panel.add(playerWinsField);
        panel.add(new JLabel("Computer Wins:"));
        panel.add(computerWinsField);
        panel.add(new JLabel("Ties:"));
        panel.add(tiesField);

        return panel;
    }

    private JScrollPane createResultsPanel() {
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));

        return scrollPane;
    }

    private class LeastUsedStrategy implements Strategy {

        @Override
        public String getMove(String playerMove) {
            if (rockCount <= paperCount && rockCount <= scissorsCount) return "P";
            else if (paperCount <= rockCount && paperCount <= scissorsCount) return "S";
            else return "R";
        }
    }

    private class MostUsedStrategy implements Strategy {

        @Override
        public String getMove(String playerMove) {
            if (rockCount >= paperCount && rockCount >= scissorsCount) return "P";
            else if (paperCount >= rockCount && paperCount >= scissorsCount) return "S";
            else return "R";
        }
    }

    private class LastUsedStrategy implements Strategy {

        @Override
        public String getMove(String playerMove) {
            if (lastMove == null) return randomStrategy.getMove(playerMove);

            switch (lastMove) {
                case "R": return "P";
                case "P": return "S";
                case "S": return "R";
                default: return randomStrategy.getMove(playerMove);
            }
        }
    }

    private Strategy chooseStrategy() {
        int roll = rand.nextInt(100) + 1;

        if (roll <= 10) return cheatStrategy;
        else if (roll <= 30) return new LeastUsedStrategy();
        else if (roll <= 50) return new MostUsedStrategy();
        else if (roll <= 70) return new LastUsedStrategy();
        else return randomStrategy;
    }

    private void handleMove(String playerMove) {
        updateCounts(playerMove);

        Strategy strategy = chooseStrategy();
        String computerMove = strategy.getMove(playerMove);

        String result = determineWinner(playerMove, computerMove);

        resultsArea.append(result + " (computer: " + strategy.getClass().getSimpleName() + ")\n");

        updateStats();
        lastMove = playerMove;
    }

    private void updateCounts(String playerMove) {
        if (playerMove.equals("R")) rockCount++;
        else if (playerMove.equals("P")) paperCount++;
        else if (playerMove.equals("S")) scissorsCount++;
    }

    private String determineWinner(String playerMove, String computerMove) {
        if (playerMove.equals(computerMove)) {
            ties++;
            return "It is a Tie!";
        }

        if ((playerMove.equals("R") && computerMove.equals("S")) ||
            (playerMove.equals("P") && computerMove.equals("R")) ||
            (playerMove.equals("S") && computerMove.equals("P"))) {
            playerWins++;
            return playerMove + " beats " + computerMove + ". Player Wins!";
        } else {
            computerWins++;
            return computerMove + " beats " + playerMove + ". Computer Wins!";
        }
    }

    private void updateStats() {
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }
}
