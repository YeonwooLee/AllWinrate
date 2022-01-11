package jyanoos.lol_bottom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString @AllArgsConstructor
public class Paging {
    List<Integer> pageList;
    List<FreeBoard> nowPageList;
    int needPagePlusOne;
    int lastIndex;
}
