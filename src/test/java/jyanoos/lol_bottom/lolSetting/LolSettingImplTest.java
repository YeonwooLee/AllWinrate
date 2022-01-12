package jyanoos.lol_bottom.lolSetting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LolSettingImplTest {
    LolSettingImpl lolSetting = new LolSettingImpl();

    @Test
    @DisplayName("한->영 챔프명 Map 생성")
    public void koToEngMap() throws IOException {
        //given
        Map<String,String> koToEngMap = lolSetting.koToEngMap();

        //when

        // then
        System.out.println("koToEngMap = " + koToEngMap);
    }
    @Test
    @DisplayName("한->영 챔프명 Map 생성")
    public void engToKoMap() throws IOException {
        //given
        Map<String,String> engToKoMap = lolSetting.engToKoMap();

        //when

        // then
        System.out.println("engToKoMap = " + engToKoMap);
    }

    @Test
    @DisplayName("한국챔프명->영어챔프명")
    public void convertKoToEng() throws IOException {
        //given
        String champName = "아칼리";
        //when
        Assertions.assertThat(lolSetting.convertKoToEng(champName)).isEqualTo("AKALI");
        // then
    }
}