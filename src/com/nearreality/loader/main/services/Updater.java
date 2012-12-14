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
package com.nearreality.loader.main.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.nearreality.loader.main.Config;
import com.nearreality.loader.main.gui.SplashScreen;
import com.nearreality.loader.main.services.util.Download;
import com.nearreality.loader.main.services.util.Download.DownloadType;

/** Updater class, handles all update checking **/
public class Updater {
	/** Parent splash screen frame **/
	private SplashScreen frame;
	/**
	 *  Start our setup for checking for updates.
	 * @param frame
	 */
	public Updater(SplashScreen frame) {
		this.frame = frame;
		init();
	}
	/** Begin checking for updates **/
	private void init() {
		drawText(0,"Checking Launcher Version.");
		if (launcherUpdateNeeded()) {
			new Download().startDownload(DownloadType.LAUNCHER,this);
		}
		drawText(0,"Grabbing Client..");
		new Download().startDownload(DownloadType.CLIENT,this);
        drawText(100,"Grabbed Client...");
		drawText(0,"Grabbing Themes..");
		new Download().startDownload(DownloadType.THEME,this);
        drawText(100,"Grabbed Themes...");
		drawText(0,"Checking Cache....");
		if(updateNeeded()){
			new Download().startDownload(DownloadType.CACHE,this);
		}
		drawText(100,"Cache Check Complete.....");
		drawText(100, "Loading Launcher..");
		this.frame.loadGame();
	}
	/** Is a launcher update needed? **/
	private boolean launcherUpdateNeeded() {
		return Config.LAUNCH_VER.equals(Config.getLaunchVer());
	}

	/** is an update needed? **/
	private boolean updateNeeded() {
			try {
			File location = new File(Config.getCacheDir());
			File version = new File(Config.getCacheDir() + "/cacheVersion"
					+ Config.getCacheVer() + ".dat");

			if (!location.exists()) {
				location.mkdirs();
				boolean success = version.createNewFile();
				return true;
			} else {
				if (!version.exists()) {
					boolean success = version.createNewFile();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/** Write stream **/
	public void writeStream(InputStream In, OutputStream Out)
			throws IOException {
		byte Buffer[] = new byte[4096];
		int Len;
		while ((Len = In.read(Buffer)) >= 0) {
			Out.write(Buffer, 0, Len);
		}
		In.close();
		Out.close();
	}
	
	
	/** Get our Frame **/
	public SplashScreen getFrame(){
		return this.frame;
	}
	
	/** Update our progress bar **/
    public void drawText(int i, String string) {
    	getFrame().getProgressBar().setValue(i);
    	getFrame().getDescLbl().setText(string);
    	getFrame().validate();
    	getFrame().repaint();
	}
  
}
