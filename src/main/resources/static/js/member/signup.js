
/* [1] 업로드 전 회원가입 함수

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

    // (2-1) 비밀번호 길이 20자 이내로
    if(mpwd.length > 20){
        alert('비밀번호를 20자 이하로 입력해주세요')
        return;
    }
    if(mpwd !== mpwdcheack){
        alert('비밀번호가 일치하지 않습니다.')
        return;
    }
    fetch('/member/cheack')

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
        .catch(error => {alert('가입오류 : 관리자에게 문의');})
}
*/
// [2] (업로드) 회원가입 함수
const onSignUp = () => {
    // - 입력된 값을 하나씩 가져오는 방식(document.qs) 가 아닌 form 전체를 한번에 가져오기 (multipart/form-data)
    // [1] 전송할 form dom 객체 가져온다.
    const signupForm = document.querySelector('#signupForm');
        console.log(signupForm); // 폼 전체 가져왔는지 확인 작업

    // * 폼 전체를 전송할때는 controller 에 dto 멤버변수와 form 안에 있는 input 의 name 속성명이 동일하게 작성
    // <input name="mid"> <---동일---> MemberDto{private String mid;}

    // [2] form dom 객체를 바이트로 변환한다. new FormData(formDom객체) : 지정한 dom객체를 바이트로 변환
        // - HTTP 대용량(첨부파일일) 자료들은 바이너리(바이트) 단위로 전송하므로 일반 JSON으로 전송 불가능.
    const signupFormData = new FormData(signupForm);
        console.log(signupFormData); //'application/json' 형식이 아닌 'multiport/form-data' 형식으로

    // [3] application/json 이 아닌 multipart/form-data 형식의 fetch 설정하는 방법
    const option = {
        method : 'POST' ,
        // content-type 생략하면 자동으로 multipart/form-data 설정된다.
        body : signupFormData
        // JSON.stringfy() 안하는 이유 : 폼 전송 해야 하므로 생략한다.
    }
    // [4] fetch 사용한다.
    fetch('/member/signup',option)
        .then(response => response.json())
        .then((data => { // .then() json으로 변환된 자료를 실행문 처리
            // (6) fetch 응답에 따른 화면 구현
            if(data == true){alert('회원가입 완료'); location.href="/member/login";}
        else {alert('가입실패 : 입력하신 정보가 올바르지 않습니다.')}
        }))
        .catch(error => {alert('가입오류 : 관리자에게 문의');})
}
