package jyanoos.lol_bottom.service;


import jyanoos.lol_bottom.domain.member.Member;
import jyanoos.lol_bottom.domain.member.MemberResult;
import jyanoos.lol_bottom.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
@Slf4j
@Component
public class MemberServiceImpl implements MemberService{

    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public MemberResult memberJoin(String userEmail, String userPassword, String userPasswordCheck, String userNickname) {
        MemberResult memberResult = new MemberResult(); //결과 저장용 객체
        Member member = new Member();
        log.info("회원가입요청 email: {}, pw: {}, pwchk: {}, nick:",userEmail,userPassword,userPasswordCheck,userNickname);
        if(memberMapper.existMemberByNickname(userNickname)==1){
            memberResult.setMessage("회원가입실패: 이미 존재하는 닉네임");
            return memberResult;
        }

        if(memberMapper.existMemberByEmail(userEmail)==1){
            memberResult.setMessage("회원가입실패: 이미 존재하는 이메일");
            return memberResult;
        }

        if(!userPassword.equals(userPasswordCheck)){
            memberResult.setMessage("회원가입실패: 비밀번호, 비밀번호 확인 불일치");
            return memberResult;
        }
        
        int i = memberMapper.insertMember(userEmail, userPassword, userNickname);
        if (i == 1) {//저장성공시 member객체 가져옴
            member = memberMapper.findMemberByEmail(userEmail);
            memberResult.setMember(member);
        }
        memberResult.setMessage("회원 가입에 성공하였습니다!");

        return memberResult;

    }

    @Override
    public MemberResult memberLogin(String userEmail, String userPassword) {
        MemberResult memberResult = new MemberResult();
        int i = memberMapper.existMemberByEmail(userEmail);
        if(i==0){
            memberResult.setSuccess(false);
            memberResult.setMessage("로그인 실패");
            log.info("로그인실패-아이디없음 {}",userEmail);
            return memberResult;
        }

        Member member = memberMapper.findMemberByEmail(userEmail);

        if(member.getUserPassword().equals(userPassword)){
            memberResult.setMember(member);
            memberResult.setSuccess(true);
            memberResult.setMessage("로그인 성공");
            return memberResult;
        }
        memberResult.setMember(member);
        memberResult.setSuccess(false);
        memberResult.setMessage("로그인 실패");
        log.info("로그인실패-비밀번호불일치 찐비번:{} 입력비번:",member.getUserPassword(),userPassword);
        return memberResult;

    }

    @Override
    public Member findMemberByNickname(String userNickName) {
        Member member = memberMapper.findMemberByNickname(userNickName);
        return member;
    }
}
