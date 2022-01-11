package np.edu.scst.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int a = Integer.valueOf(request.getParameter("a"));
        int b = Integer.valueOf(request.getParameter("b"));
        PrintWriter p = response.getWriter();
        p.println("Hello from servlet get method.");
        p.println("The Sum is "+(a+b));
    }
    
        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int a = Integer.valueOf(request.getParameter("a"));
        int b = Integer.valueOf(request.getParameter("b"));
        PrintWriter p = response.getWriter();
        p.println("Hello from servlet post method.");
        p.println("The Sum is "+(a+b));
    }

}
