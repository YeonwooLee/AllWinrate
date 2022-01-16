package jyanoos.lol_bottom.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;


@Getter @Setter @ToString
public class Member {
    String userEmail;
    String userPassword;
    String userNickname;
    int userPoint;
    Date userJoinDate;

}
