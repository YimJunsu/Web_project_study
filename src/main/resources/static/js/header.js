// [1] 로그인 정보 요청 함수 정의
const getLoginMid = ( ) =>{
    //  fetch 함수 활용하여 현재 로그인 상태 체크 
    // 1. fetch option
    const option = { method : 'GET' }
    // 2. fetch
    // 1. 출력할 위치 DOM 가져오기
    let memberBox = document.querySelector('.memberBox')
    let html = '';
    fetch( '/member/myinfo' , option )
        .then( response => response.json() ) // SyntaxError: Unexpected token 'q', "qweqwe" is not valid JSON
        //.then( response => response.text() ) // String controller 에서 String 타입으로 반환할 경우에는 .text() 함수로 변환해야한다. // day70 : dto 반환하므로 json()변경
        .then( data => {  console.log(data);
            // - 로그인 상태에 따라 버튼 활성화 여부 다르게 표현
            console.log( '로그인상태');
            //3. 로그아웃 버튼 , 마이페이지 버튼 , 로그인된 아이디 활성화
            html +=`<li class="nav-item">
                        <img src="${ data.mimg.includes('http') ? data.mimg : '/img/'+data.mimg }"
                        style=" width:60px; height:60px; padding: 1px; object-fit: cover; border-radius: 30px;  border:1px solid #bfbebe;" />
                        ${ data.mid }님
                        <span class="pointbox"> </span> </a>
                    </li>
                    <li class="nav-item">  <a class="nav-link" href="#" onclick="logOut()"> 로그아웃 </a> </li>
                    <li class="nav-item">  <a class="nav-link" href="/member/info"> 마이페이지 </a> </li>`
            // 4. 출력하기 
            memberBox.innerHTML = html;
            // 5. 포인트 지급 불러오기
             myPointInto();
            //=======================================================================
            const clientSocket = new WebSocket('ws://localhost:8080/loginsocket/server')
            //console.log(clientSocket);    
            // [2] 클라이언트 웹 소켓 속성
            // 1. 만약에 클라이언트 웹 소켓이 서버소켓과 연결을 성공 했을 때 실행되는 함수 구현
            clientSocket.onopen = (event) => {
            console.log("서버소켓에 연동 성공했다!")

            // (2) 클라이언트가 서버소켓에 접속했을때
            // type : 메시지의 종류, message : 메시지의 본문 내용
            let msg = {'type' : 'alarm' , 'message': `${data.mid}님이 로그인 했습니다..!`}
            console.log(msg);
            // 소켓은 문자열만 정송이 가능 하므로 JSON.stringify() 이용한 문자열타입으로 전송하기
            clientSocket.send(JSON.stringify(msg))

            clientSocket.onmessage = (event) => {
                console.log('서버소켓으로 부터 메시지를 받았다.');
                console.log( event ); // 받은 메시지 통신 정보 객체
                console.log( event.data ); // 받은 메시지 본문 
                
                let message = JSON.parse(event.data);

                if(message.type == 'alarm'){
                    let toastContainer  = document.querySelector('.toast-container');
                    toastContainer.innerHTML+=`
            <div class="toast-header">
                <img src="/" class="rounded me-2" alt="/">
                <strong class="me-auto">알림</strong>
                <small class="text-body-secondary">방금</small>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
            <div class="toast-body">
                ${message.message}
            </div>
        `;
            toastContainer.appendChild(toastElement); // Toast를 화면에 추가
            let toast = new bootstrap.Toast(toastElement);
            toast.show(); // 토스트 표시
                }
            }
        }
        })
        .catch( error => {   console.log( error); console.log( '비로그인상태');
            // 3. 회원가입 버튼 , 로그인 버튼 활성화
            html += `<li class="nav-item">  <a class="nav-link" href="/member/signup"> 회원가입 </a> </li>
                    <li class="nav-item">  <a class="nav-link" href="/member/login"> 로그인 </a> </li>`
            // 4. 출력하기
           memberBox.innerHTML = html;
        })
        
} // f end

getLoginMid(); // JS 실행될때. 로그인 정보 요청 함수 호출 

//// [2] 로그아웃 함수
//const logOut = ( ) => {
//    // 1. fetch option
//    const option = { method : 'GET' }
//    // 2. fetch
//    fetch( '/member/logout' , option )
//        .then( response => response.json() )
//        .then( data => {
//            // 만약에 로그아웃 성공 했다면 로그인 페이지로 이동
//            if( data == true ){ alert('로그아웃 했습니다.'); location.href="/member/login"; }
//        })
//        .catch( e => { console.log(e); })
//} // f end
// 로그아웃 함수 시큐리티 버전
const logOut = () => {
    // 시큐리티에서는 로그아웃 Get 방식
    location.href="/member/logout.do";
}

// [3] 내 포인트 함수
const myPointInto =  ( ) =>{
    // 1. fetch option
    const option = { method : 'GET' }
    // 2. fetch
     fetch( '/member/point/info' , option )
        .then( response => response.json() )
        .then( data => {
                  // 1. 출력할 위치 DOM 가져오기
              let pointbox = document.querySelector('.pointbox')
                  // 2.
              let html =`${ data } Point `
                // 3. 출력하기
              pointbox.innerHTML = html;
        })
        .catch( e => { console.log(e); })
}

//=========================================================


const toastTrigger = document.getElementById('liveToastBtn')
const toastLiveExample = document.getElementById('liveToast')

if (toastTrigger) {
  const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
  toastTrigger.addEventListener('click', () => {
    toastBootstrap.show()
  })
}