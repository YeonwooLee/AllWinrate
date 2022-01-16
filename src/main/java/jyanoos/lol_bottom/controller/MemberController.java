package jyanoos.lol_bottom.controller;

import jyanoos.lol_bottom.domain.member.MemberResult;
import jyanoos.lol_bottom.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member*")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    //회원가입창
    @RequestMapping("/join-form")
    public String joinForm(){
        return "/member/joinForm";
    }

    //회원가입
    @RequestMapping("/join")
    public String join(@RequestParam("userEmail") String userEmail,
                       @RequestParam("userPassword") String userPassword,
                       @RequestParam("userPasswordCheck") String userPasswordCheck,
                       @RequestParam("userNickname") String userNickname,
                       Model model){
        MemberResult memberResult = memberService.memberJoin(userEmail, userPassword, userPasswordCheck, userNickname);
        model.addAttribute("memberResult",memberResult);
        return "/member/result";
    }

    //로그인창
    @GetMapping("/login")
    public String loginForm(){
        return "/member/loginForm";
    }

    //로그인
    @PostMapping("/login")
    public String join(@RequestParam("userEmail") String userEmail,
                       @RequestParam("userPassword") String userPassword,
                       Model model){
        MemberResult memberResult = memberService.memberLogin(userEmail,userPassword);
        model.addAttribute("memberResult",memberResult);
        return "/member/result";
    }
}
