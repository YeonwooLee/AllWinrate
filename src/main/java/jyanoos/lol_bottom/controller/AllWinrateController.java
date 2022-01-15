package jyanoos.lol_bottom.controller;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.domain.CombiReplyBoard;
import jyanoos.lol_bottom.mapper.AllWinrateMapper;
import jyanoos.lol_bottom.service.AllWinrateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
public class AllWinrateController {
    private final AllWinrateService allWinrateService;


    //awr 기본페이지 구성
    @RequestMapping("/awrmain")
    public String awrMain(Model model) throws IOException {
        List<AllWinrate> allWinrateList = allWinrateService.mkAllWinrateList(200,7);
        model.addAttribute("allWinrateList",allWinrateList);

        return "awrMain";
    }

    //awr 조합댓글판 보기
    @RequestMapping("/awrboard/{adc}/{sup}")
    public String viewCombiBoard(@PathVariable("adc") String adc, @PathVariable("sup") String sup, Model model) throws IOException {
        CombiReplyBoard combiReplyBoard = allWinrateService.mkViewCombiBoard(adc, sup);
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

    //awr 조합댓글판 수정하기
    @GetMapping("/awrboard/{adcEng}/{supEng}/{rno}/update")
    public String updateReplyForm(@PathVariable("adcEng") String adcEng,
                                  @PathVariable("supEng") String supEng,
                                  @PathVariable("rno") int rno,
                                  Model model) throws IOException {
        CombiReplyBoard combiReplyBoard = allWinrateService.mkViewCombiBoard(adcEng,supEng);
        model.addAttribute("combiReplyBoard",combiReplyBoard);
        model.addAttribute("rno",rno);

        return "allWinrate/awrBoardUpdate";
    }

    @PostMapping("/awrboard/{adcEng}/{supEng}/{rno}/update")
    public String updateReply(@RequestParam("adcEng") String adcEng,
                              @RequestParam("supEng") String supEng,
                              @RequestParam("rno") int rno,
                              @RequestParam("writer") String writer,
                              @RequestParam("content") String content,
                              RedirectAttributes redirectAttributes){
        allWinrateService.updateReply(adcEng,supEng,writer,content,rno);
        redirectAttributes.addAttribute("adcEng",adcEng);
        redirectAttributes.addAttribute("supEng",supEng);

        return "redirect:/awrboard/{adcEng}/{supEng}";
    }

    @GetMapping("/awrboard/{adcEng}/{supEng}/{rno}/delete")
    public String updateReply(@PathVariable("adcEng") String adcEng,
                              @PathVariable("supEng") String supEng,
                              @PathVariable("rno") int rno,
                              RedirectAttributes redirectAttributes){
        allWinrateService.deleteReply(adcEng,supEng,rno);
        redirectAttributes.addAttribute("adcEng",adcEng);
        redirectAttributes.addAttribute("supEng",supEng);
        return "redirect:/awrboard/{adcEng}/{supEng}";
    }


}
