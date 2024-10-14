import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;

public class Board extends JFrame {

    // Declare variables
    private final int boardSize = 10;
    private final int numPlayers = 4;
    private int[] playerPositions; 
    private Color[] playerColors;  
    private JPanel boardPanel;
    private JLabel[] playerLabels;  // Array to hold labels for each player
    private HashMap<Integer, Integer> snakes;
    private HashMap<Integer, Integer> ladders;
    private int currentPlayerTurn;

    public Board(String[] playerNames, int[] colorIndices) {
        // Initialize player positions and convert color indices to actual colors
        this.playerPositions = new int[numPlayers];
        this.playerColors = new Color[numPlayers];

        // Mapping playerColor indices (0-3) to actual Color objects using java.awt.Color
        Color[] dotColor = new Color[] {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};

        // Starting position for all players and color of dots
        for (int i = 0; i < numPlayers; i++) {
            playerPositions[i] = 1;  
            int Index = colorIndices[i]; 
            playerColors[i] = dotColor[Index];
        }

        // Initialize snakes and ladders
        setupSnakesAndLadders();

        // Create the game window
        setTitle("Snake and Ladders Game");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

         // JPanel for player Info
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new GridLayout(numPlayers, 1));  // One row for each player

        // Create an array of JLabels to display player information
        playerLabels = new JLabel[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            // Create a label for each player
            playerLabels[i] = new JLabel("          Player " + (i + 1) + ": " + playerNames[i] + ".         ");
            playerLabels[i].setForeground(playerColors[i]);
            playerInfoPanel.add(playerLabels[i]);  
        }

        // Add the labels to the right of the board
        add(playerInfoPanel, BorderLayout.EAST);    

        // JPanel for the game board
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);   // Draw the grid and players
            }
        };
        boardPanel.setPreferredSize(new Dimension(700, 700));
        add(boardPanel, BorderLayout.CENTER);

        // Roll the Dice button
        JButton rollButton = new JButton("Roll Dice");
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int diceRoll = (int) (Math.random() * 6) + 1;  // Roll between 1 and 6
                JOptionPane.showMessageDialog(null, "Player " + (currentPlayerTurn + 1) + " rolled a " + diceRoll);
                
                 // Move the current player
                movePlayer(currentPlayerTurn, diceRoll);
                boardPanel.repaint();  // Redraw the board with updated player positions

                // Move to the next player
                currentPlayerTurn = (currentPlayerTurn + 1) % numPlayers;  // Cycle turns between players
            }
        });
        add(rollButton, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    // Set up snakes and ladders
    private void setupSnakesAndLadders() {
        snakes = new HashMap<>();
        ladders = new HashMap<>();

        // Define snakes: key = start, value = end (head to tail)
        snakes.put(23, 12);
        snakes.put(52, 29);
        snakes.put(57, 40);
        snakes.put(66, 22);
        snakes.put(88, 15);
        snakes.put(96, 51);
        snakes.put(97, 82);


        // Define ladders: key = start, value = end (bottom to top)
        ladders.put(6, 21);
        ladders.put(8, 30);
        ladders.put(28, 84);
        ladders.put(58, 77);
        ladders.put(75, 86);
        ladders.put(81, 100);
        ladders.put(90, 91);
    }

    // Draw the game board and players
    private void drawBoard(Graphics g) {
        int cellSize = boardPanel.getWidth() / boardSize; // Size of each cell
        int counter = 1;

        // Draw grid cells and numbers
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                g.setColor(Color.WHITE);
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
                g.drawString(String.valueOf(counter), x + cellSize / 2, y + cellSize / 2);

                 // Label snakes and ladders
                if (snakes.containsKey(counter)) {
                    g.setColor(Color.RED);
                    g.drawString("S(" + counter + "->" + snakes.get(counter) + ")", x + 5, y + 15);
                } else if (ladders.containsKey(counter)) {
                    g.setColor(Color.GREEN);
                    g.drawString("L(" + counter + "->" + ladders.get(counter) + ")", x + 5, y + 15);
                }

                counter++;
            }
        }

        // Draw players as colored dots using for loop
        for (int i = 0; i < numPlayers; i++) {
            int position = playerPositions[i];  
            int row = (position - 1) / boardSize;
            int col = (position - 1) % boardSize;
            int x = col * cellSize + cellSize / 4;  // Position for dot inside cell (roughly middle)
            int y = row * cellSize + cellSize / 4;

            // Draw player dot with assigned color
            g.setColor(playerColors[i]);
            g.fillOval(x, y, cellSize / 2, cellSize / 2);
        }
    }

    // Move the dot by diceRoll
    private void movePlayer(int playerIndex, int diceRoll) {
        playerPositions[playerIndex] += diceRoll;
        if (playerPositions[playerIndex] > boardSize * boardSize) {
            playerPositions[playerIndex] = boardSize * boardSize; 

            //Win message and condition
            if (playerPositions[playerIndex] == 100) {
                JOptionPane.showMessageDialog(this, "Congratulations, Player " + (playerIndex + 1) + "! You win!");
                System.exit(0);  // Exit the game after a player wins
            }
        }

        // Check for snakes
        if (snakes.containsKey(playerPositions[playerIndex])) {
            JOptionPane.showMessageDialog(this, "Oh no! You landed on a snake.");
            playerPositions[playerIndex] = snakes.get(playerPositions[playerIndex]);
        }

        // Check for ladders
        if (ladders.containsKey(playerPositions[playerIndex])) {
            JOptionPane.showMessageDialog(this, "Great! You climbed a ladder.");
            playerPositions[playerIndex] = ladders.get(playerPositions[playerIndex]);
        }
    }
}