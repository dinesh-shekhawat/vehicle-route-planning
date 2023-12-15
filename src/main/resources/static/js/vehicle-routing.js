const VehicleRoutingModule = (function () {
    const globalState = {
        contextPath: null,
        latitude: 0,
        longitude: 0,
        zoomLevel: 0,
        map: null,
        VEHICLE_TYPE: 'vehicle',
        SERVICE_TYPE: 'service',
        DELIVERY_TYPE: 'delivery',
        SHIPMENT_SOURCE_TYPE: 'shipment-source',
        SHIPMENT_DESTINATION_TYPE: 'shipment-destination',
        vehicleMarkersMap: new Map(),
        serviceMarkersMap: new Map(),
        deliveryMarkersMap: new Map(),
        shipmentSourceMarkersMap: new Map(),
        shipmentDestinationMarkersMap: new Map(),
        currentVehicleAccordionId: null,
        currentServiceAccordionId: null,
        currentDeliveryAccordionId: null,
        currentShipmentAccordionId: null,
        currentLocationType: null,
        locationDecimals: 4,
        routeData: new Map(),
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

    function solve() {
        console.log('VehicleRoutingModule solve() called');

        const vehicleAccordion = document.getElementById('vehicle-list-accordion');
        const vehicleAccordionItems = vehicleAccordion.querySelectorAll('.accordion');
        const vehiclesArray = Array.from(vehicleAccordionItems).map(accordionItem => {
            const locationValue = accordionItem.querySelector('.vehicle-location').value;
            const [latitude, longitude] = locationValue.split(',');

            const vehicleInfo = {
                name: accordionItem.querySelector('.vehicle-name').value,
                registrationNumber: accordionItem.querySelector('.registration-number').value,
                capacity: parseInt(accordionItem.querySelector('.vehicle-capacity').value),
                location: {
                    latitude: parseFloat(latitude),
                    longitude: parseFloat(longitude)
                },
            };

            return vehicleInfo;
        });

        const serviceAccordion = document.getElementById('service-list-accordion');
        const serviceAccordionItems = serviceAccordion.querySelectorAll('.accordion');
        const servicesArray = Array.from(serviceAccordionItems).map(serviceAccordionItem => {
            const locationValue = serviceAccordionItem.querySelector('.service-location').value;
            const [latitude, longitude] = locationValue.split(',');

            const serviceInfo = {
                name: serviceAccordionItem.querySelector('.service-name').value,
                location: {
                    latitude: parseFloat(latitude),
                    longitude: parseFloat(longitude)
                },
            };

            return serviceInfo;
        });

        const deliveryAccordion = document.getElementById('delivery-list-accordion');
        const deliveryAccordionItems = deliveryAccordion.querySelectorAll('.accordion');
        const deliveriesArray = Array.from(deliveryAccordionItems).map(deliveryAccordionItem => {
            const locationValue = deliveryAccordionItem.querySelector('.delivery-location').value;
            const [latitude, longitude] = locationValue.split(',');

            const deliveryInfo = {
                name: deliveryAccordionItem.querySelector('.delivery-name').value,
                location: {
                    latitude: parseFloat(latitude),
                    longitude: parseFloat(longitude)
                },
            };

            return deliveryInfo;
        });

        const shipmentAccordion = document.getElementById('shipment-list-accordion');
        const shipmentAccordionItems = shipmentAccordion.querySelectorAll('.accordion');
        const shipmentsArray = Array.from(shipmentAccordionItems).map(shipmentAccordionItem => {
            const sourceLocationValue = shipmentAccordionItem.querySelector('.shipment-source-location').value;
            const destinationLocationValue = shipmentAccordionItem.querySelector('.shipment-destination-location').value;

            const [sourceLatitude, sourceLongitude] = sourceLocationValue.split(',');
            const [destinationLatitude, destinationLongitude] = destinationLocationValue.split(',');

            const shipmentInfo = {
                name: shipmentAccordionItem.querySelector('.shipment-name').value,
                sourceLocation: {
                    latitude: parseFloat(sourceLatitude),
                    longitude: parseFloat(sourceLongitude)
                },
                destinationLocation: {
                    latitude: parseFloat(destinationLatitude),
                    longitude: parseFloat(destinationLongitude)
                },
            };

            return shipmentInfo;
        });

        const vehicleRoutingInfo = {
            vehicles: vehiclesArray,
            services: servicesArray,
            deliveries: deliveriesArray,
            shipments: shipmentsArray,
        };

        console.log('vehicleRoutingInfo', vehicleRoutingInfo);

        globalState.routeData.clear();
        clearVehicleContent();
        clearPolyline();

        fetch(
            `${globalState.contextPath}/vehicle-routing-problem-solver`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',},
            body: JSON.stringify(vehicleRoutingInfo),
            })
            .then(response => response.json())
            .then(data => {
                console.log('Response from server:', data);
                parseRoutingResponse(data)
            })
            .catch(error => {
                console.error('Error:', error);
                aleat('Error occurred while solving vehicle routing problem');
            });
    }

    function parseRoutingResponse(data) {
        const solution = data.solution;
        for (const [vehicleName, data] of Object.entries(solution)) {
            addVehicleContent(vehicleName, data.polyline);
            globalState.routeData.set(vehicleName, data);
        }
    }

    function addVehicleContent(vehicleName, polyline) {
        console.log('VehicleRoutingModule addVehicleContent() called vehicleName', vehicleName, 'polyline', polyline);

        const routeListDiv = document.getElementById('route-list');

        const buttonWrapper = document.createElement('div');
        buttonWrapper.classList.add('mb-3');

        const vehicleButton = document.createElement('button');
        vehicleButton.textContent = vehicleName;
        vehicleButton.classList.add('btn', 'btn-block', 'btn-primary');
        vehicleButton.id = `vehicle-route-button-${vehicleName}`;
        vehicleButton.addEventListener('click', () => {
            console.log(`Button for ${vehicleName} clicked!`);
            showPolylineForVehicle(vehicleName);
        });

        buttonWrapper.appendChild(vehicleButton);

        routeListDiv.appendChild(buttonWrapper);
    }

    function clearVehicleContent() {
        const routeListDiv = document.getElementById('route-list');
    
        const vehicleButtons = routeListDiv.getElementsByClassName('btn');
        for (const vehicleButton of vehicleButtons) {
            // vehicleButton.removeEventListener('click', globalState.vehicleButtonListeners[vehicleButton.id]);
            routeListDiv.removeChild(vehicleButton.parentNode);
        }
    }

    function showPolylineForVehicle(vehicleName) {
        console.log('VehicleRoutingModule showPolylineForVehicle() called vehicleName', vehicleName);

        const routeData = globalState.routeData.get(vehicleName);
        const polyline = routeData.polyline;

        clearPolyline();

        const latLngs = polyline.map(point => L.latLng(point.latitude, point.longitude));
        const newPolyline = L.polyline(latLngs, { color: 'red' }).addTo(globalState.map);
        globalState.currentPolyline = newPolyline;
    }

    function clearPolyline() {
        if (globalState.currentPolyline) {
            globalState.map.removeLayer(globalState.currentPolyline);
            globalState.currentPolyline = null;
        }
    }

    function addVehicle() {
        console.log('VehicleRoutingModule addVehicle() called');

        const vehicleListAccordion = document.getElementById('vehicle-list-accordion');

        const uniqueIdCounter = vehicleListAccordion.childElementCount;

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

        fetch(`${globalState.contextPath}/html/add-vehicle-form-template.html`)
            .then(response => response.text())
            .then(htmlTemplate => {
                const filledTemplate = htmlTemplate.replace(/\${(.*?)}/g, (_, p1) => dynamicValues[p1.trim()]);

                const parser = new DOMParser();
                const doc = parser.parseFromString(filledTemplate, 'text/html');
                const fragment = doc.body;

                Array.from(fragment.children).forEach(child => {
                    vehicleListAccordion.appendChild(child.cloneNode(true));
                });

                const subAccordion = vehicleListAccordion.querySelector(`#${dynamicValues.vehicleSubAccordionId}`);

                subAccordion
                    .querySelector('.search-vehicle-button')
                    .addEventListener('click', handleVehicleSearchButtonClick);

                subAccordion
                    .querySelector('.search-location-button')
                    .addEventListener('click', handleVehicleLocationSearchButtonClick);

                subAccordion
                    .querySelector('.delete-vehicle-button')
                    .addEventListener('click', handleVehicleDeleteButtonClick);

                subAccordion
                    .querySelector('.vehicle-name')
                    .addEventListener('change', handleVehicleNameChange);

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

    function addService() {
        console.log('VehicleRoutingModule addService() called');

        const serviceListAccordion = document.getElementById('service-list-accordion');

        const uniqueIdCounter = serviceListAccordion.childElementCount;

        const dynamicValues = {
            serviceNameLabel: 'Name',
            serviceNamePlaceholder: 'Enter Service name',
            serviceSubAccordionId: `service-sub-accordion-${uniqueIdCounter}`,
            deleteIconPath: `${globalState.contextPath}/css/images/delete-icon.png`,
            markerIconPath: `${globalState.contextPath}/css/images/marker-icon.png`,
        };

        fetch(`${globalState.contextPath}/html/add-service-form-template.html`)
            .then(response => response.text())
            .then(htmlTemplate => {
                const filledTemplate = htmlTemplate.replace(/\${(.*?)}/g, (_, p1) => dynamicValues[p1.trim()]);

                const parser = new DOMParser();
                const doc = parser.parseFromString(filledTemplate, 'text/html');
                const fragment = doc.body;

                Array.from(fragment.children).forEach(child => {
                    serviceListAccordion.appendChild(child.cloneNode(true));
                });

                const subAccordion = serviceListAccordion.querySelector(`#${dynamicValues.serviceSubAccordionId}`);

                subAccordion
                    .querySelector('.search-location-button')
                    .addEventListener('click', handleServiceLocationSearchButtonClick);

                subAccordion
                    .querySelector('.delete-service-button')
                    .addEventListener('click', handleServiceDeleteButtonClick);

                subAccordion
                    .querySelector('.service-name')
                    .addEventListener('change', handleServiceNameChange);

                addMarkerOnMap(
                    globalState.map.getCenter(),
                    dynamicValues.serviceSubAccordionId,
                    dynamicValues.markerIconPath,
                    globalState.SERVICE_TYPE);

                syncMarkerValues();
                syncMarkerPopups();
            })
            .catch(error => console.error('Error loading template:', error));
    }

    function addDelivery() {
        console.log('VehicleRoutingModule addDelivery() called');

        const deliveryListAccordion = document.getElementById('delivery-list-accordion');

        const uniqueIdCounter = deliveryListAccordion.childElementCount;

        const dynamicValues = {
            deliveryNameLabel: 'Name',
            deliveryNamePlaceholder: 'Enter Delivery name',
            deliverySubAccordionId: `delivery-sub-accordion-${uniqueIdCounter}`,
            deleteIconPath: `${globalState.contextPath}/css/images/delete-icon.png`,
            markerIconPath: `${globalState.contextPath}/css/images/marker-icon.png`,
        };

        fetch(`${globalState.contextPath}/html/add-delivery-form-template.html`)
            .then(response => response.text())
            .then(htmlTemplate => {
                const filledTemplate = htmlTemplate.replace(/\${(.*?)}/g, (_, p1) => dynamicValues[p1.trim()]);

                const parser = new DOMParser();
                const doc = parser.parseFromString(filledTemplate, 'text/html');
                const fragment = doc.body;

                Array.from(fragment.children).forEach(child => {
                    deliveryListAccordion.appendChild(child.cloneNode(true));
                });

                const subAccordion = deliveryListAccordion.querySelector(`#${dynamicValues.deliverySubAccordionId}`);

                subAccordion
                    .querySelector('.search-location-button')
                    .addEventListener('click', handleDeliveryLocationSearchButtonClick);

                subAccordion
                    .querySelector('.delete-delivery-button')
                    .addEventListener('click', handleDeliveryDeleteButtonClick);

                subAccordion
                    .querySelector('.delivery-name')
                    .addEventListener('change', handleDeliveryNameChange);

                addMarkerOnMap(
                    globalState.map.getCenter(),
                    dynamicValues.deliverySubAccordionId,
                    dynamicValues.markerIconPath,
                    globalState.DELIVERY_TYPE);

                syncMarkerValues();
                syncMarkerPopups();
            })
            .catch(error => console.error('Error loading template:', error));
    }

    function addShipment() {
        console.log('VehicleRoutingModule addshipment() called');

        const shipmentListAccordion = document.getElementById('shipment-list-accordion');

        const uniqueIdCounter = shipmentListAccordion.childElementCount;

        const dynamicValues = {
            shipmentNameLabel: 'Name',
            shipmentNamePlaceholder: 'Enter Shipment name',
            shipmentSubAccordionId: `shipment-sub-accordion-${uniqueIdCounter}`,
            deleteIconPath: `${globalState.contextPath}/css/images/delete-icon.png`,
            markerIconPath: `${globalState.contextPath}/css/images/marker-icon.png`,
        };

        fetch(`${globalState.contextPath}/html/add-shipment-form-template.html`)
            .then(response => response.text())
            .then(htmlTemplate => {
                const filledTemplate = htmlTemplate.replace(/\${(.*?)}/g, (_, p1) => dynamicValues[p1.trim()]);

                const parser = new DOMParser();
                const doc = parser.parseFromString(filledTemplate, 'text/html');
                const fragment = doc.body;

                Array.from(fragment.children).forEach(child => {
                    shipmentListAccordion.appendChild(child.cloneNode(true));
                });

                const subAccordion = shipmentListAccordion.querySelector(`#${dynamicValues.shipmentSubAccordionId}`);

                subAccordion
                    .querySelector('.search-source-location-button')
                    .addEventListener('click', handleShipmentSourceLocationSearchButtonClick);

                subAccordion
                    .querySelector('.search-destination-location-button')
                    .addEventListener('click', handleShipmentDestinationLocationSearchButtonClick);

                subAccordion
                    .querySelector('.delete-shipment-button')
                    .addEventListener('click', handleShipmentDeleteButtonClick);

                subAccordion
                    .querySelector('.shipment-name')
                    .addEventListener('change', handleShipmentNameChange);

                addMarkerOnMap(
                    globalState.map.getCenter(),
                    dynamicValues.shipmentSubAccordionId,
                    dynamicValues.markerIconPath,
                    globalState.SHIPMENT_SOURCE_TYPE);

                addMarkerOnMap(
                    globalState.map.getCenter(),
                    dynamicValues.shipmentSubAccordionId,
                    dynamicValues.markerIconPath,
                    globalState.SHIPMENT_DESTINATION_TYPE);

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

        globalState.serviceMarkersMap.forEach((marker, id) => {
            const newLatLng = marker.getLatLng();
            const roundedLatLng = {
                lat: newLatLng.lat.toFixed(globalState.locationDecimals),
                lng: newLatLng.lng.toFixed(globalState.locationDecimals),
            };

            const locationString = `${roundedLatLng.lat},${roundedLatLng.lng}`;

            const serviceAccordion = document.getElementById(id);
            const locationInput = serviceAccordion.querySelector('.service-location');
            locationInput.value = locationString;
        });

        globalState.deliveryMarkersMap.forEach((marker, id) => {
            const newLatLng = marker.getLatLng();
            const roundedLatLng = {
                lat: newLatLng.lat.toFixed(globalState.locationDecimals),
                lng: newLatLng.lng.toFixed(globalState.locationDecimals),
            };

            const locationString = `${roundedLatLng.lat},${roundedLatLng.lng}`;

            const deliveryAccordion = document.getElementById(id);
            const locationInput = deliveryAccordion.querySelector('.delivery-location');
            locationInput.value = locationString;
        });

        globalState.shipmentSourceMarkersMap.forEach((marker, id) => {
            const newLatLng = marker.getLatLng();
            const roundedLatLng = {
                lat: newLatLng.lat.toFixed(globalState.locationDecimals),
                lng: newLatLng.lng.toFixed(globalState.locationDecimals),
            };

            const locationString = `${roundedLatLng.lat},${roundedLatLng.lng}`;

            const shipmentAccordion = document.getElementById(id);
            const locationInput = shipmentAccordion.querySelector('.shipment-source-location');
            locationInput.value = locationString;
        });

        globalState.shipmentDestinationMarkersMap.forEach((marker, id) => {
            const newLatLng = marker.getLatLng();
            const roundedLatLng = {
                lat: newLatLng.lat.toFixed(globalState.locationDecimals),
                lng: newLatLng.lng.toFixed(globalState.locationDecimals),
            };

            const locationString = `${roundedLatLng.lat},${roundedLatLng.lng}`;

            const shipmentAccordion = document.getElementById(id);
            const locationInput = shipmentAccordion.querySelector('.shipment-destination-location');
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

        globalState.serviceMarkersMap.forEach((marker, id) => {
            const serviceAccordion = document.getElementById(id);
            const serviceName = serviceAccordion.querySelector('.service-name').value;

            const popupContent = `<strong>${serviceName}</strong>`;
            marker.bindPopup(popupContent);
        });

        globalState.deliveryMarkersMap.forEach((marker, id) => {
            const deliveryAccordion = document.getElementById(id);
            const deliveryName = deliveryAccordion.querySelector('.delivery-name').value;

            const popupContent = `<strong>${deliveryName}</strong>`;
            marker.bindPopup(popupContent);
        });

        globalState.shipmentSourceMarkersMap.forEach((marker, id) => {
            const shipmentAccordion = document.getElementById(id);
            const shipmentName = shipmentAccordion.querySelector('.shipment-name').value;

            const popupContent = `<strong>${shipmentName} - Source </strong>`;
            marker.bindPopup(popupContent);
        });

        globalState.shipmentDestinationMarkersMap.forEach((marker, id) => {
            const shipmentAccordion = document.getElementById(id);
            const shipmentName = shipmentAccordion.querySelector('.shipment-name').value;

            const popupContent = `<strong>${shipmentName} - Destination </strong>`;
            marker.bindPopup(popupContent);
        });
    }

    function handleVehicleSearchButtonClick() {
        console.log('handleVehicleSearchButtonClick callback');
        const vehicleSubAccordionId = this.closest('.vehicle-sub-accordion').id;

        globalState.currentVehicleAccordionId = vehicleSubAccordionId;

        console.log('vehicleSubAccordionId', vehicleSubAccordionId);
    }

    function handleVehicleLocationSearchButtonClick() {
        console.log('Location search button callback clicked');
        const vehicleSubAccordionId = this.closest('.vehicle-sub-accordion').id;
        console.log('vehicleSubAccordionId', vehicleSubAccordionId);

        globalState.currentVehicleAccordionId = vehicleSubAccordionId;
        globalState.currentLocationType = globalState.VEHICLE_TYPE;
    }

    function handleServiceLocationSearchButtonClick() {
        console.log('Location search button callback clicked');
        const serviceSubAccordionId = this.closest('.service-sub-accordion').id;
        console.log('serviceSubAccordionId', serviceSubAccordionId);

        globalState.currentServiceAccordionId = serviceSubAccordionId;
        globalState.currentLocationType = globalState.SERVICE_TYPE;
    }

    function handleDeliveryLocationSearchButtonClick() {
        console.log('Delivery Location search button callback clicked');
        const deliverySubAccordionId = this.closest('.delivery-sub-accordion').id;
        console.log('deliverySubAccordionId', deliverySubAccordionId);

        globalState.currentDeliveryAccordionId = deliverySubAccordionId;
        globalState.currentLocationType = globalState.DELIVERY_TYPE;
    }

    function handleShipmentSourceLocationSearchButtonClick() {
        console.log('Shipment Source Location search button callback clicked');
        const shipmentSubAccordionId = this.closest('.shipment-sub-accordion').id;
        console.log('shipmentSubAccordionId', shipmentSubAccordionId);

        globalState.currentShipmentAccordionId = shipmentSubAccordionId;
        globalState.currentLocationType = globalState.SHIPMENT_SOURCE_TYPE;
    }

    function handleShipmentDestinationLocationSearchButtonClick() {
        console.log('Shipment Destination Location search button callback clicked');
        const shipmentSubAccordionId = this.closest('.shipment-sub-accordion').id;
        console.log('shipmentSubAccordionId', shipmentSubAccordionId);

        globalState.currentShipmentAccordionId = shipmentSubAccordionId;
        globalState.currentLocationType = globalState.SHIPMENT_DESTINATION_TYPE;
    }

    function handleVehicleDeleteButtonClick() {
        console.log('Delete button callback clicked');
        const vehicleSubAccordionId = this.closest('.vehicle-sub-accordion').id;
        deleteVehicle(vehicleSubAccordionId);
    }

    function handleServiceDeleteButtonClick() {
        console.log('Service Delete button callback clicked');
        const serviceSubAccordionId = this.closest('.service-sub-accordion').id;
        deleteService(serviceSubAccordionId);
    }

    function handleDeliveryDeleteButtonClick() {
        console.log('Delivery Delete button callback clicked');
        const deliverySubAccordionId = this.closest('.delivery-sub-accordion').id;
        deleteDelivery(deliverySubAccordionId);
    }

    function handleShipmentDeleteButtonClick() {
        console.log('Shipment Delete button callback clicked');
        const shipmentSubAccordionId = this.closest('.shipment-sub-accordion').id;
        deleteShipment(shipmentSubAccordionId);
    }

    function searchVehicle() {
        console.log('VehicleRoutingModule searchVehicle() called');

        const vehicleSearchInput = document.getElementById('vehicle-search-input');
        const query = vehicleSearchInput.value;

        fetch(`${globalState.contextPath}/vehicle?query=${query}`)
            .then(response => response.json())
            .then(data => {
                handleVehicleSearchResults(data);
            })
            .catch(error => console.error('Error searching vehicle location:', error));
    }

    function searchLocation() {
        console.log('VehicleRoutingModule searchLocation() called');

        const locationSearchInput = document.getElementById('location-search-input');
        const query = locationSearchInput.value;

        fetch(`${globalState.contextPath}/location?query=${query}`)
            .then(response => response.json())
            .then(data => {
                handleLocationSearchResults(data);
            })
            .catch(error => console.error('Error searching vehicle location:', error));
    }

    function handleVehicleSearchResults(results) {
        const dropdownContainer = document.getElementById('searchVehicleDropdown');
        dropdownContainer.innerHTML = '';

        results.forEach(result => {
            const dropdownItem = document.createElement('div');
            dropdownItem.classList.add('hoverable');
            dropdownItem.classList.add('border');
            dropdownItem.classList.add('border-info');
            dropdownItem.classList.add('p-2');
            dropdownItem.classList.add('mb-2');

            dropdownItem.textContent = `${result.name} - ${result.registration} - Capacity: ${result.capacity}`;

            dropdownItem.addEventListener('click', () => {
                handleVehicleDropdownItemClick(result);
            });

            dropdownContainer.appendChild(dropdownItem);
        });
    }

    function handleLocationSearchResults(results) {
        const dropdownContainer = document.getElementById('searchLocationDropdown');
        dropdownContainer.innerHTML = '';

        results.forEach(result => {
            const dropdownItem = document.createElement('div');
            dropdownItem.classList.add('hoverable');
            dropdownItem.classList.add('border');
            dropdownItem.classList.add('border-info');
            dropdownItem.classList.add('p-2');
            dropdownItem.classList.add('mb-2');

            dropdownItem.textContent = `${result.name} (${result.latitude},${result.longitude})`;

            dropdownItem.addEventListener('click', () => {
                handleLocationDropdownItemClick(result);
            });

            dropdownContainer.appendChild(dropdownItem);
        });
    }

    function handleVehicleDropdownItemClick(selectedItem) {
        console.log('Selected Vehicle:', selectedItem);

        const vehicleSubAccordionId = globalState.currentVehicleAccordionId;
        const vehicleAccordion = document.getElementById(vehicleSubAccordionId);
        vehicleAccordion.querySelector('.vehicle-name').value = selectedItem.name;
        vehicleAccordion.querySelector('.registration-number').value = selectedItem.registrationNumber;
        vehicleAccordion.querySelector('.vehicle-capacity').value = selectedItem.capacity;

        const parent = vehicleAccordion.closest('.accordion-item');
        parent.querySelector('.vehicle-header-text').innerHTML = selectedItem.name;

        syncMarkerPopups();
    }

    function handleLocationDropdownItemClick(selectedItem) {
        console.log('Selected Location:', selectedItem);

        switch (globalState.currentLocationType) {
            case globalState.VEHICLE_TYPE:
                handleVehicleLocationDropdownItemClick(selectedItem);
                break;

            case globalState.SERVICE_TYPE:
                handleServiceLocationDropdownItemClick(selectedItem);
                break;

            case globalState.DELIVERY_TYPE:
                handleDeliveryLocationDropdownItemClick(selectedItem);
                break;

            case globalState.SHIPMENT_SOURCE_TYPE:
                handleShipmentSourceLocationDropdownItemClick(selectedItem);
                break;

            case globalState.SHIPMENT_DESTINATION_TYPE:
                handleShipmentDestinationLocationDropdownItemClick(selectedItem);
                break;
        }
    }

    function handleVehicleLocationDropdownItemClick(selectedItem) {
        console.log('handleVehicleLocationDropdownItemClick Location:', selectedItem);

        const vehicleSubAccordionId = globalState.currentVehicleAccordionId;
        const vehicleAccordion = document.getElementById(vehicleSubAccordionId);
        vehicleAccordion.querySelector('.vehicle-location').value = `${selectedItem.latitude},${selectedItem.longitude}`;

        globalState
            .vehicleMarkersMap
            .get(vehicleSubAccordionId)
            .setLatLng([selectedItem.latitude, selectedItem.longitude]);
    }

    function handleServiceLocationDropdownItemClick(selectedItem) {
        console.log('handleServiceLocationDropdownItemClick Location:', selectedItem);

        const serviceSubAccordionId = globalState.currentServiceAccordionId;
        const serviceAccordion = document.getElementById(serviceSubAccordionId);
        serviceAccordion.querySelector('.service-location').value = `${selectedItem.latitude},${selectedItem.longitude}`;

        globalState
            .serviceMarkersMap
            .get(serviceSubAccordionId)
            .setLatLng([selectedItem.latitude, selectedItem.longitude]);
    }

    function handleDeliveryLocationDropdownItemClick(selectedItem) {
        console.log('handleDeliveryLocationDropdownItemClick Location:', selectedItem);

        const deliverySubAccordionId = globalState.currentDeliveryAccordionId;
        const deliveryAccordion = document.getElementById(deliverySubAccordionId);
        deliveryAccordion.querySelector('.delivery-location').value = `${selectedItem.latitude},${selectedItem.longitude}`;

        globalState
            .deliveryMarkersMap
            .get(deliverySubAccordionId)
            .setLatLng([selectedItem.latitude, selectedItem.longitude]);
    }

    function handleShipmentSourceLocationDropdownItemClick(selectedItem) {
        console.log('handleShipmentSourceLocationDropdownItemClick Location:', selectedItem);

        const sourceSubAccordionId = globalState.currentShipmentAccordionId;
        const sourceAccordion = document.getElementById(sourceSubAccordionId);
        sourceAccordion.querySelector('.shipment-source-location').value = `${selectedItem.latitude},${selectedItem.longitude}`;

        globalState
            .shipmentSourceMarkersMap
            .get(sourceSubAccordionId)
            .setLatLng([selectedItem.latitude, selectedItem.longitude]);
    }

    function handleShipmentDestinationLocationDropdownItemClick(selectedItem) {
        console.log('handleShipmentDestinationLocationDropdownItemClick Location:', selectedItem);

        const destinationSubAccordionId = globalState.currentShipmentAccordionId;
        const destinationAccordion = document.getElementById(destinationSubAccordionId);
        destinationAccordion.querySelector('.shipment-destination-location').value = `${selectedItem.latitude},${selectedItem.longitude}`;

        globalState
            .shipmentDestinationMarkersMap
            .get(destinationSubAccordionId)
            .setLatLng([selectedItem.latitude, selectedItem.longitude]);
    }

    function handleVehicleNameChange() {
        console.log('Vehicle name changed');
        const accordionItem = this.closest('.accordion-item');
        const vehicleAccordionHeader = accordionItem.querySelector('.vehicle-accordion-header');

        const vehicleName = this.value;
        console.log('Vehicle name:', vehicleName);

        vehicleAccordionHeader.querySelector('.vehicle-header-text').innerHTML = vehicleName;

        syncMarkerPopups();
    }

    function handleServiceNameChange() {
        console.log('Service name changed');
        const accordionItem = this.closest('.accordion-item');
        const serviceAccordionHeader = accordionItem.querySelector('.service-accordion-header');

        const serviceName = this.value;
        console.log('Service name:', serviceName);

        serviceAccordionHeader.querySelector('.service-header-text').innerHTML = serviceName;

        syncMarkerPopups();
    }

    function handleDeliveryNameChange() {
        console.log('Delivery name changed');
        const accordionItem = this.closest('.accordion-item');
        const deliveryAccordionHeader = accordionItem.querySelector('.delivery-accordion-header');

        const deliveryName = this.value;
        console.log('Delivery name:', deliveryName);

        deliveryAccordionHeader.querySelector('.delivery-header-text').innerHTML = deliveryName;

        syncMarkerPopups();
    }

    function handleShipmentNameChange() {
        console.log('Delivery name changed');
        const accordionItem = this.closest('.accordion-item');
        const shipmentAccordionHeader = accordionItem.querySelector('.shipment-accordion-header');

        const shipmentName = this.value;
        console.log('Shipment name:', shipmentName);

        shipmentAccordionHeader.querySelector('.shipment-header-text').innerHTML = shipmentName;

        syncMarkerPopups();
    }

    function addMarkerOnMap(location, id, iconPath, type) {
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

            case globalState.SERVICE_TYPE:
                console.log('updating serviceMarkersMap');
                globalState.serviceMarkersMap.set(id, marker);
                break;

            case globalState.DELIVERY_TYPE:
                console.log('updating deliveryMarkersMap');
                globalState.deliveryMarkersMap.set(id, marker);
                break;

            case globalState.SHIPMENT_SOURCE_TYPE:
                console.log('updating source shipmentMarkersMap');
                globalState.shipmentSourceMarkersMap.set(id, marker);
                break;

            case globalState.SHIPMENT_DESTINATION_TYPE:
                console.log('updating destination shipmentMarkersMap');
                globalState.shipmentDestinationMarkersMap.set(id, marker);
                break;
        }
    }

    function handleMarkerDragEnd(event) {
        syncMarkerValues();
    }

    function attachListeners() {
        const solveButton = document.getElementById('solve');
        if (solveButton) {
            solveButton.addEventListener('click', solve);
        }

        const addVehicleButton = document.getElementById('add-vehicle-button');
        if (addVehicleButton) {
            addVehicleButton.addEventListener('click', addVehicle);
        }

        const addServiceButton = document.getElementById('add-service-button');
        if (addServiceButton) {
            addServiceButton.addEventListener('click', addService);
        }

        const addDeliveryButton = document.getElementById('add-delivery-button');
        if (addDeliveryButton) {
            addDeliveryButton.addEventListener('click', addDelivery);
        }

        const addShipmentButton = document.getElementById('add-shipment-button');
        if (addShipmentButton) {
            addShipmentButton.addEventListener('click', addShipment);
        }
    }

    function deleteVehicle(vehicleSubAccordionId) {
        console.log('VehicleRoutingModule deleteVehicle() called with vehicleSubAccordionId', vehicleSubAccordionId);

        const accordionItem = document.getElementById(vehicleSubAccordionId);
        const parent = accordionItem.closest('.accordion');
        if (!parent) {
            console.warn('Accordion item not found for deletion.');
            return;
        }

        const searchVehicleButton = accordionItem.querySelector('.search-vehicle-button');
        searchVehicleButton.removeEventListener('click', handleVehicleSearchButtonClick);

        const searchLocationButton = accordionItem.querySelector('.search-location-button');
        searchLocationButton.removeEventListener('click', handleVehicleLocationSearchButtonClick);

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

    function deleteService(serviceSubAccordionId) {
        console.log('VehicleRoutingModule deleteService() called with serviceSubAccordionId', serviceSubAccordionId);

        const accordionItem = document.getElementById(serviceSubAccordionId);
        const parent = accordionItem.closest('.accordion');
        if (!parent) {
            console.warn('Accordion item not found for deletion.');
            return;
        }

        const searchLocationButton = accordionItem.querySelector('.search-location-button');
        searchLocationButton.removeEventListener('click', handleServiceLocationSearchButtonClick);

        const deleteButton = accordionItem.querySelector('.delete-service-button');
        deleteButton.removeEventListener('click', handleServiceDeleteButtonClick);

        const serviceNameInput = accordionItem.querySelector('.service-name');
        serviceNameInput.removeEventListener('change', handleServiceNameChange);

        parent.remove();

        const marker = globalState.serviceMarkersMap.get(serviceSubAccordionId);
        marker.remove();
        globalState.serviceMarkersMap.delete(serviceSubAccordionId);

        updateServiceAccordionItems();

        syncMarkerMap(globalState.SERVICE_TYPE);
    }

    function deleteDelivery(deliverySubAccordionId) {
        console.log('VehicleRoutingModule deleteDelivery() called with deliverySubAccordionId', deliverySubAccordionId);

        const accordionItem = document.getElementById(deliverySubAccordionId);
        const parent = accordionItem.closest('.accordion');
        if (!parent) {
            console.warn('Accordion item not found for deletion.');
            return;
        }

        const searchLocationButton = accordionItem.querySelector('.search-location-button');
        searchLocationButton.removeEventListener('click', handleServiceLocationSearchButtonClick);

        const deleteButton = accordionItem.querySelector('.delete-delivery-button');
        deleteButton.removeEventListener('click', handleDeliveryDeleteButtonClick);

        const deliveryNameInput = accordionItem.querySelector('.delivery-name');
        deliveryNameInput.removeEventListener('change', handleDeliveryNameChange);

        parent.remove();

        const marker = globalState.deliveryMarkersMap.get(deliverySubAccordionId);
        marker.remove();
        globalState.deliveryMarkersMap.delete(deliverySubAccordionId);

        updateDeliveryAccordionItems();

        syncMarkerMap(globalState.DELIVERY_TYPE);
    }

    function deleteShipment(shipmentSubAccordionId) {
        console.log('VehicleRoutingModule deleteShipment() called with shipmentSubAccordionId', shipmentSubAccordionId);

        const accordionItem = document.getElementById(shipmentSubAccordionId);
        const parent = accordionItem.closest('.accordion');
        if (!parent) {
            console.warn('Accordion item not found for deletion.');
            return;
        }

        const searchSourceLocationButton = accordionItem.querySelector('.search-source-location-button');
        searchSourceLocationButton.removeEventListener('click', handleShipmentSourceLocationSearchButtonClick);

        const searchDestinationLocationButton = accordionItem.querySelector('.search-destination-location-button');
        searchDestinationLocationButton.removeEventListener('click', handleShipmentDestinationLocationSearchButtonClick);

        const deleteButton = accordionItem.querySelector('.delete-shipment-button');
        deleteButton.removeEventListener('click', handleShipmentDeleteButtonClick);

        const shipmentNameInput = accordionItem.querySelector('.shipment-name');
        shipmentNameInput.removeEventListener('change', handleShipmentNameChange);

        parent.remove();

        const sourceMarker = globalState.shipmentSourceMarkersMap.get(shipmentSubAccordionId);
        sourceMarker.remove();
        globalState.shipmentSourceMarkersMap.delete(shipmentSubAccordionId);

        const destinationMarker = globalState.shipmentDestinationMarkersMap.get(shipmentSubAccordionId);
        destinationMarker.remove();
        globalState.shipmentDestinationMarkersMap.delete(shipmentSubAccordionId);

        updateShipmentAccordionItems();

        syncMarkerMap(globalState.SHIPMENT_SOURCE_TYPE);
        syncMarkerMap(globalState.SHIPMENT_DESTINATION_TYPE);
    }

    function syncMarkerMap(type) {
        switch (type) {
            case globalState.VEHICLE_TYPE:
                syncVehicleMarkerMap();
                break;

            case globalState.SERVICE_TYPE:
                syncServiceMarkerMap();
                break;

            case globalState.DELIVERY_TYPE:
                syncDeliveryMarkerMap();
                break;

            case globalState.SHIPMENT_SOURCE_TYPE:
                syncShipmentSourceMarkerMap();
                break;

            case globalState.SHIPMENT_DESTINATION_TYPE:
                syncShipmentDestinationMarkerMap();
                break;
        }
    }

    function syncVehicleMarkerMap() {
        console.log('VehicleRoutingModule syncVehicleMarkerMap() called');

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

            if (currentIndex > deleteIndex) {
                const newKey = `vehicle-sub-accordion-${newIndex}`;
                updatedMarkersMap.set(newKey, marker);
            } else {
                updatedMarkersMap.set(id, marker);
            }
        });

        globalState.vehicleMarkersMap = updatedMarkersMap;
    }

    function syncServiceMarkerMap() {
        console.log('VehicleRoutingModule syncServiceMarkerMap() called');

        let deleteIndex = 0;
        globalState.serviceMarkersMap.forEach((marker, id) => {
            const serviceAccordion = document.getElementById(id);
            if (!serviceAccordion) {
                return;
            }

            deleteIndex++;
        });

        console.log('deleteIndex', deleteIndex);

        const updatedMarkersMap = new Map();
        globalState.serviceMarkersMap.forEach((marker, id) => {
            const currentIndex = parseInt(id.split('-').pop());
            const newIndex = currentIndex - 1;

            if (currentIndex > deleteIndex) {
                const newKey = `service-sub-accordion-${newIndex}`;
                updatedMarkersMap.set(newKey, marker);
            } else {
                updatedMarkersMap.set(id, marker);
            }
        });

        globalState.serviceMarkersMap = updatedMarkersMap;
    }

    function syncDeliveryMarkerMap() {
        console.log('VehicleRoutingModule syncDeliveryMarkerMap() called');

        let deleteIndex = 0;
        globalState.deliveryMarkersMap.forEach((marker, id) => {
            const deliveryAccordion = document.getElementById(id);
            if (!deliveryAccordion) {
                return;
            }

            deleteIndex++;
        });

        console.log('deleteIndex', deleteIndex);

        const updatedMarkersMap = new Map();
        globalState.deliveryMarkersMap.forEach((marker, id) => {
            const currentIndex = parseInt(id.split('-').pop());
            const newIndex = currentIndex - 1;

            if (currentIndex > deleteIndex) {
                const newKey = `delivery-sub-accordion-${newIndex}`;
                updatedMarkersMap.set(newKey, marker);
            } else {
                updatedMarkersMap.set(id, marker);
            }
        });

        globalState.deliveryMarkersMap = updatedMarkersMap;
    }

    function syncShipmentSourceMarkerMap() {
        console.log('VehicleRoutingModule syncShipmentSourceMarkerMap() called');

        let deleteIndex = 0;
        globalState.shipmentSourceMarkersMap.forEach((marker, id) => {
            const shipmentAccordion = document.getElementById(id);
            if (!shipmentAccordion) {
                return;
            }

            deleteIndex++;
        });

        console.log('deleteIndex', deleteIndex);

        const updatedMarkersMap = new Map();
        globalState.shipmentSourceMarkersMap.forEach((marker, id) => {
            const currentIndex = parseInt(id.split('-').pop());
            const newIndex = currentIndex - 1;

            if (currentIndex > deleteIndex) {
                const newKey = `shipment-sub-accordion-${newIndex}`;
                updatedMarkersMap.set(newKey, marker);
            } else {
                updatedMarkersMap.set(id, marker);
            }
        });

        globalState.shipmentSourceMarkersMap = updatedMarkersMap;
    }

    function syncShipmentDestinationMarkerMap() {
        console.log('VehicleRoutingModule syncShipmentDestinationMarkerMap() called');

        let deleteIndex = 0;
        globalState.shipmentDestinationMarkersMap.forEach((marker, id) => {
            const shipmentAccordion = document.getElementById(id);
            if (!shipmentAccordion) {
                return;
            }

            deleteIndex++;
        });

        console.log('deleteIndex', deleteIndex);

        const updatedMarkersMap = new Map();
        globalState.shipmentDestinationMarkersMap.forEach((marker, id) => {
            const currentIndex = parseInt(id.split('-').pop());
            const newIndex = currentIndex - 1;

            if (currentIndex > deleteIndex) {
                const newKey = `shipment-sub-accordion-${newIndex}`;
                updatedMarkersMap.set(newKey, marker);
            } else {
                updatedMarkersMap.set(id, marker);
            }
        });

        globalState.shipmentDestinationMarkersMap = updatedMarkersMap;
    }

    function updateVehicleAccordionItems() {
        const accordionContainer = document.getElementById('vehicle-list-accordion');

        const accordionItems = accordionContainer.querySelectorAll('.accordion');

        accordionItems.forEach((accordionItem, index) => {
            const newId = `vehicle-sub-accordion-${index}`;

            const accordionButton = accordionItem.querySelector('.accordion-button');
            const accordionCollapse = accordionItem.querySelector('.accordion-collapse');

            accordionButton.dataset.bsTarget = `#${newId}`;
            accordionCollapse.id = newId;
        });
    }

    function updateServiceAccordionItems() {
        const accordionContainer = document.getElementById('service-list-accordion');

        const accordionItems = accordionContainer.querySelectorAll('.accordion');

        accordionItems.forEach((accordionItem, index) => {
            const newId = `service-sub-accordion-${index}`;

            const accordionButton = accordionItem.querySelector('.accordion-button');
            const accordionCollapse = accordionItem.querySelector('.accordion-collapse');

            accordionButton.dataset.bsTarget = `#${newId}`;
            accordionCollapse.id = newId;
        });
    }

    function updateDeliveryAccordionItems() {
        const accordionContainer = document.getElementById('delivery-list-accordion');

        const accordionItems = accordionContainer.querySelectorAll('.accordion');

        accordionItems.forEach((accordionItem, index) => {
            const newId = `delivery-sub-accordion-${index}`;

            const accordionButton = accordionItem.querySelector('.accordion-button');
            const accordionCollapse = accordionItem.querySelector('.accordion-collapse');

            accordionButton.dataset.bsTarget = `#${newId}`;
            accordionCollapse.id = newId;
        });
    }

    function updateShipmentAccordionItems() {
        const accordionContainer = document.getElementById('shipment-list-accordion');

        const accordionItems = accordionContainer.querySelectorAll('.accordion');

        accordionItems.forEach((accordionItem, index) => {
            const newId = `shipment-sub-accordion-${index}`;

            const accordionButton = accordionItem.querySelector('.accordion-button');
            const accordionCollapse = accordionItem.querySelector('.accordion-collapse');

            accordionButton.dataset.bsTarget = `#${newId}`;
            accordionCollapse.id = newId;
        });
    }

    return {
        init: init,
        attachListeners: attachListeners,
        searchVehicle: searchVehicle,
        searchLocation: searchLocation,
    };
})();
