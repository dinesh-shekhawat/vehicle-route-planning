<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

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
            <a href="index.html" class="navbar-brand">DriveSync</a>
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
                            <h4>Otp</h4>
                        </div>
                        <div class="card-body">
                            <form action="otp/add" method="POST">
                                <div class="mb-3">
                                    <label for="first-name" class="form-label">Email</label>
                                    <input type="text" class="form-control" name="firstName" id="first-name" value="${param.email}" readonly>                                                                        
                                    <span class="text-success">Enter OTP sent at your email ID</span>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="otp" class="form-label">OTP</label>
                                    <input type="text" class="form-control" name="otp" id="otp">
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