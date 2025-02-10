package korweb.controller;

import korweb.model.dto.BoardDto;
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

    @GetMapping("/board/findall.do")
    public List<BoardDto> boardFindAll(@RequestParam int cno){ // 조회할 카테고리 번호
        return boardService.boardFindAll(cno);
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
