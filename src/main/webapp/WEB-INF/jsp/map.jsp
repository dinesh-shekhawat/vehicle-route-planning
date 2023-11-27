<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Map</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leaflet.css" />
</head>
<body>
    <div id="map" style="height: 500px;"></div>

    <script src="${pageContext.request.contextPath}/js/leaflet.js"></script>
    <script>
        var latitude = ${latitude};
        var longitude = ${longitude};
        var zoomLevel = ${zoomLevel};

        var map = L.map('map').setView([latitude, longitude], zoomLevel);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(map);
    </script>
</body>
</html>
