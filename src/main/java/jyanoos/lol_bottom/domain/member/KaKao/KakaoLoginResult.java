package jyanoos.lol_bottom.domain.member.KaKao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class KakaoLoginResult {
    String message;
    boolean success;
    KaKaoResponse kaKaoResponse;//카카오상 회원정보(db상 회원정보와 아마 닉네임만 다름)
    Long id;
    String nickname;
    KakaoMember kakaoMember; //db상 회원정보
}
