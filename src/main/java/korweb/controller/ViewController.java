package korweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    // [1] 메인페이지를 반환해주는 메소드
    @GetMapping
    public String index(){return "index.html";}

    // [2] 로그인페이지를 반환해주는 메소드
    @GetMapping("/member/login")
    public String login(){return "/member/login.html";}
    // [3] 회원가입페이지를 반환해주는 메소드
    @GetMapping("/member/signup")
    public String signup(){return "/member/signup.html";}
}
