package jyanoos.lol_bottom.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import jyanoos.lol_bottom.domain.member.KaKao.*;
import jyanoos.lol_bottom.domain.member.Member;
import jyanoos.lol_bottom.domain.member.MemberResult;
import jyanoos.lol_bottom.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

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

    //카카오 엑세스토큰,refreshToken 등 가져오기
    @Override
    public KakaoToken getKakaoAccessToken(String code) {
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        log.info("카카오 토큰 생성 시작(Domain/KakaoToken.class)");
        KakaoToken kakaoToken = new KakaoToken();

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=a527f058ade4d6b7075f0a57318df2d4"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://116.33.177.58:8080/member/kakaologin"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //실제 요청을 보내는 부분, 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(result);

            ObjectMapper mapper = new ObjectMapper();
            kakaoToken = mapper.readValue(result, KakaoToken.class);


            access_Token = kakaoToken.getAccess_token();
            refresh_Token=kakaoToken.getRefresh_token();
//            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        log.info("카카오토큰생성완료>>>{}",kakaoToken);
        return kakaoToken;
    }

    //카카오 유저정보 가져오기
    @Override
    public KakaoLoginResult getKakaoUserInfo(String accessToken) {
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        log.info("카카오 유저정보 가져오기 시작");
        KakaoLoginResult kakaoLoginResult = new KakaoLoginResult();
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
            log.info("response body : {}",result);

            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String,Object> jsonObject = objectMapper.readValue(result, HashMap.class); //response
            HashMap<String,Object> tempKakaoAccount = (HashMap<String, Object>) jsonObject.get("kakao_account"); //response.kakao_account (임시 계정정보)

            //계정정보 등록 시작
            Kakao_account kakao_account = new Kakao_account();

//            String nickname = (String)tempKakaoAccount.get("nickname");//닉네임
            boolean has_email = (boolean)tempKakaoAccount.get("has_email"); //이메일 있는지?
            boolean email_valid =(boolean)tempKakaoAccount.get("is_email_valid");//유효한 이메일?
            boolean email_verified = (boolean)tempKakaoAccount.get("is_email_verified");//인증된이메일?
            String email = (String)tempKakaoAccount.get("email"); //이메일

            kakao_account.setEmail(email);
//            kakao_account.setNickname(nickname);
            kakao_account.set_email_verified(email_verified);
            kakao_account.set_email_valid(email_valid);
            kakao_account.setHas_email(has_email);
            log.info("kakao_account등록>> {}",kakao_account);
            //계정정보 등록 끝

            //KakaoResponse 등록 시작 {계정아이디:id, 계정정보:kakao_account}
            KaKaoResponse kaKaoResponse = new KaKaoResponse();

            Long id = Long.valueOf(String.valueOf(jsonObject.get("id")));
            HashMap<String,Object> properties =(HashMap<String, Object>) jsonObject.get("properties");
            String nickname = String.valueOf(properties.get("nickname"));

            kaKaoResponse.setId(id);
            kaKaoResponse.setNickname(nickname);
            kaKaoResponse.setKakao_account(kakao_account);
            log.info("KaKaoResponse등록>> {}",kaKaoResponse);
            //KakaoResponse 등록 끝


//            HashMap<String,Object> kakaoAccount=(boolean) (HashMap<String, Object>) jsonObject.get("kakao_account").get("is_email_verified");
            log.info("id = {}",id);
            log.info("kakao_account = {}",kakao_account);

            //결과에 response랑 id는 미리 기입
            kakaoLoginResult.setKaKaoResponse(kaKaoResponse);
            kakaoLoginResult.setId(id);

            int i = memberMapper.existKakaoByUserId(id);
            if(i==1){//카카오멤버db에 있는사람
                KakaoMember kakaoMember = memberMapper.findKakaoByUserId(id);
                log.info("카카오닉ㄴ네임오류>>{}, id={}",kakaoMember,id);
                kakaoLoginResult.setKakaoMember(kakaoMember);
                kakaoLoginResult.setSuccess(true);
                kakaoLoginResult.setNickname(kakaoMember.getUserNickname());//닉네임은 기존 닉네임으로
                kakaoLoginResult.setMessage("기존 카카오회원 로그인 성공");
                return kakaoLoginResult;
            }else{//카카오멤버db에 없는사람
                if(kakao_account.isHas_email()){//이메일 있으면
                    memberMapper.saveKaKaoYesEmail(id,nickname,email); //있는대로 가입
                }else{//이메일 없으면
                    memberMapper.saveKaKaoNoEmail(id,nickname); //없는대로가입
                }
                KakaoMember kakaoMember = memberMapper.findKakaoByUserId(id);
                kakaoLoginResult.setKakaoMember(kakaoMember);
                kakaoLoginResult.setSuccess(true);
                kakaoLoginResult.setNickname(nickname); //첫 가입이니까 카카오상 닉네임으로
                kakaoLoginResult.setMessage("신규 카카오회원 로그인 성공");
                return kakaoLoginResult;
            }


        } catch (IOException e) {
            e.printStackTrace();
            KakaoLoginResult kakaoLoginResultFail = new KakaoLoginResult();
            kakaoLoginResultFail.setSuccess(false);
            return kakaoLoginResultFail;

        }
//        return kakaoToken;
    }
}
