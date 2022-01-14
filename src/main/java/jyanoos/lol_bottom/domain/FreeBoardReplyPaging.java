package jyanoos.lol_bottom.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class FreeBoardReplyPaging {
    List<Reply> replies; //보여줄 댓글 리스트
    List<Integer> pages; //하단에 보여줄 페이지 번호 목록
    int lastPageNum; //사용자가보는 마지막 페이지 번호(0부터시작)
    int StartPage;//페이징시작인덱스(0부터시작)
    int endPaging;//페이징인덱스 마지막(0부터시작)

}
