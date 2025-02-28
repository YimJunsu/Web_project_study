package korweb.controller;

import korweb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students") // 해당 클래스내 메소드들의 공통 URL 정의
public class StudentController {
    @Autowired private StudentService studentService;

    // [1] 학생 점수 등록
    @PostMapping("") // HTTP METHOD 중 "POST" 선택
    public int save(@RequestBody Map<String, Object> map){
        System.out.println("StudentController.save");
        System.out.println("map = " + map);
        return studentService.save(map);
    }
    // [2] 학생 전체 조회
     @GetMapping("/aa")
    public List<Map<String,Object>>findAll(){
         System.out.println("StudentController.findAll");
         return studentService.findAll();
     }
    // [3] 특정 점수 이상의 학생 조회
    @GetMapping("/scores")
    // http://localhost:8080/students/scores?minKor=90&minMath=80
    public List<Map<String,Object>>findStudentScores(@RequestParam int minKor,@RequestParam int minMath){
        System.out.println("StudentController.findStudentScores");
        return studentService.findStudentScores(minKor, minMath);
    }
    // [4] 여러명 학생 등록
    @PostMapping("/all")
    public boolean saveAll(@RequestBody List<Map<String,Object>>list){
        return studentService.saveAll(list);
    }
}
