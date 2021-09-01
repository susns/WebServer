import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class show3_jsp extends HttpServlet {

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.print(""+"\r\n"+"<!DOCTYPE html>"+"\r\n"+"<html>"+"\r\n"+"<head>"+"\r\n"+"<title>Testing for Servlet-MVC</title>"+"\r\n"+"<body> <h1>Recommended Pet - Testing for Web-MVC</h1> "+"\r\n"+"<p>"+"\r\n"+"You want a ");
        out.print(request.getParameter("legs"));
        out.print("-legged pet weighing ");
        out.print(request.getParameter("weight"));
        out.print("lbs."+"\r\n"+"</p>"+"\r\n"+"<p> We recommend getting <b>");
        out.print(request.getAttribute("pet"));
        out.print("</b>"+"\r\n"+"</p>"+"\r\n"+"</body> "+"\r\n"+""+"\r\n"+"</html>");
        out.close();
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
