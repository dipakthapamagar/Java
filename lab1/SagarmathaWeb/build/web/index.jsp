<%-- 
    Document   : index
    Created on : Dec 1, 2021, 9:39:11 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
<!--        <a href="test">Click me to launch the servlet</a>
        <p>copy this and paste in url = http://localhost:8080/SagarmathaWeb/test?a=1&b=3</p>-->
        <p>This gives get method.</p>
        <pre>
            <form method ="get" action="test">
                <input type="text" name="a">
                <input type="text" name="b">
                <input type="submit" name="Submit">
            </form>
        </pre>
        <p>This gives post method.</p>
        <pre>
            <form method ="post" action="test">
                <input type="text" name="a">
                <input type="text" name="b">
                <input type="submit" name="Submit">
            </form>
        </pre>
    </body>
</html>
