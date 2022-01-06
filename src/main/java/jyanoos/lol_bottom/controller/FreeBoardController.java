package jyanoos.lol_bottom.controller;


import jyanoos.lol_bottom.domain.FreeBoard;
import jyanoos.lol_bottom.service.FreeBoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
