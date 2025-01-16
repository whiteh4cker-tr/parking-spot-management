document.addEventListener('DOMContentLoaded', function () {
    const parkingSpotsGrid = document.getElementById('parkingSpotsGrid');
    const parkModal = document.getElementById('parkModal');
    const modalTitle = document.getElementById('modalTitle');
    const parkForm = document.getElementById('parkForm');
    const unparkForm = document.getElementById('unparkForm');
    const modalResponse = document.getElementById('modalResponse');
    const closeModal = document.querySelector('.close');
    const toggleSpotManagement = document.getElementById('toggleSpotManagement');
    const spotManagementForm = document.getElementById('spotManagementForm');
    const addSpotButton = document.getElementById('addSpotButton');
    const removeSpotButton = document.getElementById('removeSpotButton');
    const newSpotNumberInput = document.getElementById('newSpotNumber');
    const occupiedLicensePlate = document.getElementById('occupiedLicensePlate');
    const occupiedVehicleType = document.getElementById('occupiedVehicleType');

    let selectedSpot = null;

    // Fetch parking spots from the backend
    function fetchParkingSpots() {
        fetch('http://localhost:8080/api/parking/spots')
            .then(response => response.json())
            .then(data => {
                parkingSpotsGrid.innerHTML = '';
                data.forEach(spot => {
                    const spotElement = document.createElement('div');
                    spotElement.className = `parking-spot ${spot.status === 'OCCUPIED' ? 'occupied' : ''}`;
                    spotElement.innerHTML = `
                        <div>${spot.spotNumber}</div>
                        <div class="status">${spot.status}</div>
                    `;
                    spotElement.addEventListener('click', () => handleSpotClick(spot));
                    parkingSpotsGrid.appendChild(spotElement);
                });
            })
            .catch(error => console.error('Error fetching parking spots:', error));
    }

    // Handle parking spot click
    function handleSpotClick(spot) {
        selectedSpot = spot;
        if (spot.status === 'AVAILABLE') {
            modalTitle.textContent = `Park Vehicle in Spot ${spot.spotNumber}`;
            parkForm.style.display = 'block';
            unparkForm.style.display = 'none';
        } else {
            modalTitle.textContent = `Unpark Vehicle from Spot ${spot.spotNumber}`;
            parkForm.style.display = 'none';
            unparkForm.style.display = 'block';
            occupiedLicensePlate.textContent = spot.vehicle.licensePlate;
            occupiedVehicleType.textContent = spot.vehicle.type;
        }
        parkModal.style.display = 'flex';
    }

    // Park a vehicle
    parkForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const licensePlate = document.getElementById('licensePlate').value;
        const vehicleType = document.getElementById('vehicleType').value;

        const vehicle = {
            licensePlate: licensePlate,
            type: vehicleType
        };

        fetch(`http://localhost:8080/api/parking/park?spotNumber=${selectedSpot.spotNumber}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(vehicle)
        })
        .then(response => response.json())
        .then(data => {
            modalResponse.textContent = `Vehicle parked successfully: ${JSON.stringify(data)}`;
            fetchParkingSpots();
        })
        .catch(error => {
            modalResponse.textContent = `Error: ${error.message}`;
        });
    });

    // Unpark a vehicle
    unparkForm.addEventListener('submit', function (event) {
        event.preventDefault();

        fetch(`http://localhost:8080/api/parking/unpark?spotNumber=${selectedSpot.spotNumber}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            modalResponse.textContent = `Vehicle unparked successfully: ${JSON.stringify(data)}`;
            fetchParkingSpots();
        })
        .catch(error => {
            modalResponse.textContent = `Error: ${error.message}`;
        });
    });

    // Toggle the spot management form
    toggleSpotManagement.addEventListener('click', function () {
        spotManagementForm.style.display = spotManagementForm.style.display === 'none' ? 'block' : 'none';
    });

    // Add a parking spot
    addSpotButton.addEventListener('click', function () {
        const spotNumber = newSpotNumberInput.value;
        if (!spotNumber) {
            alert('Please enter a spot number');
            return;
        }

        fetch(`http://localhost:8080/api/parking/spots/add?spotNumber=${spotNumber}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            alert(`Parking spot added: ${JSON.stringify(data)}`);
            fetchParkingSpots();
        })
        .catch(error => {
            alert(`Error: ${error.message}`);
        });
    });

    // Remove a parking spot
    removeSpotButton.addEventListener('click', function () {
        const spotNumber = newSpotNumberInput.value;
        if (!spotNumber) {
            alert('Please enter a spot number');
            return;
        }

        fetch(`http://localhost:8080/api/parking/spots/remove?spotNumber=${spotNumber}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                alert(`Parking spot removed: ${spotNumber}`);
                fetchParkingSpots();
            } else {
                response.text().then(message => alert(message));
            }
        })
        .catch(error => {
            alert(`Error: ${error.message}`);
        });
    });

    // Close the modal
    closeModal.addEventListener('click', function () {
        parkModal.style.display = 'none';
        modalResponse.textContent = '';
    });

    // Initial fetch of parking spots
    fetchParkingSpots();
});