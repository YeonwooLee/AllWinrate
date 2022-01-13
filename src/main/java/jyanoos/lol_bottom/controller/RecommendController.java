package jyanoos.lol_bottom.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class RecommendController {
    @RequestMapping("recommend")
    public String recommendMain(){
        return "/recommend/recommend";
    }
}
