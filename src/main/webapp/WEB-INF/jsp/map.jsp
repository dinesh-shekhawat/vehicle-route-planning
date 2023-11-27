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
                    <button 
                        class="d-inline-block btn btn-info mr-2" 
                        id="add-stop"
                        onclick="addStop()">
                        Add Stop
                    </button>
    
                    <button 
                        class="d-inline-block btn btn-primary" 
                        id="calculate-route"
                        onclick="calculateRoute()">
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

            var markers = [];
            var markerId = 1;
            var popupContents = {};

            var map = L.map('map').setView([latitude, longitude], zoomLevel);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);

            function addStop() {
                var markerIdValue = `marker-\${markerId}`;
                var marker = L.marker(
                    map.getCenter(), 
                    { draggable: true, id: markerIdValue }
                ).addTo(map);

                markers.push(marker);

                marker.bindPopup(createPopupContent(markerId, markerIdValue));
                markerId++;
            }

            function calculateRoute() {
                var locations = [];
                markers.forEach(function (marker, index) {
                    const latLng = marker.getLatLng();
                    const name = popupContents[marker.options.id].querySelector('input').value;
                    const latitude = latLng.lat;
                    const longitude = latLng.lng;
                    locations.push({
                        name,
                        latitude,
                        longitude
                    });
                });

                console.log(locations);

                if (locations.length < 2) {
                    alert('Please add at least two stops.');
                    return;
                }

            }

            function createPopupContent(sequence, locationName) {
                var popupContent = document.createElement("div");
                popupContent.className = "popup-form";

                popupContent.innerHTML = `
                    <form class="mb-3">
                        <div class="mb-3">
                            <p class="form-control-plaintext">Sequence: \${sequence}</p>
                        </div>

                        <div class="mb-3">
                            <label for="\${locationName}" class="form-label">Location Name:</label>
                            <input type="text" id="\${locationName}" class="form-control" value="\${locationName}">
                        </div>

                        <button 
                            type="button" 
                            class="btn btn-danger" 
                            onclick="deleteLocation('\${locationName}')">
                            Delete Location
                        </button>
                    </form>
                `;

                popupContents[locationName] = popupContent;

                return popupContent;
            }

            function deleteLocation(locationName) {
                var markerToDelete = markers.find(marker => marker.options.id === locationName);
                if (markerToDelete) {
                    // Find the index of the marker in the markers array
                    var index = markers.indexOf(markerToDelete);

                    // Update the sequence for subsequent markers
                    for (var i = index + 1; i < markers.length; i++) {
                        var sequenceElement = popupContents[markers[i].options.id].querySelector('p');
                        var currentSequence = parseInt(sequenceElement.innerText.split(": ")[1]);
                        sequenceElement.innerText = "Sequence: " + (currentSequence - 1);
                    }

                    // Remove the marker from the map
                    map.removeLayer(markerToDelete);

                    // Remove the marker from the markers array
                    markers.splice(index, 1);

                    markerId--;
                }
            }
        </script>
    </body>

    </html>