import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Bazooka implements Guns
{
    BufferedImage img;
 
    public void paint(Graphics g) {
        g.drawImage(img, 110, 350, null);
    }
    
    public void aim(Graphics g, int X, int Y) {
        g.setColor(Color.black);
        g.drawRect(X-30, Y-30, 60, 60);
    }
    
    public Bazooka() {
       try {
           img = ImageIO.read(new File("bazooka gun.png"));
       } catch (IOException e) {
       
       }
    }

}

