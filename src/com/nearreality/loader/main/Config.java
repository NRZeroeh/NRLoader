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

package com.nearreality.loader.main;

import java.io.File;
import java.util.List;

import com.nearreality.loader.main.services.data.Content;

public class Config {

	/** Client Update Variables **/
	
	
	/** Json link to be read from Updater.java
	 * Format for the JSON OBject should look like the following
	 *{
                 "hash": "fe52aba5080fc48d1d3adb75373154e3c8b89db8500f304e979a0313808704d3",
                 "cachever" : (Cache Version),
                 "launchv" : "1.1",
                 "launcherurl" : "http://www.qksnap.com/nrlauncheross.jar",
                 "clienturl" : "(Client URL's)",
				 "themeurl" :  "(Theme.jar location on a webserver)",
                 "contents" : [
                				{
                 				    "id" : "0",
                 				    "ip" : "(Cache Location)"
                 				  }, {
                 				    "id" : "1",
                 				    "ip" : "(Cache Location)"
                 				  }]
		} 
	
	 * Post on http://www.near-reality.com/forums/programming/
	 * if you need assistance changing these.
	 */
	public final static String JSON_LINK = "";
	
	/**
	 * Cache Client Version
	 * 
	 */
	public final static String CACHE_VER = "3";
	
	/** 
	 * Launcher Client Version
	 */
	public final static String LAUNCH_VER = "1.1";
	 
	
	/** Cache Location **/
	public final static String CACHE_LOC = System.getProperty("user.home") + "/.NR2006cache/";
	
	/** ScreenShot Location **/
	
	
	/** Server Grabbed URL's and content servers **/
	private static String clientUrl;
	private static String _launchURL;
	private static String _themeURL;
	private static String _cacheVer;
	private static String _launchVer;
	private static List<Content> _contents;
	
	
	/** Cache Directorys **/
	
	/** output for downloading and unzipping files **/
	
	public static String getOutputDir(String url){
		return getCacheDir() + "/"+getArchivedName(url);
	}
	
	/** File Name from URL Link **/
	public static String getArchivedName(String link) {
		int lastSlashIndex = link.lastIndexOf('/');
		if (lastSlashIndex >= 0
				&& lastSlashIndex < link.length() - 1) {
			return link.substring(lastSlashIndex + 1);
		} else {
		}
		return "";
	}
	
	public static String getCacheDir()
    {
        boolean exists = (new File(CACHE_LOC)).exists();
        if (exists) {
           return CACHE_LOC;
        } else {
            File f = new File(CACHE_LOC);
            f.mkdir();
            return CACHE_LOC;
        }
    }
	
	/** Getters and setters of information we retrieve from Update Item (Our update JSON object) **/
	
	public static String getClientUrl() {
		return clientUrl;
	}
	public static String getLaunchURL() {
		return _launchURL;
	}
	public static String getThemeURL() {
		return _themeURL;
	}
	public static List<Content> getContents() {
		return _contents;
	}
	public static String getCacheVer() {
		return _cacheVer;
	}

	public static String getLaunchVer() {
		return _launchVer;
	}

	public static void setCacheVer(String _cacheVer) {
		Config._cacheVer = _cacheVer;
	}

	public static void setLaunchVer(String _launchVer) {
		Config._launchVer = _launchVer;
	}

	public static void setClientURL(String clientUrl) {
		Config.clientUrl = clientUrl;
	}
	public static void setLaunchURL(String _launchURL) {
		Config._launchURL = _launchURL;
	}
	public static void setThemeURL(String _themeURL) {
		Config._themeURL = _themeURL;
	}
	public static void setContents(List<Content> _contents) {
		Config._contents = _contents;
	}
	
	/** Configure us **/
	public static void configure(String url, String lurl, String curl, List<Content> contents, String lvar, String cvar){
		setThemeURL(url);
		setLaunchURL(lurl);
		setClientURL(curl);
		setContents(contents);
		setLaunchVer(lvar);
		setCacheVer(cvar);
	}
}
