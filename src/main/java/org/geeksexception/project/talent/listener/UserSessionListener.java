package org.geeksexception.project.talent.listener;

import java.io.File;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		
		HttpSession session = event.getSession();
		String sessionId = session.getId();
		
		File tempFolder = new File(session.getServletContext().getRealPath("/") + "/img/temp/" + sessionId);
		deleteDirectory(tempFolder);
		
	}
	
	private boolean deleteDirectory(File path) {
		
	    if (path.exists()) {
	        File[] files = path.listFiles();
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isDirectory()) {
	                deleteDirectory(files[i]);
	            } else {
	                files[i].delete();
	            }
	        }
	    }
	    return (path.delete());
	    
	}

}