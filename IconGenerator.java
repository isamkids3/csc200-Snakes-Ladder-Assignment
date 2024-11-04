import java.awt.Image;
import javax.swing.ImageIcon;

public class IconGenerator {
       //Helper method for appropriate property icon generation in the pop-ups.
    public static ImageIcon createIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        // Transform it into an image
        Image image = icon.getImage(); 
        // Scale it
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
}
