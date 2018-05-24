

import java.awt.*;
import java.util.Formatter;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
/**
 * One ball bouncing inside a rectangular box. 
 * All codes in one file. Poor design!
 */

/**
 * New click detection logic.
 * Now supports clicking on the move.
 * 
 */
// Extends JPanel, so as to override the paintComponent() for custom rendering codes. 
public class BouncingBallsColorReworked extends JPanel implements MouseListener{
   // Container box's width and height
   private static final int BOX_WIDTH = 800;
   private static final int BOX_HEIGHT = 800;
   private static final int BOX_HEIGHT_INFO = BOX_HEIGHT - 150;
  
   // Ball's properties
   private float ballRadius = 30; // Ball's radius
   private float ballX = ballRadius + 50; // Ball's center (x, y)
   private float ballY = ballRadius + 20; 
   private float ballSpeedX = (float)(Math.random()*2.5+4);   // Ball's speed for x and y
   private float ballSpeedY = (float)(Math.random()*2.5+4);
   
   //Extra Life Square properties
   private boolean exLife;
   private float rectSide = 40;
   private float rectX;
   private float rectY;
   private float rectSpeedX;
   private float rectSpeedY;
   private boolean rectHit;

   // Game properties
   private int score;
   private int bulletCount;
   private int ballColor;
   private double lives;
   private boolean gg;
   private boolean replay;
   private int timer;
   private boolean menu;
   private int bestScore;
   
   // Other properties
    private int mouseX;
    private int mouseY;
    private boolean hit;
    private boolean lost;
    private boolean rules;
   
   private static final int UPDATE_RATE = 60; // Number of refresh per second
  
   /** Constructor to create the UI components and init game objects. */
   public BouncingBallsColorReworked() {
      this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
      addMouseListener(this); 
      
      //Initialize Instance Variables 
      bulletCount = 3;
      hit = false;
      lost = false;
      rules = false;
      score = 0;
      ballColor = 1;
      lives = 3.0;
      gg = false;
      replay = false;
      timer = 0;
      menu = true;
      bestScore = 0;
      
       exLife = false;
        rectHit = false;
        rectX = (float)(Math.random()*800)*((int)(Math.random()+Math.random()))-60;
        if (rectX == -60) {
        rectY = (float)(Math.random()*600);
        rectSpeedX = (float)(Math.random()*5+10);
        rectSpeedY = 0;
        } else {
        rectY = -60;
        rectSpeedX = 0;
        rectSpeedY =(float)(Math.random()*5+10);
        }
      
      // Start the ball bouncing (in its own thread)
      Thread gameThread = new Thread() {
         public void run() {
            while (true) { // Execute one update step
               // Calculate the ball's new position
               ballX += ballSpeedX;
               ballY += ballSpeedY;
               
               //Calculate the rect's new position
                if (exLife == true && timer >= 150) {
                rectX += rectSpeedX;
                rectY += rectSpeedY;
                }
                if (rectX > 1000 || rectX < -1000 || rectY > 1000 || rectY < -1000 || rectHit == true) {
                exLife = false;
                rectX = (float)(Math.random()*800)*((int)(Math.random()+Math.random()))-60;
                if (rectX == -60) {
                rectY = (float)(Math.random()*600);
                rectSpeedX = (float)(Math.random()*5+10);
                rectSpeedY = 0;
                } else {
                rectY = -60;
                rectSpeedX = 0;
                rectSpeedY =(float)(Math.random()*5+10);
                }
                rectHit = false;
                }
               
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
               
               //if the ball is hit
               if (Math.sqrt(Math.pow(mouseX-ballX,2)+Math.pow(mouseY-ballY,2)) <= ballRadius && !gg && menu == false) {
                  ballSpeedX = 0;
                  ballSpeedY = 0;
                  hit = true;
                  try {
                    Thread.sleep(2000);
                  } catch (InterruptedException ex) { }
                  ballX = ballRadius + (float)(Math.random()*600); // Ball's center (x, y)
                  ballY = ballRadius + (float)(Math.random()*600); 
                  if (Math.random()-0.5 < 0) {
                      //ballSpeedX = (float)(Math.random()*10+10);
                      ballSpeedX = (float)(Math.random()*5+5); 
                      // Ball's speed for x and y
                      //ballSpeedY = (float)(Math.random()*10+10);
                      ballSpeedY = (float)(Math.random()*5+5);
                  } else {
                      //ballSpeedX = (float)(Math.random()*10+10)*(-1);
                      ballSpeedX = (float)(Math.random()*5+5)*(-1);
                      // Ball's speed for x and y
                      //ballSpeedY = (float)(Math.random()*10+10)*(-1);
                      ballSpeedY = (float)(Math.random()*5+5)*(-1);
                  }
                  ballColor = (int)(Math.random()*3+1);
                  rectX = (float)(Math.random()*800)*((int)(Math.random()+Math.random()))-60;
                  if (rectX == -60) {
                      rectY = (float)(Math.random()*600);
                      rectSpeedX = (float)(Math.random()*5+10);
                      rectSpeedY = 0;
                  } else {
                        rectY = -60;
                        rectSpeedX = 0;
                        rectSpeedY =(float)(Math.random()*5+10);
                  }
               }
               //if the rect is hit
               if (Math.abs(mouseX-rectX) <= rectSide && Math.abs(mouseY-rectY) <= rectSide) {
                  rectHit = true; 
                  rectSpeedX = 0;
                  rectSpeedY = 0;
                  lives+=1.0;
               }
               
               if (bulletCount == 0) {
                  lost = true;
                  ballColor = (int)(Math.random()*3+1);
                  exLife = false;
                  rectX = (float)(Math.random()*800)*((int)(Math.random()+Math.random()))-60;
                  if (rectX == -60) {
                      rectY = (float)(Math.random()*600);
                      rectSpeedX = (float)(Math.random()*5+10);
                      rectSpeedY = 0;
                  } else {
                        rectY = -60;
                        rectSpeedX = 0;
                        rectSpeedY =(float)(Math.random()*5+10);
                  }
               }
               
               if (timer >= 1000) {
                  lost = true;
               }
               
               if (gg) {
                  ballSpeedX = 0;
                  ballSpeedY = 0;
               }
               
               if (replay == true) {
                  ballX = ballRadius + (float)(Math.random()*600); // Ball's center (x, y)
                  ballY = ballRadius + (float)(Math.random()*600); 
                  if (Math.random()-0.5 < 0) {
                      ballSpeedX = (float)(Math.random()*5+5);   // Ball's speed for x and y
                      ballSpeedY = (float)(Math.random()*5+5);
                  } else {
                      ballSpeedX = (float)(Math.random()*5+5)*(-1);   // Ball's speed for x and y
                      ballSpeedY = (float)(Math.random()*5+5)*(-1);
                  }
                  ballColor = (int)(Math.random()*3+1);
                  bulletCount = 3;
                  hit = false;
                  lost = false;
                  score = 0;
                  ballColor = 1;
                  lives = 3.0;
                  gg = false;
                  replay = false;
                  mouseX = 0;
                  mouseY = 0;
                  timer = 0;
               }
               
               if (lives > 3.0) {
                   lives = 3.0;
               }
               
               // Refresh the display              
               repaint(); // Callback paintComponent()
               // Delay for timing control and give other threads a chance
               try {
                  Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
                  if (!gg && menu == false) {
                      timer++;
                  }
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
      Color myNewLightBlue = new Color (124, 195, 236);
      g.setColor(myNewLightBlue);
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
            
      // Draw Extra Life Rectangle
      if (exLife == true) {
          g.setColor(Color.magenta);
          g.fillRect((int)rectX, (int)rectY, (int)rectSide, (int)rectSide);
      }      
            
      // Draw ground
      Color myNewDarkBlue = new Color (4, 62, 94);
      g.setColor(myNewDarkBlue);
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
      //g.drawString(timer+"", 20, 30);  //tester for timer
      //g.drawString(exLife+" " + rectHit+" " + (int)rectX+" " 
      //      + (int)rectY+" " + (int)rectSpeedX+" " + (int)rectSpeedY+" " + lives+"", 20, 30);  //tester for exLife
      
      if (menu == false) {
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
          if (bestScore <= score) {
              g.setColor(Color.red);
          }
          else{
              g.setColor(Color.WHITE);
          }
          g.drawString(score+"", 605, 740);
          
          // life board
          g.setColor(Color.BLACK);
          g.fillRect(250, 680, 250, 70);
          g.setColor(Color.WHITE);
          g.setFont(new Font("Courier New", Font.PLAIN, 30));
          g.drawString("LIVES", 255, 720);
          g.setColor(Color.magenta);
          if ((int)lives >= 3) {
             g.fillRect(360, 700, 30, 30);//inner health
             g.fillRect(400, 700, 30, 30);
             g.fillRect(440, 700, 30, 30);//outer health
          } else if ((int)lives == 2) {
             g.fillRect(360, 700, 30, 30);
             g.fillRect(400, 700, 30, 30);
             g.drawRect(440, 700, 30, 30);
          } else if ((int)lives == 1) {
             g.fillRect(360, 700, 30, 30);
             g.drawRect(400, 700, 30, 30);
             g.drawRect(440, 700, 30, 30);
          } else if ((int)lives == 0){
             g.drawRect(360, 700, 30, 30);
             g.drawRect(400, 700, 30, 30);
             g.drawRect(440, 700, 30, 30);
             gg = true;
          }
          
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
         timer = 0;
         if (Math.random() <= 0.2) {
            exLife = true;
         }
      }
      

      //win
      if (hit == true) {
         g.setColor(Color.magenta);
         g.setFont(new Font("Courier New", Font.PLAIN, 60));
         g.drawString("GOT IT!", 350, 350);
         g.setColor(Color.WHITE);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         hit = false;
        
        if(bulletCount==2)
        {
         g.drawString("+1500", (int)(ballX + 60), (int)(ballY + 60));
         score += 1500;
        }
        else if(bulletCount==1)
        {
         g.drawString("+1000", (int)(ballX + 60), (int)(ballY + 60));
         score += 1000;
        }
        else if(bulletCount==0)
        {
         g.drawString("+500", (int)(ballX + 60), (int)(ballY + 60));
         score += 500;
        }
        bulletCount = 3;
        timer = 0;
        if (Math.random() <= 0.2) {
            exLife = true;
        }
      }
      
      // time warning
      if (timer >= 800) {
         if (timer%10 == 0) {
             g.setColor(Color.RED);
             g.fillRect(0, 0, 800, 5);
             g.fillRect(0, 795, 800, 5);
             g.fillRect(0, 0, 5, 800);
             g.fillRect(795, 0, 5, 800);
         }
      }
      
      // game over
      if (gg) {
         //set the new best score
         if (score > bestScore) {
             bestScore = score;
         }
         //gg text 
         g.setColor(Color.GRAY);
         g.setFont(new Font("Courier New", Font.PLAIN, 90));
         g.drawString("GG!", 330, 350);
         //replay button
         g.setColor(Color.orange);
         g.fillRect(330, 450, 130, 70);
         g.setColor(Color.BLACK);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         g.drawString("Replay", 340, 490);
         //main menu button
         g.setColor(Color.orange);
         g.fillRect(330, 550, 130, 70);
         g.setColor(Color.BLACK);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         g.drawString("EXIT", 350, 590);
      }
      //best score
         g.setColor(Color.magenta);
         g.setFont(new Font("Courier New", Font.BOLD, 30));
         g.drawString("Best Score: " + bestScore, 450, 30);
      //menu
      if (menu == true) {
         //game title
         g.setColor(Color.BLACK);
         g.setFont(new Font("Futura", Font.PLAIN, 90));
         g.drawString("Ball Hunt", 200, 350);
          
         //play button
         g.setColor(Color.orange);
         g.fillRect(330, 450, 130, 70);
         g.setColor(Color.BLACK);
         g.setFont(new Font("Courier New", Font.BOLD, 30));
         g.drawString("Play", 360, 490);
         
         //rules
         g.setColor(Color.orange);
         g.fillRect(330, 550, 130, 70);
         g.setColor(Color.BLACK);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         g.drawString("Rules", 350, 590);
      }
      if (rules == true){
         //rules content
         g.setColor(Color.BLACK);
         g.fillRect(130, 150, 530, 530);
         g.setColor(Color.white);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         g.drawString("Rules", 350, 180);
         g.setFont(new Font("Courier New", Font.PLAIN, 15));
         g.drawString("Objective: to click on as many bouncing balls", 175, 220);
         g.drawString("as possible to get maximum points.", 175, 240);
         g.drawString("Gameplays", 175, 280);
         g.drawString("- during each round, you will get 3 shots with", 175, 300);
         g.drawString("the goal to click on the ball.", 175, 320);
         g.drawString("- you will get 3 lives; you lose a life if you", 175, 340);
         g.drawString("fail to click the ball with your 3 shots.", 175, 360);
         g.drawString("- you get 1 extra life when you click on the ", 175, 380);
         g.drawString("pink square that MAY appear on your screen.", 175, 400);
         g.drawString("it’s GAME OVER when you lose all 3 lives.", 175, 420);
         g.drawString("Scoring: you get less points with each shot", 175, 460);
         g.drawString("+ 1500 points/ball during your FIRST shot!", 175, 480);
         g.drawString("+ 1000 points/ball during your SECOND shot!", 175, 500);
         g.drawString("+ 500 points/ball during your THIRD shot!", 175, 520);
         g.drawString("Timer: failing to click the ball in 15 seconds", 175, 560);
         g.drawString("cost you 1 life.", 175, 580);
         //return button
         g.setColor(Color.orange);
         g.fillRect(330, 600, 130, 70);
         g.setColor(Color.BLACK);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         g.drawString("Return", 340, 640);
        }
   }
   
   public void mousePressed(MouseEvent mouse){
        // Get the location of the current mouse click.
        if (bulletCount > 0 && !gg && menu == false) {
            mouseX = mouse.getX();
            mouseY = mouse.getY();
            wait(50);
            mouseX=0;
            mouseY=0;
            bulletCount--;
        }
        
        if (gg) {
            mouseX = mouse.getX();
            mouseY = mouse.getY();
            if (mouseX <= 460 && mouseX >= 330 && mouseY <= 520 && mouseY >= 450) { //Clicked Replay Button
                replay = true;
            }
            if (mouseX <= 460 && mouseX >= 330 && mouseY <= 620 && mouseY >= 550) { //Clicked Exit Button
                menu = true;
                gg = false;
                ballSpeedX = (float)(Math.random()*5+5);   // Ball's speed for x and y
                ballSpeedY = (float)(Math.random()*5+5);
            }
        }
        
        if (rules){
            if (mouseX <= 460 && mouseX >= 330 && mouseY <= 670 && mouseY >= 600) { //Clicked retu Button
                rules = false;
            }
        }
        
        if (menu == true) {
            mouseX = mouse.getX();
            mouseY = mouse.getY();
            if (mouseX <= 460 && mouseX >= 330 && mouseY <= 520 && mouseY >= 450 && rules == false) { //Clicked Play Button
                menu = false;
                bulletCount = 3;
                hit = false;
                lost = false;
                score = 0;
                ballColor = 1;
                lives = 3.0;
                gg = false;
                replay = false;
                mouseX = 0;
                mouseY = 0;
                timer = 0;
            }
            if (mouseX <= 460 && mouseX >= 330 && mouseY <= 620 && mouseY >= 550) { //Clicked Rules Button
                rules = true;
            }
        }
        // Tell the panel that we need to redraw things.
        repaint();
    }

   /* The following methods have to be here to comply
   with the MouseListener interface, but we don't
   use them, so their code blocks are empty. */
   public void mouseEntered(MouseEvent mouse){ }   
   public void mouseExited(MouseEvent mouse){ }
   //public void mousePressed(MouseEvent mouse){ }
   public void mouseClicked(MouseEvent mouse){ 
   }
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
            JFrame frame = new JFrame("Ball Hunt");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new BouncingBallsColorReworked());
            frame.pack();
            frame.setVisible(true);
         }
      });
   }
   
}