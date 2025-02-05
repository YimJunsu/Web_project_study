package korweb.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class CategoryEntity extends BaseTime{
    // 1. 카테고리 번호(Pk)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cno;
    // 2. 카테고리 명
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String cname;

}
