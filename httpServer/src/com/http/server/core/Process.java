package com.http.server.core;

import java.awt.im.InputContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import com.http.server.util.Log;

public class Process extends Thread{

	private static Log log = new Log("debug",Process.class);
	
	private Socket socket;
	private InputStream in;
	private PrintStream out;
	private final static String WEB_ROOT = Process.class.getResource("/").getPath().substring(1) + "res/";
	public Process(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		try {
			in = socket.getInputStream();
			out  = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("错误：", e);
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String fileName = parse(in);
		sendFile(fileName);
	}
	
	public String parse(InputStream in){
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String fileName = null;
		try {
			String message = br.readLine();
			log.info("message:{}", message);
			String[] content = message.split(" ");
			if(content.length != 3){
				sendErrorMessage(400, "code error");
				return null;
			}
			log.info("code:{},fileName:{},http version:{}", content);
			fileName = content[1];
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("错误：", e);
			e.printStackTrace();
		}
		return fileName;
	}
	
	
	public void sendErrorMessage(int errorCode, String errorMessage){
		out.println("HTTP/1.0 "+errorCode + " "+errorMessage);
		out.println("content-type:text/html");
		out.println();
		out.println("<html>");
		out.println("<title>Error Message");
		out.println("</title>");
		out.println("<body>");
		out.println("<h1>ErrorCode:"+errorCode + ", Message:"+errorMessage);
		out.println("</body>");
		out.println("</html>");
		out.flush();
		out.close();
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendFile(String fileName){
		log.debug("文件夹路径：{}",WEB_ROOT);
		File file = new File(WEB_ROOT + fileName);
		log.debug("文件名称:{}", file.getAbsolutePath());
		if(!file.exists()){
			log.info("文件不存在：{}", file.getName());
			sendErrorMessage(404, "file not found");
			return;
		}
		try {
			InputStream in = new FileInputStream(file);
			byte[] content = new byte[(int) file.length()];
			in.read(content);
			out.println("HTTP/1.0 200 queryfile");
			out.println("content-length:"+content.length);
			out.println();
			out.write(content);
			out.close();
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("错误", e);
			e.printStackTrace();
		}
		
	}
}
