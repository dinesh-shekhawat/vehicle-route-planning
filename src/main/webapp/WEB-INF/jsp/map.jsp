<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Map</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leaflet.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    </head>

    <body>
        <div class="container">
            <div class="row" id="control-center">
                <div class="col-12 p-2">
                    <button class="d-inline-block btn btn-info mr-2" id="add-stop">
                        Add Stop
                    </button>
    
                    <button class="d-inline-block btn btn-primary" id="calculate-route">
                        Calculate Route
                    </button>
                </div>
            </div>
    
            <div class="row p-2">
                <div id="map" style="height: 500px;"></div>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/js/leaflet.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

        <script>
            var latitude = ${ latitude };
            var longitude = ${ longitude };
            var zoomLevel = ${ zoomLevel };

            var map = L.map('map').setView([latitude, longitude], zoomLevel);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);
        </script>
    </body>

    </html>