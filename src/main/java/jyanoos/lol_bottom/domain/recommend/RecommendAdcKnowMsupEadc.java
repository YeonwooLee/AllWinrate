package jyanoos.lol_bottom.domain.recommend;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RecommendAdcKnowMsupEadc implements Comparable<RecommendAdcKnowMsupEadc>{
    String mAdc; //임의의 원딜
    String mSup; //입력받은 아군서폿
    String eAdc; //입력받은 적원딜

    int whole;
    int win;
    float winrate;//임의의원딜_입력받은서폿 조합의 승률

    public RecommendAdcKnowMsupEadc(int whole, int win, float winrate) {
        this.whole = whole;
        this.win = win;
        this.winrate =winrate;
    }

    @Override
    public int compareTo(RecommendAdcKnowMsupEadc o) {
        //this쪽이 승률이 높다면 -1 리턴
        if(this.winrate>o.winrate){
            return -1;
            //승률 같으면 등장 수 많은게 위로
        }else if(this.winrate==o.winrate){
            if(this.whole>o.whole){
                return -1;
            }else{
                return 1;
            }
        }else{
            return 1;
        }
    }
}

