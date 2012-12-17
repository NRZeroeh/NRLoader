package com.nearreality.loader.main.screenshot.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImagePanel extends JComponent {
	private Image image;

	public static int totalSelected = 0;
	
	public ImagePanel() throws IOException {
		this.image = ImageIO.read(getClass().getResource("/ssbackground.png"));
	}
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
		g.setColor(Color.white);
		g.drawString("("+totalSelected+")", 405, 300);
		long start = System.currentTimeMillis();
		try {
              start += (1000/30);
              Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
          } catch(Exception e){
        	  e.printStackTrace();
          }
		image.flush();
	}
}