package korweb.model.dto;

import lombok.*;

@Getter@Setter@Builder@ToString
@NoArgsConstructor@AllArgsConstructor
public class BoardDto {
    private int bno;
    private String btitle;
    private String bcontent;
    private int bview;
}
