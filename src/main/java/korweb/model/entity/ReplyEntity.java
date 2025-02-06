package korweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reply")
@Getter@Setter@Builder@ToString
@NoArgsConstructor@AllArgsConstructor
public class ReplyEntity extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String rcontent; // 댓글 내용
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity; // 댓글 작성자 : 작성자 번호 : 단방향
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bno")
    private BoardEntity boardEntity; // 게시글 번호 : 단방향
}
