package jyanoos.lol_bottom.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberResult {
    String message; //결과메세지
    boolean success; //성공여부
    Member member; //다룬 회원 객체
}
