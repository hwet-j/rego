window.initMap=function(){
    const map=new google.maps.Map(document.getElementById("map"),{
        center: { lat:37.5639635, lng:127},
        zoom:12
    });


    const malls = [];
    var mallListData = document.getElementById('mallList').getAttribute('data-variable');

    var jsonObject = JSON.parse(mallListData);

    jsonObject.forEach(function (mall) {
        malls.push({
            label: mall.touristAttractionId.toString(),
            name: mall.name,
            lat: parseFloat(mall.latitude),
            lng: parseFloat(mall.longitude),
            imageUrl: mall.image.toString()
        });
    });

    const bounds=new google.maps.LatLngBounds();
    const infoWindow= new google.maps.InfoWindow();

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

        var index = 0;
        var distanceToTravel = 0.04;   // 고정된 이동 거리
        var speed = 40;                 // 마커 이동 속도

        function moveMarker() {
            if (index < malls.length - 1) {
                var start = malls[index];
                var end = malls[index + 1];

                // 아이콘을 변경
                var rotation = calculateRotation(start, end);
                var iconUrl = getIconUrlByRotation(rotation);
                var newIcon = {
                    url: iconUrl,
                    scaledSize: new google.maps.Size(40, 40),
                    origin: new google.maps.Point(0, 0),
                    anchor: new google.maps.Point(15, 15)
                };
                marker.setIcon(newIcon);

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

        // 두 지점 간의 방향을 계산하는 함수
        function calculateRotation(start, end) {
            var latDiff = end.lat - start.lat;
            var lngDiff = end.lng - start.lng;
            var angle = (Math.atan2(latDiff, lngDiff) * 180) / Math.PI;
            return (angle + 360) % 360;
        }

        // 방향에 따라 다른 아이콘 URL을 반환하는 함수
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

    }


}
