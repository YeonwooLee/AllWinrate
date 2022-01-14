package jyanoos.lol_bottom.domain.recommend;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RecommendRequest {
    String recommendKind;
    String mAdc;
    String mSup;
    String eAdc;
    String eSup;
}
