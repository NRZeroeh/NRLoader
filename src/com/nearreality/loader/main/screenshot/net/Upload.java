package com.nearreality.loader.main.screenshot.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.net.URL;
import java.net.Socket;

import javax.activation.MimetypesFileTypeMap;

	public class Upload {
	    private final String CrLf = "\r\n";

	    	public Upload(){
	    		httpConn();
	    	}
	    public static void main(String[] args) {
	    	new Upload();
	    }

	    private String getMessage2(){
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(CrLf + "-----------------------------4664151417711--"
	                    + CrLf);
	    	return sb.toString();
	    }
	    private String getMessage1(String mimetype, String filename){
	    	 StringBuilder sb = new StringBuilder();
	    	 sb.append("-----------------------------4664151417711" + CrLf);
	         sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""+filename+"\""
	                    + CrLf);
	         sb.append("Content-Type: "+mimetype+"" + CrLf);
	         sb.append(CrLf);
	         return sb.toString();
	    }
	    private void httpConn() {
	        URLConnection conn = null;
	        OutputStream os = null;
	        InputStream is = null;

	        try {
	        	URL url = new URL("");
	            System.out.println("url:" + url);
	            conn = url.openConnection();
	            conn.setDoOutput(true);
	            File file = new File("");
	            FileInputStream imgIs = new FileInputStream(file);
	            byte[] imgData = new byte[imgIs.available()];
	            imgIs.read(imgData);
	            imgIs.close();
	            conn.setRequestProperty("Content-Type",
	                    "multipart/form-data; boundary=---------------------------4664151417711");
	            // might not need to specify the content-length when sending chunked
	            // data
	            
	            String part1 = getMessage1(new MimetypesFileTypeMap().getContentType(file),file.getName());
	            String part2 = getMessage2();
	            conn.setRequestProperty("Content-Length", String.valueOf((part1
	                    .length() + part2.length() + imgData.length)));
	            os = conn.getOutputStream();
	            os.write(part1.getBytes());

	            // SEND THE IMAGE
	            int index = 0;
	            int size = 1024;
	            do {
	                if ((index + size) > imgData.length) {
	                    size = imgData.length - index;
	                }
	                os.write(imgData, index, size);
	                index += size;
	            } while (index < imgData.length);
	            os.write(part2.getBytes());
	            os.flush();

	            is = conn.getInputStream();

	            char buff = 512;
	            int len;
	            byte[] data = new byte[buff];
	            do {
	                len = is.read(data);

	                if (len > 0) {
	                    System.out.println(new String(data, 0, len));
	                }
	            } while (len > 0);

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                os.close();
	                is.close();
	            } catch (Exception e) {
	            }
	        }
	    }
}
