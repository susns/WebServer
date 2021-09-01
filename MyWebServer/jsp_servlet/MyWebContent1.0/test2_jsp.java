import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class test2_jsp extends HttpServlet {

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.print("<b>Testing for first JSP</b>"+"\r\n"+"<br>"+"\r\n"+"<b> current time is: "+"\r\n"+"    ");
        out.print(  new java.util.Date() );
        out.print(" "+"\r\n"+"</b> "+"\r\n"+"");
        out.close();
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
