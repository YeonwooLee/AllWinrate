package jyanoos.lol_bottom.mapper;

import jyanoos.lol_bottom.AllWinrateConfig;
import jyanoos.lol_bottom.domain.AllWinrate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FreeBoardMapperTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AllWinrateConfig.class);

    FreeBoardMapper freeBoardMapper = ac.getBean(FreeBoardMapper.class);

}
