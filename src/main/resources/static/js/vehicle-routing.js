// vehicle-routing.js

const VehicleRoutingModule = (function () {
    const globalState = {
        contextPath: null,
        latitude: 0,
        longitude: 0,
        zoomLevel: 0,
        map: null,
        VEHICLE_TYPE: 'vehicle',
        vehicleMarkersMap: new Map(),
        currentVehicleLocationSearchId: null,
        locationDecimals: 4,
    };

    function init(contextPath, latitude, longitude, zoomLevel) {
        console.log('VehicleRoutingModule init() called contextPath', contextPath, 'latitude', latitude, 'longitude', longitude, 'zoomLevel', zoomLevel);
        globalState.contextPath = contextPath;
        initMap(latitude, longitude, zoomLevel);
    }

    function initMap(latitude, longitude, zoomLevel) {
        globalState.latitude = latitude;
        globalState.longitude = longitude;
        globalState.zoomLevel = zoomLevel;

        globalState.map = L.map('map').setView([latitude, longitude], zoomLevel);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(globalState.map);
    }

    // Solve the vehicle routing problem
    function solve() {
        console.log('VehicleRoutingModule solve() called');

        // Collect all vehicles in the accordion
        const vehicleAccordion = document.getElementById('vehicle-list-accordion');

        // Collect all vehicles in the accordion
        const accordionItems = vehicleAccordion.querySelectorAll('.accordion');

        // Create an array to store vehicle information
        const vehiclesArray = Array.from(accordionItems).map(accordionItem => {
            const vehicleInfo = {
                name: accordionItem.querySelector('.vehicle-name').value,
                registrationNumber: accordionItem.querySelector('.registration-number').value,
                capacity: accordionItem.querySelector('.vehicle-capacity').value,
            };

            return vehicleInfo;
        });

        // Now 'vehiclesArray' contains the information of all vehicles
        console.log('Vehicles Array:', vehiclesArray);
    }

    // Add a vehicle to the map
    function addVehicle() {
        console.log('VehicleRoutingModule addVehicle() called');

        const vehicleListAccordion = document.getElementById('vehicle-list-accordion');

        // Increment a counter for unique IDs
        const uniqueIdCounter = vehicleListAccordion.childElementCount;

        // Dynamic values
        const dynamicValues = {
            vehicleNameLabel: 'Name',
            registrationNumberLabel: 'Registration',
            capacityLabel: 'Capacity',
            vehicleNamePlaceholder: 'Enter vehicle name',
            registrationNumberPlaceholder: 'Enter registration number',
            capacityPlaceholder: 'Enter capacity',
            vehicleSubAccordionId: `vehicle-sub-accordion-${uniqueIdCounter}`,
            deleteIconPath: `${globalState.contextPath}/css/images/delete-icon.png`,
            carIconPath: `${globalState.contextPath}/css/images/car-icon.png`,
        };

        // Load the template dynamically and replace placeholders
        fetch(`${globalState.contextPath}/html/add-vehicle-form-template.html`)
            .then(response => response.text())
            .then(htmlTemplate => {
                // Append the DocumentFragment to the main container

                // Replace placeholders with dynamic values
                const filledTemplate = htmlTemplate.replace(/\${(.*?)}/g, (_, p1) => dynamicValues[p1.trim()]);

                // Parse the HTML string and create a DocumentFragment
                const parser = new DOMParser();
                const doc = parser.parseFromString(filledTemplate, 'text/html');
                const fragment = doc.body;

                Array.from(fragment.children).forEach(child => {
                    vehicleListAccordion.appendChild(child.cloneNode(true));
                });

                vehicleListAccordion
                    .querySelector('.delete-vehicle-button')
                    .addEventListener('click', handleVehicleDeleteButtonClick);

                vehicleListAccordion
                    .querySelector('.vehicle-name')
                    .addEventListener('change', handleVehicleNameChange);

                // vehicleListAccordion
                //     .querySelector('.search-vehicle-location-button')
                //     .addEventListener('click', handleVehiceLocationSearchButtonClick);

                addMarkerOnMap(
                    globalState.map.getCenter(), 
                    dynamicValues.vehicleSubAccordionId, 
                    dynamicValues.carIconPath,
                    globalState.VEHICLE_TYPE);

                syncMarkerValues();
                syncMarkerPopups();
            })
            .catch(error => console.error('Error loading template:', error));
    }

    function syncMarkerValues() {
        console.log('VehicleRoutingModule syncMarkerValues() called');

        globalState.vehicleMarkersMap.forEach((marker, id) => {
            const newLatLng = marker.getLatLng();
            const roundedLatLng = {
                lat: newLatLng.lat.toFixed(globalState.locationDecimals),
                lng: newLatLng.lng.toFixed(globalState.locationDecimals),
            };

            const locationString = `${roundedLatLng.lat},${roundedLatLng.lng}`;
            
            const vehicleAccordion = document.getElementById(id);
            const locationInput = vehicleAccordion.querySelector('.vehicle-location');
            locationInput.value = locationString;
        });
    }

    function syncMarkerPopups() {
        console.log('VehicleRouting module syncMarkerPopups');

        globalState.vehicleMarkersMap.forEach((marker, id) => {
            const vehicleAccordion = document.getElementById(id);
            const vehicleName = vehicleAccordion.querySelector('.vehicle-name').value;

            const popupContent = `<strong>${vehicleName}</strong>`;
            marker.bindPopup(popupContent);
        });
    }

    function handleVehicleDeleteButtonClick() {
        console.log('Delete button callback clicked');
        // Retrieve the vehicleSubAccordionId from the parent accordion item
        const vehicleSubAccordionId = this.closest('.vehicle-sub-accordion').id;

        // Call deleteVehicle function with the retrieved ID
        deleteVehicle(vehicleSubAccordionId);
    }

    function searchVehicleLocation(vehicleSubAccordionId) {
        console.log('VehicleRoutingModule searchVehicleLocation() called');
        
    }

    function handleVehicleNameChange() {
        console.log('Vehicle name changed');
        // Retrieve the vehicleSubAccordionId from the parent accordion header item
        const accordionItem = this.closest('.accordion-item');
        const vehicleAccordionHeader = accordionItem.querySelector('.vehicle-accordion-header');
        
        const vehicleName = this.value;
        console.log('Vehicle name:', vehicleName);

        vehicleAccordionHeader.querySelector('.vehicle-header-text').innerHTML = vehicleName;

        syncMarkerPopups();
    }

    function addMarkerOnMap(location, id, iconPath, type) {    
        // Create a marker with a custom icon
        const customIcon = L.icon({
            iconUrl: iconPath,
            iconSize: [32, 32],
        });
    
        const marker = L
            .marker(location, { icon: customIcon, draggable: true, id: id })
            .addTo(globalState.map);
    
        marker.on('dragend', handleMarkerDragEnd);

        switch (type) {
            case globalState.VEHICLE_TYPE:
                console.log('updating vehicleMarkersMap');
                globalState.vehicleMarkersMap.set(id, marker);
                break;
        }
    }

    function handleMarkerDragEnd(event) {
        // const marker = event.target;
        // const newLatLng = marker.getLatLng();
        // console.log(`Marker with ID dragged to:`, newLatLng);
        syncMarkerValues();
    }

    // Attach click listeners to HTML elements
    function attachListeners() {
        const solveButton = document.getElementById('solve');
        if (solveButton) {
            solveButton.addEventListener('click', solve);
        }

        const addVehicleButton = document.getElementById('add-vehicle-button');
        if (addVehicleButton) {
            addVehicleButton.addEventListener('click', addVehicle);
        }
    }

    // Delete a vehicle from the map
    function deleteVehicle(vehicleSubAccordionId) {
        console.log('VehicleRoutingModule deleteVehicle() called with vehicleSubAccordionId', vehicleSubAccordionId);

        // Find the closest parent with class accordion
        const accordionItem = document.getElementById(vehicleSubAccordionId);
        const parent = accordionItem.closest('.accordion');
        if (!parent) {
            console.warn('Accordion item not found for deletion.');
            return;
        }

        const deleteButton = accordionItem.querySelector('.delete-vehicle-button');
        deleteButton.removeEventListener('click', handleVehicleDeleteButtonClick);

        const vehicleNameInput = accordionItem.querySelector('.vehicle-name');
        vehicleNameInput.removeEventListener('change', handleVehicleNameChange);

        parent.remove();

        const marker = globalState.vehicleMarkersMap.get(vehicleSubAccordionId);
        marker.remove();
        globalState.vehicleMarkersMap.delete(vehicleSubAccordionId);
        
        updateVehicleAccordionItems();

        syncMarkerMap(globalState.VEHICLE_TYPE);
    }

    function syncMarkerMap(type) {
        switch (type) {
            case globalState.VEHICLE_TYPE:
                syncVehicleMarkerMap();
                break;
        }
    }

    function syncVehicleMarkerMap() {
        console.log('VehicleRoutingModule syncMarkerMap() called');
        
        let deleteIndex = 0;
        globalState.vehicleMarkersMap.forEach((marker, id) => {
            const vehicleAccordion = document.getElementById(id);
            if (!vehicleAccordion) {
                return; 
            }

            deleteIndex++;
        });

        console.log('deleteIndex', deleteIndex);

        const updatedMarkersMap = new Map();
        globalState.vehicleMarkersMap.forEach((marker, id) => {
            const currentIndex = parseInt(id.split('-').pop());
            const newIndex = currentIndex - 1;

            // Check if the current index is greater than the delete index
            if (currentIndex > deleteIndex) {
                const newKey = `vehicle-sub-accordion-${newIndex}`;
                updatedMarkersMap.set(newKey, marker);
            } else {
                // If the current index is less than or equal to the delete index, keep the original key
                updatedMarkersMap.set(id, marker);
            }
        });

        // Update globalState markersMap
        globalState.vehicleMarkersMap = updatedMarkersMap;
    }

    // Update accordion items after deleting a vehicle
    function updateVehicleAccordionItems() {
        // Find the container element
        const accordionContainer = document.getElementById('vehicle-list-accordion');

        // Select all remaining accordion items
        const accordionItems = accordionContainer.querySelectorAll('.accordion');

        // Iterate over each accordion item and update ID and data-bs-target
        accordionItems.forEach((accordionItem, index) => {
            const newId = `vehicle-sub-accordion-${index}`; // Implement your logic to generate a new ID
            
            // Update specific elements inside the accordion item
            const accordionButton = accordionItem.querySelector('.accordion-button');
            const accordionCollapse = accordionItem.querySelector('.accordion-collapse');

            // Update the ID and data-bs-target of specific elements
            accordionButton.dataset.bsTarget = `#${newId}`;
            accordionCollapse.id = newId;
        });
    }

    // Return the public API
    return {
        init: init,
        attachListeners: attachListeners,
        searchVehicleLocation: searchVehicleLocation
    };
})();
