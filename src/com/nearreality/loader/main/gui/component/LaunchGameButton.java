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
package com.nearreality.loader.main.gui.component;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.nearreality.loader.main.gui.component.SocialMediaButton.SocialMedia;
import com.nearreality.loader.main.util.Logger;
import com.nearreality.loader.main.util.Logger.Level;
/**
 * 
 *  controls game launching. You can add more game buttons if you wish using the following format
 *  name(id,linktoclient,buttonicon,gametype
 * @author zeroeh
 *
 */
public class LaunchGameButton {
	public enum GameButton {
		OSS(0,getCacheDir()+"/nrlauncheross.jar" ,new ImageIcon(LaunchGameButton.class.getResource("/ossbutton.png")),GameType.OSS),
		NR(1,"disabled" ,new ImageIcon(LaunchGameButton.class.getResource("/maincoming.png")),GameType.NR);

		;
		/** Unique Idenifier **/
		private int uID;
		/** Game Type **/
		private GameType type;
		/** BackGround Image **/
		private ImageIcon image;
		/** Client link **/
		private String clientLink;
		
		/** Create our button **/
		public static JButton createButton(int id){
			JButton temp = new JButton();
			/** Loop through our Enum map **/
			  for(GameButton gm : GameButton.values()){
				  	if(gm.getUID() == id){ /* unique id from our Enum's */
				  		final GameType type = gm.getType();
				  		final String clientLink = gm.getClientLink();
				  		temp.setIcon(gm.getImage());
				  		temp.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e) {
								switch(type){
									/* oss */
									case OSS: 
										try {
											Desktop.getDesktop().open(new File(clientLink));
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										} catch (IllegalArgumentException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										} catch (SecurityException e1) {
										// TODO Auto-generated catch block
											e1.printStackTrace();
									}
										break;
								}
								System.exit(3);
																																							
							}	
						});
				  		temp.setBorder(BorderFactory.createEmptyBorder());
						temp.setContentAreaFilled(false);
				  	}
			  }
			return temp;
		}
		/** User ID **/
		public int getUID() {
			return this.uID;
		}
		/** Game Type **/
		public GameType getType() {
			return this.type;
		}
		/** Image Icon **/
		public ImageIcon getImage() {
			return this.image;
		}
		/** Set UID **/
		public void setUID(int uID) {
			this.uID = uID;
		}
		/** Set Type **/
		public void setType(GameType type) {
			this.type = type;
		}
		/** Set Image **/
		public void setImage(ImageIcon image) {
			this.image = image;
		}
		
		/** Set Client Link **/
		public void setClientLink(String link){
			this.clientLink = link;
		}
		
		/** Get Client Link **/
		public String getClientLink(){
			return this.clientLink;
		}
		
		/**
		 * Game button
		 * @param uid - Unique ID
		 * @param link - Client jar location
		 * @param image - Image
		 * @param type - Type
		 */
		GameButton(int uid, String link, ImageIcon image, GameType type){
			setUID(uid);
			setClientLink(link);
			setImage(image);
			setType(type);
		}
	};

	/**
	 *  
	 * Return the NR cache directory
	 */
    public static String getCacheDir()
    {
        boolean exists = (new File(System.getProperty("user.home") + "/.NR2006cache/")).exists();
        if (exists) {
            System.out.println("Directory exists");
            return System.getProperty("user.home") + "/.NR2006cache/";
        } else {
            File f = new File(System.getProperty("user.home") + "/.NR2006cache/");
            f.mkdir();
            System.out.println("Directory doesnt exist, making directory");
            return System.getProperty("user.home") + "/.NR2006cache/";
        }
    }
    
    /**
     *  Game Types
     *
     */
    public enum GameType {
    	NR,OSS
    }
}


