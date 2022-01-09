package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.AllWinrateConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class FreeBoardServiceImplTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AllWinrateConfig.class);
    //AllWinrateService allWinrateService = ac.getBean(AllWinrateService.class);
    FreeBoardService freeBoardService = ac.getBean(FreeBoardService.class);

    @Test
    public void update(){
        //given
        freeBoardService.updateFreeBoard(2,"2","2tnwjdsodyd","wjwk"); //이 테스트 실패함 왜냐면 테이블 못찾는 에러 생김
        //when
        
        // then
    }
}