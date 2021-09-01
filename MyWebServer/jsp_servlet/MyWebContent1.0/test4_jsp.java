import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class test4_jsp extends HttpServlet {

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.print(" <meta http-equiv=Content-Type content=\"text/html;charset=utf-8\">"+"\r\n"+"<html> "+"\r\n"+"此工作属于提高要求�?"+"\r\n"+"<body bgcolor=\"white\"> "+"\r\n"+"<h1>The Echo JSP - Testing for Jsp tasks</h1> "+"\r\n"+"");
   java.util.Enumeration eh = request.getHeaderNames(); 
     while (eh.hasMoreElements()) { 
         String h = (String) eh.nextElement(); 
         out.print("<br> header: " + h ); 
         out.println(" value: " + request.getHeader(h)); 
     } 
        out.print(" "+"\r\n"+"</body> "+"\r\n"+"</html> "+"\r\n"+"");
        out.close();
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
