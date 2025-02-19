console.log('api.js open')


// [5] chart.js 이용 부평구 인구 현황 시각화
const api4 = () =>{
    // 1. 부평구 인구현황 api url
        // 1. 요청할 URL
        const url = 'https://api.odcloud.kr/api/3044322/v1/uddi:466eee86-a8be-447b-9c8e-802bdbe897d7?page=1&perPage=22&serviceKey='
        // 2. 요청할 API 인증 KEY, 개별 발급
        const serviceKey = 'g3%2F4abJMx8%2Ft50L57TYyOXozOVU7JlqcIHyDuZx5ZU851x5yIGMJ3dZwSJ03Vxcs20Wqk6c6vnZl27%2F0AMWH2Q%3D%3D'
    // 2. fetch
    fetch(url+serviceKey)
        .then(response=>response.json())
        .then(responseData=>{console.log(responseData)
            // 데이터 준비
            let donglist = []
            let mensu = []
            let wemensu = []

            responseData.data.forEach(obj => {
                donglist.push( obj['동별'] ) // donglist 리스트에 모든 '동별' 를 대입해준다.
                mensu.push( obj['인구수(남)'] ) // mensu 모든 ' 남자인구수'를 대입
                wemensu.push( obj['인구수(여)'] ) // wemensu 모든 ' 여자인구수'를 대입
            });
            // chart.js 활용
            // (1) 차트를 표한할 위치의 DOM 가져오기
            const myChart2 = document.querySelector('#myChart2');
            // (2) 차트 객체를 생성한다.
            new Chart(myChart2, {
                type : 'bar' ,
                data : {
                    labels : donglist , // 동별
                    datasets : [
                        {label : '남자 인구수' , data : mensu},
                        {label : '여자 인구수' , data : wemensu}
                    ] // 자료/값
                }, // data end
            }) // chart end
        })
        .catch(e=>{console.log(e);})
}
api4();

// [4] chart.js 이용 시각화 그래프
const ctx = document.getElementById('myChart');
// const ctx = document.querySelector('#myChart'); //querySelector -> #Id 명
  new Chart(ctx, { // 차트 객체 생성, new chart (ctx, {차트 옵션}
    type: 'bar', // type : 차트모양, bar(막대차트) , line(선차트) 등등 공식 홈페이지 찾아서 사용한다.
    data: { // data : 차트에 들어갈 자료들
      labels: ['1월', '2월', '3월', '4월', '5월', '6월'], // labels : 가로축 제목들
      datasets: [ // datasets : 엑셀에서의 범례, 여러개의 항목 {} 자료
      {
        label: '사이다 판매량', // 항목명
        data: [12, 19, 3, 5, 2, 3], // 항목의 (세로축)값
        borderWidth: 1 // 선굵기
      },
      {
        label: '콜라판매량', // 항목명
        data: [15, 10, 2, 4, 10, 4], // 항목의 (세로축)값
        borderWidth: 1 // 선굵기
      }
      ]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });

// [3] 인천 부평구 맛집 현황
const api3 = () => {

    const url = 'https://api.odcloud.kr/api/15103411/v1/uddi:0875260e-d807-49b7-85fe-adb00bfa76ce?page=1&perPage=70&serviceKey='
    const serviceKey = 'g3%2F4abJMx8%2Ft50L57TYyOXozOVU7JlqcIHyDuZx5ZU851x5yIGMJ3dZwSJ03Vxcs20Wqk6c6vnZl27%2F0AMWH2Q%3D%3D'
    const url2 ='https://api.odcloud.kr/api/15103411/v1/uddi:0875260e-d807-49b7-85fe-adb00bfa76ce?page=1&perPage=70&serviceKey=g3%2F4abJMx8%2Ft50L57TYyOXozOVU7JlqcIHyDuZx5ZU851x5yIGMJ3dZwSJ03Vxcs20Wqk6c6vnZl27%2F0AMWH2Q%3D%3D'

    fetch(url+serviceKey)
        .then(r => r.json())
        .then(responseData=>{console.log(responseData);

        const boardTable2 = document.querySelector('#boardTable2')
        let html='';
        responseData.data.forEach((obj)=>{
            html += `<tr>
            <td>${obj['연번']}</td>
            <td>${obj['소재지']}</td>
            <td>${obj['업 소 명']}</td>
            <td>${obj['업태']}</td>
            <td>${obj['전화번호']}</td>
            <td>${obj['지정메뉴']}</td>
            </tr>`
        })
        boardTable2.innerHTML=html;
    })
        .catch(error=>console.log(error))
}
api3();

// [2] 사업자 상태 조회 요청 함수
const api2 = () => {
    // 1. 입력받은 데이터(사업자번호) 가져오기
    const companyIdInput = document.querySelector('#companyIdInput')
    const companyId = companyIdInput.value;

    // 2. 요청 자료 만들기 // 입력받은 사업자 번호 api 요청 형식에 맞게 구성
    const data = { "b_no" : [companyId.replaceAll('-','')] } //사업자 번호에 ' - '있을경우 replace 함수 이용하여 - 제거

    // 2-1 url
    const url = 'https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey='
    const serviceKey = 'g3%2F4abJMx8%2Ft50L57TYyOXozOVU7JlqcIHyDuZx5ZU851x5yIGMJ3dZwSJ03Vxcs20Wqk6c6vnZl27%2F0AMWH2Q%3D%3D'
    // 2-2 option (post)
    const option = {
    method : 'POST',
    headers : {'Content-Type' : 'application/json'},
    body : JSON.stringify(data)
    }

    // 3. fetch
    fetch(url + serviceKey , option)
        .then(response => response.json())
        .then(responseData => {
            console.log(responseData);
            // 만약 요청 성공시 응답 자료의 결과를 HTML 에 출력하기
            const resultArea = document.querySelector('#resultArea')
            let html = responseData.data[0].tax_type
            resultArea.innerHTML = html
        })
        .catch(e=> {console.log(e);})
}

// [1] 부평구 인구 현황 요청 함수
const api1=()=>{

    // 1. 요청할 URL
    const url = 'https://api.odcloud.kr/api/3044322/v1/uddi:466eee86-a8be-447b-9c8e-802bdbe897d7?page=1&perPage=23&serviceKey='

    // 2. 요청할 API 인증 KEY, 개별 발급
    const serviceKey = 'g3%2F4abJMx8%2Ft50L57TYyOXozOVU7JlqcIHyDuZx5ZU851x5yIGMJ3dZwSJ03Vxcs20Wqk6c6vnZl27%2F0AMWH2Q%3D%3D'

    // 3. fetch 이용한 API 통신
    fetch(url + serviceKey) // url과 serviceKey
        .then(r => r.json())
        .then(responseData=>{console.log(responseData);

        // (1) 출력할 DOM(HTML 를 객체 표현) 가져온다.
        const boardTable1 = document.querySelector('#boardTable1')
        // (2) 출력할 내용을 저장할 변수 선언한다.
        let html = '';
        // (3) 출력할 자료를 반복문 이용하여 여러개 자료를 html 문법 표현한다.
        responseData.data.forEach((obj)=>{
            // 객체내 속성값을 호출하는 방법 : 객체변수명.속성명 vs 객체변수명['속성명']
                        // 객체내 속성값 호출시 주의할점 : 속성명에 특수문자가 있을 경우에는 ['속성명'] 없으면 .속성명
                        // toLocaleString js 천단위 쉼표
            html +=`<tr>
                        <td>${obj['동별']}</td>
                        <td>${obj['세대수']}</td>
                        <td>${obj['인구수(계)']}</td>
                        <td>${obj['인구수(남)']}</td>
                        <td>${obj['인구수(여)']}</td>
                    </tr>`
        })
        // (4) 출력할 DOM에 생성한 HTML 대입하기.
            boardTable1.innerHTML=html;
        })
        .catch(error=>console.log(error))
}
api1(); // 함수 실행