package Server;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.ServletException;

import Parameter.*;
import DataPool.*;

class ProcessOne implements Runnable{
	private BufferedReader in = null;
	private DataOutputStream out = null;
	
    ProcessOne(Socket sock) throws IOException{
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new DataOutputStream(sock.getOutputStream());
	}
	
	@Override
	public void run(){
		// TODO Auto-generated method stub
		try {
			Request request = getRequest();
			Response response = new Response(out);
			AFilterChain filterChain = FilterPool.getFilterChain(new String(request.getRequestURL()));
			filterChain.doFilter(request, response);
			
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException |ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Request getRequest() throws IOException {
		Map<String,String> header = new HashMap<String,String>();
		Map<String,String> param = new HashMap<String,String>();
		
		String line = in.readLine();
		String key = null;
        String val = null;
        String s = null;
	    while((s=in.readLine()).length()>0) {
			//System.out.println("got:"+s);
			int i = s.indexOf(":");
			header.put(s.substring(0,i), s.substring(i+2));
		}
		//System.out.println(line);
		
		
		if(line.indexOf("GET")>-1) {
			int i = line.indexOf(" ");
			int j = line.indexOf(" ",i+1);
			s = line.substring(i+1,j);
			if(s.indexOf("?")>-1) {
			    s = s.substring(s.indexOf("?")+1);
			}
			else{
				s = null;
			}
		}
		else {
			char cp[] = new char[2048];
		    in.read(cp);
		    s = new String(cp);
		}
		//System.out.println("param:"+s);
		int beg = 0;
		int mid = 0;
		while(s!= null && (mid=s.indexOf("=",beg))>-1) {
			key = s.substring(beg,mid);
			beg = mid + 1;
			if((mid=s.indexOf("&",beg))>-1) {
				val = s.substring(beg,mid);
				beg = mid + 1;
			}
			else {
				val = s.substring(beg);
			}
			
			param.put(key.toLowerCase(), val.trim());
			//System.out.println(key.toLowerCase()+"---"+val);
		}
		
		return new Request(header,param,line);
	}
	
}
