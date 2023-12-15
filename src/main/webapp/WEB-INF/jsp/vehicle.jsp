<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>User</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    </head>

    <body>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

        <nav class="navbar navbar-expand-sm navbar-dark bg-dark p-0">
            <div class="container-fluid p-4">
                <span class="navbar-brand">DriveSync</span>

                <!-- Vehicle Button -->
                <a>
                    <button type="button" class="btn btn-info me-2">Vehicle</button>
                </a>

                <!-- Location Button -->
                <a href="${pageContext.request.contextPath}/location">
                    <button type="button" class="btn btn-primary me-2">Location</button>
                </a>

                <!-- Vehicle Route Planning Button -->
                <a href="${pageContext.request.contextPath}/vehicle-routing">
                    <button type="button" class="btn btn-success">Routing</button>
                </a>

                <!-- Logout Button  -->
                <a href="${pageContext.request.contextPath}/logout">
                    <span class="btn btn-danger navbar-brand float-end">Logout</span>
                </a>
            </div>
        </nav>

        <div class="container mt-4">
            <div class="row">
                <div class="col-md-12">
                    <!-- Add Vehicle Form -->
                    <h2>Add Vehicle</h2>
                    <form id="addVehicleForm">
                        <div class="mb-3">
                            <label for="vehicleName" class="form-label">Vehicle Name</label>
                            <input type="text" class="form-control" id="vehicleName">
                        </div>
                        <div class="mb-3">
                            <label for="registrationNumber" class="form-label">Registration Number</label>
                            <input type="text" class="form-control" id="registrationNumber">
                        </div>
                        <div class="mb-3">
                            <label for="capacity" class="form-label">Capacity</label>
                            <input type="number" class="form-control" id="capacity">
                        </div>
                        <button type="submit" class="btn btn-success"><i class="fas fa-plus"></i> Add Vehicle</button>
                        <button type="button" id="cancelBtn" class="btn btn-secondary"
                            style="display:none">Cancel</button>
                    </form>

                    <hr>

                    <!-- Vehicle List Table -->
                    <h2>Vehicle List</h2>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Registration</th>
                                <th>Capacity</th>
                                <th>Edit</th>
                                <th>Delete</th>
                            </tr>
                        </thead>
                        <tbody id="vehicleTableBody">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const addVehicleForm = document.getElementById('addVehicleForm');
                const vehicleTableBody = document.getElementById('vehicleTableBody');
                const cancelBtn = document.getElementById('cancelBtn');

                fetchVehicles();

                addVehicleForm.addEventListener('submit', function (event) {
                    event.preventDefault();
                    if (document.getElementById('vehicleName').value) {
                        var isEditMode = addVehicleForm.dataset.editMode === 'true';

                        if (isEditMode) {
                            var vehicleId = addVehicleForm.dataset.vehicleId;
                            updateVehicle(vehicleId);
                        } else {
                            addVehicle();
                        }
                    } else {
                        alert('Please fill in the Vehicle Name field.');
                    }
                });

                document.addEventListener('click', function (event) {
                    if (event.target.classList.contains('editBtn')) {
                        var vehicleId = event.target.dataset.vehicleId;
                        enterEditMode(vehicleId);
                    }
                });

                document.addEventListener('click', function (event) {
                    if (event.target.classList.contains('deleteBtn')) {
                        var vehicleId = event.target.dataset.vehicleId;
                        deleteVehicle(vehicleId);
                    }
                });

                cancelBtn.addEventListener('click', function () {
                    exitEditMode();
                });

                function fetchVehicles() {
                    fetch('${pageContext.request.contextPath}/vehicle-rest')
                        .then(response => response.json())
                        .then(data => displayVehicles(data))
                        .catch(error => alert('Error fetching vehicles.'));
                }

                function displayVehicles(vehicles) {
                    vehicleTableBody.innerHTML = '';

                    vehicles.forEach(function (vehicle) {
                        var row = '<tr>';
                        row += '<td>' + vehicle.name + '</td>';
                        row += '<td>' + vehicle.registrationNumber + '</td>';
                        row += '<td>' + vehicle.capacity + '</td>';
                        row += '<td><button class="btn btn-primary editBtn" data-vehicle-id="' + vehicle.id + '">Edit</button></td>';
                        row += '<td><button class="btn btn-danger deleteBtn" data-vehicle-id="' + vehicle.id + '">Delete</button></td>';
                        row += '</tr>';
                        vehicleTableBody.innerHTML += row;
                    });
                }

                function addVehicle() {
                    var vehicleName = document.getElementById('vehicleName').value;
                    var registrationNumber = document.getElementById('registrationNumber').value;
                    var capacity = document.getElementById('capacity').value;

                    var newVehicle = {
                        name: vehicleName,
                        registrationNumber: registrationNumber,
                        capacity: capacity,
                    };

                    fetch('${pageContext.request.contextPath}/vehicle-rest', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(newVehicle),
                    })
                        .then(response => {
                            if (!response.ok) {
                                alert('Failed to add vehicle');
                            }
                            return response.json();
                        })
                        .then(data => {
                            fetchVehicles();
                            addVehicleForm.reset();
                        })
                        .catch(error => {
                            console.error('Error adding vehicle:', error);
                            alert('Error adding vehicle. Please try again.');
                        });
                }

                function updateVehicle(vehicleId) {
                    var vehicleName = document.getElementById('vehicleName').value;
                    var registrationNumber = document.getElementById('registrationNumber').value;
                    var capacity = +document.getElementById('capacity').value;

                    var updatedVehicle = {
                        name: vehicleName,
                        registrationNumber: registrationNumber,
                        capacity: capacity,
                    };

                    fetch('${pageContext.request.contextPath}/vehicle-rest/' + vehicleId, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(updatedVehicle),
                    })
                        .then(response => {
                            return response.json();
                        })
                        .then(data => {
                            fetchVehicles();
                            exitEditMode();
                        })
                        .catch(error => {
                            console.error('Error updating vehicle:', error);
                            alert('Error updating vehicle. Please try again.');
                        });
                }

                function deleteVehicle(vehicleId) {
                    fetch('${pageContext.request.contextPath}/vehicle-rest/' + vehicleId, {
                        method: 'DELETE',
                    })
                        .then(response => {
                            // return response.json();
                        })
                        .then(data => {
                            fetchVehicles();
                        })
                        .catch(error => {
                            console.error('Error deleting vehicle:', error);
                            // alert('Error deleting vehicle. Please try again.');
                        });
                }

                function enterEditMode(vehicleId) {
                    addVehicleForm.querySelector('button[type="submit"]').innerText = 'Update Vehicle';
                    cancelBtn.style.display = 'inline';
                    addVehicleForm.dataset.editMode = 'true';
                    addVehicleForm.dataset.vehicleId = vehicleId;
                }

                function exitEditMode() {
                    addVehicleForm.querySelector('button[type="submit"]').innerText = 'Add Vehicle';
                    cancelBtn.style.display = 'none';
                    addVehicleForm.dataset.editMode = 'false';
                    addVehicleForm.dataset.vehicleId = '';
                    addVehicleForm.reset();
                }
            });
        </script>

    </body>

    </html>