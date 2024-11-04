
import java.util.Arrays;
import javax.swing.*;

public class SnakesLadder {
    public static void main(String[] args) {

        // Array to store player names input
        String[] playerNames = new String[4];

        //Array to store Color input
        int[] playerColors = new int[4];

        //Array storing color options
        String[] Colors = new String[]{"Blue", "Red", "Yellow", "Green"};

        //Initialize playerInfo
        String playerInfo = "";

        // Ask for input of player name and color using JOptionPane Dialog and loops for all 4 players
        for (int i = 0; i < 4; i++) {

            String playerName = JOptionPane.showInputDialog("Input name for Player " + (i + 1), "Player Name"); //Get input from player
            playerNames[i] = playerName; // Assign input from user to the array

            //End program if user doesnt enter name
            if(playerNames[i] == null){
                JOptionPane.showMessageDialog(null, "Please enter a name!\nExiting the game.", "Error", JOptionPane.ERROR_MESSAGE); // Display Error Message
                System.exit(0); // Exit Program
            }

            int playerColor = JOptionPane.showOptionDialog(null, "Select Color", "Player Color Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, Colors, null); // Display Error Message
            playerColors[i] = playerColor;// Exit Program

            // End program if player selects same color
            for (int j = 0; j < i; j++) { // Check all previously selected colors
                if (playerColors[j] == playerColor) { // Compare color indices
                    JOptionPane.showMessageDialog(null, "Color already selected! Exiting the game.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0); // End the program if color is already selected
                }
            }
        }

        // Display Player Information
        for (int i = 0; i < 4; i++){
            playerInfo += "Player " + (i + 1) + ": " + playerNames[i] + "\n";
        }
        JOptionPane.showMessageDialog(null,"Player Information:\n\n" + playerInfo);

        // Create Board using Board.java
        Board snakesandladderBoard = new Board(playerNames,playerColors);
        snakesandladderBoard.setVisible(true);


       //Test to see if playerNames and playerColors arrays are correct
       System.out.println(Arrays.toString(playerNames));
       System.out.println(Arrays.toString(playerColors));
    }
}
