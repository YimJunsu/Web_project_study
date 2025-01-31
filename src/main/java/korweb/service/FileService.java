package korweb.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    // * 서버(톰캣/build) 경로 내 img 폴더
    // 프로젝트 폴더 명 -> build -> resources -> main -> static -> img 폴더 오른쪽클릭 'copy path' 클릭 -> 'absolute path' 클릭
    // 마지막 경로 뒤 '\\' 넣기.
    String uploadPath = "D:\\JAVA\\source\\kor2024_web2\\build\\resources\\main\\static\\img\\";

    // 1. 업로드 함수/메소드
    public String fileUpload(MultipartFile multipartFile){
        // (1) 매개변수로 MultipartFile 타입 객체를 받는다, 클라이언트가 보낸 첨부파일이 들어있는 객체
        System.out.println(multipartFile.getOriginalFilename()); // 첨부파일의 파일명을 반환하는 함수
        System.out.println(multipartFile.getName()); // 첨부파일이 들어있는 속성명을 반환하는 함수
        System.out.println(multipartFile.getSize()); // 첨부파일의 용량 반환 함수/Byte 단위
        System.out.println(multipartFile.isEmpty()); // 첨부파일이 존재하는 여부 반환 함수

        // (*) 만약에 서로 다른 파일을 동일한 이름으로 업로드시 파일명 식별 불가능.
        // 방안 : 파일명 앞에 UUID 난수 텍스트 조합, UUID.randomUUID().toString() : 난수로 UUID 규약의 텍스트 생성
            // 1. uuid 생성
        String uuid = UUID.randomUUID().toString(); //<- 생성된 uuid 파일명 앞 추가
        System.out.println(uuid); // 예) uuid 텍스트는 실행 할 때마다 다르게 생성 - 중복 희박

        // (2) 업로드 경로와 파일명 조합하기, 업로드 경로 + (uuid) + 파일명
            // 2. UUID 의 구분자는 '-' 하리픈 사용하므로 파일명에 하이픈이 존재하면 안된다.
            // -> 파일에 '-' 하이픈 모두 '_' 언더바로 변경 : .getOriginalFilename().replaceAll("-","_");
            // *문자열.replaceAll("수정할 문자","변경할문자") : 만약 수정할 문자 존재시 변경할 문자로 치환/변경 함수
            // -> 예) 4c896abc-68f1-4f3f-8467-22630b6ebf4f-파일명
        String fileName = uuid + multipartFile.getOriginalFilename().replaceAll("-","_");

        String uploadFile = uploadPath + fileName;
        // (3) 조합된 경로로 file 클래스 객체 만들기
        File file = new File(uploadFile);
        // (4) 업로드 하기, transferTo(지정된 경로) <- 파일 추가 함수, 항상 예외 발생
        try {multipartFile.transferTo(file);
        } catch (IOException e) {System.out.println("파일업로드 실패 : " + e); return null; // 만약에 업로드 실패하면 null 반환
        } return uploadFile; // 업로드 성공하면 성공한 파일명 반환
    }

    // 2. 다운로드 함수/메소드


}
