package korweb.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect // AOP 설정하는 어노테이션 주입
@Component // 스프링 컨테이너 빈 등록
public class Logcheck {
    // + StudentService 클래스 내 모든 메소드가 실행 되기 전에 자동으로 부가 기능 실행
    @Before("execution(* korweb.service.StudentService.*(..))")
        // 첫번째 * : 모든 반환 타입의 메소드
        // korweb.service.StudentService : 클래스가 위치한 경로를 ( src -> main -> java 이후 )
        // 두번째 .* : 앞에 있는 클래스내 모든 메소드 뜻
        // (..) : 메소드들의 매개변수 타입 뜻, (..) : 모든 타입 뜻
    public void logBefore(){
        System.out.println("[AOP] StudentService 발동"); // StudentService내 메소드가 실행되는 출력되는 구역입니다.
    }
//    @After()
//
//    @Around()
}
