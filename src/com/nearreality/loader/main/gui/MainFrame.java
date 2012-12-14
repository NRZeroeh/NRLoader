/**
 * Copyright (c) 2012-2015 Patrick "Zeroeh"
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */ 
package com.nearreality.loader.main.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.nearreality.loader.main.gui.component.LaunchGameButton;
import com.nearreality.loader.main.gui.component.SocialMediaButton;

import com.nearreality.loader.main.services.data.ThreadData;


public class MainFrame extends JFrame {
	/** Our News Pane **/
	private NewsPane newsPane;
	/** Our Launch Text **/
	private JLabel launchIcon;
	/** Social Media Buttons **/
	private JButton[] socialMediaButton; 
	/** Game Launch Buttons **/
	
	private JButton[] launchGameButton;
	
	/** Splash Screen **/
	private SplashScreen splash;
	
	/**
	 *  Create our Launcher GUI
	 * @throws Exception - image not found
	 */
	private void createGui() throws Exception {
			setContentPane(new ImagePanel());
			int width = 680;
			int height = 390;
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (screen.width - width) / 2;
			int y = (screen.height - height) / 2;
			setBounds(x, y, width, height);
			newsPane = new NewsPane(this);
			newsPane.setBounds(50, 30, 581,195);
			launchIcon = new JLabel(new ImageIcon("imgs/logotext.png"));
			launchIcon.setBounds(225,5,233,35);
			add(newsPane);
			add(launchIcon);
			generateGameButtons();
			generateSocialButtons();
			setVisible(true);
			
	}
	
	/** Generate our Game Button from LaunchGameButton.class **/
	private void generateGameButtons(){
		launchGameButton = new JButton[2];
		int x = 75;
		for(int i = 0; i < launchGameButton.length; i++){
			launchGameButton[i] = LaunchGameButton.GameButton.createButton(i);
			launchGameButton[i].setBounds(x, 250, 238, 75);
			x += 300;
			add(launchGameButton[i]);
		}
			JLabel orTxt = new JLabel(new ImageIcon("imgs/ortext.png"));
			orTxt.setBounds(325, 275, 43,40);
			add(orTxt);
	}
	
	
	/** Generate Social Buttons **/
	private void generateSocialButtons(){
		socialMediaButton = new JButton[3];
		int x = 34;
		for(int i = 0; i < socialMediaButton.length; i++){
			socialMediaButton[i] = SocialMediaButton.SocialMedia.createButton(i);
			socialMediaButton[i].setBounds(260+x, 340, 34, 34);
			x += 34;
			add(socialMediaButton[i]);
		}
		
	}
	/** Construtor 
	 *  Remove our background and jframe
	 *  Allow mouse movement to drag the window 
	 *  Create the GUI
	 */
	public MainFrame(SplashScreen splash){
		try{
			requestFocusInWindow();
			this.splash = splash;
			splash.setVisible(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setUndecorated(true);
			final Point point = new Point();
			addMouseListener(new MouseAdapter() {
		            public void mousePressed(MouseEvent e) {
		                point.x = e.getX();
		                point.y = e.getY();
		            }
		        });
		     addMouseMotionListener(new MouseMotionAdapter() {
		            public void mouseDragged(MouseEvent e) {
		                Point p = getLocation();
		                setLocation(p.x + e.getX() - point.x,
		                        p.y + e.getY() - point.y);
		            }
		        });
		 
			createGui();
		} catch (Exception e){
			e.printStackTrace();
	}
	}

	public SplashScreen getSplash(){
		return splash;
	}
}


/** Background image for JFrame
 * runs at 30fps.
 */
class ImagePanel extends JComponent {
	private Image image;

	public ImagePanel() throws IOException {
		this.image = ImageIO.read(getClass().getResource("/background.png"));
	}
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
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
