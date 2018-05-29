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
public class BouncingBallsReworked extends JPanel implements MouseListener, MouseMotionListener{
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
   private int a;  // ball acceleration
   private boolean gunshop = false;
   private boolean aim = false;
   Sniper sniper = new Sniper();
   Bazooka bazooka = new Bazooka();
   Pistol pistol = new Pistol();
   private boolean uSniper = false;;
   private boolean uBazooka = false;
   private boolean uPistol = true;
   
   // Themes
   private int theme;
   private int themeNum = 4;
   
   // Other properties
    private int mouseX;
    private int mouseY;
    private boolean hit;
    private boolean lost;
    private boolean rules;
    int X, Y;
   
   private static final int UPDATE_RATE = 60; // Number of refresh per second
  
   /** Constructor to create the UI components and init game objects. */
   public BouncingBallsReworked() {
      this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));
      addMouseListener(this); 
      addMouseMotionListener(this);
      
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
      a = 0;
      theme = 0;
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
               
               if (uSniper == true) {
                   ballRadius = 40;
               } else {
                   ballRadius = 30;
               }
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
               if (lost != true) {
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
               } 
               
               //if the ball is hit
               if (Math.sqrt(Math.pow(mouseX-ballX,2)+Math.pow(mouseY-ballY,2)) <= ballRadius && !gg && menu == false && !uBazooka) {
                  ballSpeedX = 0;
                  ballSpeedY = 0;
                  hit = true;
                  aim = true;
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException ex) { }
                  ballX = ballRadius + (float)(Math.random()*600); // Ball's center (x, y)
                  ballY = ballRadius + (float)(Math.random()*600); 
                  if (Math.random()-0.5 < 0) {
                      ballSpeedX = (float)(Math.random()*3+5 + a); 
                      ballSpeedY = (float)(Math.random()*3+5 + a);
                  } else {
                      //ballSpeedX = (float)(Math.random()*10+10)*(-1);
                      ballSpeedX = (float)(Math.random()*3+5 + a)*(-1);
                      ballSpeedY = (float)(Math.random()*3+5 + a)*(-1);
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
                  if (score%2000 <= 500 && a <= 10) {
                      a++;
                  }
               }
               if (uBazooka == true) {
                  float angle = (float)Math.toDegrees(Math.atan2((double)(ballY - mouseY),(double)(ballX - mouseX)));
                  angle = (angle + 180.0f) % 360.0f;
                  angle = 360.0f - angle;
                  float side = 0;
                  if (angle <= 90 && angle >= 0) {
                      side = ((float)(-1/3.621*Math.abs(angle-45)+(30*Math.sqrt(2))));
                  } else if (angle <= 180 && angle > 90) {
                      side = ((float)(-1/3.621*Math.abs(angle-135)+(30*Math.sqrt(2))));
                  } else if (angle <= 270 && angle > 180) {
                      side = ((float)(-1/3.621*Math.abs(angle-225)+(30*Math.sqrt(2))));
                  } else {
                      side = ((float)(-1/3.621*Math.abs(angle-315)+(30*Math.sqrt(2))));
                  }
                  if ((Math.sqrt(Math.pow(mouseX-ballX,2)+Math.pow(mouseY-ballY,2)) - side <= ballRadius 
                  ||(ballX>=mouseX-30 && ballY>=mouseY-30 && ballX<=mouseX+30 && ballY<=mouseY+30))&& 
                  ballX > 50 && ballY > 50 && !gg && menu == false) {
                       
                      ballSpeedX = 0;
                      ballSpeedY = 0;
                      hit = true;
                      aim = true;
                      try {
                        Thread.sleep(1000);
                      } catch (InterruptedException ex) { }
                      ballX = ballRadius + (float)(Math.random()*600); // Ball's center (x, y)
                      ballY = ballRadius + (float)(Math.random()*600); 
                      if (Math.random()-0.5 < 0) {
                          ballSpeedX = (float)(Math.random()*3+5 + a); 
                          ballSpeedY = (float)(Math.random()*3+5 + a);
                      } else {
                          ballSpeedX = (float)(Math.random()*3+5 + a)*(-1);
                          ballSpeedY = (float)(Math.random()*3+5 + a)*(-1);
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
                      if (score%2000 <= 500 && a <= 10) {
                          a++;
                      }  
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
                  aim = true;
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
                  if (uSniper == true) {
                      bulletCount = 3;
                  } else if (uBazooka == true) {
                      bulletCount = 2;
                  } else if (uPistol == true){
                      bulletCount = 5;
                  }
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
      if (theme != 1) {
          g.setColor(new Color(166, 235, 255));
      } else {
          g.setColor(new Color (124, 195, 236)); //lightBlue
      }
      g.fillRect(0, 0, BOX_WIDTH, BOX_HEIGHT);
      
      Sunset theme4 = new Sunset();
      if (theme == 3) {
          theme4.paint(g);
          g.setColor(new Color(102, 41, 0));
          g.fillRect(0, 710, 800, 100);
      }
      
      // Draw the ball
      if (ballColor == 1) {
          g.setColor(Color.BLUE);
      } else if (ballColor == 2) {
          g.setColor(new Color(204, 0, 0));
      } else {
          g.setColor(new Color(102, 0, 253));
      }
      g.fillOval((int) (ballX - ballRadius), (int) (ballY - ballRadius),
            (int)(2 * ballRadius), (int)(2 * ballRadius));
            
      // Draw Extra Life Rectangle
      if (exLife == true) {
          g.setColor(Color.magenta);
          g.fillRect((int)rectX, (int)rectY, (int)rectSide, (int)rectSide);
      }      
            
      // Draw ground
      if (theme == 0) {
          g.setColor(new Color(30, 139, 48));
          g.fillRect(0, 650, 800, 150);
      } else if (theme == 1) {
          g.setColor(new Color (4, 62, 94));
          g.fillRect(0, 650, 800, 150);
      } else if (theme == 2) {
          g.setColor(new Color (0, 153, 0));
          g.fillRect(0, 650, 800, 150);
      } else { }
      
      // Other stuff
      Trees tree = new Trees();
      if (theme == 2) {
          tree.paint(g);
      }
      
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
      //g.drawString(a + "", 20, 30); //tester for acceleration
      //g.drawString(hit+"", 20, 30); // hit tester
      
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
         wait(500);
         if (uSniper == true) {
             bulletCount = 3;
         } else if (uBazooka == true) {
             bulletCount = 2;
         } else if (uPistol){
             bulletCount = 5;
         }
         timer = 0;
         if (Math.random() <= 0.2) {
            exLife = true;
         }
         aim = false;
      }
      
      //win
      if (hit == true) {
        if (theme != 1) {
            g.setColor(Color.magenta);
        } else {
            g.setColor(Color.yellow);
        }
        g.setFont(new Font("Courier New", Font.BOLD, 60));
        g.drawString("GOT IT!", 350, 350);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.PLAIN, 30));
        hit = false;
        if (uSniper == true) {
            if(bulletCount>=2)
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
        } else if (uBazooka == true) {
            if(bulletCount>=1)
            {
             g.drawString("+1500", (int)(ballX + 60), (int)(ballY + 60));
             score += 1500;
            }
            else if(bulletCount==0)
            {
             g.drawString("+1000", (int)(ballX + 60), (int)(ballY + 60));
             score += 1000;
            }
        } else if (uPistol == true) {
            if(bulletCount>=4)
            {
             g.drawString("+1500", (int)(ballX + 60), (int)(ballY + 60));
             score += 1500;
            }
            else if(bulletCount==3)
            {
             g.drawString("+1000", (int)(ballX + 60), (int)(ballY + 60));
             score += 1000;
            }
            else if(bulletCount<=2)
            {
             g.drawString("+500", (int)(ballX + 60), (int)(ballY + 60));
             score += 500;
            }
        }
        
        if (uSniper == true) {
             bulletCount = 3;
         } else if (uBazooka == true) {
             bulletCount = 2;
         } else if (uPistol == true){
             bulletCount = 5;
         }
        
        timer = 0;
        if (Math.random() <= 0.2) {
            exLife = true;
        }
        aim = false;
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
      if (theme != 3) {
          g.setColor(Color.magenta);
      } else {
          g.setColor(new Color (4, 62, 94));
      }
      g.setFont(new Font("Courier New", Font.BOLD, 30));
      g.drawString("Best Score: " + bestScore, 450, 30);      
      
      //menu
      if (menu == true) {
         //game title
         g.setColor(Color.BLACK);
         g.setFont(new Font("Helvetica", Font.BOLD, 90));
         g.drawString("Ball Hunt", 200, 250);
         
         //switch theme
         int[] leftArrowX = new int[] {270, 250, 270};
         int[] arrowY = new int[] {350, 370, 390};
         int[] rightArrowX = new int[] {510, 530, 510};
         g.setColor(Color.black);
         g.setFont(new Font("Courier New", Font.BOLD, 30));
         g.drawString("Theme", 345, 320);
         g.fillPolygon(leftArrowX, arrowY, 3);
         g.fillPolygon(rightArrowX, arrowY, 3);
         
         g.setColor(Color.GRAY);
         g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
         if (theme == 0) {
            g.drawString("Day", 350, 380);
         } else if (theme == 1) {
            g.drawString("Night", 340, 380);
         } else if (theme == 2){
            g.drawString("Forest", 330, 380);
         } else {
            g.drawString("Sunset", 330, 380);
         }
         
         //gun shop button
         g.setColor(Color.PINK);
         g.fillOval(520, 500, 160, 80);
         g.setColor(new Color(128, 0, 255));
         g.setFont(new Font("Courier New", Font.BOLD, 30));
         g.drawString("Armory", 545, 545);
         
         //play button
         g.setColor(Color.orange);
         g.fillRect(330, 450, 130, 70);
         g.setColor(Color.BLACK);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
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
         g.fillRect(130, 150, 530, 580);
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
         g.fillRect(330, 640, 130, 70);
         g.setColor(Color.BLACK);
         g.setFont(new Font("Courier New", Font.PLAIN, 30));
         g.drawString("Return", 340, 680);
      }
      if (gunshop == true) {
         g.setColor(new Color(179, 102, 255));
         g.fillRect(100, 100, 600, 600);
         g.setColor(Color.white);
         g.setFont(new Font("Courier New", Font.BOLD, 30));
         g.drawString("Armory", 320, 140);
         sniper.paint(g);
         bazooka.paint(g);
         pistol.paint(g);
         g.setFont(new Font("Courier New", Font.PLAIN, 15));
         g.drawString("Sniper - It has 3 shots, deals fatal damage with expended views.", 120, 200);
         g.drawString("Bazooka - It has 2 shots, deals massive area damage.", 120, 340);
         g.drawString("Pistol - It has 5 shots, deals pretty neat damage.", 120, 490);
         //select gun
         g.drawRect(500, 250, 30, 30); // select sniper
         g.drawRect(500, 390, 30, 30); // select bazooka
         g.drawRect(500, 540, 30, 30); // select pistol
         g.setColor(Color.GREEN);
         if (uSniper == true) {
             g.fillRect(500, 250, 30, 30);
         } else if (uBazooka == true) {
             g.fillRect(500, 390, 30, 30);
         } else if (uPistol == true) {
             g.fillRect(500, 540, 30, 30);
         }
         //exit button
         g.setColor(Color.orange);
         g.fillRect(340, 620, 100, 50);
         g.setColor(Color.black);
         g.setFont(new Font("Courier New", Font.BOLD, 30));
         g.drawString("Exit", 350, 655);
      }
      
      //aimscope
      if (uSniper == true) {
          sniper.aim(g, X, Y);
      }
      if (uBazooka == true) {
          bazooka.aim(g, X, Y);
      }
      if (uPistol == true) {
          pistol.aim(g, X, Y);
      }
   }
   
   public void mousePressed(MouseEvent mouse){
        // Get the location of the current mouse click.
        boolean clickedExit = false;
        
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
                clickedExit = true;
            }
        }
        
        if (menu == true) {
            mouseX = mouse.getX();
            mouseY = mouse.getY();
            if (mouseX <= 460 && mouseX >= 330 && mouseY <= 520 && mouseY >= 450 && rules == false && gunshop == false) { //Clicked Play Button
                menu = false;
                if (uSniper == true) {
                      bulletCount = 3;
                  } else if (uBazooka == true) {
                      bulletCount = 2;
                  } else if (uPistol == true){
                      bulletCount = 5;
                  }
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
                a = 0;
            }
            if (mouseX <= 460 && mouseX >= 330 && mouseY <= 620 && mouseY >= 550 && clickedExit == false && gunshop == false) { //Clicked Rules Button
                rules = true;
            }
            if (rules){
                if (mouseX <= 460 && mouseX >= 330 && mouseY <= 720 && mouseY >= 640 && gunshop == false) { //Clicked return Button
                    rules = false;
                }
            }
            clickedExit = false;
            //choosing theme
            if (mouseX - 250 <= 20 && mouseX - 250 >= 0 && mouseY - 350 <= 20 && mouseY - 350 >= 0 && rules == false && gunshop == false) {  // left arrow clicked
               if (theme == 0) {
                   theme = themeNum-1;
               } else {
                   theme --;
               }
            }
            if (mouseX - 510 <= 20 && mouseX - 510 >= 0 && mouseY - 350 <= 20 && mouseY - 350 >= 0 && rules == false && gunshop == false) {  // right arrow clicked
               if (theme == themeNum-1) {
                   theme = 0;
               } else {
                   theme ++;
               }
            }
            //gun shop 
            if (mouseX-520 >= 0 && mouseX-520 <= 160 && mouseY-500 >= 0 && mouseY-500 <= 80 && rules == false) { //open gun shop
               gunshop = true;
            }
            if (mouseX>=340 && mouseX<=440 && mouseY>=620 && mouseY<=670) { //exit gun shop
               gunshop = false;
            }
            if (mouseX>=500 && mouseX <= 530) {
               if (mouseY >= 250 && mouseY <= 280) { //select sniper
                  uSniper = true;
                  uBazooka = false;
                  uPistol = false;
                  bulletCount = 3;
               }
               if (mouseY >= 390 && mouseY <= 420) { //select bazooka
                  uSniper = false;
                  uBazooka = true;
                  uPistol = false;
                  bulletCount = 2;
               }
               if (mouseY >= 540 && mouseY <= 570) { //select pistol
                  uSniper = false;
                  uBazooka = false;
                  uPistol = true;
                  bulletCount = 5;
               }
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
   public void mouseClicked(MouseEvent mouse){ }
   public void mouseReleased(MouseEvent mouse){ }
   
   @Override
    public void mouseMoved(MouseEvent e) {
        X = e.getX();
        Y = e.getY();
    }
 
    @Override
    public void mouseDragged(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
   
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
            frame.setContentPane(new BouncingBallsReworked());
            frame.pack();
            frame.setVisible(true);
         }
      });
   }
   
}
