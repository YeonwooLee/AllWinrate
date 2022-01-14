package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.recommend.*;

import java.util.List;

public interface RecommendService {
    //추천: 원딜, 아는것: msup
    List<RecommendAdcKnowMsup> recommendAdcKnowMsup(RecommendRequest recommendRequest);

    //추천: 원딜, 아는것: msup,eadc
    List<RecommendAdcKnowMsupEadc> recommendAdcKnowMsupEadc(RecommendRequest recommendRequest);

    //추천: 원딜, 아는것:
    List<RecommendAdcKnow> recommendAdcKnow(RecommendRequest recommendRequest);

    //추천:원딜, 아는것: msup,esup
    List<RecommendAdcKnowMsupEsup> recommendAdcKnowMsupEsup(RecommendRequest recommendRequest);

    //추천:원딜, 아는것: msup,eAdc,eSup
    List<RecommendAdcKnowMsupEadcEsup> recommendAdcKnowMsupEadcEsup(RecommendRequest recommendRequest);

    //추천:원딜, 아는것: eadc
    List<RecommendAdcKnowEadc> recommendAdcKnowEadc(RecommendRequest recommendRequest);

    //추천:원딜, 아는것: esup
    List<RecommendAdcKnowEsup> recommendAdcKnowEsup(RecommendRequest recommendRequest);

    //추천:원딜, 아는것: eadc,esup
    List<RecommendAdcKnowEadcEsup> recommendAdcKnowEadcEsup(RecommendRequest recommendRequest);
}
