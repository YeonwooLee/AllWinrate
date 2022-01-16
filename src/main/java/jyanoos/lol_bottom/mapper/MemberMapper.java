package jyanoos.lol_bottom.mapper;

import jyanoos.lol_bottom.domain.member.Member;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberMapper {
    //-- 회원 create
    @Insert("INSERT INTO member(userEmail,userPassword,userNickname) \n" +
            "VALUES(#{userEmail},#{userPassword},#{userNickname})")
    int insertMember(@Param("userEmail") String userEmail,
                     @Param("userPassword") String userPassword,
                     @Param("userNickname") String userNickname);


    //-- 이메일로 유저 read
    @Select("SELECT * FROM member WHERE userEmail=#{userEmail}")
    Member findMemberByEmail(@Param("userEmail") String userEmail);
    //-- 닉네임으로 유저 read
    @Select("SELECT * FROM member WHERE userNickname=#{userNickname}")
    Member findMemberByNickname(@Param("userNickname") String userNickname);

    //이메일로 유저유무 확인
    @Select("SELECT count(*) FROM member WHERE userEmail=#{userEmail}")
    int existMemberByEmail(@Param("userEmail") String userEmail);
    //-- 닉네임으로 유저유무 확인
    @Select("SELECT count(*) FROM member WHERE userNickname=#{userNickname}")
    int existMemberByNickname(@Param("userNickname") String userNickname);



    //-- 닉네임으로 닉네임 update
    @Update("UPDATE member SET userNickname=#{newNickname} WHERE userNickname=#{userNickname}")
    int updateMemberByNickname(@Param("newNickname") String newNickname,@Param("userNickname") String userNickname);
    //-- email로 닉네임 update
    @Update("UPDATE member SET userNickname=#{newNickname} WHERE userEmail=#{userEmail}")
    int updateMemberByEmail(@Param("newNickname") String newNickname,@Param("userEmail") String userEmail);









    //-- 닉네임으로 유저 delete
    @Delete("DELETE FROM member WHERE userNickname=#{userNickname}")
    int deleteMemberByNickname(@Param("userNickname") String userNickname);
    //-- 이메일로 유저 delete
    @Delete("DELETE FROM member WHERE userEmail=#{userEmail}")
    int deleteMemberByEmail(@Param("userEmail") String userEmail);
}
