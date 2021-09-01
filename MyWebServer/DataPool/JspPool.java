package DataPool;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class JspPool {
	private static Map<String,Object> jsp_servlet = new HashMap<String,Object>();
	
	public synchronized static Object getJsp(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {  //url:/pro/xxx.jsp
		if(jsp_servlet.containsKey(url)) {
			return jsp_servlet.get(url);
		}
		else {
			int index = url.indexOf("/",1);
			String pro = url.substring(0,index).replace("/", "\\");
			String jsp = url.substring(index).replace("/", "\\");
			
			String path = System.getProperty("user.dir");
			path = path.substring(0, path.lastIndexOf("\\")) + pro + "\\WebContent" + jsp;
			
			//System.out.println("JspPool-path:"+path);
			
			Object o = translateJspToServlet(path,pro,jsp);
			jsp_servlet.put(url, o);
			return o;
		}

	}
	
	private static Object translateJspToServlet(String jspPath, String pro, String jspName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		String sPath = System.getProperty("user.dir")+"\\jsp_servlet"+pro;
		File folder = new File(sPath);
		if(!folder.exists()) {          
			folder.mkdirs(); 
		}
		String servletPath = sPath+jspName.replace(".jsp", "_jsp")+".java";
		
		//System.out.println("Jsp转译后文件夹:"+sPath);
		//System.out.println("Jsp转译后路径:"+servletPath);
		
		File file = new File(servletPath);
		PrintWriter out = new PrintWriter(file);
		out.println("import java.io.*;\r\n" + 
				"import javax.servlet.*;\r\n" + 
				"import javax.servlet.annotation.*;\r\n" + 
				"import javax.servlet.http.*;\r\n");
		out.println("public class "+jspName.substring(1).replace(".jsp", "_jsp")+" extends HttpServlet {\r\n");
		String selfDefine = new String();
		out.println(getDoGetMethod(jspPath, selfDefine));
		out.println(getDoPostMethod());
		out.println(selfDefine);
		out.println("}");
		out.close();
		
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
	    int result = javaCompiler.run(null, null, null, servletPath);
	    //System.out.print("编译结果:");
	    //System.out.println(result == 0 ? "success" : "failure");
	    
	    URL[] urls = new URL[]{new URL("file:/" + sPath.replace("\\", "/") + "/")};
	    @SuppressWarnings("resource")
		URLClassLoader classLoader = new URLClassLoader(urls);
        Class<?> cls = classLoader.loadClass(jspName.substring(1).replace(".jsp", "_jsp"));
        
		Object servlet = cls.newInstance();
		return servlet;
	}
	
	private static String getDoGetMethod(String path, String selfDefine) throws IOException {
		String getMethodBody ="    /**\r\n" + 
				"    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)\r\n" + 
				"    */\r\n" + 
				"    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n"+
				"        PrintWriter out = response.getWriter();\r\n";
		
		File file = new File(path);
		@SuppressWarnings("resource")
		DataInputStream fin = new DataInputStream(new FileInputStream(file));
		int len = (int)file.length();
		byte[] buf = new byte[len];
		fin.readFully(buf);
		String content = new String(buf);
		
		int beg = 0;
		while(beg<content.length()) {
			if(content.indexOf("<%",beg) > -1) {
				int i = content.indexOf("<%",beg);
				//html部分
				if(i>beg) {
					getMethodBody += "        out.print(\""+content.substring(beg,i).replace("\"", "\\\"").replace("\r\n", "\"+\"\\r\\n\"+\"")+"\");\r\n";
				}
				//jsp标签部分
				if(content.charAt(i+2)=='!') {
					beg = content.indexOf("%>",beg);
					selfDefine += content.substring(i+3,beg);
					beg = beg + 2;
				}
				else if(content.charAt(i+2)=='='){
					beg = content.indexOf("%>",beg);
					getMethodBody += "        out.print("+content.substring(i+3,beg)+");\r\n";
					beg += 2;
				}
				else if(content.charAt(i+2)=='-' && content.charAt(i+3)=='-') {
					beg = content.indexOf("--%>",beg)+4;
				}
				else if(content.charAt(i+2)=='@') {
					beg = content.indexOf("%>",beg)+2;
				}
				else {
					beg = content.indexOf("%>",beg);
					getMethodBody += content.substring(i+2,beg);
					beg += 2;
				}
			}
			else {
				//html部分
				getMethodBody += "        out.print(\""+content.substring(beg).replace("\"", "\\\"").replace("\r\n", "\"+\"\\r\\n\"+\"")+"\");\r\n";
				getMethodBody += "        out.close();\r\n";
				getMethodBody += "    }\r\n";
				beg = content.length();
			}
			
		}
		
		return getMethodBody;
	}
	
    private static String getDoPostMethod() {
		String s = "    /**\r\n" + 
				"    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)\r\n" + 
				"    */\r\n"+
                "    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n" +
				"        doGet(request, response);\r\n    }";    
		return s;
	}
	
}
