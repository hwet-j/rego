window.initMap=function(){

    /* Map의 초기 설정 */
    const map=new google.maps.Map(document.getElementById("map"),{
        center: { lat:37.5639635, lng:127},
        zoom:12,
        minZoom:3,
        maxZoom:30
    });

    /* 관광지 목록을 저장할 변수 */
    const malls = [];

    /*
    Controller -> Html -> Js 로 받아온 정보 (Html에서 Js로 받아올 경우 String으로 변환되어 온다.
    Controller에서 JSON형식으로 변환하여 전송해 JS에서 String으로 받아온 정보를 JSON타입으로 변환하여 사용한다.
    */
    // HTML로 부터 String 타입으로 데이터를 전달 받는다.
    var mallListData = document.getElementById('mallList').getAttribute('data-variable');

    // JSON 타입으로 변환한다. (String의 형식이 JSON형식이여야지 가능하다.)
    var jsonObject = JSON.parse(mallListData);

    // ForEach문을 사용해서 JS에서 사용할 수 있도록 변수에 저장한다.
    jsonObject.forEach(function (mall) {
        malls.push({
            label: mall.touristAttractionId.toString(),     // 마커 중앙에 표시될 문자 (문자열로 받는거같은데 문자열로 변환을 해줘야 작동함)
            name: mall.name,                                // 마커 클릭 시 표시되는 정보에 사용될 이름(관광지 이름)
            lat: parseFloat(mall.latitude),                 // 위도 (실수 형태로 변환필요)
            lng: parseFloat(mall.longitude),                // 경도 (실수 형태로 변환필요)
            imageUrl: mall.image.toString()                 // 이미지 경로
        });
    });

    /*
    bounds : 마커를 찍기위한 객체
    infoWindow : 마커 클릭 시 표시되는 창 (마커 클릭 없어도 표시 가능)
    markers : 마커 목록을 설정할 변수
     */
    const bounds=new google.maps.LatLngBounds();
    const infoWindow= new google.maps.InfoWindow();
    const markers = [];

    /* 모든 정보에 마커 및 정보창 설정 */
    malls.forEach(({label,name,lat,lng,imageUrl}) => {
        const marker=new google.maps.Marker({
            position: {lat,lng},
            label,
            map
        });
        bounds.extend(marker.position);

        marker.addListener("click",() => {
            map.panTo(marker.position);
            const content = `
                            <div>
                                <img src="${imageUrl}" alt="${name}" width="200">
                                <p>${name}</p>
                            </div>
                        `;
            infoWindow.setContent(content);
            infoWindow.open({
                anchor:marker,
                map
            });
        });
        
        markers.push({ label, name, marker });
    });

    map.fitBounds(bounds);

    // 선 그리기 함수 호출
    drawPolyline(map);


    // 선 그리기 함수
    function drawPolyline(map) {

        var marker = new google.maps.Marker({
            map: map,
            animation: google.maps.Animation.BOUNCE,
            icon: {
                url: "https://github.com/hwet-j/hwet-j.github.io/assets/81364742/ef2f4530-9c23-4b1c-aae7-8054e46bd2a7",
                scaledSize: new google.maps.Size(40, 40),
                origin: new google.maps.Point(0, 0),
                anchor: new google.maps.Point(15, 15)
            }
        });

        var index = 0;                  // 마커의 위치
        var distanceToTravel = 0.01;    // 고정된 이동 거리
        var speed = 40;                 // 마커 이동 속도
        var rotation = 0;

        function moveMarker() {

            if (index < malls.length - 1) {
                var start = malls[index];
                var end = malls[index + 1];

                var latDiff = end.lat - start.lat;
                var lngDiff = end.lng - start.lng;
                var totalDistance = Math.sqrt(latDiff * latDiff + lngDiff * lngDiff);

                var fraction = 0;
                var numSteps = totalDistance / distanceToTravel;

                var latStep = latDiff / numSteps;
                var lngStep = lngDiff / numSteps;

                if (fraction === 0) {
                    // console.log("방향전환")
                    rotation = calculateRotation(start, end);
                    var initialIconUrl = getIconUrlByRotation(rotation);

                    var initialIcon = {
                        url: initialIconUrl,
                        scaledSize: new google.maps.Size(40, 40),
                        origin: new google.maps.Point(0, 0),
                        anchor: new google.maps.Point(15, 15)
                    };
                    marker.setIcon(initialIcon);
                }

                var move = function () {
                    fraction += 1;
                    if (fraction <= numSteps) {
                        var lat = start.lat + latStep * fraction;
                        var lng = start.lng + lngStep * fraction;
                        marker.setPosition({ lat: lat, lng: lng });
                        setTimeout(move, speed);
                    } else {
                        index++;
                        if (index >= malls.length - 1) {
                            index = 0;  // 1부터 다시 시작
                        }
                        moveMarker();
                    }
                };

                move();
            }
        }


        /* 두 지점 간의 방향을 계산하는 함수 */
        function calculateRotation(start, end) {
            var latDiff = end.lat - start.lat;
            var lngDiff = end.lng - start.lng;
            var angle = (Math.atan2(latDiff, lngDiff) * 180) / Math.PI;
            return (angle + 360) % 360;
        }

        /* 방향에 따라 다른 아이콘 URL을 반환하는 함수 */
        function getIconUrlByRotation(rotation) {
            // 방향에 따라 다른 이미지 URL을 반환합니다.
            // 예를 들어, 0도에서 180도 사이이면 "오른쪽을 가리키는 이미지 URL"을 반환하고,
            // 그 외의 경우에는 "왼쪽을 가리키는 이미지 URL"을 반환합니다.
            if (rotation >= 0 && rotation <= 180) {
                return "https://github-production-user-asset-6210df.s3.amazonaws.com/81364742/269530380-4c8c756c-bb05-447a-83dc-bf6464a2fc92.gif";
            } else {
                return "https://github.com/hwet-j/hwet-j.github.io/assets/81364742/ef2f4530-9c23-4b1c-aae7-8054e46bd2a7";
            }
        }

        moveMarker();

        /* 화살표 형식으로 선긋기 */
        for (var i = 0; i < malls.length - 1; i++) {
            var start = malls[i];
            var end = malls[i + 1];

            var lineSymbol = {
                path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW, // 화살표 모양
                scale: 3, // 화살표 크기
                strokeColor: "#9900ff" // 화살표의 색상
            };

            /* 이 기능을 활용하면 이동방식에 따른 항공,기차 등을 구현 가능할듯함 */
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

        /* 검색기능 사용시 비슷한 문자를 찾아 검색 */
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


        /* 검색 기능 추가 */
        const searchInput = document.getElementById("searchInput");
        const searchButton = document.getElementById("searchButton");

        // Enter 키 누를 때 검색 함수 호출
        searchInput.addEventListener("keyup", function(event) {
            if (event.key === "Enter") {
                const query = searchInput.value.trim();
                if (query) {
                    handleSearch(query);
                }
            }
        });

        // 검색 버튼 클릭 이벤트 처리
        searchButton.addEventListener("click", () => {
            const query = searchInput.value.trim();
            if (query) {
                handleSearch(query);
            }
        });

        function handleSearch(query) {
            const results = [];

            malls.forEach(({ label, name, lat, lng, imageUrl }) => {
                // 검색어와 이름 간의 유사성을 계산
                if (calculateSimilarity(query, name)) {
                    results.push({ label, name, lat, lng, imageUrl });
                }
            });

            const searchResultsElement = document.getElementById("searchResults");
            searchResultsElement.innerHTML = ""; // 검색 결과 초기화

            if (results.length > 0) {
                results.forEach(({ label, name, lat, lng, imageUrl }) => {
                    const listItem = document.createElement("li");
                    listItem.innerHTML = `
                <a href="#" data-lat="${lat}" data-lng="${lng}">
                    <img src="${imageUrl}" alt="${name}" width="40">
                    ${name}
                </a>
            `;
                    searchResultsElement.appendChild(listItem);

                    // 검색 결과 항목을 클릭했을 때 지도 이동 및 정보 표시
                    listItem.querySelector("a").addEventListener("click", (e) => {
                        e.preventDefault();
                        const clickedLat = parseFloat(e.target.getAttribute("data-lat"));
                        const clickedLng = parseFloat(e.target.getAttribute("data-lng"));
                        const resultMarker = markers.find((marker) => marker.label === label);

                        if (resultMarker) {
                            const { marker } = resultMarker;
                            map.panTo({ lat: clickedLat, lng: clickedLng });
                            google.maps.event.trigger(marker, "click");
                        }
                    });
                });
            } else {
                searchResultsElement.innerHTML = "<li>검색된 장소가 없습니다.</li>";
            }


            /*if (results.length > 0) {
                // 검색 결과가 있을 경우, 가장 첫 번째 결과를 선택하여 표시
                const { label, name, lat, lng, imageUrl } = results[0];
                const resultMarker = markers.find((marker) => marker.label === label);

                if (resultMarker) {
                    const { marker } = resultMarker;
                    map.panTo(marker.position);
                    google.maps.event.trigger(marker, "click");
                }
            } else {
                alert("검색된 장소가 없습니다.");
            }*/
        }

    }

}
