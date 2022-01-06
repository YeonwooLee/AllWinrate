package jyanoos.lol_bottom.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class FreeBoard {
    /*
    #자유게시판 생성
    CREATE TABLE tbl_free_board(
    bno INT NOT NULL AUTO_INCREMENT, -- 게시글 번호
	title VARCHAR(50) NOT NULL, -- 제목
    content TEXT NOT NULL, --내용
    writer VARCHAR(30) NOT NULL, --작성자
    regDate TIMESTAMP DEFAULT NOW(), --작성시간
    viewCnt INT DEFAULT 0, -- 조회수
    PRIMARY KEY(bno) -- 기본키 = 게시글번호
	);
    */

    private int bno;
    private String title;
    private String content;
    private String writer;
    private Date regDate;
    private int viewCnt;
}
