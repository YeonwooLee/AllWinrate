package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.domain.CombiReplyBoard;

import java.io.IOException;
import java.util.List;

public interface AllWinrateService {
    //db에서 조합별 총승률 리스트 가져옴
    List<AllWinrate> mkAllWinrateList(int minPansoo, int lenList) throws IOException;

    CombiReplyBoard mkViewCombiBoard(String adc, String sup) throws IOException;

    void writeReply(String adc, String sup, String writer, String content);

}
