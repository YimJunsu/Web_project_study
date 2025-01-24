package korweb.model.entity;

import jakarta.persistence.*;
import korweb.model.dto.MemberDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class MemberEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mno;
    @Column(columnDefinition = "varchar(30)", nullable = false, unique = true /* , length = 30 으로도 가능 */ )
    private String mid;
    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String mpwd;
    @Column(columnDefinition = "varchar(20)", nullable = false)
    private String mname;
    @Column(columnDefinition = "varchar(40)", nullable = false, unique = true)
    private String mmail;


    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<PointEntity> pointEntityList = new ArrayList<>();


    // 엔티티 -> DTO
    public MemberDto toMDto(){
        return MemberDto.builder()
                .mno(this.mno)
                .mid(this.mid)
                //패스워드 보안상 생략
                .mname(this.mname)
                .mmail(this.mmail)
                .build();
    }
}