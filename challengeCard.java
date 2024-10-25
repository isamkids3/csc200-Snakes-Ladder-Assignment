import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class challengeCard{
    public challengeCard(String title, String imagePath){
        JFrame f = new JFrame(title);
                    f.setSize(600, 500);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    try {
                     // Load the image using ImageIO
                        BufferedImage img = ImageIO.read(new File(imagePath)); // Path to image
                        JLabel imageLabel = new JLabel(new ImageIcon(img)); // Create a label with the BufferedImage

                        f.add(imageLabel); // Add the label to the frame
                     } catch (IOException e) {
                    }
                    f.setVisible(true); // Make the frame visible
    }
}