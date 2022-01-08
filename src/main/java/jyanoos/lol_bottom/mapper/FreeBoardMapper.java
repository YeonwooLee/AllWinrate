package jyanoos.lol_bottom.mapper;


import jyanoos.lol_bottom.domain.FreeBoard;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    //최신글 번호 조회
    @Select("SELECT MAX(bno) FROM tbl_free_board")
    int getMaxBno();

    //글번호로 글조회
    @Select("SELECT * FROM tbl_free_board where bno=#{bno}")
    FreeBoard findByBno(@Param("bno") int bno);







    //tbl_free_board에 행 입력(성공시1 반환 인듯)
    @Insert("insert into tbl_free_board(title, content, writer)\n" +
            "  values('${title}', '${content}', '${writer}')")
    int save(@Param("title") String title,
             @Param("content") String content,
             @Param("writer") String writer);



}
