import java.awt.*;
import java.util.Formatter;
import javax.swing.*;
import java.awt.event.*;

/**
 * One ball bouncing inside a rectangular box. 
 * All codes in one file. Poor design!
 */
// Extends JPanel, so as to override the paintComponent() for custom rendering codes. 
public class BouncingBallSimple extends JPanel implements MouseListener{
   // Container box's width and height
   private static final int BOX_WIDTH = 800;
   private static final int BOX_HEIGHT = 800;
   private static final int BOX_HEIGHT_INFO = BOX_HEIGHT - 150;
  
   // Ball's properties
   private float ballRadius = 30; // Ball's radius
   private float ballX = ballRadius + 50; // Ball's center (x, y)
   private float ballY = ballRadius + 20; 
   private float ballSpeedX = (float)(Math.random()*5+5);   // Ball's speed for x and y
   private float ballSpeedY = (float)(Math.random()*5+5);
   private int mouseX;
   private int mouseY;
   private int bulletCount;
   private boolean hit;
   
   private static final int UPDATE_RATE = 30; // Number of refresh per second
  
   /** Constructor to create the UI components and init game objects. */
   public BouncingBallSimple() {
      this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
      addMouseListener(this); 
      bulletCount = 3;
      hit = false;
      // Start the ball bouncing (in its own thread)
      Thread gameThread = new Thread() {
         public void run() {
            while (true) { // Execute one update step
               // Calculate the ball's new position
               ballX += ballSpeedX;
               ballY += ballSpeedY;
               // Check if the ball moves over the bounds
               // If so, adjust the position and speed.
               if (ballX - ballRadius < 0) {
                  ballSpeedX = -ballSpeedX; // Reflect along normal
                  ballX = ballRadius; // Re-position the ball at the edge
               } else if (ballX + ballRadius > BOX_WIDTH) {
                  ballSpeedX = -ballSpeedX;
                  ballX = BOX_WIDTH - ballRadius;
               }
               // May cross both x and y bounds
               if (ballY - ballRadius < 0) {
                  ballSpeedY = -ballSpeedY;
                  ballY = ballRadius;
               } else if (ballY + ballRadius > BOX_HEIGHT_INFO) {
                  ballSpeedY = -ballSpeedY;
                  ballY = BOX_HEIGHT_INFO - ballRadius;
               }
               
               if (Math.sqrt(Math.pow(mouseX-ballX,2)+Math.pow(mouseY-ballY,2)) <= ballRadius) {
                  ballSpeedX = 0;
                  ballSpeedY = 0;
                  hit = true;
               }
               
               // Refresh the display              
               repaint(); // Callback paintComponent()
               // Delay for timing control and give other threads a chance
               
               try {
                  Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
               } catch (InterruptedException ex) { }
            }
         }
      };
      gameThread.start();  // Callback run()
   }
  
   /** Custom rendering codes for drawing the JPanel */
   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);    // Paint background
  
      // Draw the box
      g.setColor(Color.cyan);
      g.fillRect(0, 0, BOX_WIDTH, BOX_HEIGHT);
  
      // Draw the ball
      g.setColor(Color.BLUE);
      g.fillOval((int) (ballX - ballRadius), (int) (ballY - ballRadius),
            (int)(2 * ballRadius), (int)(2 * ballRadius));
            
      // Draw ground
      g.setColor(Color.GREEN);
      g.fillRect(0, 650, 800, 150);
      
      // Display the ball's information
      g.setColor(Color.WHITE);
      g.setFont(new Font("Courier New", Font.PLAIN, 12));
      StringBuilder sb = new StringBuilder();
      Formatter formatter = new Formatter(sb);
      formatter.format("Ball @(%3.0f,%3.0f) Speed=(%2.0f,%2.0f)", ballX, ballY,
            ballSpeedX, ballSpeedY);
      g.drawString(sb.toString(), 20, 30);
      //g.drawString(mouseX+"" + " " +mouseY, 50, 30);
      g.drawString("Bullets: " + bulletCount+"", 20, 50);
      // Lost
      if (bulletCount == 0) {
         g.setColor(Color.RED);
         g.setFont(new Font("Courier New", Font.PLAIN, 60));
         g.drawString("LOST IT!", 350, 350);
      }
      //win
      if (hit == true) {
         g.setColor(Color.magenta);
         g.setFont(new Font("Courier New", Font.PLAIN, 60));
         g.drawString("GOT IT!", 350, 350);
         hit = false;
      }
      
   }
   
   public void mouseClicked(MouseEvent mouse){
        // Get the location of the current mouse click.
        mouseX = mouse.getX();
        mouseY = mouse.getY();
        bulletCount--;
        // Tell the panel that we need to redraw things.
        repaint();
    }

   /* The following methods have to be here to comply
   with the MouseListener interface, but we don't
   use them, so their code blocks are empty. */
   public void mouseEntered(MouseEvent mouse){ }   
   public void mouseExited(MouseEvent mouse){ }
   public void mousePressed(MouseEvent mouse){ }
   public void mouseReleased(MouseEvent mouse){ }
   
   /**
     * Will delay output for the number of milliseconds provided as an argument
     */ 
    private void wait(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
   /** main program (entry point) */
   public static void main(String[] args) {
      // Run GUI in the Event Dispatcher Thread (EDT) instead of main thread.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // Set up main window (using Swing's Jframe)
            JFrame frame = new JFrame("A Bouncing Ball");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new BouncingBallSimple());
            frame.pack();
            frame.setVisible(true);
         }
      });
   }
   
}