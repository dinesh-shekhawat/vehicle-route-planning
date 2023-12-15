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
            <h2 class="mb-4">You have been logged out</h2>
            <p class="lead mb-4">Thanks for using the application!</p>
            <div class="btn-group" role="group">

                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Go Back</a>
            </div>
        </div>

    </div>
    </div>
</body>


</html>