package jyanoos.lol_bottom.service;



import jyanoos.lol_bottom.domain.FreeBoard;
import java.util.List;

public interface FreeBoardService {
    //자유게시판 글 리스트 리턴
    List<FreeBoard> freeBoardList();

    //자유게시판 글 저장, 성공시1, 실패시0 리턴
    int writeFreeBoard(FreeBoard freeBoard);

    FreeBoard findByBno(int bno);
}
