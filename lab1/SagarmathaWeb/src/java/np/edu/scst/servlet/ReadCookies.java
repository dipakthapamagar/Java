package np.edu.scst.servlet;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReadCookies extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        pw.println("Reading cookies");
        Cookie cookies[] = request.getCookies();
        for (Cookie cookie : cookies) {
            pw.println(cookie.getName() + " : " + cookie.getValue());
        }
    }

}
