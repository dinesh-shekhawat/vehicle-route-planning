// map.js

const MapModule = (function () {
    const globalState = {
        latitude : 0,
        longitude : 0,
        zoomLevel: 0,
        markers: [],
        markerId: 1,
        popupContents: {},
        map: null,
    };

    function initMap(latitude, longitude, zoomLevel) {
        globalState.latitude = latitude;
        globalState.longitude = longitude;
        globalState.zoomLevel = zoomLevel;

        globalState.map = L.map('map').setView([latitude, longitude], zoomLevel);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(globalState.map);
    }

    function addStop() {
        let markerIdValue = `marker-${globalState.markerId}`;
        let marker = L.marker(
            globalState.map.getCenter(), 
            { draggable: true, id: markerIdValue }
        ).addTo(globalState.map);

        globalState.markers.push(marker);

        const popupContent = createPopupContent(globalState.markerId, markerIdValue);
        marker.bindPopup(createPopupContent(globalState.markerId, markerIdValue));
        
        const deleteButton = popupContent.querySelector('.delete-btn');
        if (deleteButton) {
            deleteButton.addEventListener('click', function () {
                deleteLocation(locationName);
            });
        } else {
            console.log('Delete button not found for markerIdValue', markerIdValue);
        }

        globalState.markerId++;
    }

    function calculateRoute() {
        const locations = [];
        globalState.markers.forEach(function (marker, _) {
            const latLng = marker.getLatLng();
            const name = globalState.popupContents[marker.options.id].querySelector('input').value;
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

    function createPopupContent(sequence, markerId) {
        const popupContent = document.createElement("div");
        popupContent.className = "popup-form";

        popupContent.innerHTML = `
            <form class="mb-3">
                <div class="mb-3">
                    <p class="form-control-plaintext">Sequence: ${sequence}</p>
                </div>

                <div class="mb-3">
                    <label for="${markerId}" class="form-label">Location Name:</label>
                    <input type="text" id="${markerId}" class="form-control" value="${markerId}">
                </div>

                <button 
                    type="button" 
                    class="btn btn-danger delete-btn"
                    onclick="MapModule.deleteLocation('${markerId}')">
                    Delete Location
                </button>
            </form>
        `;

        globalState.popupContents[markerId] = popupContent;

        return popupContent;
    }

    function deleteLocation(locationName) {
        const markerToDelete = globalState.markers.find(marker => marker.options.id === locationName);
        if (markerToDelete) {
            // Find the index of the marker in the markers array
            const index = globalState.markers.indexOf(markerToDelete);

            // Update the sequence for subsequent markers
            for (let i = index + 1; i < globalState.markers.length; i++) {
                const marker = globalState.markers[i];;
                const sequenceElement = globalState.popupContents[marker.options.id].querySelector('p');
                const currentSequence = parseInt(sequenceElement.innerText.split(": ")[1]);
                sequenceElement.innerText = "Sequence: " + (currentSequence - 1);
            }

            // Remove the marker from the map
            globalState.map.removeLayer(markerToDelete);

            // Remove the marker from the markers array
            globalState.markers.splice(index, 1);

            globalState.markerId--;
        }
    }


    // Attach click listeners to HTML elements
    function attachListeners() {
        const addStopButton = document.getElementById('add-stop');
        const calculateRouteButton = document.getElementById('calculate-route');

        if (addStopButton) {
            addStopButton.addEventListener('click', addStop);
        }

        if (calculateRouteButton) {
            calculateRouteButton.addEventListener('click', calculateRoute);
        }
    }

    // Return the public API
    return {
        initMap: initMap,
        attachListeners: attachListeners,
        deleteLocation: deleteLocation,
    };
})();
