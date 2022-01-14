package jyanoos.lol_bottom.mapper;

import jyanoos.lol_bottom.domain.recommend.Game;
import jyanoos.lol_bottom.lolSetting.LolSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecommendMapper {
    String version= LolSetting.versionDot;

    //아는거x
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version)
    List<Game> vs();

    //서폿 vs
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version+" AND (lol_blue.sup=#{supA} OR lol_red.sup=#{supA})")
    List<Game> supVs(@Param("supA") String supA);

    //원딜 vs 서폿
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version+" AND ((lol_blue.adc=#{adcA} and lol_red.sup=#{supB})or(lol_red.adc=#{adcA} and lol_blue.sup=#{supB}));\n" +
            "\t\t")
    List<Game> adcVsSup(@Param("adcA") String adcA,
                        @Param("supB") String supB);

    //서폿 vs 서폿
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version+" AND ((lol_blue.sup=#{supA} and lol_red.sup=#{supB})or(lol_red.sup=#{supA} and lol_blue.sup=#{supB}))")
    List<Game> supVsSup(@Param("supA") String adcA,
                        @Param("supB") String supB);

    //원딜서폿 vs 서폿
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version+" \n" +
            "\t\t\tAND ((lol_blue.adc=#{adcA} AND lol_blue.sup=#{supA} AND lol_red.sup=#{supB})or(lol_red.adc=#{adcA} AND lol_red.sup=#{supA} and lol_blue.sup=#{supB}))")
    List<Game> adcSupVsSup(@Param("adcA") String adcA,
                           @Param("supA") String supA,
                           @Param("supB") String supB);


    //원딜 vs
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version+" AND (lol_blue.adc=#{adcA} OR lol_red.adc=#{adcA})")
    List<Game> adcVs(@Param("adcA") String adcA);

    //원딜vs원딜
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version+" AND ((lol_blue.adc=#{adcA} and lol_red.adc=#{adcB})or(lol_red.adc=#{adcA} and lol_blue.adc=#{adcB}))")
    List<Game> adcVsAdc(@Param("adcA") String adcA,
                        @Param("adcB") String adcB);

    //원딜서폿vs
    @Select("SELECT lol_blue.adc AS badc, lol_blue.sup AS bsup, lol_blue.win AS bwin, lol_red.win AS rwin, lol_red.adc AS radc, lol_red.sup AS rsup\n" +
            "FROM \n" +
            "\tlol_blue INNER JOIN lol_red ON lol_blue.gameid=lol_red.gameid\n" +
            "\t\tINNER JOIN lol_time_v ON lol_blue.gameid=lol_time_v.gameid\n" +
            "\t\t\tWHERE lol_time_v.version="+version+" AND ((lol_blue.adc=#{adcA} and lol_blue.sup=#{supA})or(lol_red.adc=#{adcA} and lol_red.sup=#{supA}))")
    List<Game> adcSupVs(@Param("adcA") String adcA,
                        @Param("supA") String supA);
}
