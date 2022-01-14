package jyanoos.lol_bottom.domain.recommend;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//블루팀(원딜,서폿,승리) 레드팀(원딜,서폿,승리)
@Getter @Setter @ToString
public class Game {
    String badc;
    String bsup;
    int bwin;

    String radc;
    String rsup;
    int rwin;

}
