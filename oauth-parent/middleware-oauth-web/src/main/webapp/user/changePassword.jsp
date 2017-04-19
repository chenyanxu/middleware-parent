<%@ page import="com.kalix.framework.core.util.PasswordHelper" %>
<%@ page import="com.kalix.middleware.oauth.api.biz.IUserBeanService" %>
<%@ page import="com.kalix.middleware.oauth.entities.UserBean" %>
<%@ page import="org.osgi.framework.BundleContext" %>
<%@ page import="org.osgi.framework.ServiceReference" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../top.jsp"/>
</head>

<%
    BundleContext ctx = (BundleContext) getServletContext().getAttribute("osgi-bundlecontext");
    ServiceReference ref = ctx.getServiceReference(IUserBeanService.class.getName());
    IUserBeanService userBeanService = (IUserBeanService) ctx.getService(ref);

    if ((request.getMethod().equals("GET"))) {
        UserBean bean = userBeanService.getEntity(Long.parseLong(request.getParameter("id")));
        request.setAttribute("msg", "密码修改");
        request.setAttribute("op", "密码修改");
        request.setAttribute("user", bean);
    }
    if (request.getMethod().equals("POST")) {
        UserBean bean = userBeanService.getEntity(Long.parseLong(request.getParameter("id")));
        String pwd = request.getParameter("password");
        bean.setPassword(PasswordHelper.encryptPassword(bean.getUsername(), pwd, bean.getSalt()));
        request.setAttribute("msg", "密码修改成功");
        userBeanService.saveEntity(bean);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/user/list.jsp");
        requestDispatcher.forward(request, response);
    }

%>
<body>

<div class="container">

    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-right">
            </ul>
        </nav>
        <h3 class="text-muted">OAuth2 Server 用户</h3>
    </div>

    <form method="post" class="form-inline">
        <input type="hidden" name="id" value="${user.id}"/>
        <div class="form-group">
            <label for="password">新密码：</label>
            <input type="text" id="password" name="password"/>
        </div>
        <input type="submit" value="修改密码" class="btn btn-default">
    </form>

<jsp:include page="../footer.jsp"/>