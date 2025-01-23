// [1] 회원가입 함수
const onSignUp = () => {console.log('함수 실행됨.');

    // (1) Input DOM 가져온다.
    let midInput = document.querySelector('.midInput'); console.log(midInput)
    let mpwdInput = document.querySelector('.mpwdInput'); console.log(mpwdInput)
    let mpwdCheckInput = document.querySelector('.mpwdCheckInput'); console.log(mpwdCheckInput)
    let mnameInput = document.querySelector('.mnameInput'); console.log(mnameInput)
    let mmailInput = document.querySelector('.mmailInput'); console.log(mmailInput)


    // (2) DOM의 value(입력받은값) 반환 받는다.
    let mid = midInput.value; console.log(mid);
    let mpwd = mpwdInput.value; console.log(mpwd); 
    let mpwdcheack = mpwdCheckInput.value; console.log(mpwdcheack);
    let mname = mnameInput.value; console.log(mname);
    let mmail = mmailInput.value; console.log(mmail);


    // (3) 입력받은 값들을 객체화 한다.
    let dataObj = { mid : mid , mpwd : mpwd, mname : mname, mmail : mmail}
    console.log(dataObj);
    // (4) fetch 옵션
    const option = {
        method : 'POST', // - HTTP 통신 요청 보낼때 사용할 METHOD 선택
        headers : {'Content-Type' : 'application/json'}, // - HTTP 통신 요청 보낼때 header body(본문) 타입 설정
        body : JSON.stringify(dataObj) // HTTP 통신 요청 보낼때 body(본문) 자료 대입하는데 
                    //JSON.stringify(객체)  : 객체 타입 --> 문자열타입 변환, HTTP는 문자열 타입만 전송이 가능하다.
    };
    // (5) fetch 통신
    fetch('/member/signup', option) // fetch('통신할URL', 옵션)
        .then(response => response.json()) // .then() 통신 요청 보내고 응답객체를 반환받고 .json() 를 이용한 응답객체를 json타입으로 변환
        .then(data => { // .then() json으로 변환된 자료를 실행문 처리
            // (6) fetch 응답에 따른 화면 구현
            if(data == true){alert('회원가입 완료'); location.href="/member/login";}
        else {alert('가입실패')}
        })
        .catch(error => {alert('가입 오류 : 관리자에게 문의');})
}

