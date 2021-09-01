package Server;

import java.io.*;
import java.net.*;

public class VirtualMachine {
	public static int httpd;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		httpd = 8888;
		@SuppressWarnings("resource")
		ServerSocket ssock = new ServerSocket(httpd);
		//System.out.println("have open port "+ httpd +" locally");
		
		while(true) {
			Socket sock = ssock.accept();
			//System.out.println();
			//System.out.println("client has made socket connection");
			
			ProcessOne client = new ProcessOne(sock);
			new Thread(client).start();
		}
	}
}
