package jyanoos.lol_bottom.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
//총승률 정보
public class AllWinrate implements Comparable<AllWinrate>{
    String bot_combi;
    String adc;
    String sup;
    String engAdc;
    String engSup;
    int whole;
    int win;
    int lose;
    float win_rate;

    public AllWinrate(String bot_combi, int whole, int win, int lose, float win_rate) {
        this.bot_combi = bot_combi;
        this.whole = whole;
        this.win = win;
        this.lose = lose;
        this.win_rate = win_rate;
        splitCombi(bot_combi);
    }

    @Override
    public int compareTo(AllWinrate o) {
        //this쪽이 승률이 높다면 -1 리턴
        if(this.win_rate>o.win_rate){
            return -1;
        //승률 같으면 등장 수 많은게 위로
        }else if(this.win_rate==o.win_rate){
            if(this.whole>o.whole){
                return -1;
            }else{
                return 1;
            }
        }else{
            return 1;
        }
    }
    public void splitCombi(String bot_combi){
        String[] combiList = bot_combi.split("_");
        adc=combiList[0];
        sup=combiList[1];
    }
}
