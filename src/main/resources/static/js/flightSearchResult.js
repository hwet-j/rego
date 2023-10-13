document.addEventListener('DOMContentLoaded', function() {
    const airlinesContainer = document.getElementById('airlines-container');
    const selectAllAirlines = document.getElementById('selectAllAirlines');
    const airlinesSet = new Set();

    const flightBoxes = document.querySelectorAll('.flight-box-wrapper');
    flightBoxes.forEach(flightBox => {
        const airlineNames = flightBox.querySelectorAll('.airlineName');
        airlineNames.forEach(airlineName => {
            airlinesSet.add(airlineName.textContent.trim());
        });
    });

    airlinesSet.forEach(airline => {
        const checkboxWrapper = document.createElement('div');
        checkboxWrapper.className = "airlineOption";
        const checkboxInput = document.createElement('input');
        const label = document.createElement('label');

        checkboxInput.type = "checkbox";
        checkboxInput.name = "airline";
        checkboxInput.value = airline;
        checkboxInput.checked = true; // 초기에 모든 체크박스를 선택한 상태로 만듭니다.
        label.textContent = airline;

        checkboxInput.addEventListener('change', function() {
            filterFlights();
        });

        checkboxWrapper.appendChild(checkboxInput);
        checkboxWrapper.appendChild(label);
        airlinesContainer.appendChild(checkboxWrapper);
    });

    selectAllAirlines.addEventListener('change', function() {
        const allAirlineCheckboxes = airlinesContainer.querySelectorAll('input[type="checkbox"]');
        allAirlineCheckboxes.forEach(checkbox => {
            checkbox.checked = selectAllAirlines.checked;
        });
        filterFlights();
    });

    function filterFlights() {
        const selectedAirlines = new Set();
        const allAirlineCheckboxes = airlinesContainer.querySelectorAll('input[type="checkbox"]');
        allAirlineCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                selectedAirlines.add(checkbox.value);
            }
        });

        flightBoxes.forEach(flightBox => {
            const airlineNames = flightBox.querySelectorAll('.airlineName');
            let boxShouldBeVisible = false;
            airlineNames.forEach(airlineName => {
                if (selectedAirlines.has(airlineName.textContent.trim())) {
                    boxShouldBeVisible = true;
                }
            });
            if (boxShouldBeVisible) {
                flightBox.style.display = "";
            } else {
                flightBox.style.display = "none";
            }
        });
    }
});


function bookFlightInfo(button) {
    var flightBox = button.closest('.flight-box-wrapper');

    // flightData 객체를 생성합니다. 이 객체는 서버에 전송될 데이터를 담습니다.
    var flightData = {
        routes: [],
        price: null
    };

    // 루트 정보를 수집합니다.
    var routes = flightBox.querySelectorAll('tr');
    routes.forEach(function(route) {
        var airlineImg = route.querySelector('.Airline img').getAttribute('src');
        var airlineName = route.querySelector('.airlineName').textContent.trim();
        var departureTime = route.querySelector('.depart').querySelectorAll('span')[0].textContent.trim();
        var departureAirport = route.querySelector('.depart').querySelectorAll('span')[1].textContent.trim();
        var duration = route.querySelector('.duration').textContent.trim();
        var arrivalTime = route.querySelector('.arrive').querySelectorAll('span')[0].textContent.trim();
        var arrivalAirport = route.querySelector('.arrive').querySelectorAll('span')[1].textContent.trim();

        // 루트 정보를 flightData 객체의 routes 배열에 추가합니다.
        flightData.routes.push({
            airlineImg: airlineImg,
            airlineName: airlineName,
            departureTime: departureTime,
            departureAirport: departureAirport,
            arrivalTime: arrivalTime,
            arrivalAirport: arrivalAirport,
            duration: duration
        });
    });

    // 가격 정보를 수집합니다.
    var price = flightBox.querySelector('.price .money span').textContent.trim();
    flightData.price = price.replace('₩', ''); // '₩' 기호 제거

    // 서버에 전송하기 전에 flightData 객체를 확인합니다.
    console.log(flightData);

    // 서버로 데이터를 전송하는 함수를 호출합니다.
    sendFlightDataToServer(flightData);
}

