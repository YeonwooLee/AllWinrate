package jyanoos.lol_bottom.mapper;


import jyanoos.lol_bottom.domain.FreeBoard;
import jyanoos.lol_bottom.domain.Reply;
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

//자게 게시판 페이징 시작
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
//자게 게시판 페이징 끝

//자게 검색 구현 시작

    //tbl_free_board에서 글제목에 {}가 포함된 행 중 startIndexSql부터 numFreeBoardSql개만 가져오기
    @Select("SELECT\n" +
            "\tbno,title,writer,regDate,viewCnt\n" +
            "\tFROM tbl_free_board\n" +
            "\tWHERE ${findCol} like concat('%',#{findKeyword},'%')\n" +
            "\tORDER BY bno DESC\n" +
            "\t\tLIMIT #{startIndexSql},#{numFreeBoardSql}")
    List<FreeBoard> freeBoardFindByCol(
            @Param("startIndexSql") int startIndex,
            @Param("numFreeBoardSql") int numFreeBoard,
            @Param("findKeyword") String findKeyword,
            @Param("findCol") String findCol
    );
    //tbl_free_board에서 글제목에 {}가 포함된 행 갯수
    @Select("SELECT COUNT(bno) FROM tbl_free_board WHERE ${findCol} LIKE concat('%',#{findKeyword},'%')")
    int freeBoardCountFindByCol(@Param("findKeyword") String findKeyword,
                                @Param("findCol") String findCol);


    //tbl_free_board에서 글제목이나 글내용에 findkeyword가 포함된 글 리스트 startIndexSql부터 numFreeBoardSql개만 가져오기
    @Select("SELECT\n" +
            "\tbno,title,writer,regDate,viewCnt\n" +
            "\tFROM tbl_free_board\n" +
            "\tWHERE title like concat('%',#{findKeyword},'%')\n" +
            "\tor content like concat('%',#{findKeyword},'%')\n" +
            "\tORDER BY bno DESC\n" +
            "\t\tLIMIT #{startIndexSql},#{numFreeBoardSql}")
    List<FreeBoard> freeBoardFindByTitleOrContent(
            @Param("startIndexSql") int startIndex,
            @Param("numFreeBoardSql") int numFreeBoard,
            @Param("findKeyword") String findKeyword
    );
    //tbl_free_board에서 글제목이나 글내용에 findkeyword가 포함된 글 갯수
    @Select("SELECT COUNT(bno) FROM tbl_free_board WHERE title LIKE concat('%',#{findKeyword},'%') or content LIKE concat('%',#{findKeyword},'%')")
    int freeBoardCountFindByTitleOrContent(@Param("findKeyword") String findKeyword);

//자게 검색 구현 끝

    //글번호로 해당 글 댓글 조회
    @Select("SELECT * FROM tbl_free_board_reply WHERE bno =#{bno} ORDER BY rno asc")
    List<Reply> findReplyByBno(@Param("bno") int bno);



    //tbl_free_board에 행 입력(성공시1 반환 인듯)
    @Insert("insert into tbl_free_board(title, content, writer)\n" +
            "  values('${title}', '${content}', '${writer}')")
    int save(@Param("title") String title,
             @Param("content") String content,
             @Param("writer") String writer);


    @Insert("INSERT INTO tbl_free_board_reply(bno,writer,content)\n" +
            "VALUES (#{bno},#{writer},#{content})")
    int save_free_board_reply(
            @Param("bno") int bno,
            @Param("writer") String writer,
            @Param("content") String content
    );




    @Update("UPDATE tbl_free_board\n" +
            "SET\n" +
            "title = #{titleSql}, content=#{contentSql}, writer=#{writerSql}\n" +
            "WHERE\n" +
            "bno=${bnoSql}")
    boolean freeBoardUpdate(@Param("titleSql") String title,
                                 @Param("contentSql") String content,
                                 @Param("writerSql") String writer,
                                 @Param("bnoSql") int bno);



    @Update("UPDATE tbl_free_board_reply SET\n" +
            "writer = #{writer},\n" +
            "content = #{content}\n" +
            "WHERE rno=#{rno} AND bno=#{bno}")
    boolean freeBoardReplyUpdate(
            @Param("writer") String writer,
            @Param("content") String content,
            @Param("rno") int rno,
            @Param("bno") int bno
    );


    //tbl_free_board 에서 글번호가 bno인 글 삭제, 성공시true
    @Delete("DELETE FROM tbl_free_board WHERE bno=#{bnoSql}")
    boolean deleteByBno(@Param("bnoSql")int bno);

    @Delete("delete from tbl_free_board_reply\n" +
            "WHERE rno=#{rno} AND bno=#{bno}")
    boolean deleteReply(@Param("rno") int rno, @Param("bno") int bno);
}
