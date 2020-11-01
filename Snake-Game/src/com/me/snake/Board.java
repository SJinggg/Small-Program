package com.me.snake;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//audio input
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class Board extends JPanel implements ActionListener{
  private final int BWIDTH = 500;
  private final int BHEIGHT = 500;
  private final int IMGSIZE = 10;
  private final int SEED = 43;
  private final int DELAY = 140;

  private enum Direction {
    UP, DOWN, LEFT, RIGHT
  };

  /** snake's body position
     BWIDTH * BHEIGHT / (Image size (10 * 10)) = 2500; */
  private final int snake_x[] = new int[2500];
  private final int snake_y[] = new int[2500];

  private int snakeLength = 3;
  private Direction snake_direction = Direction.RIGHT;

  private int rat_posX;
  private int rat_posY;

  /** Image name */
  private Image rat;
  private Image dot;

  private Timer timer;
  private boolean playing = true;

  public Board() {
    initBoard();
  }

  private void initBoard () {
    setBackground(Color.BLACK);
    setFocusable(true);

    setPreferredSize(new Dimension(BWIDTH, BHEIGHT));
    addKeyListener(new BoardListener());
    loadImg();
    initGame();
  }

  private void loadImg () {
    ImageIcon dots = new ImageIcon("src/resources/dot.png");
    dot = dots.getImage();

    ImageIcon rats = new ImageIcon("src/resources/apple.png");
    rat = rats.getImage();
  }

  public void playSound(String soundName){
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));	
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex){
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

  private void initGame () {
    /** Initialize snake position */
    for(int x = 0; x < snakeLength; x++){
      snake_x[x] = 50 - x * IMGSIZE;
      snake_y[x] = 50;
      
    }

    generateRat();
    timer = new Timer(DELAY, this);
    timer.start();
  }

  private void generateRat() {

    int r = (int) (Math.random() * SEED);
    rat_posX = ((r * IMGSIZE));

    r = (int) (Math.random() * SEED);
    rat_posY = ((r * IMGSIZE));
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    doDrawing(g);
  }
  
  private void doDrawing(Graphics g) {
    if (playing) {
        g.drawImage(rat, rat_posX, rat_posY, this);

        for (int x = 0; x < snakeLength; x++) {
          g.drawImage(dot, snake_x[x], snake_y[x], this);
        }

        Toolkit.getDefaultToolkit().sync();
    } else {
        gameOver(g);
    } 
  }

  private void gameOver(Graphics g) { 
    String msg = "Game Over";
    Font small = new Font("Helvetica", Font.BOLD, 14);
    FontMetrics metr = getFontMetrics(small);

    g.setColor(Color.white);
    g.setFont(small);
    g.drawString(msg, (BWIDTH - metr.stringWidth(msg)) / 2, BHEIGHT / 2);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

      if (playing) {

          checkEaten();
          checkCollision();
          performMovement();
      }

      repaint();
  }

  private void checkEaten () {
    if(snake_x[0] == rat_posX && snake_y[0] == rat_posY){
      snakeLength += 1;
      playSound("src/resources/eat-sound.wav");
      generateRat();
    }
  }

  private void checkCollision () {
    for(int x = snakeLength; x > 0; x--){
      if(x > 3 && snake_x[0] == snake_x[x] && snake_y[0] == snake_y[x]){
        playing = false;
      }
    }
    if(snake_x[0] >= BWIDTH || snake_x[0] < 0 || snake_y[0] >= BHEIGHT || snake_y[0] < 0)
      playing = false;
  }

  private void performMovement () {
    for(int x = snakeLength; x > 0 ; x--){
      snake_x[x] = snake_x[x-1];
      snake_y[x] = snake_y[x-1];
    }

    if(snake_direction == Direction.LEFT){
      snake_x[0] -= IMGSIZE;
    }
    else if(snake_direction == Direction.RIGHT){
      snake_x[0] += IMGSIZE;
    }
    else if(snake_direction == Direction.UP){
      snake_y[0] -= IMGSIZE;
    }
    else if(snake_direction == Direction.DOWN){
      snake_y[0] += IMGSIZE;
    }
  }

  private class BoardListener extends KeyAdapter {
    public void keyPressed(KeyEvent evt){
      int inputKey = evt.getKeyCode();

      if((inputKey == KeyEvent.VK_UP || inputKey == KeyEvent.VK_W) && snake_direction != Direction.DOWN){
        snake_direction = Direction.UP;
      }
      else if((inputKey == KeyEvent.VK_DOWN || inputKey == KeyEvent.VK_S) && snake_direction != Direction.UP){
        snake_direction = Direction.DOWN;
      }
      else if((inputKey == KeyEvent.VK_LEFT || inputKey == KeyEvent.VK_A) && snake_direction != Direction.RIGHT){
        snake_direction = Direction.LEFT;
      }
      else if((inputKey == KeyEvent.VK_RIGHT || inputKey == KeyEvent.VK_D) && snake_direction != Direction.LEFT){
        snake_direction = Direction.RIGHT;
      }

    }
  }
}