package jyanoos.lol_bottom.controller;

import jyanoos.lol_bottom.domain.recommend.*;
import jyanoos.lol_bottom.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class RecommendController {
    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }


    @RequestMapping("recommend")
    public String recommendMain(){
        return "/recommend/recommend";
    }

    @RequestMapping("recommend/cal")
    public String recommendCal(@ModelAttribute RecommendRequest recommendRequest, Model model){
        log.info("챔피언추천요청 {}",recommendRequest);


        //요청 케이스 구분
        String recommendCase = getRecommendCase(recommendRequest);
        log.info("recommendCase is {}",recommendCase);

        //케이스에 맞는 메소드 호출
        //추천: 원딜, 아는것: msup
        if(recommendCase.equals("recommendAdcKnowMsup")){
            List<RecommendAdcKnowMsup> recommendAdcKnowMsupList = recommendService.recommendAdcKnowMsup(recommendRequest);
            model.addAttribute("recommendAdcKnowMsupList",recommendAdcKnowMsupList);
            return "/recommend/recommendAdcKnowMsup";
        }//추천: 원딜, 아는것: msup,eadc
        else if(recommendCase.equals("recommendAdcKnowMsupEadc")){
            List<RecommendAdcKnowMsupEadc> recommendAdcKnowMsupEAdcList = recommendService.recommendAdcKnowMsupEadc(recommendRequest);
            model.addAttribute("recommendAdcKnowMsupEAdcList",recommendAdcKnowMsupEAdcList);
            return "/recommend/recommendAdcKnowMsupEAdc";
        }//추천: 원딜, 아는것:
        else if(recommendCase.equals("recommendAdcKnow")){
            List<RecommendAdcKnow> recommendAdcKnowList = recommendService.recommendAdcKnow(recommendRequest);
            model.addAttribute("recommendAdcKnowList",recommendAdcKnowList);
            return "/recommend/recommendAdcKnow";
        }//추천: 원딜, 아는것: msup, esup
        else if(recommendCase.equals("recommendAdcKnowMsupEsup")){
            List<RecommendAdcKnowMsupEsup> recommendAdcKnowMsupEsupList = recommendService.recommendAdcKnowMsupEsup(recommendRequest);
            model.addAttribute("recommendAdcKnowMsupEsupList",recommendAdcKnowMsupEsupList);
            return "/recommend/recommendAdcKnowMsupEsup";
        }//추천: 원딜, 아는것: mSup, eAdc, eSup
        else if(recommendCase.equals("recommendAdcKnowMsupEadcEsup")){
            List<RecommendAdcKnowMsupEadcEsup> recommendAdcKnowMsupEadcEsupList = recommendService.recommendAdcKnowMsupEadcEsup(recommendRequest);
            model.addAttribute("recommendAdcKnowMsupEadcEsupList",recommendAdcKnowMsupEadcEsupList);
            return "/recommend/recommendAdcKnowMsupEadcEsup";
        }//추천: 원딜, 아는것: eAdc
        else if(recommendCase.equals("recommendAdcKnowEadc")){
            List<RecommendAdcKnowEadc> recommendAdcKnowEadcList = recommendService.recommendAdcKnowEadc(recommendRequest);
            model.addAttribute("recommendAdcKnowEadcList",recommendAdcKnowEadcList);
            return "/recommend/recommendAdcKnowEadc";
        }//추천: 원딜, 아는것: eSup
        else if(recommendCase.equals("recommendAdcKnowEsup")){
            List<RecommendAdcKnowEsup> recommendAdcKnowEsupList = recommendService.recommendAdcKnowEsup(recommendRequest);
            model.addAttribute("recommendAdcKnowEsupList",recommendAdcKnowEsupList);
            return "/recommend/recommendAdcKnowEsup";
        }//추천: 원딜, 아는것: eAdc, eSup
        else if(recommendCase.equals("recommendAdcKnowEadcEsup")){
            List<RecommendAdcKnowEadcEsup> recommendAdcKnowEadcEsupList = recommendService.recommendAdcKnowEadcEsup(recommendRequest);
            model.addAttribute("recommendAdcKnowEadcEsupList",recommendAdcKnowEadcEsupList);
            return "/recommend/recommendAdcKnowEadcEsup";
        }



        return"/awrMain"; //임시로해둠 추후 삭제
    }

    private String getRecommendCase(RecommendRequest recommendRequest) {
        //추천타입 = 원딜
        if(recommendRequest.getRecommendKind().equals("adc")){
            //아는 정보=mSup (mSup만 ""이 아님)
            if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()!=""){
                return "recommendAdcKnowMsup";
            }//아는정보=mSup,eAdc
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()!=""){
                return "recommendAdcKnowMsupEadc";
            }//아는 정보 없음
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendAdcKnow";
            }//아는 정보=mSup,eSup
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()!=""){
                return "recommendAdcKnowMsupEsup";
            }//아는 정보=mSup, eAdc, eSup
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()!=""){
                return "recommendAdcKnowMsupEadcEsup";
            }//아는 정보=eadc
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendAdcKnowEadc";
            }//아는 정보=eSup
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendAdcKnowEsup";
            }//아는 정보=eSup,eAdc
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendAdcKnowEadcEsup";
            }
        }
        return null; //여기 걸리면 안됨
    }
}
