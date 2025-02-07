// *썸머노트 실행

$(document).ready(function() {
  $('#summernote').summernote({
  height : 500, // 썸머노트 게시판의 노트 조절
  lang: 'ko-KR', // 썸머노트 메뉴 한글화
  placeholder : '게시물 내용 입력해주세요.' // 입력전 가이드라인 제공 속성
  });
});

// [1] 게시글 등록 요청 함수
const onWrite = () => {
    // [1] 현재 html의 DOM 객체의 입력 받은 값(value) 가져오기
    const cno=document.querySelector('.cno').value
    const btitle=document.querySelector('.btitle').value
    const bcontent=document.querySelector('.bcontent').value

    // [2] 입력받은 값들을 JSON 본기 위해 입력받은 값 객체로 만들기
    const obj = { cno : cno , btitle : btitle, bcontent : bcontent}

    // [3] fetch
    const option = {
        method : 'POST',
        headers : {'Content-Type' : 'application/json'},
        body : JSON.Stringify(obj)
    }
    fetch('/board/write.do' , option )
        .then(r => r.json())
        .then(data => {
            console.log(data);
            if(data==true){
                alert('글쓰기 성공');
                location.href = `/board?cno=${cno}`
            }else{
                alert('글쓰기 실패 : 로그인 해주세요');
            }
        });
        .catch(e => {console.log(e)};)
}