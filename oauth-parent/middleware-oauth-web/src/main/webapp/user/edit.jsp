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
        request.setAttribute("msg", "更新");
        request.setAttribute("op", "更新");
        request.setAttribute("user", bean);
    }
    if ((request.getMethod().equals("POST"))) {

        UserBean bean = userBeanService.getEntity(Long.parseLong(request.getParameter("id")));
        bean.setUsername(request.getParameter("username"));
        request.setAttribute("msg", "更新成功");
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
    <c:if test="${not empty msg}">
    <div class="alert alert-danger" role="alert">${msg}</div>
    </c:if>
    <form method="post" commandName="user" cssClass="form-inline">
        <input type="hidden" name="id" value="${user.id}"/>
        <input type="hidden" name="salt" value="${user.salt}"/>
        <%--<c:if test="${op ne '新增'}">
            <input type="hidden" id="password" value="${user.password}"/>
        </c:if>--%>

        <div class="form-group">
            <label path="username">用户名：</label>
            <input name="username" value="${user.username}"/>
        </div>

        <%--<div class="form-group">
            <label path="password">密码：</label>
            <input type="password" name="password"/>
        </div>--%>

        <input type="submit" value="${op}" class="btn btn-default">

    </form>

<jsp:include page="../footer.jsp"/>