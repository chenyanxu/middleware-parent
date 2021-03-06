<%@ page import="com.kalix.middleware.oauth.api.biz.IClientBeanService" %>
<%@ page import="com.kalix.middleware.oauth.entities.ClientBean" %>
<%@ page import="org.osgi.framework.BundleContext" %>
<%@ page import="org.osgi.framework.ServiceReference" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../top.jsp"/>
<%
    BundleContext ctx = (BundleContext) getServletContext().getAttribute("osgi-bundlecontext");
    ServiceReference ref = ctx.getServiceReference(IClientBeanService.class.getName());
    IClientBeanService clientBeanService = (IClientBeanService) ctx.getService(ref);


    if ((request.getMethod().equals("GET"))) {
        ClientBean bean = clientBeanService.getEntity(Long.parseLong(request.getParameter("id")));
        request.setAttribute("msg", "更新");
        request.setAttribute("op", "更新");
        request.setAttribute("client", bean);
    }
    if ((request.getMethod().equals("POST"))) {

        ClientBean bean = clientBeanService.getEntity(Long.parseLong(request.getParameter("id")));
        bean.setClientName(request.getParameter("clientName"));
        request.setAttribute("msg", "更新成功");
        clientBeanService.saveEntity(bean);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/client/list.jsp");
        requestDispatcher.forward(request, response);

    }

%>
</head>
<body>
<div></div>

<div class="container">

    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-right">
            </ul>
        </nav>
        <h3 class="text-muted">OAuth2 Server 应用</h3>
    </div>

    <c:if test="${not empty msg}">
    <div class="alert alert-danger" role="alert">${msg}</div>
    </c:if>

    <form name="form" method="post" commandName="client" cssClass="form-inline">
        <input type="hidden" name="id" value="${client.id}"/>
        <input type="hidden" name="clientId" value="${client.clientId}"/>
        <input type="hidden" name="clientSecret" value="${client.clientSecret}"/>

        <div class="form-group">
            <label path="clientName">应用名：</label>
            <input name="clientName" value="${client.clientName}"/>
        </div>

        <%--<button>${op}</button>--%>
        <input type="submit" value="${op}"/>

    </form>

<jsp:include page="../footer.jsp"/>