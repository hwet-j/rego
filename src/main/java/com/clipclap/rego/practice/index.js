window.initMap = function () {
    const map = new google.maps.Map(document.getElementById("map"), {
        center: {lat: 37.5639635, lng: 127},
        zoom: 12,
    });

    const malls = [
        {
            label: "A",
            name: "코엑스(COEX)",
            lat: 37.5113169,
            lng: 127.0595695,
            imageUrl: "https://upload.wikimedia.org/wikipedia/commons/a/a5/COEX_%28%EC%BD%94%EC%97%91%EC%8A%A4%EC%9D%98_%EC%A0%84%EA%B2%BD%29.jpg"
        },
        {
            label: "B",
            name: "타임스퀘어",
            lat: 37.5169933,
            lng: 126.9035425,
            imageUrl: "https://cdn.ftoday.co.kr/news/photo/202106/218511_218598_3444.jpg"
        },
        {
            label: "C",
            name: "IFC",
            lat: 37.5251644,
            lng: 126.9255491,
            imageUrl: "https://byline.network/wp-content/uploads/2022/03/%EB%A9%94%EC%9D%B8-1080x720.jpg"
        },
        {
            label: "D",
            name: "가든파이브",
            lat: 37.4775778,
            lng: 127.1249983,
            imageUrl: "https://garden5life.com/home/skin/g5_skin/images/info/info_img_01.jpg"
        },
        {
            label: "E",
            name: "롯데타워몰",
            lat: 37.5124641,
            lng: 127.102543,
            imageUrl: "https://tong.visitkorea.or.kr/cms/resource/47/2577147_image2_1.bmp"
        }
    ];

    const bounds = new google.maps.LatLngBounds();
    const infoWindow = new google.maps.InfoWindow();
    const markers = [];

    malls.forEach(({label, name, lat, lng, imageUrl}) => {
        const marker = new google.maps.Marker({
            position: {lat, lng},
            label,
            map

        });
        bounds.extend(marker.position);

        marker.addListener("click", () => {
            map.panTo(marker.position);
            const content = `
                            <div>
                                <img src="${imageUrl}" alt="${name}" width="200">
                                <p>${name}</p>
                            </div>
                        `;
            infoWindow.setContent(content);
            infoWindow.open({
                anchor: marker,
                map,
            });
        });

        markers.push({label, name, marker});
    });

    map.fitBounds(bounds);


    // 선 그리기 함수 호출
    drawPolyline(map);


    // 선 그리기 함수
    function drawPolyline(map) {
        var lineCoordinates = [
            {lat: 37.5113169, lng: 127.0595695},        // 1번 지점
            {lat: 37.4775778, lng: 127.1249983},        // 2번 지점
            {lat: 37.5169933, lng: 126.9035425},        // 3번 지점
            {lat: 37.5251644, lng: 126.9255491},        // 4번 지점
            {lat: 37.5124641, lng: 127.102543}          // 5번 지점
        ];

        var marker = new google.maps.Marker({
            map: map,
            animation: google.maps.Animation.BOUNCE,
            icon: {
                url: "https://media.tenor.com/smxDF7eQs6EAAAAi/%EB%8B%AC%EB%A6%AC%EB%8A%94%EB%86%8D%EB%8B%B4%EA%B3%B0-joke-bear.gif",
                scaledSize: new google.maps.Size(40, 40),
                origin: new google.maps.Point(0, 0),
                anchor: new google.maps.Point(15, 15)
            }
        });

        var index = 0;
        var distanceToTravel = 0.001;   // 고정된 이동 거리
        var speed = 40;                 // 마커 이동 속도

        function moveMarker() {
            if (index < lineCoordinates.length - 1) {
                var start = lineCoordinates[index];
                var end = lineCoordinates[index + 1];

                var latDiff = end.lat - start.lat;
                var lngDiff = end.lng - start.lng;
                var totalDistance = Math.sqrt(latDiff * latDiff + lngDiff * lngDiff);

                var fraction = 0;
                var numSteps = totalDistance / distanceToTravel;

                var latStep = latDiff / numSteps;
                var lngStep = lngDiff / numSteps;

                var move = function () {
                    fraction += 1;
                    if (fraction <= numSteps) {
                        var lat = start.lat + latStep * fraction;
                        var lng = start.lng + lngStep * fraction;
                        marker.setPosition({lat: lat, lng: lng});
                        setTimeout(move, speed);
                    } else {
                        index++;
                        if (index >= lineCoordinates.length - 1) {
                            index = 0;  // 1부터 다시 시작
                        }
                        moveMarker();
                    }
                };

                move();
            }
        }

        moveMarker();

        /* 화살표 형식으로 선긋기 */
        for (var i = 0; i < lineCoordinates.length - 1; i++) {
            var start = lineCoordinates[i];
            var end = lineCoordinates[i + 1];

            var lineSymbol = {
                path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW, // 화살표 모양
                scale: 3, // 화살표 크기
                strokeColor: "#9900ff" // 화살표의 색상
            };

            var line = new google.maps.Polyline({
                path: [start, end],
                icons: [
                    {
                        icon: lineSymbol,
                        offset: "100%" // 화살표 위치 (끝)
                    }
                ],
                strokeColor: "#002aff", // 선의 색상
                strokeOpacity: 1.0,     // 선의 불투명도 (0.0 - 1.0)
                strokeWeight: 2,        // 선의 두께
                map: map
            });
        }

        function calculateSimilarity(query, name) {
            const queryLower = query.toLowerCase();
            const nameLower = name.toLowerCase();
            let similarity = false;

            // 간단한 유사성 평가 방법으로 문자열에서 공백을 제거하고, 검색어가 이름에 포함되어 있는지 확인합니다.
            if (/^[가-힣]+$/.test(query)) {
                const normalizedQuery = queryLower.replace(/[^가-힣]/g, "");
                const normalizedName = nameLower.replace(/[^가-힣]/g, "");
                similarity = normalizedName.includes(normalizedQuery);
            } else {
                const normalizedQuery = queryLower.replace(/[^a-z]/g, "");
                const normalizedName = nameLower.replace(/[^a-z]/g, "");
                similarity = normalizedName.includes(normalizedQuery);
            }

            return similarity;
        }


        // 검색 기능 추가
        const searchInput = document.getElementById("searchInput");
        const searchButton = document.getElementById("searchButton");

        searchButton.addEventListener("click", () => {
            const query = searchInput.value.trim();
            if (query) {
                handleSearch(query);
            }
        });

        function handleSearch(query) {
            const results = [];

            malls.forEach(({label, name, lat, lng, imageUrl}) => {
                // 검색어와 이름 간의 유사성을 계산
                if (calculateSimilarity(query, name)) {
                    results.push({label, name, lat, lng, imageUrl});
                }
            });

            if (results.length > 0) {
                // 검색 결과가 있을 경우, 가장 첫 번째 결과를 선택하여 표시
                const {label, name, lat, lng, imageUrl} = results[0];
                const resultMarker = markers.find((marker) => marker.label === label);

                if (resultMarker) {
                    const {marker} = resultMarker;
                    map.panTo(marker.position);
                    google.maps.event.trigger(marker, "click");
                }
            } else {
                alert("검색된 장소가 없습니다.");
            }
        }
    }
}
