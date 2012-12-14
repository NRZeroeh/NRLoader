package com.nearreality.loader.main.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.nearreality.loader.main.services.util.Download.DownloadType;

public class Logger {
	
	
	public enum Level {
		ERROR,WARNING,INFORMATION,SYSTEM,EXCEPTION;
	}

	public static void writeLog(Level level, String text){
		switch(level){
			case ERROR:
			case WARNING:
			case EXCEPTION:
				PrintWriter pw = null;
				try {
					pw = new PrintWriter(new FileWriter(System.getProperty("user.home") +"/nrlauncher.log",true));
				} catch (IOException e) {
						e.printStackTrace();
				}
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String log = "["+dateFormat.format(cal.getTime())+"] - ["+level+"]: " + text + "\n";
				pw.append(log);
				pw.flush();
				break;
		default: // we don't write others for now 
			break;
		}
	}
}
