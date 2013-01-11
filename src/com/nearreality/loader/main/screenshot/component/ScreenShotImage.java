package com.nearreality.loader.main.screenshot.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import com.nearreality.loader.main.Config;
import com.nearreality.loader.main.screenshot.gui.ImagePanel;
import com.nearreality.loader.main.screenshot.gui.MainFrame;

public class ScreenShotImage extends JLabel {
	/** Paint our Btn **/
    private Painter painter;
    /** File Name **/
    private String fileName;
    /** File Location **/
    private String fileLocation;
    /** Are we Selected? **/
    private boolean selected;
    /** next image **/
    private ScreenShotImage next;
    /** id **/
    private int id;
    
    /** 
     *  Create a screenshot image
     */
    public ScreenShotImage(int id,ImageIcon icon, String fileName, String fileLocation){
    	super(new ImageIcon(icon.getImage().getScaledInstance(161, 106,Image.SCALE_DEFAULT)));
    	this.setBorder(BorderFactory.createMatteBorder(
                2, 2, 2, 2, Color.white));
    	this.fileName = fileName;
    	this.fileLocation = fileLocation;
    	this.id = id;
    	this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(getFileName() == null)
					return;
				switch(e.getButton()) {
					case MouseEvent.BUTTON1:
						JOptionPane.showMessageDialog(null, "", getFileName(), JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getFileLocation()));
					break;
					case MouseEvent.BUTTON3:
			           selected = !selected;
			           changeUpload(selected);
			           repaint();
					break;
					
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    	setupImageElements();
    	setVisible(false);
    	repaint();
    	
    }
    
    public void changeUpload(boolean in){
    	if(in)
    	ImagePanel.totalSelected++;
    	else
    	ImagePanel.totalSelected--;
    	
    	MainFrame.imgPane.repaint();
    }
    public void setupImageElements(){
    	painter = new Painter(){
			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D)g;
				if(selected){
					g2d.setColor(Color.RED);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
					g2d.fillRect(0, 0, 161,106);
				}
				g2d.setColor(Color.black);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 6 * 0.1f));
			    g2d.fillRect(0,88, 90, 16);
			    g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
			    g2d.setColor(Color.white);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 8 * 0.1f));
				g2d.drawString(getFileName() != null ? getFileName().replaceAll(".png", "") : "No IMG" , 10, 101);
				try {
					g2d.drawImage(ImageIO.read(ScreenShotImage.class.getResource("/zoom.png")), 140,84,null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		
    	};
    }
    public void paintComponent(Graphics g) {
       super.paintComponent(g);
       long 		start = System.currentTimeMillis();
		try{
		// Delay depending on how far behind current time we are.
       Graphics2D g2d = (Graphics2D)g;
		if(selected){
			g2d.setColor(Color.RED);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 4 * 0.1f));
			g2d.fillRect(0, 0, 161,106);
		}
       painter.paint(g);
       start += (1000/45);
       Thread.sleep(Math.max(0, start - System.currentTimeMillis()));
   } catch(Exception e){
 	  e.printStackTrace();
   }
    }

    public int getId() {
    	return id;
    }
    
    public void setID(int id){
    	this.id = id;
    }

	public String getFileName() {
		return fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ScreenShotImage getNext() {
		return next;
	}

	public void setNext(ScreenShotImage next) {
		this.next = next;
	}
}
interface Painter {
    public void paint(Graphics g);
}
