package korweb.model.dto;

import lombok.*;

@Getter@Setter@Builder@ToString
@NoArgsConstructor@AllArgsConstructor
public class CategoryDto {
    private int cno;
    private String cname;
    private String cdate;
    private String udate;
}
