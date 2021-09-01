package DataPool;

import java.io.*;
import java.util.*;

import javax.servlet.*;

import Parameter.AFilterChain;

public class FilterPool {
	private static Map<String,ArrayList<UF>> pro_url_filter = new HashMap<String,ArrayList<UF>>();
	
	public synchronized static AFilterChain getFilterChain(String url) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {  //  url:/Susns/Loan.jsp
		int index = url.indexOf("/",1);
		String pro = url.substring(0,index);
		String mapping_url = url.substring(index);
		
		//System.out.println("FilterPool-pro:"+pro);
		//System.out.println("FilterPool-mapping_url:"+mapping_url);
		
		ArrayList<UF> filters = null;
		if(pro_url_filter.containsKey(pro)) {
			filters = pro_url_filter.get(pro);
		}
		else {
			String path = System.getProperty("user.dir");
			path = path.substring(0, path.lastIndexOf("\\")) + pro.replace('/', '\\') + "\\WebContent\\WEB-INF\\web.xml";
			
			//System.out.println("FilterPool-xml-path:"+path);
			
			filters = readXmlForFilters(path);
			pro_url_filter.put(pro, filters);
		}
		
		AFilterChain head = new AFilterChain();
		AFilterChain cur = head;
		for(int i = 0; i < filters.size(); i++) {
			UF t = filters.get(i);
			if(t.url.equals(mapping_url) || t.url.equals("/*")) {
				cur.next = new AFilterChain(t.filter);
				cur = cur.next;
			}
		}
		if(head.next == null) {
			return head;
		}
		else {
			return head.next;
		}
	}
	
	private static ArrayList<UF> readXmlForFilters(String path) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		ArrayList<UF> url_filters = new ArrayList<UF>();
		Scanner input = new Scanner(new File(path));
		while(input.hasNextLine()) {
			String s = input.nextLine();
			if(s.indexOf("<filter>")>-1) {
				input.nextLine();
				input.nextLine();
				String filterClass = input.nextLine();
				int beg = filterClass.indexOf(">");
				int end = filterClass.indexOf("<", beg);
				filterClass = filterClass.substring(beg+1,end);
				//System.out.println("FilterClass:"+filterClass);
				Class<?> cls = Class.forName(filterClass);
				Filter filter = (Filter)cls.newInstance();
				
				input.nextLine();
				input.nextLine();
				input.nextLine();
				String urlPattern = input.nextLine();
				beg = urlPattern.indexOf(">");
				end = urlPattern.indexOf("<", beg);
				urlPattern = urlPattern.substring(beg+1,end);
				UF tmp = new UF(urlPattern, filter);
				url_filters.add(tmp);
			}
		}
		input.close();
		return url_filters;
	}
	
}

class UF{
	String url;
	Filter filter;
	UF(String url,Filter filter){
		this.url = url;
		this.filter = filter;
	}
}
