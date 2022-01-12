package jyanoos.lol_bottom.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

//조합별 게시판 구성하는 형태
@Getter @Setter
public class CombiReplyBoard {
    List<CombiReply> replyList;
    AllWinrate allWinrate;
}
