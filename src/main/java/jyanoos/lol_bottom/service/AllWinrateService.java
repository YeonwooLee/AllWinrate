package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.domain.CombiReplyBoard;

import java.io.IOException;
import java.util.List;

public interface AllWinrateService {
    //db에서 조합별 총승률 리스트 가져옴
    List<AllWinrate> mkAllWinrateList(int minPansoo, int lenList) throws IOException;

    //조합댓글판 생성용 자료 allWinrate(해당조합), List<Reply> 해당조합에 대한 댓글들
    CombiReplyBoard mkViewCombiBoard(String adc, String sup, int lastReplyIndex) throws IOException;

    //댓글 작성
    void writeReply(String adc, String sup, String writer, String content,String nowVersion);

    //댓글 수정
    int updateReply(String adcEng, String supEng, String writer, String content, int rno);

    //댓글삭제
    int deleteReply(String adcEng, String supEng, int rno);

    //대댓작성
    int writeSecReply(String adc, String sup, int rno, String writer, String content,String nowVersion);

    //대댓글 수정
    int updateSecReply(String adcEng, String supEng, String writer, String content, int secRno, int rno);

    //대댓글 삭제
    int deleteSecReply(String adcEng, String supEng, int secRno,int rno);

}
