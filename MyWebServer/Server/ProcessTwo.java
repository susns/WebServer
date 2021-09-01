package Server;

import java.io.*;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import DataPool.*;
import Parameter.*;

public class ProcessTwo {
	private Request request;
	private Response response;
	
	public ProcessTwo(ServletRequest request, ServletResponse response) {
		this.request = (Request)request;
		this.response = (Response)response;
	}
	
	public ProcessTwo() {
		// TODO Auto-generated constructor stub
	}

	public void handle() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		String url = new String(request.getRequestURL());
		//System.out.println("BeforeSendFile:"+url);
		if(url.indexOf(".jsp")>-1) {
			handleJsp(url);
			url = new String(request.getRequestURL());
			url = "html" + url.replace("/", "\\");
		}
		else if(url.substring(url.lastIndexOf("/")).indexOf(".") == -1) {
			handleServlet(url);
			url = new String(request.getRequestURL());
			url = "html" + url.replace("/", "\\");
		}
		else {
			int index = url.indexOf("/",1);
			String pro = url.substring(0,index);
			String page = url.substring(index);
			String path = System.getProperty("user.dir");
			url = path.substring(0, path.lastIndexOf("\\")) + pro.replace("/", "\\") + "\\WebContent"+page.replace("/", "\\");
		}
		
		//Session session = (Session)request.getSession();
		//System.out.println("Session Id: " + session.getId() ); 
		//Enumeration<String> eh = session.getAttributeNames(); 
	    // while (eh.hasMoreElements()) { 
	         //String h = eh.nextElement(); 
	         //System.out.println("Session Attribute: " + h ); 
	         //System.out.println("value: " + session.getAttribute(h)); 
	     //}
		
		sendFile(url,response.isRedirect());
	}
	
	public void handleJsp(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Object jsp_servlet = JspPool.getJsp(url);
		
		int index = url.indexOf("/",1);
		String pro = url.substring(0,index).replace("/", "\\");
		String jsp = url.substring(index).replace("/", "\\");
		String sPath = System.getProperty("user.dir")+"\\jsp_servlet"+pro;
		URL[] urls = new URL[]{new URL("file:/" + sPath.replace("\\", "/") + "/")};
	    @SuppressWarnings("resource")
		URLClassLoader classLoader = new URLClassLoader(urls);
        Class<?> cls = classLoader.loadClass(jsp.substring(1).replace(".jsp", "_jsp"));
        
		Response response = new Response(new FileOutputStream(new File("html\\"+url)));
		if(request.isGetOrPost()) {
			Method meth = cls.getDeclaredMethod("doPost", HttpServletRequest.class,HttpServletResponse.class);
			meth.setAccessible(true);
			meth.invoke(cls.newInstance(), request,response);
		}
		else {
			Method meth = cls.getDeclaredMethod("doGet", HttpServletRequest.class,HttpServletResponse.class);
			meth.setAccessible(true);
			meth.invoke(cls.newInstance(), request,response);
		}
		if(response.isRedirect()) {
			this.response.setRedirect(true);
		}
	}
	
	public void handleServlet(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		CS node = ServletPool.getServlet(url);
		Class<?> cls = Class.forName(node.cls);
		
		int index = url.indexOf("/",1);
		String pro = url.substring(0,index).replace("/", "\\");
		String servlet = url.substring(index).replace("/", "\\");
		
		String sPath = System.getProperty("user.dir")+"\\html"+pro;
		File folder = new File(sPath);
		if(!folder.exists()) {          
			folder.mkdirs(); 
		}
		
		Response response = new Response(new FileOutputStream(new File(sPath+servlet)));
		if(request.isGetOrPost()) {
			Method meth = cls.getDeclaredMethod("doPost", HttpServletRequest.class,HttpServletResponse.class);
			meth.setAccessible(true);
			meth.invoke(node.servlet, request,response);
		}
		else {
			Method meth = cls.getDeclaredMethod("doGet", HttpServletRequest.class,HttpServletResponse.class);
			meth.setAccessible(true);
			meth.invoke(node.servlet, request,response);
		}
		if(response.isRedirect()) {
			this.response.setRedirect(true);
		}
	}
	
	public void sendFile(String filePath, boolean redir) throws IOException {
		//System.out.println("SendFile:"+filePath);
		File f = new File(filePath);
		DataInputStream din = new DataInputStream(new FileInputStream(f));
		int len = (int)f.length();
		byte[] buf = new byte[len];
		din.readFully(buf);
		din.close();
		
		DataOutputStream out = response.getDataOutputStream();
		
		if(!redir) {
			out.writeBytes("Http-1.0 200 OK\r\n");
			out.writeBytes("Content-Length:"+len+"\r\n");
			if(filePath.indexOf(".gif")>-1||filePath.indexOf(".jpg")>-1||filePath.indexOf(".png")>-1){
				String type = filePath.substring(filePath.indexOf(".")+1);
				out.writeBytes("Content-Type:image/" + type + "\r\n");
			}
			else if(filePath.indexOf(".PDF")>-1) {
				out.writeBytes("Content-Type:application/pdf\r\n");
			}
			else {
				out.writeBytes("Content-Type:text/html\r\n");
			}
		}
		
		if(request.getHeader("Cookie")==null) {
			Session session = (Session) request.getSession();
			out.writeBytes("Set-Cookie:"+session.getId()+"\r\n");
		}
		out.writeBytes("\r\n");
		out.write(buf);
		//String s = new String(buf);
		out.flush();
		out.close();
	}
	
}
