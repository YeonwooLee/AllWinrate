package jyanoos.lol_bottom.lolSetting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface LolSetting {
    static String version = "12_2"; //이거 세팅 바꿀 때 koToEngV_v.json도 같이 변경해주세요
    static String versionDot="12.2";//이거 세팅 바꿀 때 koToEngV_v.json도 같이 변경해주세요
    static String tier="GOLD";
    //한글챔프명 -> 영어챔프명
    String convertKoToEng(String korChamp) throws IOException;

    Map<String,String> koToEngMap() throws IOException;


    //영어챔프명->한글챔프명
    String convertEngToKo(String engChamp) throws IOException;

    Map<String,String> engToKoMap() throws IOException;
}
