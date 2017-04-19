<%@ page import="com.kalix.middleware.oauth.api.biz.IUserBeanService" %>
<%@ page import="com.kalix.middleware.oauth.entities.UserBean" %>
<%@ page import="org.osgi.framework.BundleContext" %>
<%@ page import="org.osgi.framework.ServiceReference" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../top.jsp"/>

<%
    BundleContext ctx = (BundleContext) getServletContext().getAttribute("osgi-bundlecontext");
    ServiceReference ref = ctx.getServiceReference(IUserBeanService.class.getName());
    IUserBeanService userBeanService = (IUserBeanService) ctx.getService(ref);
    List<UserBean> userList = userBeanService.getAllEntity();
    /*String msg = "dfdfdf";
    request.setAttribute("msg", msg);*/
    request.setAttribute("userList", userList);
%>
</head>
<body>

<div class="container">

    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-right">
            </ul>
        </nav>
        <h3 class="text-muted">OAuth2 Server 用户列表</h3>
        <div>
            <a href="${pageContext.request.contextPath}/client/list.jsp">应用管理</a>
            <a href="${pageContext.request.contextPath}/user/list.jsp">用户管理</a>
        </div>
    </div>


    <c:if test="${not empty msg}">
    <div class="alert alert-danger" role="alert">${msg}</div>
    </c:if>

    <h3><a href="${pageContext.request.contextPath}/user/add.jsp?op=add">用户新增</a></h3>

    <table class="table table-bordered table-hover table-condensed">
        <thead>
        <tr>
            <th>用户名</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${userList}" var="user">
            <tr>
                <td>${user.username}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/user/edit.jsp?id=${user.id}">修改</a>
                    <a href="${pageContext.request.contextPath}/user/add.jsp?op=delete&id=${user.id}">删除</a>
                    <a href="${pageContext.request.contextPath}/user/changePassword.jsp?id=${user.id}">改密</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
<jsp:include page="../footer.jsp"/>