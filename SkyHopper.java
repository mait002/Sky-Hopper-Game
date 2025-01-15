import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SkyHopper extends JPanel implements ActionListener, KeyListener {
    int boardheight = 640;
    int boardwidth = 360;

    //images
    Image backgroundImage;
    Image ballImage;
    Image barImage;
    

    //bird
    int ballX = boardwidth/8;
    int ballY = boardheight/2;
    int ballWidth = 34;
    int ballHeight = 34;

    class Ball{
        int x = ballX;
        int y = ballY;
        int width = ballWidth;
        int height = ballHeight;
        Image img;

        Ball(Image img){
            this.img = img;
        }
    }

    //Bars
    int barX = boardwidth/8;
    int barY = boardheight/2;
    int barWidth = 100; 
    int barHeight = 20;


    class Bar{
        int x = barX;
        int y = barY;
        int width = barWidth;
        int height = barHeight;
        Image img;
        boolean passed = false;

        Bar(Image img){
            this.img = img;
        }
    }
    //game logic
    Ball ball;
    int velocityX = -4; // moves bars to the left speed (simulates ball moving right)
    int velocityY = 0; // move ball up and down speed
    int gravity = 1;

    ArrayList<Bar> bars;

    Random random = new Random();
    Timer gameLoop;
    Timer placeBarsTimer;

    boolean gameOver = false;
    double score = 0;

    boolean showGameOver = false;
    Timer flashingTextTimer;

    

    SkyHopper() {
        setPreferredSize(new Dimension(boardwidth, boardheight));
        //setBackground(Color.green);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImage = new ImageIcon(getClass().getResource("./img/background.jpg")).getImage();
        ballImage = new ImageIcon(getClass().getResource("./img/pink.png")).getImage();
        barImage = new ImageIcon(getClass().getResource("./img/bar1.png")).getImage();

        //ball
        ball = new Ball(ballImage);
        bars = new ArrayList<Bar>();

        //place bars timer
        placeBarsTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placeBars();
            }
        });
        placeBarsTimer.start();

        

        //game timer
        gameLoop = new Timer(1000/60, this); //(1000ms = 1s) and (1000/60 as we want to draw 60 frames/sec) 16.6s, this refers to skyhopper class
        gameLoop.start();

        flashingTextTimer = new Timer(500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameOver = !showGameOver;
                repaint();
            }
        });
        
    }

    

    public void placeBars(){
    
        int randomBarY = random.nextInt(boardheight - barHeight);
        int startX = boardwidth;

        //int openingSpace = boardheight/4;

        Bar bar = new Bar(barImage);
        bar.x = startX;
        bar.y = randomBarY;
        bars.add(bar);

        

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        
        //background
        g.drawImage(backgroundImage, 0, 0, boardwidth, boardheight, null);

        //ball
        g.drawImage(ballImage, ball.x, ball.y, ball.width, ball.height, null);

        //barsth
        for (int i = 0; i < bars.size(); i++){
            Bar bar = bars.get(i);
            g.drawImage(bar.img, bar.x, bar.y, bar.width, bar.height, null);
        }

        //score
        g.setColor(Color.black);

        g.setFont(new Font("Arial", Font.PLAIN, 32));

        //Game Over
        if (gameOver && showGameOver){
            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("GAME OVER " + String.valueOf((int) score), boardwidth/6, boardheight/2);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }
    

    public void move() {
        //ball
        velocityY += gravity;
        ball.y += velocityY; // moves bird -6 px/frame
        ball.y = Math.max(ball.y, 0);

        //bars
        for (int i = 0; i < bars.size(); i++){
            Bar bar = bars.get(i);
            bar.x += velocityX;

            if (!bar.passed && (ball.x > (bar.x + bar.width)) && ((ball.y + ball.height) < bar.y)) {
                bar.passed = true;
                score += 1;
            }

            if (collision(ball, bar)){
                gameOver = true;
            }
        }

        if (ball.y > boardheight){
            gameOver = true;
        }
    }

    
    

    public boolean collision(Ball a, Bar b){
        return a.x < b.x + b.width && //a's top left corner doesnt reach b's top right corner
               a.x + a.width > b.x && //a's top right corner passes b's top left corner
               a.y < b.y + b.height && //a's top left corner doesnt reach b's bottom left corner
               a.y + a.height > b.y; //a's bottom left corner passes b's top left corner
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint(); // will call paint function

        if (gameOver){
            placeBarsTimer.stop();
            gameLoop.stop();
            flashingTextTimer.start();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if (gameOver){
                //restart the game by resetting the condition
                ball.y = ballY;
                velocityY = 0;
                bars.clear();
                score = 0;
                gameOver = false;

                gameLoop.start();
                placeBarsTimer.start();
                flashingTextTimer.stop();
                showGameOver = false;
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    

}
