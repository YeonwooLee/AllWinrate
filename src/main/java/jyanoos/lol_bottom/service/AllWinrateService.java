package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.AllWinrate;

import java.io.IOException;
import java.util.List;

public interface AllWinrateService {
    //db에서 조합별 총승률 리스트 가져옴
    List<AllWinrate> mkAllWinrateList(int minPansoo, int lenList) throws IOException;



}
