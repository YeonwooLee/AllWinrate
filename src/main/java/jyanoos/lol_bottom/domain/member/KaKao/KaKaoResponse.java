package jyanoos.lol_bottom.domain.member.KaKao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@ToString
public class KaKaoResponse {
    Long id;
    String nickname;
    Kakao_account kakao_account;
}
