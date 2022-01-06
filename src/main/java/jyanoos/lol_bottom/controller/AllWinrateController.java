package jyanoos.lol_bottom.controller;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.mapper.AllWinrateMapper;
import jyanoos.lol_bottom.service.AllWinrateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AllWinrateController {
    private final AllWinrateService allWinrateService;


    //awr 기본페이지 구성
    @RequestMapping("/awrmain")
    public String awrMain(Model model) throws IOException {
        List<AllWinrate> allWinrateList = allWinrateService.mkAllWinrateList(100,7);
        model.addAttribute("allWinrateList",allWinrateList);

        return "awrMain";
    }
}
