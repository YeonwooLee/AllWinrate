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
        isTblExist("lol_data","lol_red");
        return freeBoardList;
    }


    //tblExist("lol_data","lol_red");
    public int isTblExist(String dBName, String tableName) {
        int exist = freeBoardMapper.tblExist(dBName,tableName);
        log.info("{} is {}",tableName, exist);
        return exist;
    }
}
