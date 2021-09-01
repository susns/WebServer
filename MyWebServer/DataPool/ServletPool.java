package DataPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ServletPool {
	private static Map<String,Map<String,String>> pro_url_class = new HashMap<String,Map<String,String>>();
	private static Map<String,Map<String,Object>> pro_url_servlet =  new HashMap<String,Map<String,Object>>();
	
	public synchronized static CS getServlet(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException {
		int index = url.indexOf("/",1);
		String pro = url.substring(0,index);
		String mapping_url = url.substring(index);
		
		//System.out.println("ServletPool-pro:"+pro);
		//System.out.println("ServletPool-mapping_url:"+mapping_url);
		
		Map<String,String> url_class = null;
		Map<String,Object> url_servlet = null;
		if(!pro_url_class.containsKey(pro)) {
			String path = System.getProperty("user.dir");
			path = path.substring(0, path.lastIndexOf("\\")) + pro.replace("/", "\\") + "\\WebContent\\WEB-INF\\web.xml";
			
			System.out.println("ServletPool-xml-path:"+path);
			
			url_class = readXmlForServlets(path);
			pro_url_class.put(pro, url_class);
		}
		url_class = pro_url_class.get(pro);
		
		if(!pro_url_servlet.containsKey(pro)) {
			url_servlet = new HashMap<String,Object>();
			pro_url_servlet.put(pro,url_servlet);
		}	
		url_servlet = pro_url_servlet.get(pro);
			
		if(url_class.containsKey(mapping_url)) {
			if(url_servlet.containsKey(mapping_url)) {
				CS node = new CS(url_class.get(mapping_url),url_servlet.get(mapping_url));
				return node;
			}
			else {
				//System.out.print(url_class.get(mapping_url));
				Class<?> cls = Class.forName(url_class.get(mapping_url));
				Object servlet = cls.newInstance();
				url_servlet.put(mapping_url, servlet);
				CS node = new CS(url_class.get(mapping_url),servlet);
				return node;
			}
		}
		return null;
	}
	
	private static Map<String,String> readXmlForServlets(String path) throws FileNotFoundException {
		Map<String,String> url_class = new HashMap<String,String>();
		Scanner input = new Scanner(new File(path));
		while(input.hasNextLine()) {
			String servlet = input.nextLine();
			if(servlet.indexOf("<servlet>")>-1) {
				input.nextLine();
				input.nextLine();
				input.nextLine();
				String servletClass = input.nextLine();
				int beg = servletClass.indexOf(">");
				int end = servletClass.indexOf("<", beg);
				servletClass = servletClass.substring(beg+1,end);
				
				input.nextLine();
				input.nextLine();
				input.nextLine();
				String urlPattern = input.nextLine();
				beg = urlPattern.indexOf(">");
				end = urlPattern.indexOf("<", beg);
				urlPattern = urlPattern.substring(beg+1,end);
				url_class.put(urlPattern, servletClass);
			}
		}
		input.close();
		return url_class;
	}
	
}
