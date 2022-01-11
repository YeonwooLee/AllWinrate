package jyanoos.lol_bottom.controller;


import jyanoos.lol_bottom.domain.FreeBoard;
import jyanoos.lol_bottom.domain.Paging;
import jyanoos.lol_bottom.domain.Reply;
import jyanoos.lol_bottom.service.FreeBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/free_board/*")
public class FreeBoardController {
    private final FreeBoardService freeBoardService; //싱글톤

    //의존성 주입
    public FreeBoardController(FreeBoardService freeBoardService){
        this.freeBoardService = freeBoardService;
    }

    //게시글 리스트
    @RequestMapping("/list") //더이상 사용x -> getListPage로 버전업
    public String freeBoardList(Model model){
        //List<FreeBoard> freeBoardList = freeBoardService.freeBoardList();
        //model.addAttribute("freeBoardList",freeBoardList);
        //return "freeBoard";
        return "redirect:/free_board/listpage/20/0"; //버전업한 컨트롤러로 리다이렉트

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


    //게시글 상세 조회, 댓글조회
    @GetMapping("/{bno}")
    public String readFreeBoard(@PathVariable("bno") int bno, Model model){
        //게시글 상세
        FreeBoard freeBoard = freeBoardService.findByBno(bno);
        model.addAttribute("freeBoard",freeBoard);

        //댓글
        List<Reply> replies= freeBoardService.findReplyByBno(bno);
        model.addAttribute("replies",replies);

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
    @GetMapping("/listpage/{num}/{nowPage}")
    public String getListPage(@PathVariable int num, @PathVariable int nowPage, Model model){

        //버전1 List<Object> 사용
//        List<Object> pageListAndNowPage = freeBoardService.freeBoardListPage(num, nowPage); //0번째는 페이지인덱스리스트,1번째는 현재페이지글목록
//        List<Integer> pageList = (List<Integer>) pageListAndNowPage.get(0);//object형으로 가져와서 형변환필요 -
//        List<FreeBoard> nowPageList = (List<FreeBoard>) pageListAndNowPage.get(1);//object형으로 가져와서 형변환필요
//        int needPagePlusOne = (int) pageListAndNowPage.get(2);
//        int lastIndex = (int) pageListAndNowPage.get(3);
//        model.addAttribute("pageList",pageList);
//        model.addAttribute("nowPageList",nowPageList);
//        model.addAttribute("needPagePlusOne", needPagePlusOne);
//        model.addAttribute("lastIndex",lastIndex);

        //버전2 Paging 클래스사용
        Paging paging = freeBoardService.freeBoardListPage(num, nowPage);
        log.info("paging is {}",paging);
        model.addAttribute("paging",paging);

        return "freeBoardList";
    }


    //searchType과 keyword로 글 검색
    @GetMapping("/listFindpage/{num}/{nowPage}")
    String findArticle(@PathVariable("num") int num, @PathVariable("nowPage") int nowPage,@RequestParam("searchType")String searchType, @RequestParam("keyword")String keyword, Model model){
        Paging paging = freeBoardService.freeBoardFindListPage(num, nowPage, searchType, keyword);
        model.addAttribute("paging",paging);
        model.addAttribute("searchType",searchType);
        model.addAttribute("keyword",keyword);
        return "freeBoardFindList";

    }

    @PostMapping("/reply/save")
    String saveReply(@ModelAttribute Reply reply, Model model, RedirectAttributes redirectAttributes){
        int success = freeBoardService.writeFreeBoardReply(reply);
        log.info("댓글입력됨. 번호 = {} ",success);
        redirectAttributes.addAttribute("bno",reply.getBno());
        return "redirect:/free_board/{bno}";
    }
}
