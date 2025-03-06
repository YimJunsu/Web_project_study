package korweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링 컨테이너의 빈 등록
public class SecurityConfig {

    // [1] 시큐리티의 필터 정의하기
    @Bean // 필드 또는 메소드에 빈 등록하는 방법
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // [3] HTTP 요청에 따른 부여된 권한/상태 확인후 자원 허가 제한
        http.authorizeHttpRequests(
                (httpReq) -> {
                    // 3-4 : 관리자 페이지(admin) 엔 로그인회원이면서 Role 이 admin 이거나 team1 회원만 접근할 수 있다.
                    // 3-3 : 채팅페이지(chat) 에는 로그인 회원이면서 Role 이 User 회원만 접근 할 수 있다.
                    // 3-2 : 권한에 따른 페이지 제한 가능 / 글쓰기 페이지(board/write) 에는 로그인 된 회원만 접근 가능.
                    // 3-1 : 모든 http 요청에 '/**'(모든url 뜻) .antMatcher('http경로').premitAll() : 지정한 경로에는 누구나 접속할 수 있다.
                    httpReq
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/admin")).hasAnyRole(("admin") , "team1")
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/chat")).hasRole("USER")
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/board/write")).authenticated()
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll();
        });
        // [4] CSRF : post, put (Body) 요청을 금지 -> 특정한 URL 만 post/put 가능하도록 허용
        // 개발 : CSRF 사용안함
        http.csrf(AbstractHttpConfigurer :: disable); // csrf 끄기, --> post/put 사용할 수 있다.
        // 배포/운영 : 특정한 URL 수동으로 허용 , 운영환경에서는 안전하게 몇몇개의 REST만 허용한다.
        //http.csrf(csrf->csrf.ignoringRequestMatchers("csrf예외할URL"));
        http.csrf(csrf->csrf.ignoringRequestMatchers("/member/signup"));
        http.csrf(csrf->csrf.ignoringRequestMatchers("/member/login"));
        // [2] http 객체를 빌드/실행하여 보안 필터 체인을 생성
        return http.build();
    }

    // [2] 암호화 : 시큐리티가 회원의 패스워드 검즐할 때 사용할 암호화 객체


}
