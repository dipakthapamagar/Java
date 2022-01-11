<%-- 
    Document   : first
    Created on : Dec 2, 2021, 9:25:39 AM
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
        <h1>Hello World! from my first jsp</h1>
        <%= request.getAttribute("result") %>
    </body>
</html>
