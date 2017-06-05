<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="top.jsp"/>

<html>

<head>
    <title>OAuth2 Server</title>
</head>

<body>
<div class="container">

    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-right">
            </ul>
        </nav>
        <h3 class="text-muted">OAuth2 Server 应用登录</h3>
    </div>

    <form method="POST">
        <div class="form-group">
            <label path="clientName">Login Name:</label>
            <input type="text" name="username">
        </div>
        <div class="form-group">
            <label path="clientName">Login Password: </label>
            <input type="password" name="password">
        </div>
        <input type="submit" value="登录"><br>
    </form>
</div>
<%
    if (request.getParameter("username") != null && request.getParameter("password") != null) {
        String Name = request.getParameter("username");
        String Password = request.getParameter("password");
        if (Name.equals("admin") && Password.equals("1234")) {
            session.setAttribute("Login", "OK");
            response.sendRedirect("index.jsp");
        } else {
%>
登录失败:用户名或密码不正确～
<%
        }
    }
%>
</body>
</html>