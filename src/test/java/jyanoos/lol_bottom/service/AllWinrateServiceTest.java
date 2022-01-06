package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.AllWinrateConfig;
import jyanoos.lol_bottom.domain.AllWinrate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AllWinrateServiceTest {

    @Test
    @DisplayName("AWR구현체는 AWRImpl임")
    void AllWinrateList() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AllWinrateConfig.class);
        AllWinrateService allWinrateService = ac.getBean(AllWinrateService.class);
        Assertions.assertThat(allWinrateService).isInstanceOf(AllWinrateServiceImpl.class);

    }

}