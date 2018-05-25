import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
 
/**
 * This class demonstrates how to load an Image from an external file
 */
public class Sunset extends Component {
           
    BufferedImage img;
 
    public void paint(Graphics g) {
        g.drawImage(img, 0, -10, null);
    }
 
    public Sunset() {
       try {
           img = ImageIO.read(new File("sunset theme.jpg"));
       } catch (IOException e) {
       
       }
    }
 
}