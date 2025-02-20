console.log("open")
//Geolocation API : 접속한 클라이언트 주소 API

// [1] 지도 api 호출
// const map = () => {
//     var container = document.getElementById('map');
//     var options = {
//         center: new kakao.maps.LatLng(33.450701, 126.570667),
//         level: 3
//     };

// [2] 지도에 마커 표시하기(클릭한 위치)
//     var map = new kakao.maps.Map(container, options);
// }
// map();

// 클릭한 위치에 마커 표시하기

// var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
//     mapOption = { 
//         center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
//         level: 3 // 지도의 확대 레벨
//     };

// var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// // 지도를 클릭한 위치에 표출할 마커입니다
// var marker = new kakao.maps.Marker({ 
//     // 지도 중심좌표에 마커를 생성합니다 
//     position: map.getCenter() 
// }); 
// // 지도에 마커를 표시합니다
// marker.setMap(map);

// // 지도에 클릭 이벤트를 등록합니다
// // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
// kakao.maps.event.addListener(map, 'click', function(mouseEvent) {        
    
//     // 클릭한 위도, 경도 정보를 가져옵니다 
//     var latlng = mouseEvent.latLng; 
    
//     // 마커 위치를 클릭한 위치로 옮깁니다
//     marker.setPosition(latlng);
    
//     var message = '클릭한 위치의 위도는 ' + latlng.getLat() + ' 이고, ';
//     message += '경도는 ' + latlng.getLng() + ' 입니다';
//         // 추후 사용자가 클릭한 위치의 위도와 경도를 DB에 저장, 활용하는 방법
//     // var resultDiv = document.getElementById('clickLatlng'); 
//     // resultDiv.innerHTML = message;
// });

// [3] 마커 메세지 호출
// var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
//     mapOption = { 
//         center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
//         level: 3 // 지도의 확대 레벨
//     };

// var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
  
// // 마커를 표시할 위치입니다 
// var position =  new kakao.maps.LatLng(33.450701, 126.570667);

// // 마커를 생성합니다
// var marker = new kakao.maps.Marker({
//   position: position,
//   clickable: true // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
// });

// // 아래 코드는 위의 마커를 생성하는 코드에서 clickable: true 와 같이
// // 마커를 클릭했을 때 지도의 클릭 이벤트가 발생하지 않도록 설정합니다
// // marker.setClickable(true);

// // 마커를 지도에 표시합니다.
// marker.setMap(map);

// // 마커를 클릭했을 때 마커 위에 표시할 인포윈도우를 생성합니다
// var iwContent = '<div style="padding:5px;">본사 !</div>', // 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
//     iwRemoveable = true; // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다

// // 인포윈도우를 생성합니다
// var infowindow = new kakao.maps.InfoWindow({
//     content : iwContent,
//     removable : iwRemoveable
// });
    // ****************************************************************************
// // 마커에 클릭이벤트를 등록합니다
// kakao.maps.event.addListener(marker, 'click', function() {
//       // 마커 위에 인포윈도우를 표시합니다
//       infowindow.open(map, marker);  
// });


// [5] 마커 클러스터러 사용하기 + 공공데이터 API + qnxmtmxmfoq
// + 클러스터러 사용시 html js 앱키 뒤에 &libraries=clusterer

// (1) 지도의 중심좌표와 지도확대 레벨 초기 설정, 지도를 표시항 div(dom) 가져오기기
var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
    center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표 
    level : 14 // 지도의 확대 레벨 
});

// (2) 마커 클러스터 객체 생성 : 클러스터란? 마커 주변에 근처에 마커들이 있을때 집합 아이콘
// 마커 클러스터러를 생성합니다 
var clusterer = new kakao.maps.MarkerClusterer({
    map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체 
    averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정 
    minLevel: 10 // 클러스터 할 최소 지도 레벨 
});
// (3) 공식문서에는 jquery Ajax, 강의에서는 fetch 이용한 자료 가져오기.
// 공공데이터의 자료 요청청
fetch('https://api.odcloud.kr/api/15051492/v1/uddi:852bbc11-63ed-493e-ab09-caaaf54fd144?page=1&perPage=35&serviceKey=g3%2F4abJMx8%2Ft50L57TYyOXozOVU7JlqcIHyDuZx5ZU851x5yIGMJ3dZwSJ03Vxcs20Wqk6c6vnZl27%2F0AMWH2Q%3D%3D')
.then(r=>r.json())
.then(responseData =>{console.log(responseData);
    // 응답 받은 자료로 마커 만들기
    // 반복문 : 1. for 문, 2. forEach, 3. map
    // // 1. for 문
    // let markers = []; // 마커 목록을 추후 클러스터 넣을 예정
    // for(let index = 0; index <= responseData.data.length -1; index++){
    //     // 1-2 응답받은 자료에서 data목록에서 index번째 약국 객체 가져오기
    //     let data = responseData.data[index];
    //     // 1-3 마커 생성, index 번째 약국의 위도, 경도 정보를 마커에 대입한다.
    //     let marker = new kakao.maps.Marker({position : new kakao.maps.LatLng(data.위도, data.경도)});
    //     // 1-4 생성한 마커를 마커목록/리스트에 넣어준다.
    //     markers.push(marker);
    // }
    // 2. foreach 문
    // let markers = [];
    // responseData.data.forEach(data=>{
    //     //마커 생성
    //     let marker = new kakao.maps.Marker({position : new kakao.maps.LatLng(data.위도, data.경도)});
    //     markers.push(marker);// 생성한 마커를 마커목록 넣어준다.

    // })
    // 3. map
    let markers = responseData.data.map(data=>{
        // 3-1. 마커 생성
        let marker = new kakao.maps.Marker({position : new kakao.maps.LatLng(data.위도, data.경도)});

        
        // * 각 마커에 클릭 이벤트 등록한다. *** return 전에 하기
        //kakao.maps.event.addListener(marker, 'click' , function(){})
        kakao.maps.event.addListener(marker, 'click' , () => {
            // alert(`${data.약국명}클릭 했습니다.`);
            // 클릭한 마커의 약국 정보를 사이드 바(div) html 값 대입하기
            document.querySelector('.약국명').innerHTML = data.약국명;
            document.querySelector('.전화번호').innerHTML = data.전화번호;
            document.querySelector('.주소').innerHTML = data.소재지도로명주소;
            // + 부트 스트랩의 '오프캔버스' 실행 버튼 클릭 이벤트
            document.querySelector('.사이드바버튼').click(); // JS에서 특정한 버튼 강제로 클릭하기.
        
            
        })

        // 3-2 forEach와 다르게 map은 return을 사용 가능, -> return 값은 새로운 배열에 대입된다. 
        // 반복문에서 return값은 markers 배열에 대입, 즉 push 생략
        return marker; 
    })
    // 5. 반복문 종료 후 마커 목록 리스트 클러스터에 추가
    clusterer.addMarkers(markers);
 })
.catch(e=>{console.log(e);})


// // [4] 여러개 마커 표시하기,
//     // (1) 지도를 출력할 DOM 객체 가져온다.
// var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
//     // (2) 지도를 출력하기 전에 옵션[ 지도의 중심좌표-위도/경도, Level(확대0~14축소)] 정보설정
//     mapOption = { 
//         center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
//         level: 3 // 지도의 확대 레벨
//     };
//     // (3) 지도객체를 생성한다.
// var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
 
// // 마커를 표시할 위치와 title 객체 배열입니다 
//     // (4) 지도객체에 출력할 마크업들의 정보( 마크업 제목, 마크업 위치 : LatLng(위도, 경도))
//     // + 데이터 베이스에 저장된 위도/경도 여러개를 여러마커로 표시할 수 있다.
// var positions = [
//     {
//         title: '카카오', 
//         latlng: new kakao.maps.LatLng(33.450705, 126.570677)
//     },
//     {
//         title: '생태연못', 
//         latlng: new kakao.maps.LatLng(33.450936, 126.569477)
//     },
//     {
//         title: '텃밭', 
//         latlng: new kakao.maps.LatLng(33.450879, 126.569940)
//     },
//     {
//         title: '근린공원',
//         latlng: new kakao.maps.LatLng(33.451393, 126.570738)
//     },
//     {
//         title: '부평역',
//         latlng: new kakao.maps.LatLng(37.4895528 , 126.723325411)
//     }
// ];

// // 마커 이미지의 이미지 주소입니다
//     // (5) 마커의 이미지는 배포된 HTTP의 이미지가 위차한 경로 이미지
// var imageSrc = "http://localhost:8080/img/logo.png"; 
    
// for (var i = 0; i < positions.length; i ++) {
    
//     // 마커 이미지의 이미지 크기 입니다
//     var imageSize = new kakao.maps.Size(24, 35); 
    
//     // 마커 이미지를 생성합니다    
//     var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
    
//     // 마커를 생성합니다
//     var marker = new kakao.maps.Marker({
//         map: map, // 마커를 표시할 지도
//         position: positions[i].latlng, // 마커를 표시할 위치
//         title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
//         image : markerImage // 마커 이미지 
//     });
// }