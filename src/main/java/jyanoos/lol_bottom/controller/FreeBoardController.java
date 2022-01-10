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

    //게시글 리스트
    @RequestMapping("/list")
    public String freeBoardList(Model model){
        List<FreeBoard> freeBoardList = freeBoardService.freeBoardList();
        model.addAttribute("freeBoardList",freeBoardList);
        return "freeBoard";
    }

    //get 요청시 글 등록 폼
    @GetMapping("/write")
    public String writePage(){
        return "write";
    }
    
    //post 요청시 글 등록 후 글 상세 조회로 redirect
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


    //{bno} 글 수정창으로 이동
    @GetMapping("/{bno}/update")
    public String updateFreeBoardForm(@PathVariable("bno") int bno, Model model){
        FreeBoard freeBoard = freeBoardService.findByBno(bno);
        model.addAttribute("freeBoard",freeBoard);
        return "updateFreeBoard";
    }

    //updateFreeBoard의 form에서 FreeBoard 필드 받아오고 해당 정보로 글 수정 후 글 상세로 이동
    @PostMapping("/{bno}/update")
    public String updateFreeBoard(@ModelAttribute("freeBoard") FreeBoard freeBoard, RedirectAttributes redirectAttributes){

        //성공시 글번호, 실패시 0 리턴
        int bno = freeBoardService.updateFreeBoard(freeBoard.getBno(), freeBoard.getTitle(), freeBoard.getContent(), freeBoard.getWriter());

        redirectAttributes.addAttribute("bno",bno);
        return "redirect:/free_board/{bno}";

    }

    //bno에 해당하는 글 삭제
    @GetMapping("/{bno}/delete")
    public String deleteFreeBoard(@PathVariable("bno") int bno){
        boolean deleteSuccess = freeBoardService.deleteByBno(bno); //삭제 성공시 true 실패시 false
        return "redirect:/free_board/list";
    }

    //자게 목록 + 페이징
    @GetMapping("/listPage/{num}/{nowPage}")
    public String getListPage(@PathVariable int num, @PathVariable int nowPage, Model model){

        List<Object> pageListAndNowPage = freeBoardService.freeBoardListPage(num, nowPage); //0번째는 페이지인덱스리스트,1번째는 현재페이지글목록
        List<Integer> pageList = (List<Integer>) pageListAndNowPage.get(0);//object형으로 가져와서 형변환필요 -
        List<FreeBoard> nowPageList = (List<FreeBoard>) pageListAndNowPage.get(1);//object형으로 가져와서 형변환필요
        int needPagePlusOne = (int) pageListAndNowPage.get(2);
        int lastIndex = (int) pageListAndNowPage.get(3);

        model.addAttribute("pageList",pageList);
        model.addAttribute("nowPageList",nowPageList);
        model.addAttribute("needPagePlusOne", needPagePlusOne);
        model.addAttribute("lastIndex",lastIndex);
        return "freeBoardList";
    }
}
