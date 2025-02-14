package korweb.service;

import jakarta.transaction.Transactional;
import korweb.model.dto.BoardDto;
import korweb.model.dto.MemberDto;
import korweb.model.dto.PageDto;
import korweb.model.entity.BoardEntity;
import korweb.model.entity.CategoryEntity;
import korweb.model.entity.MemberEntity;
import korweb.model.entity.ReplyEntity;
import korweb.model.repository.BoardRepository;
import korweb.model.repository.CategoryRepository;
import korweb.model.repository.MemberRepository;
import korweb.model.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardService {
    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository; // member 엔티티 조작 인터페이스
    @Autowired private CategoryRepository categoryRepository;  // category 엔티티 조작 인터페이스
    @Autowired private ReplyRepository replyrepository;
    // [1] 게시글 쓰기
    public boolean boardWrite(BoardDto boardDto) {
        // (1) 사용자로부터 전달받은 boardDto(btitle, bcontent, cno)를 엔티티로 변환
        // 0. boardDto 를 entity 로 변환
        BoardEntity boardEntity = boardDto.toBEntity();
        // 1. 게시물 작성자는 현재 로그인된 회원이므로 세션에서 현재 로그인된 회원번호 조회
        // - 현재 로그인된 세션 객체 조회
        MemberDto loginDto = memberService.getMyInfo();
        // - 만약에 로그인된 상태가 아니면 글쓰기 종료
        if (loginDto == null) return false;
        // - 로그인된 상태이면 회원번호 조회
        int loginMno = loginDto.getMno();

        // 1. 로그인된 회원번호를 글쓰기 작성자로 대입한다.
        MemberEntity loginEntity = memberRepository.findById(loginMno).get();
        boardEntity.setMemberEntity(loginEntity);
        // 2. 게시물 카테고리는 cno 를 entity 조회해서 게시물 엔티티에 대입한다.
        CategoryEntity categoryEntity = categoryRepository.findById(boardDto.getCno()).get();
        boardEntity.setCategoryEntity(categoryEntity);

        // (2) 엔티티 save
        BoardEntity saveBoardEntity = boardRepository.save(boardEntity);
        // (3) 만약에 게시물 등록 성공 했다면
        if (saveBoardEntity.getBno() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // [2] 게시글 전체 조회
    public PageDto boardFindAll(int cno, int page, String  key, String keyword) {
        System.out.println("카테고리 번호 : "+cno);
        System.out.println("페이비 번호 : "+page);
        // 페이징처리 방법 : 1.SQL 2.라이브러리(*JPA*)
        // 1. 페이지 처리 설정, PageRequest.of(페이지번호, 페이지당갯수, 정렬);
        Pageable pageable = PageRequest.of(page-1, 3 , Sort.by(Sort.Direction.DESC, "bno"));
        // 2. find~~~(pageable) , find~~(pageable) 매개변수로 설정 넣어주면 반환값은 Page

        // (1-1) 모든 게시물 엔티티를 조회
        //List<BoardEntity> boardEntityList = boardRepository.findAll(); // (모든 엔티티를 조회)
        // (1-2) 모든 게시물의 엔티티를 조회 + 페이징처리
        // Page<BoardEntity> boardEntityList = boardRepository.findAll(pageable);
        // (1 - 3) 특정한 카테고리의 엔티티를 조회 + 페이징처리
        // Page<BoardEntity> boardEntityList = boardRepository.findByCategoryEntity_Cno(cno, pageable);
        // (1 - 4) 특정한 카테고리의 키워드 검색 조회 + 페이징 처리
        Page<BoardEntity> boardEntityList = boardRepository.findBySearch(cno, key, keyword, pageable);
        System.out.println(boardEntityList); // 확인용 출력

            // * cno 이용한 동일한 cno의 게시물 정보 찾기 + 0207
        // (2) 모든 게시물의 엔티티를 DTO로 변환
        // - DTO를 저장할 리스트 선언
        List<BoardDto> boardDtoList = new ArrayList<>();
        // - 반복문을 이용하여 모든 엔티티를 dto로 변환하기
        // [1-1] foreach() 사용 리스트 변수명.foreach(반복변수명 -> {} );
        boardEntityList.forEach(entity -> {
            // [1-2] for 문 사용
            // for(int index = 0; index <= boardEntityList.size() -1; index++){
            // boardEntityList.get(index);}
            // [2] 엔티티 --> dto 변환
            // * 만약 현재 조회중인 게시물의 카테고리가 선택한 카테고리와 같다면
            if (entity.getCategoryEntity().getCno() == cno) {
                BoardDto boardDto = entity.toBDto();
                // [3] 변환된 dto 를 dto 리스트에 담는다.
                boardDtoList.add(boardDto);
            }else{

            }
        });
        // (3) 결과를 리턴한다.
//        return boardDtoList;
        // [*] 페이징 처리된 게시물 정보(자료) 외 페이징 정보도 같이 반환
        // (1) 현재페이지번호 = page
        // (2) 전체페이지번호 = totalPage, JPA의 getTotalPages() : 조회된 정보의 전체 페이지 수 반환 함수
        int totalPage = boardEntityList.getTotalPages();
        // (3) 전체조회된수 = totalCount, JPA의 .getTotalElements() : 조회된 정보의 전체 개수 반환 함수
        long totalCount = boardEntityList.getNumberOfElements();
        int btnSize = 5; // 페이지당 표시할 페이지버튼 수
        // (4) 조회 페이지의 페이징버튼 시작번호, 계산식 : ((현재페이지번호-1)/페이징버튼수) * 페이징버튼수 +1;
        int startBtn = ((page-1) / btnSize) * btnSize + 1;
        // (5) 조회 페이지의 페이징버튼 끝번호, 계산식 : (시작번호 + (페이징버튼수-1)
        int endBtn = startBtn + (btnSize -1);
            // 만약에 페이징번호 끝번호는 전체 페이지수 보다 커질수 없다.
        if(endBtn == startBtn) endBtn = totalPage;
        // 페이징 DTO 이용한 페이징정보와 자료를 같이 응답/리턴
        PageDto pageDto = PageDto.builder()
                .totalcount(totalPage)
                .page(page)
                .totalpage(totalPage)
                .startbtn(startBtn)
                .endbtn(endBtn)
                .data(boardDtoList)
                .build();
        // 페이징 dto 를 반환한다.
        // List<boardDto>로 반환했었지만 현재는 페이징 dto로 반환, 페이징 dto 안에 boardDtoList 존재
        return pageDto;
    }
    // [3] 게시글 특정(개별) 조회
    @Transactional
    public BoardDto boardFind(int bno) {
        // (1) 조회할 특정 게시물의 번호를 매개변수로 받는다. int bno
        // (2) 조회할 특정 게시물의 번호의 엔티티를 조회한다. .findById() 메소드는 반환타입이 Optional 이다. 조회된 엔티티 여부 메소드 제공한다.
        Optional<BoardEntity> optional = boardRepository.findById(bno);
        // (3) 만약에 조회된 엔티티가 있으면 .isPresent() -> 내용물 있다,없다 알려줌
        if (optional.isPresent()) {
            // (4) optional 에서 엔티티 있으면 꺼내기, .get()
            BoardEntity boardEntity = optional.get();
            // (5) 엔티티를 Dto로
            BoardDto boardDto = boardEntity.toBDto();
                // (5-1) 현재 게시물의 댓글 리스트 조회 / 0210
                // 1. 모든 게시물 댓글 조회한다.
            List<ReplyEntity> replyEntityList = replyrepository.findAll();
                // 2. 모든 댓글을 DTO/MAP으로 변환한 객체들을 저장할 리스트 선언 -> ReplyDto 대신 MAP 컬렉션 이용한 방법
                // **List 컬렉션 : [ 값, 값, 값 ] vs Map 컬렉션 : {key : value, key : value, key : value }
            List<Map<String, String>> replylist = new ArrayList<>();
                // 3. 엔티티를 MAP로 변환
            replyEntityList.forEach((reply) -> {
                //*만약에 댓글번호의 게시물번호 와 게시물 번호가 같으면
                if(reply.getBoardEntity().getBno() == bno) {
                    // 4. map 객체 선언
                    Map<String, String> map = new HashMap<>();
                    // 5. map 객체에 하나씩 key:value (엔트리)로 지정한다.
                    map.put("rno", reply.getRno() + ""); // 숫자타입 +"" => 문자타입 변환
                    map.put("rcontent", reply.getRcontent() + "");
                    map.put("cdate", reply.getCdate().toLocalDate().toString()); // 날짜와 시간
                    map.put("mid", reply.getMemberEntity().getMid()); // 댓글 작성자 아이디
                    map.put("mimg", reply.getMemberEntity().getMimg());
                    // 6. map를 리스트에 담는다.
                    replylist.add(map);
                }
                });
                // 7. 반복문 종료된 후 boardDto에 댓글리스트 담기
            boardDto.setReplylist(replylist);
            // (6) Dto로 반환
            return boardDto;
        }
        return null; // 없으면 null 값 반환
    }

    // [4] 게시글 수정
    @Transactional
    public boolean boardUpdate(BoardDto boardDto) {
        BoardEntity boardEntity = boardRepository.findByBno(boardDto.getBno());
        if(boardDto.getBno()>0) {
            boardEntity.setBtitle(boardDto.getBtitle());
            boardEntity.setBcontent(boardDto.getBcontent());
            return true;
        } return false;
    }

    // [5] 게시글 삭제
    public boolean boardDelete(int bno) {
            boardRepository.deleteById(bno);
            return false;
    }

    // ======================댓글================

    // [6] 댓글 쓰기
    public boolean replyWrite(Map<String,String> replyDto){

        // 1. 현재 로그인된 회원번호 조회
        MemberDto memberDto = memberService.getMyInfo();
        // 2. 만약에 로그인된 정보가 없으면 함수 종료
        if(memberDto == null) return false;
        // [ 로그인 중이면 ]
        // 3. 회원엔티티 조회
        MemberEntity memberEntity = memberRepository.findById(memberDto.getMno()).get();
        // 3. 현재 작성할 댓글이 위치한 조회중인 게시물 엔티티 조회
            // Integer.parseInt("문자열") 문자열 타입 --> 정수 타입 변환 함수
        int bno = Integer.parseInt(replyDto.get("bno"));
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        // 4. 입력받은 매개변수 map를 entity로 변환
        ReplyEntity replyEntity = new ReplyEntity();
        replyEntity.setRcontent(replyDto.get("rcontent")); // 댓글 내용 등록
        replyEntity.setMemberEntity(memberEntity); // 작성자 등록
        replyEntity.setBoardEntity(boardEntity); // 댓글 위치한 게시물 등록
        // 5. 생성한 entity를 저장한다.
        ReplyEntity saveEntity = replyrepository.save(replyEntity);
        if(saveEntity.getRno()>0){return true;}
        else {return false;}
    }

    // [7] 댓글 전체 조회
    public List<Map<String, String>>replyFindAll(int bno){
        // 1. 모든 댓글 엔티티 조회
        List<ReplyEntity> replyEntityList = replyrepository.findAll();
        // 2. 모든 갯글 amp 저장할 list 선언
        List<Map<String, String>> replylist = new ArrayList<>();
        // 3. 모든 댓글 엔티티를 반복문으로 조회
        replyEntityList.forEach((reply) -> {
            //*만약에 댓글번호의 게시물번호 와 게시물 번호가 같으면
            if(reply.getBoardEntity().getBno() == bno) {
                // 4. map 객체 선언
                Map<String, String> map = new HashMap<>();
                // 5. map 객체에 하나씩 key:value (엔트리)로 지정한다.
                map.put("rno", reply.getRno() + ""); // 숫자타입 +"" => 문자타입 변환
                map.put("rcontent", reply.getRcontent() + "");
                map.put("cdate", reply.getCdate().toLocalDate().toString()); // 날짜와 시간
                map.put("mid", reply.getMemberEntity().getMid()); // 댓글 작성자 아이디
                map.put("mimg", reply.getMemberEntity().getMimg());
                // 6. map를 리스트에 담는다.
                replylist.add(map);
            }
        });
        return replylist;
    }
}
