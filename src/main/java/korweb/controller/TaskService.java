package korweb.controller;

import org.springframework.stereotype.Service;

@Service
public class TaskService {
        // 싱글턴 사용시
//    private static final TaskService instance = new TaskService();
//    private TaskService(){}
//    public static TaskService getInstance(){return instance;}

    public void method1(){
        System.out.println("실행");
    }

//    // 메소드에 static 사용시
//    public static void method1(){
//    }


}
