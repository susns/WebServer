package DataPool;

import java.util.*;

import Parameter.Session;

public class SessionPool {
	private static Map<String,Session> sessions = new HashMap<String,Session>();
	private static Integer count = 1;
	
	public synchronized static Session getSession(String sessionId) {
		if(sessionId == null) {
			sessionId = count.toString();
			count++;
			Session session = new Session(sessionId);
			sessions.put(sessionId, session);
			return session;
		}
		if(sessions.containsKey(sessionId)) {
			return sessions.get(sessionId);
		}
		else {
			Session session = new Session(sessionId);
			sessions.put(sessionId, session);
			return session;
		}
	}
	
}
