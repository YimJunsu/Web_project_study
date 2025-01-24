package korweb.model.dto;

import korweb.model.entity.PointEntity;
import lombok.*;

@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class PointDto {
    private int pno;
    private String pname;
    private int pcount;
    private String cdate;
    private String udate;

    public PointEntity toPEntity(){
        return PointEntity.builder()
                .pno(this.pno)
                .pname(this.pname)
                .pcount(this.pcount)
                .build();
    }
}
