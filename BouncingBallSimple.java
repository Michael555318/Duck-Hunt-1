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
   private float ballSpeedX = (float)(Math.random()*5+8);   // Ball's speed for x and y
   private float ballSpeedY = (float)(Math.random()*5+8);
   
   // Game properties
   private int score;
   private int bulletCount;
   private int ballColor;
   private double lives;
   private boolean gg;
   
   // Other properties
   private int mouseX;
   private int mouseY;
   private boolean hit;
   private boolean lost;
   
   private static final int UPDATE_RATE = 30; // Number of refresh per second
  
   /** Constructor to create the UI components and init game objects. */
   public BouncingBallSimple() {
      this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
      addMouseListener(this); 
      bulletCount = 3;
      hit = false;
      lost = false;
      score = 0;
      ballColor = 1;
      lives = 3.0;
      gg = false;
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
                  try {
                    Thread.sleep(2000);
                  } catch (InterruptedException ex) { }
                  ballX = ballRadius + (float)(Math.random()*600);; // Ball's center (x, y)
                  ballY = ballRadius + (float)(Math.random()*600); 
                  if (Math.random()-0.5 < 0) {
                      ballSpeedX = (float)(Math.random()*10+10);   // Ball's speed for x and y
                      ballSpeedY = (float)(Math.random()*10+10);
                  } else {
                      ballSpeedX = (float)(Math.random()*10+10)*(-1);   // Ball's speed for x and y
                      ballSpeedY = (float)(Math.random()*10+10)*(-1);
                  }
                  ballColor = (int)(Math.random()*3+1);
               }
               
               if (bulletCount == 0) {
                  lost = true;
                  ballColor = (int)(Math.random()*3+1);
               }
               
               if (gg) {
                  ballSpeedX = 0;
                  ballSpeedY = 0;
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
      if (ballColor == 1) {
          g.setColor(Color.BLUE);
      } else if (ballColor == 2) {
          g.setColor(Color.RED);
      } else {
          g.setColor(Color.PINK);
      }
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
      //g.drawString(sb.toString(), 20, 30);  // tester for ball movement
      //g.drawString(mouseX+"" + " " +mouseY, 50, 30);  //tester for mouse
      
      
      // bullet count board
      g.setColor(Color.BLACK);
      g.fillRect(50, 680, 100, 70);
      g.setColor(Color.YELLOW);
      g.setFont(new Font("Courier New", Font.PLAIN, 30));
      g.drawString("SHOT", 60, 705);
      g.setColor(Color.WHITE);
      g.drawString(bulletCount+"", 90, 740);
      
      // score board
      g.setColor(Color.BLACK);
      g.fillRect(600, 680, 100, 70);
      g.setColor(Color.YELLOW);
      g.setFont(new Font("Courier New", Font.PLAIN, 30));
      g.drawString("SCORE", 605, 705);
      g.setColor(Color.WHITE);
      g.drawString(score+"", 605, 740);
      
      // life board
      g.setColor(Color.BLACK);
      g.fillRect(250, 680, 250, 70);
      g.setColor(Color.WHITE);
      g.setFont(new Font("Courier New", Font.PLAIN, 30));
      g.drawString("LIVES", 255, 720);
      g.setColor(Color.magenta);
      if (lives == 3.0) {
         g.fillRect(360, 700, 30, 30);//inner health
         g.fillRect(400, 700, 30, 30);
         g.fillRect(440, 700, 30, 30);//outer health
      } else if (lives == 2.0) {
         g.fillRect(360, 700, 30, 30);
         g.fillRect(400, 700, 30, 30);
         g.drawRect(440, 700, 30, 30);
      } else if (lives == 1.0) {
         g.fillRect(360, 700, 30, 30);
         g.drawRect(400, 700, 30, 30);
         g.drawRect(440, 700, 30, 30);
      } else if (lives == 0.0){
         g.drawRect(360, 700, 30, 30);
         g.drawRect(400, 700, 30, 30);
         g.drawRect(440, 700, 30, 30);
         gg = true;
      }
      
      // Lost
      if (lost == true) {
         g.setColor(Color.RED);
         g.setFont(new Font("Courier New", Font.PLAIN, 60));
         g.drawString("LOST IT!", 350, 350);
         lost = false;
         lives-=0.5;
         wait(1000);
         bulletCount = 3;
      }
      //win
      if (hit == true) {
         g.setColor(Color.magenta);
         g.setFont(new Font("Courier New", Font.PLAIN, 60));
         g.drawString("GOT IT!", 350, 350);
         g.setColor(Color.WHITE);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         g.drawString("+1500", (int)(ballX + 60), (int)(ballY + 60));
         hit = false;
         bulletCount = 3;
         score += 1500;
      }
      
      if (gg) {
         g.setColor(Color.GRAY);
         g.setFont(new Font("Courier New", Font.PLAIN, 90));
         g.drawString("GG!", 330, 350);
      }
   }
   
   public void mouseClicked(MouseEvent mouse){
        // Get the location of the current mouse click.
        if (bulletCount > 0) {
            mouseX = mouse.getX();
            mouseY = mouse.getY();
            wait(50);
            mouseX=0;
            mouseY=0;
            bulletCount--;
        }
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

