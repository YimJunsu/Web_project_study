package korweb.controller;

import korweb.model.dto.MemberDto;
import korweb.model.dto.PointDto;
import korweb.model.entity.PointEntity;
import korweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    /*// 1. 회원가입
    @PostMapping("/member/signup")
    public boolean signup (@RequestBody MemberDto memberDto){
        return memberService.signup(memberDto);
    }*/
    // 11. 첨부파일이 포함된 회원가입 HTTP 매핑, 첨부파일은 JSON 타입이 아닌 multipart/form-data 타입으로 @RequestBody 사용하지 않는다.
    @PostMapping("/member/signup")
    public boolean signup(MemberDto memberDto){ // @RequestBody를 사용하지 않는다. Application/json 아니라서
        return memberService.signup(memberDto);
    }

    // 로그인
    @PostMapping("/member/login")
    public boolean login (@RequestBody MemberDto memberDto){
        return memberService.login(memberDto);
    }
    // 현재 로그인된 회원 아이디 http 매핑
    @GetMapping("/member/login/id")
    public String login (){
        return memberService.getSession();
    }
    // 현재 로그인된 회원 로그아웃
    @GetMapping("/member/logout")
    public boolean logout(){
        return memberService.deleteSession();
    }
    // [6] 현재 로그인된 회원의 정보 조회
    @GetMapping("/member/myinfo")
    public MemberDto MyInfo(){
        return memberService.getMyInfo();
    }
    // [7] 현재 로그인된 회원 탈퇴
    @DeleteMapping("/member/delete")
    public boolean myDelete(){
        return memberService.myDelete();
    }
    // [8] 회원정보 수정
    @PutMapping("/member/myupdate")
    public boolean myUpdate(@RequestBody MemberDto memberDto){
        return memberService.myUpdate(memberDto);
    }
    // [10] 내 포인트 지급 내역 전체 조회
    @GetMapping("/member/point/list")
    public List<PointDto> pointList(){
        return memberService.pointList();
    }
    // [11] 내 포인트 조회
    @GetMapping("/member/point/info")
    public int pointInfo(){
        return memberService.pointInfo();
    }
    // 따라쓰기 연습

}
