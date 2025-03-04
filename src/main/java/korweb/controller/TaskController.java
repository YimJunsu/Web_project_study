package korweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    //    // (1) 인스턴스 생성, 메소드 호출
    //    public void method1 ( ) {
    //        TaskService taskService = new TaskService();
    //        taskService.method1();
    //    }
    // (2) 생성자
//    public void method1(){
//        new TaskService().method1();
//    }
//    // (3) 싱글톤
//    public void method1(){
//        TaskService.getInstance().method1();
//    }
//    // (4) 메소드가 static 일때
//    public void method1() {
//        TaskService.method1();
//    }
    // (5) 스프링 IOC, DI 활용
    @Autowired private TaskService taskService;
    public void method1() {
        taskService.method1();
    }
}

