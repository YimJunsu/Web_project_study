package korweb.model.entity;

import jakarta.persistence.*;
import korweb.model.dto.BoardDto;
import lombok.*;
@Entity
@Table(name = "board")
@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class BoardEntity extends BaseTime{
    // 1. 게시물번호(PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    // 2. 게시물제목
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String btitle;
    // 3. 게시물내용
    @Column(columnDefinition = "longtext")
    private String bcontent;
    // 4. 게시물조회수
    @Column
    private int bview;
    // 5. 작성자 번호(FK)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity;
    // 6. 카테고리 번호(FK)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cno")
    private CategoryEntity categoryEntity;

    // Entity -> Dto 변환 메소드
    // 데이터베이스에 저장된 entity를 조회한 후 dto로 변환해야 하므로
    public BoardDto toBDto(){
        return BoardDto.builder()
                .bno(this.bno)
                .btitle(this.btitle)
                .bcontent(this.bcontent)
                .bview(this.bview)
                .build();
    }
}
