package com.http.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Myserver {

	public void serverStart(){
		try {
			ServerSocket serverSocket = new ServerSocket(80);
			while (true) {
				Socket socket = serverSocket.accept();
				new Process(socket).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		int port = 80;
		if(args.length == 1){
			port = Integer.parseInt(args[0]);
		}
		new Myserver().serverStart();
	}
}
