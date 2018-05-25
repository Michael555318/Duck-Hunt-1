import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
 
/**
 * This class demonstrates how to load an Image from an external file
 */
public class Trees extends Component {
           
    BufferedImage img;
 
    public void paint(Graphics g) {
        g.drawImage(img, -50, 300, null);
        g.drawImage(img, 400, 300, null);
    }
 
    public Trees() {
       try {
           img = ImageIO.read(new File("tree.png"));
       } catch (IOException e) {
       
       }
    }
}