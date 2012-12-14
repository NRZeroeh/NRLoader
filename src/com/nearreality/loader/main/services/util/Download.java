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
package com.nearreality.loader.main.services.util;

import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import com.nearreality.loader.main.Config;
import com.nearreality.loader.main.listeners.DownloadListener;
import com.nearreality.loader.main.services.Updater;

public class Download {
	public static Updater update;
	
    public void startDownload(DownloadType type,Updater up){
    	if(update == null)
    		update = up;
    	
    		for(DownloadInstance di : DownloadInstance.values()){
    			if(di.getType() == type){
    			di.wget(type);
    			}
    		}
    	}
		public enum DownloadInstance {
			CACHE(Config.getContents().get(0).getLink(), Config.getOutputDir(Config.getContents().get(0).getLink()), DownloadType.CACHE),
			CLIENT(Config.getLaunchURL(),Config.getOutputDir(Config.getLaunchURL()), DownloadType.CLIENT),
			THEME(Config.getThemeURL(),Config.getOutputDir(Config.getThemeURL()),DownloadType.THEME),
			LAUNCHER(Config.getLaunchURL(), new String(System.getProperty("user.dir")+"/"), DownloadType.LAUNCHER);
			;
			/** Our file buffer **/
			private final int BUFFER = 1024;
	        
	        /** Download Type **/
	        private DownloadType type;
	        
	        private String urlLink;
	        
	        private String output;
	        
	        public DownloadType getType(){
	        	return type;
	        }
	        DownloadInstance(String link, String output, DownloadType type){
	        	this.urlLink = link; 
	        	this.output = output;
	        	this.type = type;
	        }
	        /** Download Listeners **/
	        private List<DownloadListener> downloadListeners = new ArrayList<DownloadListener>();
	       
	        	
			 /** We are going to clean up some downloading code and call it wget **/
	        public boolean wget(final DownloadType type) {
	        	addDownloadListener(new DownloadListener(){
					@Override
					public void downloadComplete() {
						String fileToExtract = getUpdate().getCacheDir() + getUpdate().getArchivedName(urlLink);
			        	switch(type){
			        		case CACHE:
			        			unZipFile(fileToExtract);
			        			break;
			        		case LAUNCHER:
			        			getUpdate().drawText(100, "Launcher updated.");
			        			break;
			        		case CLIENT:
			        			getUpdate().drawText(100, "Client Grabbed.");
			        			break;
			        		case THEME:
			        			getUpdate().drawText(100, "Theme Grabbed.");
			        			break;
			        		}
					}

					@Override
					public void downloadInterrupted() {
						getUpdate().drawText(100, "Connection dropped...");
					}
	        		
	        	});
	        	try { 
	        		 URL url = new URL(urlLink);
	            	 URLConnection conn = url.openConnection();
	            	 InputStream inStream = conn.getInputStream();
	            	 BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(new File(output)));
	            	 byte buffer[] = new byte[BUFFER];
	            	 int writtenTotal = 0;
	                 int length = conn.getContentLength();
	            	 while (true) {
	            		 int nRead = inStream.read(buffer, 0, buffer.length);
	            		 if (nRead <= 0)
	            			 break;
	            		 writtenTotal += nRead;
	            		 bufOut.write(buffer, 0, nRead);
	            		 
	            		 int percentage = (int)(((double)writtenTotal / (double)length) * 100D);
	                     getUpdate().drawText(percentage, "Downloading "+getDownloadName(type)+": " + percentage + "%");
	            	 }
	            	    bufOut.flush();
	            	    inStream.close();
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	} 	
	        	fireDownloadCompletedEvent();
				return true;
	        }
	        
	        public void unZipFile(String name) {
	        	getUpdate().drawText(0,"Loading Files for Extracting..");
	    		try {

	    			ZipFile ZipFile = new ZipFile(name);
	    			int length = ZipFile.size();
	    			int size = 0;
	    			for (Enumeration Entries = ZipFile.entries(); Entries
	    					.hasMoreElements();) {
	    				size++;
	    				int percentage = (int) (((double) size / (double) length) * 100D);
	    				getUpdate().drawText(percentage, "Extracting - " + percentage + "%");
	    				ZipEntry Entry = (ZipEntry) Entries.nextElement();
	    				if (Entry.isDirectory()) {
	    					(new File(getUpdate().getCacheDir() + Entry.getName())).mkdir();
	    				} else {
	    					getUpdate().writeStream(ZipFile.getInputStream(Entry),
	    							new BufferedOutputStream(new FileOutputStream(
	    									getUpdate().getCacheDir() + Entry.getName())));
	    				}
	    				}
	    			ZipFile.close();
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	        
	        public String getDownloadName(DownloadType type){
	        	switch(type){
	    		case CACHE:
	    			return "Cache";
	    		case LAUNCHER:		
	    			return "NR Launcher";
	    		case CLIENT:
	    			return "Game Client";
	    		case THEME:
	    			return "Game Themes";
	    	}
	        	return null;
	   
	        }
	        
	        
	        public void fireDownloadCompletedEvent(){
	        	Iterator<DownloadListener> itr = downloadListeners.iterator(); 
	        	while(itr.hasNext()) {
	        		DownloadListener element = (DownloadListener) itr.next();
	        		element.downloadComplete();
	        	}
	        }
	        public void fireDownloadInterruptedEvent(){
	        	Iterator<DownloadListener> itr = downloadListeners.iterator(); 
	        	while(itr.hasNext()) {
	        		DownloadListener element = (DownloadListener) itr.next();
	        		element.downloadInterrupted();
	        	}
	        }
	        
	        public void addDownloadListener(DownloadListener in){
	        	if(downloadListeners.contains(in)){
	        		downloadListeners.add(in);
	        	}
	        }
	        
	        public void removeDownloadListener(DownloadListener in){
	        	Iterator<DownloadListener> itr = downloadListeners.iterator(); 
	        	while(itr.hasNext()) {
	        		DownloadListener element = (DownloadListener) itr.next();
	        		if(element == in){	
	        			itr.remove();
	        		}
	        	} 
	        }

			public static Updater getUpdate() {
				return update;
			}
		
		}

		public enum DownloadType {
		    CACHE,CLIENT,LAUNCHER,THEME
		}
}