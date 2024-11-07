import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Board extends JFrame {

    // Declare variables
    private final int boardSize = 10;
    private final int numPlayers = 4;
    private int[] playerPositions; 
    private Color[] playerColors;  
    private JPanel boardPanel;
    private JLabel[] playerLabels;  
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

        // Initialize snakes and ladders on the board
        setupSnakesAndLadders();

        // Create the game window
        setTitle("Snake and Ladders Game");
        setSize(525, 525);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

         // JPanel for player Info
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setLayout(new GridLayout(numPlayers, 1));  // One row for each player

        // Create an array of JLabels to display player information
        playerLabels = new JLabel[numPlayers];
        
        // Using for loop to create label for each player
        for (int i = 0; i < numPlayers; i++) {
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
        boardPanel.setPreferredSize(new Dimension(525, 525));

        // Add the board to the center of the panel
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

        //Add button to the bottom of the board
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

        // Draw grid cells and numbers with coloured cells for special tiles
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                if ( counter >= 1 && counter <= 5 ) {
                    g.setColor(new Color(184, 220, 228));
                }
                else if ( counter >= 16 && counter <= 20 ) {
                    g.setColor(new Color(255,212,180));
                }
                else if ( counter >= 31 && counter <= 35 ) {
                    g.setColor(new Color(200,212,156));
                }
                else if ( counter >= 46 && counter <= 50 ) {
                    g.setColor(new Color(255,252,156));
                }
                else if ( counter >= 61 && counter <= 65 ) {
                    g.setColor(new Color(208,196,220));
                }
                else if ( counter >= 76 && counter <= 80 ) {
                    g.setColor(new Color(255,156,204));
                }
                 else if ( counter >= 91 && counter <= 95 ) {
                    g.setColor(new Color(8,212,212));
                }
                else if ( counter == 85 || counter == 55 || counter == 25) {
                    g.setColor(Color.green);
                }
                else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
                g.drawString(String.valueOf(counter), x + cellSize / 2, y + cellSize / 2);

                 // Label snakes and ladders
                if (snakes.containsKey(counter)) {
                    Font originalFont = g.getFont();
                    g.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    g.setColor(Color.RED);
                    g.drawString("S(" + counter + "->" + snakes.get(counter) + ")", x + 5, y + 15);
                    g.setFont(originalFont);
                } else if (ladders.containsKey(counter)) {
                    Font originalFont = g.getFont();
                    g.setFont(new Font("SansSerif", Font.PLAIN, 10));
                    g.setColor(Color.GREEN);
                    g.drawString("L(" + counter + "->" + ladders.get(counter) + ")", x + 5, y + 15);
                    g.setFont(originalFont);

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

            //Black outline for better contrast
            g.setColor(Color.BLACK);
            g.drawOval(x, y, cellSize / 2, cellSize / 2);
        }
    }

    // Move the dot by diceRoll
   private void movePlayer(int playerIndex, int diceRoll) {
    playerPositions[playerIndex] += diceRoll;
    
    // Check if the player exceeds the final tile, bring them back to the final tile if needed
    if (playerPositions[playerIndex] > boardSize * boardSize) {
        playerPositions[playerIndex] = boardSize * boardSize;
    }

    // Display the tile number the player lands on
    JOptionPane.showMessageDialog(this, "Player " + (playerIndex + 1) + " lands on tile " + playerPositions[playerIndex]);

    //Call the IconGenerator class to  generate icons for property images
    ImageIcon iconGreen = IconGenerator.createIcon("greenhouse.png", 100, 100);
    ImageIcon iconBlue = IconGenerator.createIcon("bluehouse.png", 100, 100);
    ImageIcon iconPeach = IconGenerator.createIcon("peachhouse.png", 100, 100);
    ImageIcon iconCyan = IconGenerator.createIcon("cyanhouse.png", 100, 100);
    ImageIcon iconPink = IconGenerator.createIcon("pinkhouse.png", 100, 100);
    ImageIcon iconPurple = IconGenerator.createIcon("purplehouse.png", 100, 100);
    ImageIcon iconYellow = IconGenerator.createIcon("yellowhouse.png", 100, 100);


    // Showing message when player lands in blue properties
    if (playerPositions[playerIndex] >= 1 && playerPositions[playerIndex] <= 5){
        String place = "";
        switch(playerPositions[playerIndex]){
            case 1: place = "Tropicana The Residences"; 
            break;
            case 2: place = "Pavilion Embassy Kuala Lumpur"; 
            break;
            case 3: place = "Binjai On The Park"; 
            break;
            case 4: place = "Marc Service Residence"; 
            break;
            case 5: place = "The Mews"; 
            break;
        }
        JOptionPane.showMessageDialog(null, "Welcome to Blue Sky Properties! You are at "+ place + ".","Stepped on Property!",JOptionPane.INFORMATION_MESSAGE,iconBlue);// Lok Sang or Jericho u can change this to include an image
    }

     // Showing message when player lands in peach properties
    if (playerPositions[playerIndex] >= 16 && playerPositions[playerIndex] <= 20){
        String place = "";
        switch(playerPositions[playerIndex]){
            case 16: place = "D’Ivo Residences"; 
            break;
            case 17: place = "Petalz Residences"; 
            break;
            case 18: place = "Southbank Residence"; 
            break;
            case 19: place = "The Address"; 
            break;
            case 20: place = "Waltz Residences"; 
            break;
        }
        JOptionPane.showMessageDialog(null, "Welcome to Peach Princess Properties! You are at "+ place + ".","Stepped on Property!",JOptionPane.INFORMATION_MESSAGE,iconPeach);// Lok Sang or Jericho u can change this to include an image
    }

    // Showing message when player lands in green properties
    if (playerPositions[playerIndex] >= 31 && playerPositions[playerIndex] <= 35){
        String place = "";
        switch(playerPositions[playerIndex]){
            case 31: place = "TTDI Ascencia"; 
            break;
            case 32: place = "Sri TTDI"; 
            break;
            case 33: place = "The Residence Condominium"; 
            break;
            case 34: place = "Sinaran TTDI"; 
            break;
            case 35: place = "Villa Flora"; 
            break;
        }
        JOptionPane.showMessageDialog(null, "Welcome to Green Go Properties! You are at "+ place + ".","Stepped on Property!",JOptionPane.INFORMATION_MESSAGE,iconGreen);
    }
    // Showing message when player lands in yellow properties
    if (playerPositions[playerIndex] >= 46 && playerPositions[playerIndex] <= 50){
         String place = "";
        switch(playerPositions[playerIndex]){
            case 46: place = "LEA by the Hills"; 
            break;
            case 47: place = "KL East - The Ridge"; 
            break;
            case 48: place = "Upperville @ 16 Quartz"; 
            break;
            case 49: place = "Nadayu63"; 
            break;
            case 50: place = "M Adora"; 
            break;
        }
        JOptionPane.showMessageDialog(null, "Welcome to Yellow Wish Properties! You are at "+ place + ".","Stepped on Property!",JOptionPane.INFORMATION_MESSAGE,iconYellow);
    }
    // Showing message when player lands in purple properties
    if (playerPositions[playerIndex] >= 61 && playerPositions[playerIndex] <= 65){
        String place = "";
       switch(playerPositions[playerIndex]){
           case 61: place = "Damai Residence"; 
           break;
           case 62: place = "The Vyne"; 
           break;
           case 63: place = "Kenwingston Avenue"; 
           break;
           case 64: place = "Anyaman Residence"; 
           break;
           case 65: place = "Razak City Residences"; 
           break;
       }
       JOptionPane.showMessageDialog(null, "Welcome to Purple Rain Properties! You are at "+ place + ".","Stepped on Property!",JOptionPane.INFORMATION_MESSAGE,iconPurple);
   }
   // Showing message when player lands in pink properties
   if (playerPositions[playerIndex] >= 76 && playerPositions[playerIndex] <= 80){
    String place = "";
   switch(playerPositions[playerIndex]){
       case 76: place = "Tropicana Gardens"; 
       break;
       case 77: place = "Emporis Kota Damansara"; 
       break;
       case 78: place = "Sunway d'hill Residences"; 
       break;
       case 79: place = "The Estana"; 
       break;
       case 80: place = "Mahogany Residences"; 
       break;
    }
    JOptionPane.showMessageDialog(null, "Welcome to Pink Panther Properties! You are at "+ place + ".","Stepped on Property!",JOptionPane.INFORMATION_MESSAGE,iconPink);
    }

    // Showing message when player lands in cyan properties
    if (playerPositions[playerIndex] >= 91 && playerPositions[playerIndex] <= 95){
        String place = "";
    switch(playerPositions[playerIndex]){
        case 91: place = "Mossaz @ Empire City"; 
        break;
        case 92: place = "D'Eroca Residences"; 
        break;
        case 93: place = "D'Vervain Residences"; 
        break;
        case 94: place = "D'Cosmos Residences"; 
        break;
        case 95: place = "The Essence"; 
        break;
    }
    JOptionPane.showMessageDialog(null, "Welcome to Cyan D'Luffy Properties! You are at "+ place + ".","Stepped on Property!",JOptionPane.INFORMATION_MESSAGE,iconCyan);
    }

    //Message when player lands on pick a card tile
    if (playerPositions[playerIndex] == 25 || playerPositions[playerIndex] == 55 || playerPositions[playerIndex] == 85){
        JOptionPane.showMessageDialog(null, "Pick a card!");
        int randomCard = (int) (Math.random() * 6) + 1;
        
        //Challenge based on random number generated. Challenge generated using method in challengeCard.java
        switch (randomCard){
            case 1: 
                    new challengeCard("Sing your favourite song!", "singingDude.jpg");
                    break;
            case 2: 
                    new challengeCard("Clap your hand three times, spin around twice and sit down!", "clappingHands.jpg");
                    break;
            case 3: 
                    new challengeCard("Do the Chicken Dance!", "chickenDance.jpg");
                    break;
            case 4: 
                    new challengeCard("Do the Floss Dance!", "floss.jpg");
                    break;
            case 5: 
                    new challengeCard("Walk like a robot!", "robot.jpg");
                    break;
            case 6: 
                    new challengeCard("Say the alphabet backwards!", "alphabet.jpg");
                    break;
            case 7: 
                    new challengeCard("Name five states in the USA!", "usa.jpg");
                    break;
            case 8: 
                    new challengeCard("Count to ten in a different language!", "count.jpg");
                    break;
            case 9: 
                    new challengeCard("Play air guitar!", "airguitar.png");
                    break;
            case 10: 
                    new challengeCard("Do five star jumps!", "star.png");
                    break;
        
    }
                
      
        }

    // Check for snakes
    if (snakes.containsKey(playerPositions[playerIndex])) {
        JOptionPane.showMessageDialog(this, "Oh no! You landed on a snake.");
        playerPositions[playerIndex] = snakes.get(playerPositions[playerIndex]);
        JOptionPane.showMessageDialog(this, "Player " + (playerIndex + 1) + " moves down to tile " + playerPositions[playerIndex]);
    }

    // Check for ladders
    if (ladders.containsKey(playerPositions[playerIndex])) {
        JOptionPane.showMessageDialog(this, "Great! You climbed a ladder.");
        playerPositions[playerIndex] = ladders.get(playerPositions[playerIndex]);
        JOptionPane.showMessageDialog(this, "Player " + (playerIndex + 1) + " moves up to tile " + playerPositions[playerIndex]);
    }

    // Check if the player wins
    if (playerPositions[playerIndex] == boardSize * boardSize) {
        //Reuse challenge card method to display a win image
        new challengeCard("Winner!","winner.png");
        //Sleep method to pause the program for 3 seconds.
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace(); // Handle the exception, e.g., logging it
        }
        JOptionPane.showMessageDialog(this, "Congratulations, Player " + (playerIndex + 1) + "! You win!");
        System.exit(0);  // Exit the game 
    }
}
}