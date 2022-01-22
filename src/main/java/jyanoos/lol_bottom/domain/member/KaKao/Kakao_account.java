package jyanoos.lol_bottom.domain.member.KaKao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter @Setter @ToString
public class Kakao_account {
//    String nickname; 널로나오길래뻄
    boolean has_email;
    boolean is_email_valid;
    boolean is_email_verified;
    String email;
}
