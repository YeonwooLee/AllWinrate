package jyanoos.lol_bottom.controller;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.domain.CombiReplyBoard;
import jyanoos.lol_bottom.service.AllWinrateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
public class AllWinrateController {
    private final AllWinrateService allWinrateService;


    //awr 기본페이지 구성
    @RequestMapping("/awrmain")
    public String awrMain(Model model, HttpServletRequest request) throws IOException {
        List<AllWinrate> allWinrateList = allWinrateService.mkAllWinrateList(200,7);
        model.addAttribute("allWinrateList",allWinrateList);



        return "awrMain";
    }

    //awr 조합댓글판 보기
    @RequestMapping(value = {"/awrboard/{adc}/{sup}/{lastReplyIndex}","/awrboard/{adc}/{sup}"})
    public String viewCombiBoard(@PathVariable("adc") String adc, @PathVariable("sup") String sup, @PathVariable(value="lastReplyIndex",required = false) Integer lastReplyIndex, Model model) throws IOException {
        //log.info("라승트리플{}",lastReplyIndex);
        if(lastReplyIndex==null){
            lastReplyIndex=20;
        }
        CombiReplyBoard combiReplyBoard = allWinrateService.mkViewCombiBoard(adc, sup,lastReplyIndex);
        combiReplyBoard.setLastReplyIndex(lastReplyIndex);
        model.addAttribute("combiReplyBoard",combiReplyBoard);
        return "/allWinrate/awrBoard";
    }

    //awr 조합 댓글판 댓글 저장하기
    @PostMapping("/awrboard/reply/save")
    public String writeReply(
            @RequestParam("adc") String adc,
            @RequestParam("sup") String sup,
            @RequestParam("writer") String writer,
            @RequestParam("content") String content,
            RedirectAttributes redirectAttribute){
        redirectAttribute.addAttribute("adc",adc);
        redirectAttribute.addAttribute("sup",sup);
        allWinrateService.writeReply(adc,sup,writer,content);
        return "redirect:/awrboard/{adc}/{sup}";
    }

    //awr 조합댓글판 수정하기 수정폼으로 이동
    @GetMapping("/awrboard/{adcEng}/{supEng}/{rno}/update/{lastReplyIndex}")
    public String updateReplyForm(@PathVariable("adcEng") String adcEng,
                                  @PathVariable("supEng") String supEng,
                                  @PathVariable("rno") int rno,
                                  @PathVariable("lastReplyIndex") int lastReplyIndex,
                                  Model model) throws IOException {
        CombiReplyBoard combiReplyBoard = allWinrateService.mkViewCombiBoard(adcEng,supEng,lastReplyIndex);
        log.info("원래창->댓글수정창에서의 lastReplyIndx{}",lastReplyIndex);
        model.addAttribute("combiReplyBoard",combiReplyBoard);
        model.addAttribute("rno",rno);

        return "allWinrate/awrBoardUpdate";
    }
    //awr 조합댓글판 수정하기 실제 수정
    @PostMapping("/awrboard/{adcEng}/{supEng}/{rno}/update/{lastReplyIndex}")
    public String updateReply(@RequestParam("adcEng") String adcEng,
                              @RequestParam("supEng") String supEng,
                              @RequestParam("rno") int rno,
                              @RequestParam("writer") String writer,
                              @RequestParam("content") String content,
                              @PathVariable("lastReplyIndex") int lastReplyIndex,
                              RedirectAttributes redirectAttributes){
        allWinrateService.updateReply(adcEng,supEng,writer,content,rno);
        log.info("실제수정창->에서의 lastReplyIndx{}",lastReplyIndex);
        redirectAttributes.addAttribute("adcEng",adcEng);
        redirectAttributes.addAttribute("supEng",supEng);
        redirectAttributes.addAttribute("lastReplyIndex",lastReplyIndex);
        return "redirect:/awrboard/{adcEng}/{supEng}/{lastReplyIndex}";
    }

    @GetMapping("/awrboard/{adcEng}/{supEng}/{rno}/delete/{lastReplyIndex}")
    public String deleteReply(@PathVariable("adcEng") String adcEng,
                              @PathVariable("supEng") String supEng,
                              @PathVariable("rno") int rno,
                              @PathVariable("lastReplyIndex") int lastReplyIndex,
                              RedirectAttributes redirectAttributes){
        allWinrateService.deleteReply(adcEng,supEng,rno);
        redirectAttributes.addAttribute("adcEng",adcEng);
        redirectAttributes.addAttribute("supEng",supEng);
        redirectAttributes.addAttribute("lastReplyIndex",lastReplyIndex);
        return "redirect:/awrboard/{adcEng}/{supEng}/{lastReplyIndex}";
    }

    @PostMapping("awr_secReply/write")
    public String writeSecReply(
            @RequestParam("writer") String writer,
            @RequestParam("content") String content,
            @RequestParam("adcE") String adcE,
            @RequestParam("supE") String supE,
            @RequestParam("rno") int rno,
            @RequestParam("lastReplyIndex") int lastReplyIndex,
            RedirectAttributes redirectAttributes
    ){
        allWinrateService.writeSecReply(adcE,supE,rno,writer,content);
        redirectAttributes.addAttribute("adcE",adcE);
        redirectAttributes.addAttribute("supE",supE);
        redirectAttributes.addAttribute("lastReplyIndex",lastReplyIndex);

        return "redirect:/awrboard/{adcE}/{supE}/{lastReplyIndex}";
    }

    //조합댓글판 대댓글 삭제
    @GetMapping("/awrboard/{adcEng}/{supEng}/{secRno}/{rno}/delete/{lastReplyIndex}")
    public String deleteSecReply(@PathVariable("adcEng") String adcEng,
                              @PathVariable("supEng") String supEng,
                              @PathVariable("secRno") int secRno,
                              @PathVariable("rno") int rno,
                              @PathVariable("lastReplyIndex") int lastReplyIndex,
                              RedirectAttributes redirectAttributes){
        allWinrateService.deleteSecReply(adcEng,supEng,secRno,rno);
        redirectAttributes.addAttribute("adcEng",adcEng);
        redirectAttributes.addAttribute("supEng",supEng);
        redirectAttributes.addAttribute("lastReplyIndex",lastReplyIndex);

        return "redirect:/awrboard/{adcEng}/{supEng}/{lastReplyIndex}";
    }


    //조합댓글판 대댓글 수정
    @PostMapping("/awrboard/{adcEng}/{supEng}/{secRno}/{rno}/update/{lastReplyIndex}")
    public String updateSecReply(@RequestParam("adcEng") String adcEng,
                              @RequestParam("supEng") String supEng,
                              @RequestParam("secRno") int secRno,
                              @RequestParam("rno") int rno,
                              @RequestParam("writer") String writer,
                              @RequestParam("content") String content,
                              @PathVariable("lastReplyIndex") int lastReplyIndex,
                              RedirectAttributes redirectAttributes){
        allWinrateService.updateSecReply(adcEng,supEng,writer,content,secRno,rno);
        redirectAttributes.addAttribute("adcEng",adcEng);
        redirectAttributes.addAttribute("supEng",supEng);
        redirectAttributes.addAttribute("lastReplyIndex",lastReplyIndex);

        return "redirect:/awrboard/{adcEng}/{supEng}/{lastReplyIndex}";
    }

    //awr 조합대댓글댓글판 수정폼이동
    @GetMapping("/awrboard/{adcEng}/{supEng}/{secRno}/{rno}/updateForm/{lastReplyIndex}")
    public String updateReplyForm(@PathVariable("adcEng") String adcEng,
                                  @PathVariable("supEng") String supEng,
                                  @PathVariable("secRno") int secRno,
                                  @PathVariable("rno") int rno,
                                  @PathVariable("lastReplyIndex") int lastReplyIndex,
                                  Model model) throws IOException {
        CombiReplyBoard combiReplyBoard = allWinrateService.mkViewCombiBoard(adcEng,supEng,lastReplyIndex);
        model.addAttribute("combiReplyBoard",combiReplyBoard);
        model.addAttribute("rno",rno);
        model.addAttribute("sdcRno",secRno);

        return "allWinrate/awrBoardSecUpdate";
    }
}
