<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/fontawesome-free-5.6.3-web/css/all.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
</head>

<body>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

    <nav class="navbar navbar-expand-sm navbar-dark bg-dark p-0">
        <div class="container">
            <span class="navbar-brand">DriveSync</a>
        </div>
    </nav>
    
    <header id="main-header" class="py-2 bg-primary text-white">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h1><i class="fas fa-user"></i> User Registration</h1>
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
                            <h4>User Registration</h4>
                        </div>
                        <div class="card-body">
                            <form action="register/add" method="POST">
                                <div class="mb-3">
                                    <label for="first-name" class="form-label">First Name</label>
                                    <input type="text" class="form-control" name="firstName" id="first-name" value="${param.firstName}">
                                                                        
                                    <c:if test="${param.firstNameEror != null}">                                        
                                        <span class="text-danger">First Name is required</span>
                                    </c:if>
                                </div>

                                <div class="mb-3">
                                    <label for="last-name" class="form-label">Last Name</label>
                                    <input type="text" class="form-control" name="lastName" id="last-name" value="${param.lastName}">
                                    <c:if test="${param.lastNameEror != null}">                                        
                                        <span class="text-danger">Last Name is required</span>
                                    </c:if>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" name="email" id="email" value="${param.email}">
                                    <c:if test="${param.emailEror != null}">                                        
                                        <span class="text-danger">Email is required</span>
                                    </c:if>
                                    <c:if test="${param.emailFormatEror != null}">                                        
                                        <span class="text-danger">Email format is invalid</span>
                                    </c:if>
                                    <c:if test="${param.duplicateEmail != null}">                                        
                                        <span class="text-danger">Email has already been registered</span>
                                    </c:if>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="nickname" class="form-label">Nickname</label>
                                    <input type="text" class="form-control" name="nickname" id="nickname" value="${param.nickname}">
                                </div>
                                                    
                                <div class="mb-3 text-center">
                                    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
                                </div>

                                <div class="d-grid col-12">
                                    <input type="submit" value="Register" class="mt-3 btn btn-primary btn-block">
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