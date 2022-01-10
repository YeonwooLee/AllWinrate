package jyanoos.lol_bottom.mapper;


import jyanoos.lol_bottom.domain.FreeBoard;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FreeBoardMapper {

    //모든 게시글 리스트 작성
    @Select("select * from tbl_free_board")
    List<FreeBoard> freeBoardList();


    //DBName DB에 tableName 테이블 존재하는지 확인(있으면1 없으면0 리턴)
    @Select("SELECT EXISTS " +
            "(SELECT 1 FROM Information_schema.tables " +
            "WHERE table_schema = '${DBName}' AND table_name = '${tableName}') " +
            "AS flag")
    int tblExist(@Param("DBName") String DBName, @Param("tableName") String tableName);

    //가장 최근에 등록된 글 번호 조회
    @Select("SELECT MAX(bno) FROM tbl_free_board")
    int getMaxBno();

    //글번호로 글조회
    @Select("SELECT * FROM tbl_free_board where bno=#{bno}")
    FreeBoard findByBno(@Param("bno") int bno);


    //게시물 총 갯수 조회
    @Select("select count(bno) from tbl_free_board")
    int getFreeBoardNum();


    //tbl_free_board에서 startIndexSql부터 numFreeBoardSql개만 가져오기
    @Select("SELECT\n" +
            "\tbno,title,writer,regDate,viewCnt\n" +
            "\tFROM tbl_free_board\n" +
            "\tORDER BY bno DESC\n" +
            "\t\tLIMIT #{startIndexSql},#{numFreeBoardSql}")
    List<FreeBoard> freeBoardListPage(
            @Param("startIndexSql") int startIndex,
            @Param("numFreeBoardSql") int numFreeBoard
    );



    //tbl_free_board에 행 입력(성공시1 반환 인듯)
    @Insert("insert into tbl_free_board(title, content, writer)\n" +
            "  values('${title}', '${content}', '${writer}')")
    int save(@Param("title") String title,
             @Param("content") String content,
             @Param("writer") String writer);




    @Update("UPDATE tbl_free_board\n" +
            "SET\n" +
            "title = #{titleSql}, content=#{contentSql}, writer=#{writerSql}\n" +
            "WHERE\n" +
            "bno=${bnoSql}")
    boolean freeBoardUpdate(@Param("titleSql") String title,
                                 @Param("contentSql") String content,
                                 @Param("writerSql") String writer,
                                 @Param("bnoSql") int bno);


    //tbl_free_board 에서 글번호가 bno인 글 삭제, 성공시true
    @Delete("DELETE FROM tbl_free_board WHERE bno=#{bnoSql}")
    boolean deleteByBno(@Param("bnoSql")int bno);

}
