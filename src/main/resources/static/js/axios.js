
// [1] 비동기 함수 란?

const test1 = () => {
    console.log('test1');
    const result = axios.get('/students')
    console.log(result.data)
    console.log('test2');
    // 실행 순서 : 첫번째 콘솔 -> 두번째 콘솔(undefined) -> 세번째 콘솔
    // 즉) JS흐름이 AXIOS를 이용하여 요청을 보내고 응답을 기다리지 않고 두번째 콘솔을 출력 했으므로 undefined
}
test1()

// [2] 동기식 함수란?
// 1. 함수내 매개변수( ) 앞에 'async' 키우드를 붙인다. 2. axios/fetch 앞에 'await' 키워드 붙인다.
const test2 = async () => {
    console.log('(2)test1')
    const response = await axios.get('/students');
    console.log(response.data)
    console.log('(2)test')
    // 실행순거 : 첫번째 -> 두번째 -> 세번째
    // 즉) JS 흐름이 AXIOS 를 이용하여 요청을 보내고 응답을 올 때까지 기다린다. 응답이 오면 다음코드를 실행한다.
}
test2()

// [3] 학생점수등록 버튼 클릭했을때.
const onSave = async () => {
    // 입력받은 값 가져오기
    const name = document.querySelector('.name').value;
    const kor = document.querySelector('.kor').value;
    const math = document.querySelector('.math').value;

    // 2. 객체화,
    // const obj = {name : name, kor:kor, math:math};
    // JS에서는 객체 속성명과 대입변수명이 같다면 생략가능
    const obj = {name,kor,math}

    // 선택 1 (fetch)
    /*
    const option = {
        method : 'POST',
        header : {'Content-Type' : 'application/json'},
        body : JSON.stringify(obj)
    }
    fetch('/student',option)
        .then(r=>r.json())
        .then(d=>{
            console.log(d);
        })
        .catch(e=>{console.log(e)})
    */
    // 선택 2 (axios) + 동기 : 'application/json' 언급하지 않아도 기본적으로 'application/json' 사용한다.
    const response = await axios.post('/students',obj) //
    console.log(response.data); //response 응답(정보)객체, response.data응답 본문 내용
    if(response.data == 1){
        alert('글쓰기 성공'); onFindAll(); // 알림 및 새로고침
    } else {alert("글쓰기 실패");}
}

// [4] 학생점수 전체 조회(동기화)
const onFindAll = async () => {
    // (1) AXION 이용한 SPRING controller 매핑
    const response = await axios.get('/students')
    // (2) 응답 본문 꺼내기
    const data = response.data;
    // (3) 반복문을 이용, HTML 출력
    // 어디에
    const tbody = document.querySelector('tbody')
    console.log(response.data);
    //무엇을
    let html = ``;
    data.forEach((student) => {
        html += `
            <tr>
                <td>${student.sno}</td>
                <td>${student.name}</td>
                <td>${student.kor}</td>
                <td>${student.math}</td>
            </tr>
            `;
    })
    // 출력
    tbody.innerHTML = html;
}
onFindAll();