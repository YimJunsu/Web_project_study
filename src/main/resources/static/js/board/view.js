console.log( 'view.js open' )

// [1] 개별 게시물 조회 요청 함수
const onFind = () =>{

    // 1. 현재 보고자하는 게시물의 번호를 찾기 / 사용자가 클릭한 게시물 번호 찾기
    const bno = new URL( location.href ).searchParams.get('bno')

    // 2. fetch
    fetch( `/board/find.do?bno=${bno}` )
        .then( r => r.json() )
        .then( data => {
            console.log( data );
            document.querySelector('.midbox').innerHTML = data.mid
            document.querySelector('.bviewbox').innerHTML = data.bview
            document.querySelector('.cdatebox').innerHTML = data.cdate

            document.querySelector('.btitle').innerHTML = data.btitle
            document.querySelector('.bcontent').innerHTML = data.bcontent
        })
        .catch( e =>{ console.log(e); })

}
onFind(); // 페이지가 열릴때 개별 게시물 조회 함수 실행

// [2] 댓글 쓰기 요청 함수, 실행조건 : 댓글 게시 버튼을 클릭했을때
const onReplyWrite = () => {
    // 1. 입력받은 값 가져오기
    const rcontentInput = document.querySelector('.rcontentInput')
    const rcontent = rcontentInput.value;
    // 2. 현재 게시물 번호는 URL 쿼리스트링에서 가져오기
    const bno = new URL(location.href).searchParams.get('bno')
    // 3. 객체화
    const obj = { rcontent : rcontent , bno : bno }
    // 4. fetch
    const option = {
        method : 'POST',
        headers : {'Content-Type' : 'application/json'},
        body : JSON.stringify(obj)
    }
    fetch('/reply/write.do' , option)
        .then(r=>r.json())
        .then(data=>{
            if(data==true){
                alert('댓글등록');
            } else {
                alert('댓글 등록 실패');
            }
        })
}

// [3] 개별 게시물의 존재하는 댓글 조회 요청 함수
const onReplyFindAll = () => {}
