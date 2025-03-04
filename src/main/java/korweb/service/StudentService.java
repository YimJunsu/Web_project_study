package korweb.service;

import korweb.model.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service // 스피링 컨테이너에 빈(인스턴스) 등록, springMVC 에서 비지니스 로직
// 비즈니스로직 : 어떠한 기능 핵심이 되는 코드 예) 회원가입에서 '저장' 로직
public class StudentService {

    @Autowired private StudentMapper studentMapper;

    public int save(Map<String, Object>map){
        System.out.println("StudentService.save");
        System.out.println("map = " + map);
        return studentMapper.save(map);
    }
    public List<Map<String,Object>> findAll(){
        System.out.println("StudentService.findAll");
        return studentMapper.findAll();
    }
    public List<Map<String, Object>> findStudentScores(int minKor, int minMath){
        System.out.println("StudentService.findStudentScores");
        System.out.println("minKor = " + minKor + ", minMath = " + minMath);
        return studentMapper.findStudentScores(minKor, minMath);
    }
    public boolean saveAll(List<Map<String,Object>>list){
        return studentMapper.saveAll(list);
    }

}
