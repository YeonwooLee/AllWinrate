package jyanoos.lol_bottom.domain.member.KaKao;

import jyanoos.lol_bottom.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class KakaoMember extends Member {
    Long userId;
    String userEmail;
    String userNickname;
    int userPoint;
    Date userJoinDate;
    String userSocial;
}
