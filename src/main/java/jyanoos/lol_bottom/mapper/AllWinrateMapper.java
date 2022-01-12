package jyanoos.lol_bottom.mapper;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.domain.CombiReply;
import jyanoos.lol_bottom.lolSetting.LolSetting;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AllWinrateMapper {
    String version= LolSetting.version;

    //adc_sup 게시판에 writer이름으로 content라는 내용의 댓글 달기
    @Insert("INSERT INTO ${adc}_${sup}(writer,content)\n" +
            "VALUES(#{writer},#{content})")
    int writeAwlReply(
            @Param("adc") String adc,
            @Param("sup") String sup,
            @Param("writer") String writer,
            @Param("content") String content
    );







    //all_winrate_version(버전은 lolsetting에서 관리) 모든 행 가져옴
    @Select("select * from all_winrate_"+version)
    List<AllWinrate> allWinrateList();

    //adc_sup 게시판의 모든 댓글 가져옴
    @Select("select * from ${adc}_${sup} ORDER BY rno DESC")
    List<CombiReply> combiReplyList(
            @Param("adc") String adc,
            @Param("sup") String sup
    );

    //DBName DB에 tableName 테이블 존재하는지 확인(있으면1 없으면0 리턴)
    @Select("SELECT EXISTS " +
            "(SELECT 1 FROM Information_schema.tables " +
            "WHERE table_schema = '${DBName}' AND table_name = '${tableName}') " +
            "AS flag")
    int tblExist(@Param("DBName") String DBName, @Param("tableName") String tableName);


    //원딜, 서폿 영문명으로 총전적 구하기
    @Select("SELECT * FROM all_winrate_"+version+"\n" +
            "WHERE bot_combi LIKE CONCAT('%',#{adc},'_',#{sup},'%','"+version+"')")
    AllWinrate getAllwinrate(@Param("adc") String adc,@Param("sup") String sup);






    //adc_sup라는 이름의 테이블 생성함
    @Update("CREATE TABLE ${adc}_${sup}(\n" +
            "\trno INT NOT NULL AUTO_INCREMENT,\n" +
            "\twriter VARCHAR(30) NOT NULL,\n" +
            "\tcontent TEXT NOT NULL,\n" +
            "\tregDate TIMESTAMP NOT NULL DEFAULT NOW(),\n" +
            "\tPRIMARY KEY(rno)\n" +
            "\t)")
    int createAwrTbl(@Param("adc") String adc, @Param("sup") String sup);
}
