<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>OTP</title>
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
                    <h1><i class="fas fa-user"></i> User OTP Verification</h1>
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
                            <h4>OTP</h4>
                        </div>
                        <div class="card-body">
                            <form action="otp/add" method="POST">
                                <div class="mb-3">
                                    <label for="first-name" class="form-label">Email</label>
                                    <input type="text" class="form-control" name="email" id="first-name" value="${param.email}">                                                                        
                                    <span class="text-success">Enter OTP sent at your email ID</span>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="otp" class="form-label">OTP</label>
                                    <input type="text" class="form-control" name="otp" id="otp">
                                    <c:if test="${param.incorrectOtp != null}">                                        
                                        <span class="text-danger">Incorrect OTP entered</span>
                                    </c:if>
                                    <c:if test="${param.otpError != null}">                                        
                                        <span class="text-danger">Invalid OTP provided</span>
                                    </c:if>
                                </div>

                                <div class="mb-3 text-center">
                                    <p>Did not get it yet? <a href="${pageContext.request.contextPath}/login">Retry Login</a></p>
                                </div>
                                                                
                                <div class="d-grid col-12">
                                    <input type="submit" value="Submit" class="mt-3 btn btn-primary btn-block">
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