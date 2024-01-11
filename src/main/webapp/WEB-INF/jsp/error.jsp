<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org" lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
</head>

<body>
    <div class="d-flex align-items-center justify-content-center vh-100">
        <div class="text-center">
            <div class="display-1 text-danger">404</div>
            <h2 class="mb-4">Oops! Something went wrong.</h2>
            <p class="lead mb-4">We're sorry for the inconvenience!</p>
            <div class="btn-group" role="group">

                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Go Back</a>
            </div>
        </div>

    </div>
    </div>
</body>


</html>