<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Vehicle Routing</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vehicle-routing.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leaflet.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    </head>

    <body>
        <script src="${pageContext.request.contextPath}/js/vehicle-routing.js"></script>
        <script src="${pageContext.request.contextPath}/js/leaflet.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/Polyline.encoded.js"></script>

        <nav class="navbar navbar-expand-sm navbar-dark bg-dark p-0">
            <div class="container-fluid p-4">
                <span class="navbar-brand">DriveSync</span>

                <!-- Vehicle Button -->
                <a href="${pageContext.request.contextPath}/vehicle">
                    <button type="button" class="btn btn-info me-2">Vehicle</button>
                </a>

                <!-- Location Button -->
                <a href="${pageContext.request.contextPath}/location">
                    <button type="button" class="btn btn-primary me-2">Location</button>
                </a>

                <!-- Vehicle Route Planning Button -->
                <a>
                    <button type="button" class="btn btn-success">Routing</button>
                </a>

                <!-- Logout Button  -->
                <a href="${pageContext.request.contextPath}/logout">
                    <span class="btn btn-danger navbar-brand float-end">Logout</span>
                </a>
            </div>
        </nav>

        <div class="container-fluid">
            <div class="row" id="control-center">
                <div class="col-12 p-2 pe-4">
                    <button 
                        class="d-inline-block btn btn-primary float-end" 
                        id="solve">
                        Solve
                    </button>
                </div>
            </div>

            <div class="row p-2">
                <div class="col-md-5">
                    <div class="accordion" id="accordion-panel">
                        <!-- Vehicles Accordiong -->
                        <div class="accordion-item">
                            <div class="accordion-header">
                                <button 
                                    class="accordion-button" 
                                    type="button" 
                                    data-bs-toggle="collapse" 
                                    data-bs-target="#vehicleAccordion" 
                                    aria-expanded="true" 
                                    aria-controls="vehicleAccordion">
                                    Vehicles
                                </button>
                            </div>

                            <div 
                                id="vehicleAccordion" 
                                class="accordion-collapse collapse" 
                                aria-labelledby="vehicleAccordion" 
                                data-bs-parent="#vehicle-accordion-panel">
                                <div class="accordion-body">
                                    <div 
                                        class="btn btn-link" 
                                        type="button"
                                        id="add-vehicle-button">
                                        Add Vehicle
                                    </div>

                                    <div id="vehicle-list-accordion">
                                        
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Services Accordion -->
                        <div class="accordion-item mt-3">
                            <div class="accordion-header">
                                <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#servicesAccordion" aria-expanded="true" aria-controls="servicesAccordion">
                                    Services
                                </button>
                            </div>
                            <div id="servicesAccordion" class="accordion-collapse collapse" aria-labelledby="servicesAccordion"
                                data-bs-parent="#services-accordion-panel">
                                <div class="accordion-body">
                                    <div class="btn btn-link" type="button" id="add-service-button">
                                        Add Service
                                    </div>

                                    <div id="service-list-accordion">
                                        
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Deliveries Accordion -->
                        <div class="accordion-item mt-3">
                            <div class="accordion-header">
                                <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#deliveriesAccordion" aria-expanded="true" aria-controls="deliveriesAccordion">
                                    Deliveries
                                </button>
                            </div>
                            <div id="deliveriesAccordion" class="accordion-collapse collapse" aria-labelledby="deliveriesAccordion"
                                data-bs-parent="#deliveries-accordion-panel">
                                <div class="accordion-body">
                                    <div class="btn btn-link" type="button" id="add-delivery-button">
                                        Add Delivery
                                    </div>

                                    <div id="delivery-list-accordion">
                                        <!-- Add content for the list of deliveries here -->
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Shipments Accordion -->
                        <div class="accordion-item mt-3">
                            <div class="accordion-header">
                                <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#shipmentsAccordion" aria-expanded="true" aria-controls="shipmentsAccordion">
                                    Shipments
                                </button>
                            </div>
                            <div id="shipmentsAccordion" class="accordion-collapse collapse" aria-labelledby="shipmentsAccordion"
                                data-bs-parent="#shipments-accordion-panel">
                                <div class="accordion-body">
                                    <div class="btn btn-link" type="button" id="add-shipment-button">
                                        Add Shipment
                                    </div>

                                    <div id="shipment-list-accordion">
                                        <!-- Add content for the list of shipments here -->
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Routes Accordion -->
                        <div class="accordion-item mt-3">
                            <div class="accordion-header">
                                <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#routesAccordion" aria-expanded="true" aria-controls="routesAccordion">
                                    Routes
                                </button>
                            </div>
                            <div id="routesAccordion" class="accordion-collapse collapse" aria-labelledby="routesAccordion"
                                data-bs-parent="#routes-accordion-panel">
                                <div class="accordion-body">
                                    <div id="route-list">
                                        <!-- Add content for the list of routes here -->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>                  
                </div>

                <div class="col-md-7">
                    <div id="map"></div>
                </div>
            </div>
        </div>
        
        <script>
            const vehicleRoutingModule = VehicleRoutingModule;
            vehicleRoutingModule.init('${pageContext.request.contextPath}', ${latitude}, ${longitude}, ${zoomLevel});
            vehicleRoutingModule.attachListeners();
        </script>

        <!-- Modal for searching vehicle -->
        <div class="modal fade" id="searchVehicleModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Search Vehicle</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="input-group">
                            <input type="text" id="vehicle-search-input" class="form-control"
                                placeholder="Search Vehicle">
                            <span 
                                class="input-group-text btn btn-secondary"
                                onclick="vehicleRoutingModule.searchVehicle()">
                                Search
                            </span>
                        </div>

                        <!-- Dropdown container for search results -->
                        <div id="searchVehicleDropdown" class="dropdown p-2"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal for searching location -->
        <div class="modal fade" id="searchLocationModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Search Location</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="input-group">
                            <input type="text" id="location-search-input" class="form-control"
                                placeholder="Search Location">
                            <span 
                                class="input-group-text btn btn-secondary"
                                onclick="vehicleRoutingModule.searchLocation()">
                                Search
                            </span>
                        </div>

                        <!-- Dropdown container for search results -->
                        <div id="searchLocationDropdown" class="dropdown p-2"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>

    </html>