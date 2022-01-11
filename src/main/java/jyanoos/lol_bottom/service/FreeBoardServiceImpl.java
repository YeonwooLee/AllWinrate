package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.FreeBoard;
import jyanoos.lol_bottom.domain.Paging;
import jyanoos.lol_bottom.domain.Reply;
import jyanoos.lol_bottom.mapper.FreeBoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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


    //글번호로 제목, 내용, 작성자 수정, 성공시 수정된 글 번호, 실패시 0 리턴
    @Override
    public int updateFreeBoard(int bno, String title, String content, String writer) {
        //번호가 n번인 글의 제목 저자 내용을 title, content, writer로 수정
        boolean updateBno = freeBoardMapper.freeBoardUpdate(title,content,writer,bno);
        if(updateBno) return bno; //업데이트 성공
        return 0;
        //
    }

    @Override
    public boolean deleteByBno(int bno) {
        boolean deleteSuccess = freeBoardMapper.deleteByBno(bno);
        return deleteSuccess;

    }

    @Override
    public Paging freeBoardListPage(int num,int nowPage) {
        //List<Object> result = new ArrayList<Object>(); //object타입으로 받은 후 controller에서 int[]와 List<FreeBoard>로 캐스팅하여 사용 -->버전2로 변경

        //1. 게시글 전체 갯수 조회
        int freeBoardAllCount = freeBoardMapper.getFreeBoardNum();

        //2. 페이지당 num개 출력시 몇 페이지 필요한가 확인 {총글수/페이지당글수의 몫}
        int needPage = freeBoardAllCount/num;

//        //3. int[] pageList = 1~pageCount -->하단 페이지 번호 보여주기위해 모델에 추가
//        int[] pageList = new int[needPage];
//        for(int i=0;i<pageList.length;i++){
//            pageList[i]=i+1;
//        }
//        //3-1. 현재 페이지 페이지리스트, 10개씩 끊어보여줌
//        int[] pageList= new int[10];
//        for(int i=0;i<10;i++){
//            int index=(nowPage/10)*10+i+1;
//            if(index<=needPage+1){
//                pageList[i]=index;
//            }else{
//                pageList[i]=-1;
//            }
//        }
        //3-2. 현재 페이지 페이징리스트, 10개씩 끊어보여줌
        List<Integer> pageList = new ArrayList<>();
        int lastIndex = 0;
        for(int i=0;i<10;i++){
            int index=(nowPage/10)*10+i+1;
            if(index<=needPage+1){
                pageList.add(index);
            }else{
                lastIndex = index-1;
            }
        }


        //4.현재페이지 글목록
        int startIndex = num*nowPage; //페이지당 첫 글 인덱스
        List<FreeBoard> nowPageList = freeBoardMapper.freeBoardListPage(startIndex, num);


//        //result.add(pageList); // result의 첫 객체 = 페이지리스트
//        result.add(pageList); //result의 첫번째 객체 = 페이징리스트(10개씩 끊어서 보여줌)
//        result.add(freeBoardNowPageList); // result의 두번째 객체 = 현재 페이지 게시글 리스트
//        result.add(needPage+1); //result의 세번째 객체 총 필요한 페이지 수(페이징에 <이전 다음> 처리용)
//        result.add(lastIndex); //result의 네번쨰 객체 페이징인덱스 마지막(페이징에 <이전 다음> 처리용)
        Paging paging = new Paging(pageList,nowPageList,needPage+1,lastIndex); //버전2 List<Object> -> Paging class
        return paging;
    }

    @Override
    public Paging freeBoardFindListPage(int num, int nowPage, String searchType, String keyword) {
        Paging paging = null;

        if(searchType.equals("searchByTitle")){
            paging = pagingByCol(num, nowPage,keyword,"title");
        }else if(searchType.equals("searchByContent")){
            paging = pagingByCol(num, nowPage,keyword,"content");
        }else if(searchType.equals("searchByWriter")){
            paging = pagingByCol(num, nowPage,keyword,"writer");
        }else if(searchType.equals("searchByTitleOrContent")){
            paging = pagingByTitleOrContent(num, nowPage,keyword);
        }
        return paging;
    }

    @Override
    public List<Reply> findReplyByBno(int bno) {
        List<Reply> replyByBno = freeBoardMapper.findReplyByBno(bno);
        return replyByBno;
    }

    @Override
    public int writeFreeBoardReply(Reply reply) {
        int success = freeBoardMapper.save_free_board_reply(reply.getBno(), reply.getWriter(), reply.getContent());
        return success;

    }

    @Override
    public int updateReplyFreeBoard(Reply reply) {
        boolean success = freeBoardMapper.freeBoardReplyUpdate(reply.getWriter(), reply.getContent(), reply.getRno(), reply.getBno());
        return reply.getBno();
    }

    @Override
    public boolean deleteReply(int bno, int rno) {
        boolean success = freeBoardMapper.deleteReply(rno, bno);
        return success;
    }

    public Paging pagingByCol(int num, int nowPage, String findKeyword, String findCol) {
        //List<Object> result = new ArrayList<Object>(); //object타입으로 받은 후 controller에서 int[]와 List<FreeBoard>로 캐스팅하여 사용 -->버전2로 변경

        //1. 게시글 전체 갯수 조회
        int freeBoardAllCount = freeBoardMapper.freeBoardCountFindByCol(findKeyword,findCol);

        //2. 페이지당 num개 출력시 몇 페이지 필요한가 확인 {총글수/페이지당글수의 몫}
        int needPage = freeBoardAllCount/num;


        //3-2. 현재 페이지 페이징리스트, 10개씩 끊어보여줌
        List<Integer> pageList = new ArrayList<>();
        int lastIndex = 0;
        for(int i=0;i<10;i++){
            int index=(nowPage/10)*10+i+1;
            if(index<=needPage+1){
                pageList.add(index);
            }else{
                lastIndex = index-1;
            }
        }
        //4.현재페이지 글목록
        int startIndex = num*nowPage; //페이지당 첫 글 인덱스
        List<FreeBoard> nowPageList = freeBoardMapper.freeBoardFindByCol(startIndex, num, findKeyword,findCol);


        Paging paging = new Paging(pageList,nowPageList,needPage+1,lastIndex); //버전2 List<Object> -> Paging class
        return paging;
    }

    public Paging pagingByTitleOrContent(int num, int nowPage, String findKeyword) {
        //List<Object> result = new ArrayList<Object>(); //object타입으로 받은 후 controller에서 int[]와 List<FreeBoard>로 캐스팅하여 사용 -->버전2로 변경

        //1. 게시글 전체 갯수 조회
        int freeBoardAllCount = freeBoardMapper.freeBoardCountFindByTitleOrContent(findKeyword);

        //2. 페이지당 num개 출력시 몇 페이지 필요한가 확인 {총글수/페이지당글수의 몫}
        int needPage = freeBoardAllCount/num;


        //3-2. 현재 페이지 페이징리스트, 10개씩 끊어보여줌
        List<Integer> pageList = new ArrayList<>();
        int lastIndex = 0;
        for(int i=0;i<10;i++){
            int index=(nowPage/10)*10+i+1;
            if(index<=needPage+1){
                pageList.add(index);
            }else{
                lastIndex = index-1;
            }
        }
        //4.현재페이지 글목록
        int startIndex = num*nowPage; //페이지당 첫 글 인덱스
        List<FreeBoard> nowPageList = freeBoardMapper.freeBoardFindByTitleOrContent(startIndex, num, findKeyword);


        Paging paging = new Paging(pageList,nowPageList,needPage+1,lastIndex); //버전2 List<Object> -> Paging class
        return paging;
    }



    //tblExist("lol_data","lol_red");
    @Override
    public int isTblExist(String dBName, String tableName) {
        int exist = freeBoardMapper.tblExist(dBName,tableName);
        log.info("{} is {}",tableName, exist);
        return exist;
    }

}
