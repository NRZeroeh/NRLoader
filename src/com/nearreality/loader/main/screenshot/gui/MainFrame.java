package com.nearreality.loader.main.screenshot.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.nearreality.loader.main.gui.SplashScreen;
import com.nearreality.loader.main.screenshot.component.ScreenShotImage;


public class MainFrame extends JFrame {
	

	public static ImagePanel imgPane;
	/** Screen Shot Text Logo **/
	private JLabel screenShotTxt;
	/** Screen Shot Close Btn **/
	private JButton closeBtn;
	/** Screen Shot Left Btn **/
	private JButton leftArrowBtn;
	/** Screen Shot Right Logo **/
	private JButton rightArrowBtn;
	/** Screen Shot Upload Btn **/
	private JButton uploadBtn;
	/** Screen Shot Image head **/
	private List<ScreenShotImage> image = new ArrayList<ScreenShotImage>();
	
	public static void main(String[] args){
		new MainFrame();
	}
	private void createGui() throws IOException{
		imgPane = new ImagePanel();
		setContentPane(imgPane);
		int width = 650;
		int height = 331;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		createOtherButton();
		createScreenShotButton();
		
		viewPage(1);
		setBounds(x, y, width, height);
		setVisible(true);
	}
	
	
	public void createOtherButton(){
		uploadBtn = new JButton(new ImageIcon(MainFrame.class.getResource("/upload_button.png")));
		uploadBtn.setBounds(275, 285, 125,24);
		add(uploadBtn);
		
		leftArrowBtn = new JButton(new ImageIcon(MainFrame.class.getResource("/left_arrow.png")));
		leftArrowBtn.setBounds(20, 145, 32,36);
		leftArrowBtn.setBorderPainted(false);
		add(leftArrowBtn);
		
		rightArrowBtn = new JButton(new ImageIcon(MainFrame.class.getResource("/right_arrow.png")));
		rightArrowBtn.setBounds(600, 145, 32,36);
		rightArrowBtn.setBorderPainted(false);
		add(rightArrowBtn);
		
		screenShotTxt = new JLabel(new ImageIcon(MainFrame.class.getResource("/header.png")));
		screenShotTxt.setBounds(5,2,650,17);
		add(screenShotTxt);
		
		closeBtn = new JButton(new ImageIcon(MainFrame.class.getResource("/x.png")));
		closeBtn.setBounds(635,8,8,8);
		closeBtn.setBorderPainted(false);
		add(closeBtn);
	}
	/** Create a 6 by 6 matrix of images using mod 6 and mod 3 to control x and y coords **/
	public void createScreenShotButton(){
		int count = 0;
		int x = 80, y = 50;
		File directory = new File("/Users/zeroeh/.NR2006cache/Screenshots");
		File[] files = directory.listFiles();
		/** Lets loop through all the files **/
		for (int index = 0; index < files.length; index++)
		{ /** Only files with .png, should file filter but w.e **/
		   if(files[index].toString().endsWith(".png")){
			   /** If we are the after the 3rd item lets "carriage return" our row of images **/
			   if(count % 3 == 0 && count != 0){
					y+=115;
					x= 80;
			   }
			   /** Create a new Button **/
			   ScreenShotImage in = new ScreenShotImage(count,new ImageIcon(files[index].getPath()),files[index].getName(),files[index].getPath());
			   in.setBounds(x, y, 161, 106);
			   x+= 170;
			   /** Set us false Initally! **/
			   in.setVisible(false);
			   
			   add(in);
			   /** Add us to our list **/
			   image.add(in);
			   count++;
		   }
		}
		/** If we are not inital more then 6 we should fill us more **/
		while(count % 6 != 0){
			 /** If we are after 3rd we should carriage return our thumbnails **/
			  if(count % 3 == 0){
					y+=115;
					x= 80;
			   }
			  	/** Create our new "null" button **/
			   ScreenShotImage in = new ScreenShotImage(count,new ImageIcon(MainFrame.class.getResource("/noimage.png")),null,null);
		       in.setBounds(x, y, 161, 106);
			   x+= 170;
			   in.setVisible(false);
			   add(in);
			   image.add(in);
			   count++;
		}
	}
	
	/** Reset all images **/
	public void reset(){
		for(ScreenShotImage in: image){
			in.setVisible(false);
		}
	}
	/** View the page **/
	public void viewPage(int page){
		reset();
		int imageIndex = (page * 6) - 6; // 6 items per page, arrays start at 0.
		int x = 80, y = 50;
		for(int i = imageIndex; i < image.size(); i++){
			image.get(i).setVisible(true);
		}
		repaint();
	}
	
	public MainFrame(){
		try{
			requestFocusInWindow();
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
}
