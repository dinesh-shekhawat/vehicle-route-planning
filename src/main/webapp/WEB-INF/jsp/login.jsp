<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontawesome-free-5.6.3-web/css/all.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
</head>

<body>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

    <nav class="navbar navbar-expand-sm navbar-dark bg-dark p-0">
        <div class="container">
            <span class="navbar-brand">DriveSync</span>
        </div>
    </nav>
    
    <header id="main-header" class="py-2 bg-success text-white">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h1><i class="fas fa-user"></i> User Login</h1>
                </div>
            </div>
        </div>
    </header>

    <section id="login" class="mt-4">
        <div class="container">
            <div class="row">
                <div class="col-md-6 mx-auto">
                    <div class="card">
                        <div class="card-header">
                            <h4>User Login</h4>
                        </div>
                        <div class="card-body">
                            <form action="login/add" method="POST">
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" name="email" id="email" value="${param.email}">
                                    <c:if test="${param.emailEror != null}">                                        
                                        <span class="text-danger">Email is required</span>
                                    </c:if>
                                    <c:if test="${param.emailFormatEror != null}">                                        
                                        <span class="text-danger">Email format is invalid</span>
                                    </c:if>
                                    <c:if test="${param.noUser != null}">                                        
                                        <span class="text-danger">Email has not been registered</span>
                                    </c:if>
                                </div>

                                <div class="mb-3 text-center">
                                    <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Register</a></p>
                                </div>

                                <div class="d-grid col-12">
                                    <input type="submit" value="Send OTP" class="mt-3 btn btn-success btn-block">
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</body>

</html>