<%@ page import="com.kalix.middleware.oauth.api.biz.IClientBeanService" %>
<%@ page import="com.kalix.middleware.oauth.entities.ClientBean" %>
<%@ page import="org.osgi.framework.BundleContext" %>
<%@ page import="org.osgi.framework.ServiceReference" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="../top.jsp"/>

<%
    BundleContext ctx = (BundleContext) getServletContext().getAttribute("osgi-bundlecontext");
    ServiceReference ref = ctx.getServiceReference(IClientBeanService.class.getName());
    IClientBeanService clientBeanService = (IClientBeanService) ctx.getService(ref);
    List<ClientBean> clientList = clientBeanService.getAllEntity();
    request.setAttribute("clientList", clientList);
%>

</head>
<body>

<div class="container">

    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-right">
            </ul>
        </nav>
        <h3 class="text-muted">OAuth2 Server 应用列表</h3>
        <div>
            <a href="${pageContext.request.contextPath}/client/list.jsp">应用管理</a>
            <a href="${pageContext.request.contextPath}/user/list.jsp">用户管理</a>
        </div>
    </div>


    <c:if test="${not empty msg}">
    <div class="alert alert-danger" role="alert">${msg}</div>
    </c:if>

    <div>
        <h3><a href="${pageContext.request.contextPath}/client/add.jsp?op=add">应用新增</a></h3>
    </div>

    <table class="table table-bordered table-hover table-condensed">
        <thead>
        <tr>
            <th>客户端名</th>
            <th>客户端ID</th>
            <th>客户端安全KEY</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${clientList}" var="client">
            <tr>
                <td>${client.clientName}</td>
                <td>${client.clientId}</td>
                <td>${client.clientSecret}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/client/edit.jsp?id=${client.id}">修改</a>
                    <a href="${pageContext.request.contextPath}/client/add.jsp?op=delete&id=${client.id}">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

<jsp:include page="../footer.jsp"/>