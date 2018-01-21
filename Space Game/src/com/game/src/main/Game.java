package com.game.src.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// final variable = cannot be changed
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH /12 *9;
	public static final int SCALE =2;
	public final String TITLE = "2D Space Game";
	
	//setting up variables
	private boolean gameloop = false;
	private Thread thread;
	//starting some buffers
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	
	private synchronized void start() {
		if(gameloop)
			return;
		gameloop = true; //if thread is already running, no need to start game
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		if (!gameloop)
			return;
		
		gameloop = false;
		try {
			thread.join(); //join all threads together and wait them to die
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
		
		
	}
	
	//Runnable need this function 
	public void run() {
		long initialTime = System.nanoTime();
		final double FPS = 60.0;
		double ns = 1000000000/FPS;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(gameloop) {
			long now = System.nanoTime();
			delta += (now-initialTime) / ns;
			initialTime = now;
			if(delta >= 1) {
				tick();
				updates++;
				delta--;
				
				
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000; //print every 1 seconds
				System.out.println(updates + " Ticks, Fps "+frames);
				updates=0;
				frames=0;
			}
		}
		stop();
		
	}
	
	private void tick() {
		
	}
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			
			createBufferStrategy(3);//create 3 buffers
			return;
		}
		Graphics g = bs.getDrawGraphics();
		///////////////////////////////////
		
		
		g.drawImage(image,0,0,getWidth(),getHeight(),this);
		
		
		///////////////////////////////////
		g.dispose(); //remove buffers
		bs.show(); //show out bufferstrategy
		
		
	};
	
	public static void main(String args[]) {
		Game game = new Game();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack(); //causes this Window to be sized to fit the preferred size and layouts of its subcomponents.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //takes an int, to allow X button to work
		frame.setResizable(false); //cannot resize
		frame.setLocationRelativeTo(null); //not setting the location
		frame.setVisible(true); //allow visibility
	
		
		game.start(); //we dont need to call run because its Runnable (lul)
	}

}
