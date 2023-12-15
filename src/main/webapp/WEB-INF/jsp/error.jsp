<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
</head>

<body>
    <div class="d-flex align-items-center justify-content-center vh-100">
        <div class="text-center">
            <h1 class="display-1 fw-bold">400</h1>
            <p class="fs-3"> <span class="text-danger">Oops!</span>&nbsp;Something went wrong</p>
            <p class="lead">
                Here is a list of errors that might help you debug the problem
            </p>
            <div class="my-2 alert alert-danger">
                <ul style="list-style-type:none">
                    <c:forEach var="error" items="${errors}">
                        <li class="my-2">
                            <c:out value="${error}" />
                        </li>
                    </c:forEach>

                </ul>
            </div>
            <a href="/" class="btn btn-primary">Go Home</a>
        </div>
    </div>
</body>


</html>