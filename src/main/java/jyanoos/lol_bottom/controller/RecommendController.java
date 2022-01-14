package jyanoos.lol_bottom.controller;

import jyanoos.lol_bottom.domain.recommend.*;
import jyanoos.lol_bottom.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public String recommendCal(@ModelAttribute RecommendRequest recommendRequest, Model model) throws IOException {
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
        }//추천: 서폿, 아는것:
        else if(recommendCase.equals("recommendSupKnow")){
            List<RecommendSupKnow> recommendSupKnowList = recommendService.recommendSupKnow(recommendRequest);
            model.addAttribute("recommendSupKnowList",recommendSupKnowList);
            return "/recommend/recommendSupKnow";
        }//추천: 서폿, 아는것: mAdc
        else if(recommendCase.equals("recommendSupKnowMadc")){
            List<RecommendSupKnowMadc> recommendSupKnowMadcList = recommendService.recommendSupKnowMadc(recommendRequest);
            model.addAttribute("recommendSupKnowMadcList",recommendSupKnowMadcList);
            return "/recommend/recommendSupKnowMadc";
        }//추천: 서폿, 아는것: mAdc eAdc
        else if(recommendCase.equals("recommendSupKnowMadcEadc")){
            List<RecommendSupKnowMadcEadc> recommendSupKnowMadcEadcList = recommendService.recommendSupKnowMadcEadc(recommendRequest);
            model.addAttribute("recommendSupKnowMadcEadcList",recommendSupKnowMadcEadcList);
            return "/recommend/recommendSupKnowMadcEadc";
        }//추천: 서폿, 아는것: mAdc eSup
        else if(recommendCase.equals("recommendSupKnowMadcEsup")){
            List<RecommendSupKnowMadcEsup> recommendSupKnowMadcEsupList = recommendService.recommendSupKnowMadcEsup(recommendRequest);
            model.addAttribute("recommendSupKnowMadcEsupList",recommendSupKnowMadcEsupList);
            return "/recommend/recommendSupKnowMadcEsup";
        }//추천: 서폿, 아는것: mAdc eAdc eSup
        else if(recommendCase.equals("recommendSupKnowMadcEadcEsup")){
            List<RecommendSupKnowMadcEadcEsup> recommendSupKnowMadcEadcEsupList = recommendService.recommendSupKnowMadcEadcEsup(recommendRequest);
            model.addAttribute("recommendSupKnowMadcEadcEsupList",recommendSupKnowMadcEadcEsupList);
            return "/recommend/recommendSupKnowMadcEadcEsup";
        }//추천: 서폿, 아는것: eAdc
        else if(recommendCase.equals("recommendSupKnowEadc")){
            List<RecommendSupKnowEadc> recommendSupKnowEadcList = recommendService.recommendSupKnowEadc(recommendRequest);
            model.addAttribute("recommendSupKnowEadcList",recommendSupKnowEadcList);
            return "/recommend/recommendSupKnowEadc";
        }//추천: 서폿, 아는것: eSup
        else if(recommendCase.equals("recommendSupKnowEsup")){
            List<RecommendSupKnowEsup> recommendSupKnowEsupList = recommendService.recommendSupKnowEsup(recommendRequest);
            model.addAttribute("recommendSupKnowEsupList",recommendSupKnowEsupList);
            return "/recommend/recommendSupKnowEsup";
        }//추천: 서폿, 아는것: eadc eSup
        else if(recommendCase.equals("recommendSupKnowEadcEsup")){
            List<RecommendSupKnowEadcEsup> recommendSupKnowEadcEsupList = recommendService.recommendSupKnowEadcEsup(recommendRequest);
            model.addAttribute("recommendSupKnowEadcEsupList",recommendSupKnowEadcEsupList);
            return "/recommend/recommendSupKnowEadcEsup";
        }//추천: 조합, 아는것 없음
        else if(recommendCase.equals("recommendCombiKnow")){
            List<RecommendCombiKnow> recommendCombiKnowList = recommendService.recommendCombiKnow(recommendRequest,200);
            model.addAttribute("recommendCombiKnowList",recommendCombiKnowList);
            return "/recommend/recommendCombiKnow";
        }//추천: 조합, 아는것: eAdc
        else if(recommendCase.equals("recommendCombiKnowEadc")){
            List<RecommendCombiKnowEadc> recommendCombiKnowEadcList = recommendService.recommendCombiKnowEadc(recommendRequest);
            model.addAttribute("recommendCombiKnowEadcList",recommendCombiKnowEadcList);
            return "/recommend/recommendCombiKnowEadc";
        }//추천: 조합, 아는것: eAdc eSup
        else if(recommendCase.equals("recommendCombiKnowEadcEsup")){
            List<RecommendCombiKnowEadcEsup> recommendCombiKnowEadcEsupList = recommendService.recommendCombiKnowEadcEsup(recommendRequest);
            model.addAttribute("recommendCombiKnowEadcEsupList",recommendCombiKnowEadcEsupList);
            return "/recommend/recommendCombiKnowEadcEsup";
        }//추천: 조합, 아는것: eSup
        else if(recommendCase.equals("recommendCombiKnowEsup")){
            List<RecommendCombiKnowEsup> recommendCombiKnowEsupList = recommendService.recommendCombiKnowEsup(recommendRequest);
            model.addAttribute("recommendCombiKnowEsupList",recommendCombiKnowEsupList);
            return "/recommend/recommendCombiKnowEsup";
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
        }//추천타입 = 서폿
        else if(recommendRequest.getRecommendKind().equals("sup")) {
            //아는 정보 없음
            if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendSupKnow";
            }//아는 정보: mAdc
            else if(recommendRequest.getMAdc()!=""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendSupKnowMadc";
            }//아는 정보: mAdc, eadc
            else if(recommendRequest.getMAdc()!=""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendSupKnowMadcEadc";
            }//아는 정보: mAdc, eSup
            else if(recommendRequest.getMAdc()!=""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendSupKnowMadcEsup";
            }//아는 정보: mAdc, eAdc, eSup
            else if(recommendRequest.getMAdc()!=""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendSupKnowMadcEadcEsup";
            }//아는 정보: eAdc
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendSupKnowEadc";
            }//아는 정보: eSup
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendSupKnowEsup";
            }//아는 정보: eAdc eSup
            else if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendSupKnowEadcEsup";
            }
        }//추천타입 = 조합
        else if(recommendRequest.getRecommendKind().equals("combi")){
            //아는 정보 없음
            if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendCombiKnow";
            }//아는 정보: eAdc
            if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()==""&& recommendRequest.getMSup()==""){
                return "recommendCombiKnowEadc";
            }//아는 정보: eAdc eSup
            if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()!=""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendCombiKnowEadcEsup";
            }//아는 정보: eSup
            if(recommendRequest.getMAdc()==""&& recommendRequest.getEAdc()==""&& recommendRequest.getESup()!=""&& recommendRequest.getMSup()==""){
                return "recommendCombiKnowEsup";
            }
        }
        return null; //여기 걸리면 안됨
    }
}
