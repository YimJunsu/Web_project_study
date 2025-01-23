// [2] 로그인 함수
const onLogin = () => {console.log("!로그인 함수 실행!")
    // (1) Input DOM 가져오기
    let midInput = document.querySelector('.midInput'); console.log("1번실행")
    let mpwdInput = document.querySelector('.mpwdInput'); console.log("2번실행")
    // (2) 가져온 DOM의 value(입력값) 가져오기
    let mid = midInput.value;
    let mpwd = mpwdInput.value;
    // (!!) 유효성 검사 생량

    // (3) 입력받은 값들을 보낼 객체 만들기
    let dataObj = { mid : mid , mpwd : mpwd }
    // (4) fetch 함수 옵션 정의
    const option = {
        method : 'POST',
        headers : {'Content-Type':'application/json'},
        body : JSON.stringify(dataObj)
    };
    // (5) fetch 함수 실행
    fetch('/member/login', option)
        .then(r => r.json())
        .then(d => {
            if(d == true){alert('로그인 성공'); location.href="/";}
            else{alert('회원정보가 일치하지 않습니다.');}
        })
        .catch(e => {alert('시스템 오류 : 관리자에게 문의'); console.log(e);
         })
    }