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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nearreality.loader.main.gui.MainFrame;
import com.nearreality.loader.main.gui.SplashScreen;
import com.nearreality.loader.main.services.data.ThreadData;

/**
 *  Grabbing latest threads from our forums, including title, name, author and content.
 *  This class utilizes a lot of "Terrible" designed regex expressions. I believe VBulletin has a webservice extension 
 *  we just didn't have it enabled at the time of writing.
 *  
 *  Feel free to rewrite this class
 *  
 * @author Patrick "zeroeh"
 *
 */
public class LatestThread {
	/** String to VBulletin */
	private final String WEBSITE_URL = "http://www.near-reality.com/forums/news/";
	/** Regex search Thread Name */
	private final String REGEX_THREAD_NAMES = "<font color=\"#0099FF\"><strong>(.*?)</a> ";
	/** Regex search Thread URL */
	private final String REGEX_THREAD_URL = "border=\"0\" /> <a href=\"(.*)\">1</a>";
	/** Regex search Thread Content */
	private final String REGEX_THREAD_CONTENT ="<div class=\"blizzquote\">(.*)";
	/** This variable is the index of your latest news post **/
	private final int THREADINDEX = 3;
	/** Splash Screen **/
	private SplashScreen splash;

	public LatestThread(SplashScreen splash){
		this.splash = splash;
	}
	
	/** 
	 *  Read the url link,
	 *  do some fun screenscrapping then url following then screenscrap again
	 *  
	 *  *Warning* this method is messy and will be rewritten 
	 * 
	 * @return ThreadData List
	 */
	public List<ThreadData> getData(){
		List<ThreadData> out = new ArrayList<ThreadData>();
		try{
			URL url = new URL(WEBSITE_URL);
			BufferedReader reader =  new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			int count = 0;
			while ((inputLine = reader.readLine()) != null  && count < THREADINDEX) {
				ThreadData in = getDetails(inputLine);
				if(in != null){
					out.add(in);
					count++;
					this.getScreen().getProgressBar().setValue(this.splash.getProgressBar().getValue() + 33);
					this.getScreen().getDescLbl().setText("Loading News Data.");
					this.getScreen().repaint();
					this.getScreen().validate();
				}
			}
			reader.close();
        } catch (Exception e){
			e.printStackTrace();
		}
		
		return out;
	}
	/** 
	 *  Create a new ThreadData Object from our regex strings
	 *  This method should be rewritten, rushed job.
	 * @param line - our line from the URL
	 * @return new ThreadData object
	 * 
	 */
	private ThreadData getDetails(String line){
		Pattern pattern = Pattern.compile(REGEX_THREAD_NAMES);
    	Matcher matcher = pattern.matcher(line);
    	if(matcher.find()){
    		  String title = matcher.group(1);
    		  pattern = Pattern.compile(REGEX_THREAD_URL);
    		  matcher = pattern.matcher(line);
    		  if(matcher.find()){
    			  String urlLink = matcher.group(1);
    				return new ThreadData(title,urlLink,getContent(urlLink)); 
    		  }
    	}
		return null;
	}
	/** Grab content from our News forums 
	 * NR Uses a custom theme that allows us to have different text compared to other users
	 * @param urlLink
	 * @return
	 */
	private String getContent(String urlLink){
		  String output = null;
		  try {
		URL url = new URL(urlLink);
		BufferedReader reader =  new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
        	   Pattern pattern = Pattern.compile(REGEX_THREAD_CONTENT);
        	   Matcher matcher = pattern.matcher(inputLine);
        	    while (matcher.find()) {
        	    	output = matcher.group();
        	    	output = output.replace("<div class=\"blizzquote\">","");
        	    	try {
        	    		output = output.substring(0, output.indexOf("</div>")-1);
        	    	} catch (Exception e){ }
        	    }
		}
	    reader.close();
		  } catch (Exception e){
			  e.printStackTrace();
		  }
		return output;
	}

	private SplashScreen getScreen(){
		return this.splash;
	}
}

