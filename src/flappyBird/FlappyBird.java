package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, KeyListener {

	public static FlappyBird flappyBird;

	public Renderer renderer;

	public Rectangle bird;

	public int ticks, yMotion, score;

	public Random rand;

	public ArrayList<Rectangle> columns;

	public boolean gameOver, started;

	public final int WIDTH = 1200, HEIGHT = 600;

	public FlappyBird() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);

		renderer = new Renderer();
		rand = new Random();

		jframe.add(renderer);
		jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setVisible(true);
		jframe.setResizable(false);
		jframe.addKeyListener(this);

		bird = new Rectangle(WIDTH / 2 - 50, HEIGHT / 2 - 50, 20, 20);
		columns = new ArrayList<Rectangle>();
		

		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);

		timer.start();
	}

	public void addColumn(Boolean start) {

		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);

		if (start) {
			columns.add(new Rectangle(width + WIDTH + columns.size() * 300, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}

	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}

	public void repaint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT - 120, WIDTH, 120);

		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 120, WIDTH, 20);

		g.setColor(Color.yellow);
		g.fillRect(bird.x, bird.y, 20, 20);
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		
		if(!started) {
			g.drawString("Click Space to Jump", 75, 300);
		}
		if(gameOver) {
			g.drawString("Game Over!", 200, 300);
			g.drawString("Score: " + score/2, 200, 400);
		}
		if(!gameOver && started) {
			g.drawString(""+score/2, WIDTH/2 -50, 100);
		}

		for (Rectangle c : columns) {
			paintColumn(g, c);
		}
	}
	
	public void jump() {
		if(gameOver) {
			bird = new Rectangle(WIDTH / 2 - 50, HEIGHT / 2 - 50, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;

			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			gameOver = false;
		}
		if(!started) {
			started = true;
		} else if(!gameOver) {
			if(yMotion > 0) {
				yMotion = 0;
			} 
			yMotion -= 10;
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int speed = 10;
		ticks++;
		

		if (started) {
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);

				column.x -= speed;
			}

			if (ticks % 2 == 0 && yMotion < 15) {

				yMotion += 2;
			}

			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);

				if (column.x + column.width < 0) {
					columns.remove(column);

					if (column.y == 0) {
						addColumn(false);
					}
				}
			}

			bird.y += yMotion;

			for (Rectangle column : columns) {
				
				if(bird.x + bird.width/2 > column.x + column.width/2 - 10 && bird.x + bird.width/2 < column.x + column.width/2 +10) {
					score++;
				}
				if (column.intersects(bird)) {
					gameOver = true;
					bird.x = column.x - bird.width;
				}
				if (bird.y > HEIGHT - 120 || bird.y < 0) {
					
					gameOver = true;
	
				}
				
				if(gameOver) {
					bird.y = HEIGHT - 120 - bird.height;
				}
			}
		}
		renderer.repaint();
	}

	public static void main(String[] args) {
		flappyBird = new FlappyBird();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}
