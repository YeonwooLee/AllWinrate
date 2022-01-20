package jyanoos.lol_bottom.mapper;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.domain.CombiReply;
import jyanoos.lol_bottom.domain.CombiSecReply;
import jyanoos.lol_bottom.domain.Reply;
import jyanoos.lol_bottom.lolSetting.LolSetting;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AllWinrateMapper {
    String version= LolSetting.version;

    //adc_sup 게시판에 writer이름으로 content라는 내용의 댓글 달기
    @Insert("INSERT INTO ${adc}_${sup}(writer,content,nowVersion)\n" +
            "VALUES(#{writer},#{content},#{nowVersion})")
    int writeAwlReply(
            @Param("adc") String adc,
            @Param("sup") String sup,
            @Param("writer") String writer,
            @Param("content") String content,
            @Param("nowVersion") String nowVersion
    );

    //대댓글 입력: 원글번호, 대댓글쓴이, 대댓내용
    @Insert("INSERT INTO ${adc}_${sup}_secreply(rno,writer,content,nowVersion)\n" +
            "VALUES(${rno},#{writer},#{content},#{nowVersion})")
    int insertSecReply(@Param("adc") String adc,
                       @Param("sup") String sup,
                       @Param("rno") int rno,
                       @Param("writer") String writer,
                       @Param("content") String content,
                       @Param("nowVersion") String nowVersion);




    //원딜_서폿 테이블에 컬럼 유무 확인
    @Select("SELECT COUNT(*) FROM information_schema.columns\n" +
            "WHERE table_schema = 'lol_data' AND TABLE_NAME ='${tableName}' AND COLUMN_NAME='${colName}'")
    int checkColExist(@Param("tableName") String tableName,
                      @Param("colName") String colName);


    // --테이블에 column 추가
    @Update("ALTER TABLE `${tableName}` ADD `nowVersion` VARCHAR(10) NULL")
    int addColumn(@Param("tableName") String tableName);


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

    //조합댓글판 대댓목록 가져오기 원딜,서폿명필요~
    @Select("SELECT * FROM ${adc}_${sup}_secreply")
    List<CombiSecReply> getListSecReply(@Param("adc") String adc, @Param("sup") String sup);

    //조합 댓글판 대댓글 rno 하위 댓글 전체 조회: 원글번호필요
    @Select("SELECT * FROM ${adc}_${sup}_secreply WHERE rno=${rno} ORDER BY secRno desc")
    List<CombiSecReply> getListSecReplyByRno(
            @Param("adc") String adcE,
            @Param("sup") String supE,
            @Param("rno") int rno
    );


    //조합댓글판 rno, secrno로 단일 대댓 가져오기
    @Select("SELECT * FROM ${adc}_${sup}_secreply\n" +
            "WHERE secRno=${secRno} AND rno=${rno}")
    CombiSecReply getSecReply(@Param("adc") String adc,
                      @Param("sup") String sup,
                      @Param("secRno") int secRno,
                      @Param("rno") int rno);


    //adc_sup라는 이름의 테이블 생성함
    @Update("CREATE TABLE ${adc}_${sup}(\n" +
            "\trno INT NOT NULL AUTO_INCREMENT,\n" +
            "\twriter VARCHAR(30) NOT NULL,\n" +
            "\tcontent TEXT NOT NULL,\n" +
            "\tregDate TIMESTAMP NOT NULL DEFAULT NOW(),\n" +
            "\tnowVersion VARCHAR(10) NULL,\n" +
            "\tPRIMARY KEY(rno)\n" +
            "\t)")
    int createAwrTbl(@Param("adc") String adc, @Param("sup") String sup);


    //adc_sup_secReply라는 테이블 생성함 대댓 저장용
//    @Update("CREATE TABLE ${adc}_${sup}_secReply(\n" +
//            "\tsecRno INT NOT NULL AUTO_INCREMENT,\n" +
//            "\trno INT NOT NULL,\n" +
//            "\twriter VARCHAR(30) NOT NULL,\n" +
//            "\tcontent TEXT NOT NULL,\n" +
//            "\tregDate TIMESTAMP NOT NULL DEFAULT NOW(),\n" +
//            "\tPRIMARY KEY(secRno,rno),\n" +
//            "\tFOREIGN KEY(rno)\n" +
//            "\tREFERENCES ${adc}_${sup}(rno) ON DELETE CASCADE\n")
//    int createAwrSecTbl(@Param("adc") String adc, @Param("sup") String sup);
    @Update("CREATE TABLE ${adc}_${sup}_secReply(\n" +
            "\tsecRno INT NOT NULL AUTO_INCREMENT,\n" +
            "\trno INT NOT NULL,\n" +
            "\twriter VARCHAR(30) NOT NULL,\n" +
            "\tcontent TEXT NOT NULL,\n" +
            "\tregDate TIMESTAMP NOT NULL DEFAULT NOW(),\n" +
            "\tnowVersion VARCHAR(10) NULL,\n" +
            "\tPRIMARY KEY(secRno,rno),\n" +
            "\tFOREIGN KEY(rno)\n" +
            "\tREFERENCES ${adc}_${sup} (rno) ON DELETE CASCADE\n" +
            "\t)")
    int createAwrSecTbl(@Param("adc") String adc, @Param("sup") String sup);

    //조합댓글판 수정 필요정보: 영문원딜서폿명, 보낼정보:수정내용, 수정저자
    @Update("UPDATE ${adcEng}_${supEng} SET\n" +
            "writer=#{writer},\n" +
            "content=#{content}\n" +
            "WHERE rno=#{rno}")
    int updateAwrTbl(
            @Param("adcEng") String adcEng,
            @Param("supEng") String supEng,
            @Param("writer") String writer,
            @Param("content") String content,
            @Param("rno") int rno
    );

    //-- 대댓글 수정: 원글번호, 대댓글 번호
    @Update("UPDATE ${adc}_${sup}_secreply SET\n" +
            "writer=#{writer}, content=#{content}\n" +
            "WHERE secRno=${secRno} AND rno=${rno}")
    int updateSecReply(@Param("adc") String adc,
                       @Param("sup") String sup,
                       @Param("writer") String writer,
                       @Param("content") String content,
                       @Param("secRno") int secRno,
                       @Param("rno") int rno
                       );




    @Delete("DELETE FROM ${adcEng}_${supEng}\n" +
            "WHERE rno=#{rno}")
    int deleteAwrTbl(@Param("adcEng") String adcEng,
                     @Param("supEng") String supEng,
                     @Param("rno") int rno);


    @Delete("DELETE FROM ${adc}_${sup}_secreply\n" +
            "WHERE secRno=${secRno} AND rno=${rno}")
    int deleteSecReply(@Param("adc") String adc,
                       @Param("sup") String sup,
                       @Param("secRno") int secRno,
                       @Param("rno") int rno);
}
