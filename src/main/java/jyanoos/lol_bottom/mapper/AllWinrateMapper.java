package jyanoos.lol_bottom.mapper;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.lolSetting.LolSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AllWinrateMapper {
    String version= LolSetting.version;

    @Select("select * from all_winrate_"+version)
    List<AllWinrate> allWinrateList();

}
