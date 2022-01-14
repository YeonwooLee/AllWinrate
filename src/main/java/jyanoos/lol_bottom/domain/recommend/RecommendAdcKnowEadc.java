package jyanoos.lol_bottom.domain.recommend;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RecommendAdcKnowEadc implements Comparable<RecommendAdcKnowEadc>{
    String mAdc; //임의의 원딜
    String eAdc; //입력받은 원딜
    int whole;
    int win;
    float winrate;//

    public RecommendAdcKnowEadc(int whole, int win, float winrate) {
        this.whole = whole;
        this.win = win;
        this.winrate =winrate;
    }

    @Override
    public int compareTo(RecommendAdcKnowEadc o) {
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