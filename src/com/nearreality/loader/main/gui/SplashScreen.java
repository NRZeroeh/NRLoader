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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


import com.nearreality.loader.main.Config;
import com.nearreality.loader.main.gui.component.ProgressBar;
import com.nearreality.loader.main.services.LatestThread;
import com.nearreality.loader.main.services.Updater;
import com.nearreality.loader.main.services.data.ThreadData;
import com.nearreality.loader.main.services.data.UpdateItem;
import com.nearreality.loader.main.util.Logger;
import com.nearreality.loader.main.util.Logger.Level;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class SplashScreen extends JWindow {

	/**
	 * Splash screen duration (milliseconds).
	 */

	private int splashDuration = 2000;
	
	/** Progress Bar **/
	private JProgressBar progressBar;
	
	/** Description Lbl **/
	private JLabel descLbl;
	
	/** Latest News Thread Instance **/
	private LatestThread lt;
	/** List of our news data **/
	private List<ThreadData> newsData;
	
	/** Create Splash Screen **/
	public void createSplash(){
		JPanel content = (JPanel) getContentPane();
		
		/** Background Image **/
		Image img = new ImageIcon(getClass().getResource("/splashbg.png")).getImage();
		
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);
		
		JLabel label = new JLabel(new ImageIcon(getClass().getResource("/splashbg.png")));
		JLabel logo = new JLabel(new ImageIcon(getClass().getResource("/logotext.png")));
		JLabel progressBarBg = new JLabel(new ImageIcon(getClass().getResource("/progressbarplaceholder.png")));
		progressBarBg.setBounds(82, 175, 422, 40);
		logo.setBounds(165,100,233,35);
		label.setOpaque(false);
		/** Some Progress Bar editting **/
		progressBar = new JProgressBar();
        progressBar.setValue(0);
        UIManager.put("ProgressBar.selectionForeground", Color.white);
        UIManager.put("ProgressBar.selectionBackground", Color.white);
        progressBar.setUI(new ProgressBar());
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.white);
        progressBar.setFont(new Font("Tahoma", Font.PLAIN, 10).deriveFont(Font.BOLD));
        progressBar.setOpaque(false);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(Color.black);
        progressBar.setBounds(82,175, 422, 49);
        /** DescLbl **/
        descLbl = new JLabel("Loading Gui Elements");
        descLbl.setForeground(Color.white);
		content.setOpaque(false);
        descLbl.setFont(new Font("Tahoma", Font.PLAIN, 10).deriveFont(Font.BOLD));
        descLbl.setBounds(235, 205, 512, 32);
        content.add(descLbl);
        content.add(progressBar);
		content.add(logo);
		content.add(progressBarBg);
		content.add(label, BorderLayout.CENTER);
	}
	
	/**
	 *  Connect to the NR Forums, Read /News/ and parse the news articles.
	 *  
	 */
	public void loadData(){
		this.lt = new LatestThread(this);
		this.newsData = this.lt.getData();
	
	}
	/** Our News List **/
	public List<ThreadData> getNews() {
		return this.newsData;
	}
	/** Load some Configuration files relating to cache, client and launcher versions **/
	public void loadConfiguration(){
		
		UpdateItem item = new UpdateItem();
		getDescLbl().setText("Configuring Launcher..");
		Config.configure(item.getThemeURL(), item.getLaunchURL(), item.getCientURL(), item.getContents(),item.getLaunchVer(),item.getCacheVer());
	}
	
	/** Show Splash **/
	public void showSplash() {
		createSplash();
		setVisible(true);
		repaint();
		validate();
		Logger.writeLog(Level.INFORMATION, "Preparing to load news data.");
		loadData();
		getProgressBar().setValue(100); 
		Logger.writeLog(Level.INFORMATION, "Preparing to configure system.");
		loadConfiguration();
		getDescLbl().setText("Preparing to check Cache..");
		try {
			Thread.sleep(splashDuration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getProgressBar().setValue(0);
		getDescLbl().setText("Checking Cache..");
		Logger.writeLog(Level.INFORMATION, "Preparing to check cache.");
		new Updater(this);
		
	}

	/** Load Launcher **/
	public void loadGame(){
		new MainFrame(this);
	}
	/**
	 * Starts splash screen.
	 */
	public static void main(String[] args) {
		final SplashScreen splash = new SplashScreen();    
				  splash.showSplash();
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JLabel getDescLbl() {
		return descLbl;
	}
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	public void setDescLbl(JLabel descLbl) {
		this.descLbl = descLbl;
	}

}