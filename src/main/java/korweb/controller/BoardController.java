package korweb.controller;

import korweb.model.dto.BoardDto;
import korweb.model.dto.PageDto;
import korweb.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BoardController {
    @Autowired BoardService boardService;

    @PostMapping("/board/write.do")
    public boolean boardWrite(@RequestBody BoardDto boardDto){
        return boardService.boardWrite(boardDto);
    }

    // 카테고리별 게시물 전체 조회 + 페이징 처리( vs 무한스크롤) + 검색
    @GetMapping("/board/findall.do")
    public PageDto boardFindAll(@RequestParam int cno , @RequestParam int page, @RequestParam String key, @RequestParam String keyword){ // 조회할 카테고리 번호
        // cno = 조회할 카테고리 번호, page = 현재 페이지 번호, key = 검색할 데이터의 속성명(btitle, bcontent), key worword = 검색할 단어
        return boardService.boardFindAll(cno,page, key, keyword);
    }

    @GetMapping("/board/find.do")
    public BoardDto boardFind(@RequestParam int bno){
        return boardService.boardFind(bno);
    }

    @PutMapping("/board/update.do")
    public boolean boardUpdate(@RequestBody BoardDto boardDto){
        return boardService.boardUpdate(boardDto);
    }

    @DeleteMapping("/board/delete.do")
    public boolean boardDelete(@RequestParam int bno){
        return boardService.boardDelete(bno);
    }

    // === 댓글 ===
    // [6] 댓글 쓰기
    @PostMapping("/reply/write.do")
    public boolean replyWrite(@RequestBody Map<String, String>replyDto){ // *Dto 클래스 대신에 map 컬렉션 활용
        return boardService.replyWrite(replyDto);
    }

    // [7] 특정 게시물의 댓글 전체 조회
    @GetMapping("/reply/findall.do")
    public List<Map<String,String>> replyFindAll (@RequestParam int bno){
        return boardService.replyFindAll(bno);
    }
}
