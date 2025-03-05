package korweb.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect // AOP 설정하는 어노테이션 주입
@Component // 스프링 컨테이너 빈 등록
public class Logcheck {
    // + StudentService 클래스 내 모든 메소드가 실행 되기 전에 자동으로 부가 기능 실행 + 실행된 메서드의 매개변수
            // 첫번째 * : 모든 반환 타입의 메소드
            // korweb.service.StudentService : 클래스가 위치한 경로를 ( src -> main -> java 이후 )
            // 두번째 .* : 앞에 있는 클래스내 모든 메소드 뜻
            // (..) : 메소드들의 매개변수 타입 뜻, (..) : 모든 타입 뜻
    @Before("execution(* korweb.service.StudentService.*(..)) &&args(param) ") // args : 실행된 메서드의 인수 : arguments
    public void logBefore(Object param){ // Object : 여러 메소드들의 인수값 타입을 대입 받기 위해서는 슈퍼클래스인 Object
        System.out.println("[AOP] StudentService 발동"); // StudentService내 메소드가 실행되는 출력되는 구역입니다.
        System.out.println("[AOP] 매개변수 : " + param);
    }
    // [2] @AfterReturning
        // + StudentService 클래스내 모든 메소드가 *정상 실행 종료* 했을때 자동으로 부가 기능 실행 + 실행된 메소드의 리턴값
    @AfterReturning(value = "execution(* korweb.service.StudentService.*(..))" , returning = "result")
    public void logReturn(Object result){
        System.out.println("[AOP] StudentService 종료");
        System.out.println("[AOP] 반환값 : " + result);
    }
    //[3] @Around:
    @Around("execution(* korweb.service.StudentService.*(..) )")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // AOP 에서 제공하는 인터페이스 : ProceedingJoinPoint
        System.out.println(joinPoint.getArgs()); // 발동된 메소드의 인수 반환,  && args 필요 x [배열 타입으로 반환 : 인수가 여러개 있을 수 있으므로 ]
        System.out.println(joinPoint.getSignature()); // 발동된 메소드의 선언부(반환타입 함수명 매개변수 정보) 반환 / 가져오기

        // (1) 발동된(현재 실행된) 메소드의 함수명
        System.out.println("[AOP] 현재 실행된 서비스명 : " + joinPoint.getSignature());
        // (2) 발동된 메소드의 인수값, Arrary.toString(배열 타입의 변수명) : 배열내 값들을 문자열로 반환( vs Dto의 toString() 같은 의미)
        System.out.println("[AOP] 현재 실행된 서비스의 인수 : " + joinPoint.getArgs());
        // (3) 메소드 발동하고 리턴값 받기 , proceed() : 현재 발동한 메소드 직접 실행
        Object result = joinPoint.proceed();
        // (4) 메소드 실행 후 리턴값 받기
        System.out.println("[AOP] 현재 실행된 서비스의 반환 : " + result );
        return result;
    }
}
