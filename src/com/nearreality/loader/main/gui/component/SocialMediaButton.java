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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.nearreality.loader.main.util.Logger;
import com.nearreality.loader.main.util.Logger.Level;

/** 
 * 
 *  Handles all the Social Media Launching
 * 
 * 
 * @author Patrick "zeroeh"
 *
 */
public class SocialMediaButton {
	
	/** 
	 * 
	 * Our Social Media object with there respected #MediaType
	 *
	 */
	public enum SocialMedia {
		
		TWITTER(0,"https://twitter.com/OfficialNrpk",MediaType.TWITTER),
		FACEBOOK(1,"https://www.facebook.com/pages/Near-Reality/102822093117271",MediaType.FACEBOOK),
		YOUTUBE(2,"hhttp://www.youtube.com/user/OfficialNearReality",MediaType.YOUTUBE);
		
		/** URL Link **/
		private String urlLink;
		
		/** Media Type **/
		private MediaType type;
		
		/** Unique ID **/
		private int uID; 
		
		/**
		 *  Social Media Name
		 *  
		 * @param uid - Unique ID
		 * @param urlLink - Url Link
		 * @param type - #MediaType
		 */
		SocialMedia(int uid,String urlLink, MediaType type){
			setuID(uid);
			setURLLink(urlLink);
			setType(type);
		}

		/**
		 *  Create a JButton using our Social Media Data
		 * @param id - ID of the button
		 * @return new Jbutton
		 */
		public static JButton createButton(int id){
			/** Loop through our Enum map **/
			  for(SocialMedia sm : SocialMedia.values()){
				  	if(sm.getUID() == id){
				  		final String urlLink = sm.getUrlLink();
				  		JButton temp = new JButton(getIcon(sm.getType()));
				  		temp.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent e) {
								if( !java.awt.Desktop.isDesktopSupported() ) {
								  	Logger.writeLog(Level.ERROR, "Desktop is not supported." );
								  	JOptionPane.showMessageDialog(null,"You computer does not support AWT Desktop :(","Desktop is not supported!", 2);
								}   
								java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
								if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
									Logger.writeLog(Level.ERROR, "Desktop doesn't support the browse action." );
									JOptionPane.showMessageDialog(null,"You computer does not support AWT Desktop Browsing :(","Desktop Browse is not supported!", 2);
									
								}
								    try {
								         desktop.browse( new URI(urlLink));
								    }
								    catch ( Exception ed ) {
								 
								    }
							}	
						});
				  		temp.setBorder(BorderFactory.createEmptyBorder());
						temp.setContentAreaFilled(false);
						return temp;
				  	}
			  }
			return null;
			
		}
		
		/** 
		 * button background image
		 * @param type - Which social media
		 * @return - image
		 */
		private static ImageIcon getIcon(MediaType type){
			switch(type){
				case YOUTUBE:
					return new ImageIcon(SocialMediaButton.class.getResource("/youtube.png"));
				case FACEBOOK:
					return new ImageIcon(SocialMediaButton.class.getResource("/facebook.png"));
				case TWITTER:
					return new ImageIcon(SocialMediaButton.class.getResource("/twitter.png"));
			}
			
			return null;
			
		}
		
		/** 
		 * Set our URL Link
		 * @param link
		 */
		
		public void setURLLink(String link){
			this.urlLink = link;
		}
		/** Set our Type **/
		public void setType(MediaType type){
			this.type = type;
		}
		/** Get our URL **/
		public String getUrlLink() {
			return urlLink;
		}

		/** Get our Media Type **/
		public MediaType getType() {
			return type;
		}

		/** Get Our Unique IDE **/
		public int getUID() {
			return uID;
		}

		/** Set URL Link**/
		public void setUrlLink(String urlLink) {
			this.urlLink = urlLink;
		}

		/** Set UId **/
		public void setuID(int uID) {
			this.uID = uID;
		}

	};
	
	/** Support Media Types - Add more if you wish
	 */
	public enum MediaType {
	    YOUTUBE,FACEBOOK,TWITTER
	}
}


