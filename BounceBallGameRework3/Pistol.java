import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Pistol implements Guns
{
    BufferedImage img;
 
    public void paint(Graphics g) {
        g.drawImage(img, 110, 500, null);
    }
    
    public void aim(Graphics g, int X, int Y) {
        g.drawOval(X-5, Y-5, 10, 10);
    }
    
    public Pistol() {
       try {
           img = ImageIO.read(new File("pistol gun.png"));
       } catch (IOException e) {
       
       }
    }

}

