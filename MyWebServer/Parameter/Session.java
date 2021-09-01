package Parameter;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class Session implements HttpSession{
	private Map<String,Object> attributes = new HashMap<String,Object>();
	private Date createTime;
	private String Id;
	
	public Session(String id){
		Id = id;
		createTime = new Date();
	}

	@Override
	public Object getAttribute(String arg0) {
		if(attributes.containsKey(arg0)) {
			return attributes.get(arg0);
		}
		return null;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		Set<String> keys = attributes.keySet();
		Vector<String> attributeNames = new Vector<String>(keys);
		return attributeNames.elements();
	}

	@Override
	public long getCreationTime() {
		return createTime.getTime();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return Id;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attributes.put(arg0, arg1);
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		
	}

}