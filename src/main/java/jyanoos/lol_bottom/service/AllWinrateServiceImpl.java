package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.lolSetting.LolSetting;
import jyanoos.lol_bottom.mapper.AllWinrateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AllWinrateServiceImpl implements AllWinrateService {
    private final AllWinrateMapper allWinrateMapper;
    private final LolSetting lolSetting;

    @Override
    public List<AllWinrate> mkAllWinrateList(int minPansoo,int lenList) throws IOException {
        //조합별 총승률 리스트 가져옴
        List<AllWinrate> allWinrateList = allWinrateMapper.allWinrateList();

        //판수 minPansoo 이하인 애들 제거
        for(Iterator<AllWinrate> iterator = allWinrateList.iterator(); iterator.hasNext();){
            AllWinrate awr = iterator.next();
            if(awr.getWhole()<minPansoo){
                iterator.remove();
            }
            awr.setEngAdc(lolSetting.convertKoToEng(awr.getAdc()));
            awr.setEngSup(lolSetting.convertKoToEng(awr.getSup()));

        }

        //정렬(1기준 승률, 2기준 판수)
        Collections.sort(allWinrateList);
        
        //상위n개만 리턴
        allWinrateList = allWinrateList.subList(0,lenList);
        return allWinrateList;
    }

}
