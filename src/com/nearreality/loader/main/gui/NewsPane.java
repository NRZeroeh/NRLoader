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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.nearreality.loader.main.gui.component.NewsTextPane;
import com.nearreality.loader.main.services.LatestThread;
import com.nearreality.loader.main.services.data.ThreadData;
import com.nearreality.loader.main.util.Logger;
import com.nearreality.loader.main.util.Logger.Level;

public class NewsPane extends JPanel{
	/** Our News Pane **/
	private JTextPane newsTextPane;
	/** Our Threads **/
	private List<ThreadData> data;
	/** Scroll Pane **/
	private JScrollPane scrollPane;

	private MainFrame mainFrame;

	/** Build our JTextPane and attach it to our ScrollPane **/
	private void buildTextPane(){
		/** Formatting purposes **/
		StringBuilder sb = new StringBuilder();
		sb.append("<font face=Tahoma size=3 color=white><b>Latest News<b></font><br><br><font color=green size=3 face=Tahoma >"+data.get(1).getTitle()+"<br><br><font face=Tahoma size=2 color=white>"+data.get(1).getPost() +"</font><P ALIGN=\"right\"><a href=\""+data.get(1).getUrl()+"\"><font color=green><b>Read More...</b></font></p></a>");
     	/** Create our new Text Pane **/
		newsTextPane = new NewsTextPane();
		/** We read HTML **/
		newsTextPane.setContentType("text/html");
		/** Set Text from string builder **/
    	newsTextPane.setText(sb.toString()); 
    	/** set us to non editable **/
        newsTextPane.setEditable(false);
        /** Attach NewsText Scroll Pane **/ 
        scrollPane = new JScrollPane(newsTextPane);
        scrollPane.setPreferredSize(new Dimension(581, 185));
        scrollPane.setMinimumSize(new Dimension(0, 0));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.black);
        scrollPane.setBounds(0, 0, 581, 185);
        /** Add us to scroll pane **/
        add(scrollPane);
   
        setBackground(Color.black);
        /** We must invoke HYPERLINKLISTENER to follow <a href tags in jtextpane! **/
        ToolTipManager.sharedInstance().registerComponent(newsTextPane);
        HyperlinkListener l = new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
                    if( !java.awt.Desktop.isDesktopSupported() ) {
					    System.err.println( "Desktop is not supported (fatal)" );
					    Logger.writeLog(Level.ERROR, "Desktop not supported!");
					}   
					java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
					if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
					    System.err.println( "Desktop doesn't support the browse action (fatal)" );
					    Logger.writeLog(Level.ERROR, "Desktop Browse not supported!");

					}
					    try {
					         desktop.browse( new URI(e.getURL().toString()));
					    }
					    catch ( Exception ed ) {
					        System.err.println( ed.getMessage() );
					    }
                }
            }

        };
        newsTextPane.addHyperlinkListener(l);
	}
	
	/** Build our GUI **/ 
	public void initalize(){
			data = this.mainFrame.getSplash().getNews();
			buildTextPane();
	}
	
	public NewsPane(MainFrame mf){
		super(null);
		this.mainFrame = mf;
		initalize();
	}
}
