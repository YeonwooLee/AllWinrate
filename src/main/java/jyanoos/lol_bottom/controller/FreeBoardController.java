package jyanoos.lol_bottom.controller;


import jyanoos.lol_bottom.domain.FreeBoard;
import jyanoos.lol_bottom.service.FreeBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/free_board/*")
public class FreeBoardController {
    private final FreeBoardService freeBoardService; //싱글톤

    //의존성 주입
    public FreeBoardController(FreeBoardService freeBoardService){
        this.freeBoardService = freeBoardService;
    }

    @RequestMapping("/list")
    public String freeBoardList(Model model){
        List<FreeBoard> freeBoardList = freeBoardService.freeBoardList();
        model.addAttribute("freeBoardList",freeBoardList);
        return "freeBoard";
    }


    @GetMapping("/write")
    public String writePage(){
        return "write";
    }
    
    //글 등록
    @PostMapping("/write")
    public String writeFreeBoard(@ModelAttribute("freeBoard")FreeBoard freeBoard, RedirectAttributes redirectAttributes){
        int bno = freeBoardService.writeFreeBoard(freeBoard); //저장성공시 글번호, 실패시 0 리턴
        redirectAttributes.addAttribute("bno",bno);
        return "redirect:/free_board/{bno}";
    }


    //게시글 상세 조회
    @GetMapping("/{bno}")
    public String readFreeBoard(@PathVariable("bno") int bno, Model model){
        FreeBoard freeBoard = freeBoardService.findByBno(bno);
        model.addAttribute("freeBoard",freeBoard);
        return "freeBoardArticle";

    }
}
