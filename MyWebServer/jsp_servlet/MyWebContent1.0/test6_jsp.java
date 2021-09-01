import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

public class test6_jsp extends HttpServlet {

    /**
    * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.print("<html> <body>"+"\r\n"+"<b>Login to System</B>"+"\r\n"+""+"\r\n"+"Current user is:");
        out.print(request.getSession().getAttribute("username"));
        out.print(""+"\r\n"+" </br>"+"\r\n"+" </hr>"+"\r\n"+""+"\r\n"+"<form  action=\"Login\" method=\"post\"> "+"\r\n"+""+"\r\n"+"   <h4> User Name: </h4>"+"\r\n"+"        <input type=\"text\"  name=\"username\"  size=\"10\">"+"\r\n"+""+"\r\n"+"   <h4> Password: </h4>"+"\r\n"+"   <input type=\"text\"  name=\"password\"  size=\"10\">"+"\r\n"+"        <p>"+"\r\n"+"    <input type=\"submit\" value=\"Login\" >"+"\r\n"+"    </p></body>"+"\r\n"+"</form>"+"\r\n"+"");
        out.close();
    }

    /**
    * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
