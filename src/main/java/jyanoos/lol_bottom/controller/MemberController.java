package jyanoos.lol_bottom.controller;

import jyanoos.lol_bottom.domain.member.Member;
import jyanoos.lol_bottom.domain.member.MemberResult;
import jyanoos.lol_bottom.domain.member.SessionConst;
import jyanoos.lol_bottom.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Slf4j
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
                       Model model, HttpServletRequest request){
        MemberResult memberResult = memberService.memberLogin(userEmail,userPassword);

        HttpSession session = request.getSession();

        log.info("로그인요청 email:{} pw:{}",userEmail,userPassword);
        //세션테스트용
        if(memberResult.isSuccess()){//로그인 성공
            Member loginMember = memberResult.getMember();
            session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember); //세션에 {"loginMember":Member loginMember} 저장
            return "redirect:/awrmain";
        }
        //세션테스트끝

        model.addAttribute("memberResult",memberResult);
        return "/member/result";
    }
    //로그아웃
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/{userNickName}")
    public String userInfo(@PathVariable("userNickName") String userNickName,Model model){
        Member member = memberService.findMemberByNickname(userNickName);
        model.addAttribute("member",member);
        return "/member/info";

    }


}
