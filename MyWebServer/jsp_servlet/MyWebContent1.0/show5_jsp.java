import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class show5_jsp extends HttpServlet {

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.print(""+"\r\n"+"<!DOCTYPE html>"+"\r\n"+"<html>"+"\r\n"+"<head>"+"\r\n"+"<title>Testing for Filter</title>"+"\r\n"+"<body> <h1>Testing for Filter</h1> "+"\r\n"+"<p>"+"\r\n"+"The site have been visited ");
        out.print(course.AccessFilter.nNum);
        out.print(" times."+"\r\n"+"<p>"+"\r\n"+"</body> "+"\r\n"+""+"\r\n"+"</html>");
        out.close();
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
