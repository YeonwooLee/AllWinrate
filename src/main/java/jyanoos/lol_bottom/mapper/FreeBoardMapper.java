package jyanoos.lol_bottom.mapper;


import jyanoos.lol_bottom.domain.FreeBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FreeBoardMapper {


    @Select("select * from tbl_free_board")
    List<FreeBoard> freeBoardList();

    @Select("SELECT EXISTS " +
            "(SELECT 1 FROM Information_schema.tables " +
            "WHERE table_schema = '${DBName}' AND table_name = '${tableName}') " +
            "AS flag")
    int tblExist(@Param("DBName") String DBName, @Param("tableName") String tableName);


}
