package jyanoos.lol_bottom.lolSetting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface LolSetting {
    static String version = "12_1";
    static String versionDot="12.1";
    static String tier="GOLD";

    String convertKoToEng(String korChamp) throws IOException;

    Map<String,String> koToEngMap() throws IOException;



    String convertEngToKo(String engChamp) throws IOException;

    Map<String,String> engToKoMap() throws IOException;
}
