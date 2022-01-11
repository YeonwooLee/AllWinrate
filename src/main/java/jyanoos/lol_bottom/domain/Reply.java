package jyanoos.lol_bottom.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class Reply {
    int rno;
    int bno;
    String writer;
    String content;
    Date regDate;

    public Reply(int bno, String writer, String content) {
        this.bno = bno;
        this.writer = writer;
        this.content = content;
    }
}
