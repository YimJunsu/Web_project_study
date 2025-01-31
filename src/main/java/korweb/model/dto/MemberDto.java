package korweb.model.dto;

import korweb.model.entity.MemberEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
public class MemberDto {
    private int mno;
    private String mid;
    private String mpwd;
    private String mname;
    private String mmail;
    private String cdate;
    private String udate;
    private String mimg; // 회원 프로필 사진명
    private MultipartFile uploadfile; // 업로드 파일객체

    // DTO -> 엔티티
    public MemberEntity toMEntity(){
        return MemberEntity.builder()
                .mno(this.mno)
                .mid(this.mid)
                .mpwd(this.mpwd)
                .mname(this.mname)
                .mmail(this.mmail)
                .mimg(this.mimg)
                .build();
    }

}
