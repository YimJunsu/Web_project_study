package korweb.model.dto;

import korweb.model.entity.BoardEntity;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter@Setter@Builder@ToString
@NoArgsConstructor@AllArgsConstructor
public class BoardDto {
    private int bno;
    private String btitle;
    private String bcontent;
    private int bview;
    private int mno; // 작성자의 회원번호
    private int cno; // 카테고리 번호
    private String cdate; // 작성일

    // 화면에는 작성자의 회원번호가 아닌 아이디를 출력해야 하므로
    private String mid; // 작성자의 회원 아이디
    private String cname; // 카테고리명

    // +댓글 리스트
    private List<Map<String, String>> replylist;

    // Dto -> Entity 변환 메소드
    // dto 를 entity 객체로 변환해서 데이터베이스에 저장해야 하므로

    public BoardEntity toBEntity(){
        return BoardEntity.builder()
                .bno(this.bno)
                .btitle(this.btitle)
                .bcontent(this.bcontent)
                .bview(this.bview)
                .build();
    }
}
