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

    if (request.getParameter("op").equals("delete")) {
        request.setAttribute("msg", "删除成功");
        clientBeanService.deleteEntity(Long.parseLong(request.getParameter("id")));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/client/list.jsp");
        requestDispatcher.forward(request, response);
    }
    if (request.getParameter("op").equals("add") && (request.getMethod().equals("GET"))) {
        request.setAttribute("op", "新增");
        ClientBean bean = new ClientBean();
        request.setAttribute("client", bean);
    }
    if (request.getParameter("op").equals("add") && (request.getMethod().equals("POST"))) {
        ClientBean bean = new ClientBean();
        bean.setClientName(request.getParameter("clientName"));
        request.setAttribute("msg", "新增成功");
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

    <form name="form" method="post" commandName="client" cssClass="form-inline">
        <input type="hidden" name="id" value="${client.id}"/>
        <input type="hidden" name="clientId" value="${client.clientId}"/>
        <input type="hidden" name="clientSecret" value="${client.clientSecret}"/>

        <div class="form-group">
            <label path="clientName">应用名：</label>
            <input name="clientName"/>
        </div>

        <%--<button>${op}</button>--%>
        <input type="submit" value="${op}"/>

    </form>

<jsp:include page="../footer.jsp"/>