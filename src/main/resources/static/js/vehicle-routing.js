// vehicle-routing.js

const VehicleRoutingModule = (function () {
    const globalState = {
        contextPath: null,
        latitude: 0,
        longitude: 0,
        zoomLevel: 0,
        map: null,
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

                // Scan the entire DOM for delete buttons and attach click event listeners
                document.querySelectorAll('.delete-button').forEach(deleteButton => {
                    deleteButton.addEventListener('click', function () {
                        // Retrieve the vehicleSubAccordionId from the parent accordion item
                        const vehicleSubAccordionId = this.closest('.vehicle-sub-accordion').id;

                        // Call deleteVehicle function with the retrieved ID
                        deleteVehicle(vehicleSubAccordionId);
                    });
                });
            })
            .catch(error => console.error('Error loading template:', error));
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
        if (parent) {
            parent.remove();
        } else {
            console.warn('Accordion item not found for deletion.');
        }
    }

    // Return the public API
    return {
        init: init,
        attachListeners: attachListeners,
    };
})();
