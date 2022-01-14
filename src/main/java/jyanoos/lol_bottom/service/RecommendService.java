package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.recommend.*;

import java.io.IOException;
import java.util.List;

public interface RecommendService {
    //추천: 원딜, 아는것: msup
    List<RecommendAdcKnowMsup> recommendAdcKnowMsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;

    //추천: 원딜, 아는것: msup,eadc
    List<RecommendAdcKnowMsupEadc> recommendAdcKnowMsupEadc(RecommendRequest recommendRequest, int minPansoo) throws IOException;

    //추천: 원딜, 아는것:
    List<RecommendAdcKnow> recommendAdcKnow(RecommendRequest recommendRequest, int minPansoo) throws IOException;

    //추천:원딜, 아는것: msup,esup
    List<RecommendAdcKnowMsupEsup> recommendAdcKnowMsupEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;

    //추천:원딜, 아는것: msup,eAdc,eSup
    List<RecommendAdcKnowMsupEadcEsup> recommendAdcKnowMsupEadcEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;

    //추천:원딜, 아는것: eadc
    List<RecommendAdcKnowEadc> recommendAdcKnowEadc(RecommendRequest recommendRequest, int minPansoo) throws IOException;

    //추천:원딜, 아는것: esup
    List<RecommendAdcKnowEsup> recommendAdcKnowEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;

    //추천:원딜, 아는것: eadc,esup
    List<RecommendAdcKnowEadcEsup> recommendAdcKnowEadcEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;



    //추천:서폿, 아는것:
    List<RecommendSupKnow> recommendSupKnow(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:서폿, 아는것:mAdc
    List<RecommendSupKnowMadc> recommendSupKnowMadc(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:서폿, 아는것:mAdc eAdc
    List<RecommendSupKnowMadcEadc> recommendSupKnowMadcEadc(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:서폿, 아는것:mAdc eSup
    List<RecommendSupKnowMadcEsup> recommendSupKnowMadcEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:서폿, 아는것:mAdc eAdc eSup
    List<RecommendSupKnowMadcEadcEsup> recommendSupKnowMadcEadcEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:서폿, 아는것:eAdc
    List<RecommendSupKnowEadc> recommendSupKnowEadc(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:서폿, 아는것:eSup
    List<RecommendSupKnowEsup> recommendSupKnowEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:서폿, 아는것:eAdc, eSup
    List<RecommendSupKnowEadcEsup> recommendSupKnowEadcEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;


    //추천:조합, 아는것 없음 minPansoo적용
    List<RecommendCombiKnow> recommendCombiKnow(RecommendRequest recommendRequest,int minPansoo) throws IOException;
    //추천:조합, 아는것: eAdc
    List<RecommendCombiKnowEadc> recommendCombiKnowEadc(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:조합, 아는것: eAdc, eSup
    List<RecommendCombiKnowEadcEsup> recommendCombiKnowEadcEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;
    //추천:조합, 아는것: eSup
    List<RecommendCombiKnowEsup> recommendCombiKnowEsup(RecommendRequest recommendRequest, int minPansoo) throws IOException;
}
