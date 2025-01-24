package korweb.model.entity;

import jakarta.persistence.*;
import korweb.model.dto.PointDto;
import lombok.*;

@Entity
@Table(name = "point")
@Setter@Getter@Builder@ToString
@NoArgsConstructor@AllArgsConstructor
public class PointEntity extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pno;
    @Column(columnDefinition = "varchar(30)" , nullable = false)
    private String pname;
    @Column(columnDefinition = "int" , nullable = false)
    private int pcount;

    @ManyToOne
    @JoinColumn(name = "mno")
    private MemberEntity memberEntity;

    public PointDto toPDto(){
        return PointDto.builder()
                .pno(this.pno)
                .pname(this.pname)
                .pcount(this.pcount)
                .build();
    }
}
