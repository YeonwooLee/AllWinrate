package jyanoos.lol_bottom.service;



import jyanoos.lol_bottom.domain.FreeBoard;
import java.util.List;

public interface FreeBoardService {
    //자유게시판 글 리스트 리턴
    List<FreeBoard> freeBoardList();
}
