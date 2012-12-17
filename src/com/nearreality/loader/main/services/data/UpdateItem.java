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
package com.nearreality.loader.main.services.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import com.nearreality.loader.main.Config;

/** 
 * 
 *
 * This class represents Json Data which we will parse and retrieve from the 
 * web server indicating cache update data
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
public class UpdateItem {

	/** Our Json Object will return the following **/
	private String _hash;
	private String _cacheVer;
	private String _launchVer;
	private String _clientUrl;
	private String _launchURL;
	private String _themeURL;
	private List<Content> _contents;
	
	public UpdateItem(){
		initalize();	
	}
	
	private void initalize(){
		try {
			JSONObject jsonObject = readJsonFromUrl(Config.JSON_LINK);
			setHash((String) jsonObject.get("hash"));
			setCacheVer(Long.toString((Long) jsonObject.get("cachever")));
			setLaunchVer((String) jsonObject.get("launchv"));
			setLaunchURL( (String) jsonObject.get("launcherurl"));
			setClientURL((String) jsonObject.get("clienturl"));
			setThemeURL((String) jsonObject.get("themeurl"));
			_contents = new ArrayList<Content>();
			JSONArray array = (JSONArray)jsonObject.get("contents"); 
		        for (int i=0; i < array.size(); i++) { 
		          JSONObject list = (JSONObject) ((JSONObject)array.get(i)); 
		          _contents.add(new Content(list.get("id").toString(),list.get("ip").toString()));
		        }
		} catch(Exception e){
			e.printStackTrace();
		}
}

private String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

public JSONObject readJsonFromUrl(String url) throws IOException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);	 
      JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonText);
     
      return jsonObject;
    } finally {
      is.close();
    }
  }
	public String getHash() {
		return _hash;
	}
	public String getCacheVer() {
		return _cacheVer;
	}
	public String getLaunchVer() {
		return _launchVer;
	}
	public String getLaunchURL() {
		return _launchURL;
	}
	public List<Content> getContents() {
		return _contents;
	}
	public void setHash(String _hash) {
		this._hash = _hash;
	}
	public void setCacheVer(String _version) {
		this._cacheVer = _version;
	}
	public void setLaunchVer(String _launchVar) {
		this._launchVer = _launchVar;
	}
	public void setLaunchURL(String _launchURL) {
		this._launchURL = _launchURL;
	}
	public void setContents(List<Content> _contents) {
		this._contents = _contents;
	}

	public String getCientURL() {
		return _clientUrl;
	}

	public void setClientURL(String _clientUrl) {
		this._clientUrl = _clientUrl;
	}

	public String getThemeURL() {
		return _themeURL;
	}

	public void setThemeURL(String _themeURL) {
		this._themeURL = _themeURL;
	}
}
