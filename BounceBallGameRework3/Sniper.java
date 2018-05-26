import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Sniper implements Guns
{
    BufferedImage img;
 
    public void paint(Graphics g) {
        g.drawImage(img, 110, 200, null);
    }
    
    public void aim(Graphics g, int X, int Y) {
        g.setColor(Color.black);
        g.drawOval(X-25, Y-25, 50, 50);
        g.drawLine(X-25, Y, X+25, Y);
        g.drawLine(X, Y-25, X, Y+25);
    }
    
    public Sniper() {
       try {
           img = ImageIO.read(new File("sniper gun1.png"));
       } catch (IOException e) {
       
       }
    }

}

