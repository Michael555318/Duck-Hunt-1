import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
 
/**
 * This class demonstrates how to load an Image from an external file
 */
public class Boom extends Component {
           
    BufferedImage img;
 
    public void paint(Graphics g, int x, int y) {
        g.drawImage(img, x, y, null);
    }
 
    public Boom() {
       try {
           img = ImageIO.read(new File("bam.png"));
       } catch (IOException e) {
       
       }
    }
 
}