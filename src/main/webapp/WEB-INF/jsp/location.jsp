<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Location</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    <!-- Include Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
</head>

<body>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

    <nav class="navbar navbar-expand-sm navbar-dark bg-dark p-0">
        <div class="container-fluid p-4">
            <span class="navbar-brand">DriveSync</span>

            <!-- Vehicle Button -->
            <a href="${pageContext.request.contextPath}/vehicle">
                <button type="button" class="btn btn-info me-2">Vehicle</button>
            </a>

            <!-- Location Button -->
            <a>
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
                <!-- Add Location Form -->
                <h2>Add Location</h2>
                <form id="addLocationForm">
                    <div class="mb-3">
                        <label for="locationName" class="form-label">Location Name</label>
                        <input type="text" class="form-control" id="locationName" required>
                    </div>
                    <div class="mb-3">
                        <label for="latitude" class="form-label">Latitude</label>
                        <input type="text" class="form-control" id="latitude" required>
                    </div>
                    <div class="mb-3">
                        <label for="longitude" class="form-label">Longitude</label>
                        <input type="text" class="form-control" id="longitude" required>
                    </div>
                    <button type="submit" class="btn btn-success"><i class="fas fa-plus"></i> Add Location</button>
                    <button type="button" id="cancelBtn" class="btn btn-secondary" style="display:none">Cancel</button>
                </form>

                <hr>

                <!-- Location List Table -->
                <h2>Location List</h2>
                <table class="table">
                	<caption>Location List Table</caption>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Latitude</th>
                            <th>Longitude</th>
                            <th>Edit</th>
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody id="locationTableBody">
                        
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const addLocationForm = document.getElementById('addLocationForm');
            const locationTableBody = document.getElementById('locationTableBody');
            const cancelBtn = document.getElementById('cancelBtn');

            
            fetchLocations();

            addLocationForm.addEventListener('submit', function (event) {
                event.preventDefault();
                if (document.getElementById('locationName').value) {
                    var isEditMode = addLocationForm.dataset.editMode === 'true';

                    if (isEditMode) {
                        var locationId = addLocationForm.dataset.locationId;
                        updateLocation(locationId);
                    } else {
                        addLocation();
                    }
                } else {
                    alert('Please fill in the Location Name field.');
                }
            });

            document.addEventListener('click', function (event) {
                if (event.target.classList.contains('editBtn')) {
                    var locationId = event.target.dataset.locationId;
                    enterEditMode(locationId);
                }
            });

            document.addEventListener('click', function (event) {
                if (event.target.classList.contains('deleteBtn')) {
                    var locationId = event.target.dataset.locationId;
                    deleteLocation(locationId);
                }
            });

            cancelBtn.addEventListener('click', function () {
                exitEditMode();
            });

            function fetchLocations() {
                fetch('${pageContext.request.contextPath}/location-rest')
                    .then(response => response.json())
                    .then(data => displayLocations(data))
                    .catch(error => alert('Error fetching locations.'));
            }

            function displayLocations(locations) {
                locationTableBody.innerHTML = '';

                locations.forEach(function (location) {
                    var row = '<tr>';
                    row += '<td>' + location.name + '</td>';
                    row += '<td>' + location.latitude + '</td>';
                    row += '<td>' + location.longitude + '</td>';
                    row += '<td><button class="btn btn-primary editBtn" data-location-id="' + location.id + '">Edit</button></td>';
                    row += '<td><button class="btn btn-danger deleteBtn" data-location-id="' + location.id + '">Delete</button></td>';
                    row += '</tr>';
                    locationTableBody.innerHTML += row;
                });
            }

            function addLocation() {
                var locationName = document.getElementById('locationName').value;
                var latitude = document.getElementById('latitude').value;
                var longitude = document.getElementById('longitude').value;
                
                    var newLocation = {
                    name: locationName,
                    latitude: parseFloat(latitude),  
                    longitude: parseFloat(longitude),
                };

                fetch('${pageContext.request.contextPath}/location-rest', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(newLocation),
                })
                .then(response => {
                    if (!response.ok) {
                        alert('Failed to add location');
                    }
                    return response.json();
                })
                .then(data => {
                    fetchLocations();
                    addLocationForm.reset();
                })
                .catch(error => {
                    console.error('Error adding location:', error);
                    alert('Error adding location. Please try again.');
                });
            }


            function updateLocation(locationId) {
                var locationName = document.getElementById('locationName').value;
                var latitude = document.getElementById('latitude').value;
                var longitude = document.getElementById('longitude').value;

                var updatedLocation = {
                    name: locationName,
                    latitude: parseFloat(latitude),
                    longitude: parseFloat(longitude),
                };

                fetch('${pageContext.request.contextPath}/location-rest/' + locationId, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(updatedLocation),
                })
                .then(response => {
                    return response.json();
                })
                .then(data => {
                    fetchLocations();
                    exitEditMode();
                })
                .catch(error => {
                    console.error('Error updating location:', error);
                    alert('Error updating location. Please try again.');
                });
            }

            function deleteLocation(locationId) {
                fetch('${pageContext.request.contextPath}/location-rest/' + locationId, {
                    method: 'DELETE',
                })
                .then(response => {
                    // return response.json();
                })
                .then(data => {
                    fetchLocations();
                })
                .catch(error => {
                    console.error('Error deleting location:', error);
                    // alert('Error deleting location. Please try again.');
                });
            }

            function enterEditMode(locationId) {
                addLocationForm.querySelector('button[type="submit"]').innerText = 'Update Location';
                cancelBtn.style.display = 'inline';
                addLocationForm.dataset.editMode = 'true';
                addLocationForm.dataset.locationId = locationId;
            }

            function exitEditMode() {
                addLocationForm.querySelector('button[type="submit"]').innerText = 'Add Location';
                cancelBtn.style.display = 'none';
                addLocationForm.dataset.editMode = 'false';
                addLocationForm.dataset.locationId = '';
                addLocationForm.reset();
            }
        });
    </script>
</body>

</html>
