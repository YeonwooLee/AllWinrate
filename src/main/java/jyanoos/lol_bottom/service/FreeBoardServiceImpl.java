package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.controller.FreeBoardController;
import jyanoos.lol_bottom.domain.FreeBoard;
import jyanoos.lol_bottom.mapper.FreeBoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FreeBoardServiceImpl implements FreeBoardService {
    private final FreeBoardMapper freeBoardMapper;

    public FreeBoardServiceImpl(FreeBoardMapper freeBoardMapper) {
        this.freeBoardMapper = freeBoardMapper;
    }

    @Override
    public List<FreeBoard> freeBoardList() {
        List<FreeBoard> freeBoardList = freeBoardMapper.freeBoardList();
        //isTblExist("lol_data","lol_red"); <<테이블 유무 체크 테스트용으로 한줄 넣어봄
        return freeBoardList;
    }

    //자유게시판 글 저장, 성공시1, 실패시0 리턴
    @Override
    public int writeFreeBoard(FreeBoard freeBoard) {
        int success = freeBoardMapper.save(freeBoard.getTitle(),freeBoard.getContent(),freeBoard.getWriter()); //저장성공시 1, 실패시0 리턴
        if(success==1){
            return freeBoardMapper.getMaxBno(); //저장 성공시 게시글 번호 리턴
        }
        return 0; //저장실패시 0리턴
    }

    //글번호로 글찾기
    @Override
    public FreeBoard findByBno(int bno) {
        FreeBoard freeBoardByBno = freeBoardMapper.findByBno(bno);
        return freeBoardByBno;

    }


    //tblExist("lol_data","lol_red");
    public int isTblExist(String dBName, String tableName) {
        int exist = freeBoardMapper.tblExist(dBName,tableName);
        log.info("{} is {}",tableName, exist);
        return exist;
    }

}
