package Parameter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.*;

import Server.*;

public class Dispatcher implements RequestDispatcher{
	
	@Override
	public void forward(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		ProcessTwo p = new ProcessTwo(arg0, arg1);
		try {
			p.handle();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException
				| SecurityException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void include(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
