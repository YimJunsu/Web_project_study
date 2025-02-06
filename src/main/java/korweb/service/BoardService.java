package korweb.service;

import jakarta.transaction.Transactional;
import korweb.model.dto.BoardDto;
import korweb.model.dto.MemberDto;
import korweb.model.entity.BoardEntity;
import korweb.model.entity.CategoryEntity;
import korweb.model.entity.MemberEntity;
import korweb.model.repository.BoardRepository;
import korweb.model.repository.CategoryRepository;
import korweb.model.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository; // member 엔티티 조작 인터페이스
    @Autowired
    CategoryRepository categoryRepository;  // category 엔티티 조작 인터페이스

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
    public List<BoardDto> boardFindAll() {
        // (1) 모든 게시물 엔티티를 조회
        List<BoardEntity> boardEntityList = boardRepository.findAll(); // (모든 엔티티를 조회)
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
            BoardDto boardDto = entity.toBDto();
            // [3] 변환된 dto 를 dto 리스트에 담는다.
            boardDtoList.add(boardDto);
        });
        // (3) 결과를 리턴한다.
        return boardDtoList;
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
}
