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

    if (request.getParameter("op").equals("delete")) {
        request.setAttribute("msg", "删除成功");
        userBeanService.deleteEntity(Long.parseLong(request.getParameter("id")));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/user/list.jsp");
        requestDispatcher.forward(request, response);
    }
    if (request.getParameter("op").equals("add") && (request.getMethod().equals("GET"))) {
        request.setAttribute("op", "新增");
        UserBean bean = new UserBean();
        request.setAttribute("client", bean);
    }
    if (request.getParameter("op").equals("add") && (request.getMethod().equals("POST"))) {
        UserBean bean = new UserBean();
        String name = request.getParameter("username");
        bean.setUsername(name);
        String pwd = request.getParameter("password");
        bean.setPassword(PasswordHelper.encryptPassword(name, pwd, bean.getSalt()));
        request.setAttribute("msg", "新增成功");
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

    <form name="form" method="post" commandName="user" cssClass="form-inline">
        <input type="hidden" name="id"/>
        <input type="hidden" name="salt"/>


        <div class="form-group">
            <label path="username">用户名：</label>
            <input name="username"/>
        </div>

        <div class="form-group">
            <label path="password">密码：</label>
            <input type="password" name="password"/>
        </div>

        <input type="submit" value="${op}" class="btn btn-default">

    </form>

<jsp:include page="../footer.jsp"/>