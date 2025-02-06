package korweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file")
@Getter@Setter@ToString@Builder
@AllArgsConstructor@NoArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fno; // 1. 첨부파일번호(pk)
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String fname; // 2. 첨부파일명
    // 3. 첨부파일이 위치할 게시물번호 : 게시물번호(FK) : 단방향
    // 어떤 게시물에 올릴건지
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bno")
    private BoardEntity boardEntity;
}
