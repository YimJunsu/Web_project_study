package korweb.model.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter@Setter@ToString@Builder
@NoArgsConstructor@AllArgsConstructor
// 시큐리티의 일반회원과 oauth2 회원 정보를 통합하는 Dto
public class LoginDto implements UserDetails , OAuth2User {
    // 필수
    private String mid; // 로그인할 회원의 아이디.
    private String mpwd; // 로그인할 회원의 비밀번호. (oauth2 회원은 사용하지 않은다.)
    private List<GrantedAuthority> mrolList; // 로그인 한 회원의 권한/등급/레벨 목록
    // + 선택 :
    //    private String name, String phone; -- 등등 도 가능

    // UserDetails : 시큐리티에서 일반회원들을 정보를 조작하는 인터페이스(미리 만들어짐 - 라이브러리)
    // OAuth2User : 시큐리티에서 oauth 회원들의 정보를 조작하는 인터페이서(미리 만들어짐 - 라이브러리)
        //-> 두 인터페이스를 LoginDto(내가만든 클래스) 에서 구현(implements)
        // 즉] LoginDto에서 두 인터페이스를 모두 포함하므로 LoginDto 타입으로 두 타입들을 조작할 수 있다.
    // 오른쪽 클릭 -> Generate(생성) -> 재정의 Override -> UserDetails, OAuth2User 전체

    // 로그인 한 회원의 아이디를 반환할 메소드 - 재정의
    @Override
    public String getName() {
        return this.mid;
    }
    // 계정 만료 여부 확인 , 기본값 (true) - 만료되지 않았다 / 됬으면 false
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }
    // 계정 잠금 여부 확인 , 기본값(ture) - 잠금이 아니면 true 회원이 사용못하게 / (휴면계정) 일 때 false
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }
    // 비밀번호 만료 여부 확인 , 기본값(true) - 문제 없으면 true 있으면 false 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
    // 계정 활성화 여부 - 기본값(true) , 계정 잠금 구현시 활용
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
    // 권한 목록을 반환하는 메소드 - 재정의
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.mrolList;
    }
    // 일반 회원이 로그인한 비밀번호를 반환하는 메소드 - 재정의
    @Override
    public String getPassword() {
        return this.mpwd;
    }
    // 일반 회원이 로그인한 아이디를 반환하는 메소드 - 재정의
    // 시큐리티에서는 Username <--> 로그인할때 사용하는 아이디
    @Override
    public String getUsername() {
        return this.mid;
    }
    // Oauth2 회원 에서 사용하는 oauth2 속성 객체 반환
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }
    // Oauth2 회원 에서 사용하는 oauth2 속성 객체 반환
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }
}
