package korweb.controller;

import korweb.model.dto.MemberDto;
import korweb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    // 회원가입
    @PostMapping("/member/signup")
    public boolean signup (@RequestBody MemberDto memberDto){
        return memberService.signup(memberDto);
    }
    // 로그인
    @PostMapping("/member/login")
    public boolean login (@RequestBody MemberDto memberDto){
        return memberService.login(memberDto);
    }
}