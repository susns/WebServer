package Parameter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.*;

import Server.*;

public class AFilterChain implements FilterChain{
	private Filter cur;
	public AFilterChain next;
	
	public AFilterChain() {
		cur = null;
		next = null;
	}
	
	public AFilterChain(Filter filter) {
		cur = filter;
		next = null;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp) throws IOException, ServletException {
		// TODO Auto-generated method stub
		if(cur == null) {
			ProcessTwo p = new ProcessTwo(req,resp);
			try {
				p.handle();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
					| SecurityException | IllegalArgumentException | InvocationTargetException e) {
				 //TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			if(next == null) {
				Filter t = cur;
				cur = null;
				next = null;
				t.doFilter(req, resp, this);
			}
			else {
				Filter t = cur;
				cur = next.cur;
				next = next.next;
				t.doFilter(req, resp, this);
			}
		}
	}
	
}