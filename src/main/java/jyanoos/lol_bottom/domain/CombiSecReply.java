package jyanoos.lol_bottom.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
//조합별 게시판에 달린 글(댓글형식) 형식
public class CombiSecReply {
    int secRno;
    int rno;
    String writer;
    String content;
    Date regDate;
}
