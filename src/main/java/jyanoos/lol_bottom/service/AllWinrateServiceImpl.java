package jyanoos.lol_bottom.service;

import jyanoos.lol_bottom.domain.AllWinrate;
import jyanoos.lol_bottom.domain.CombiReply;
import jyanoos.lol_bottom.domain.CombiReplyBoard;
import jyanoos.lol_bottom.domain.CombiSecReply;
import jyanoos.lol_bottom.lolSetting.LolSetting;
import jyanoos.lol_bottom.mapper.AllWinrateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllWinrateServiceImpl implements AllWinrateService {
    private final AllWinrateMapper allWinrateMapper;
    private final LolSetting lolSetting;

    @Override
    public List<AllWinrate> mkAllWinrateList(int minPansoo,int lenList) throws IOException {
        //조합별 총승률 리스트 가져옴
        List<AllWinrate> allWinrateList = allWinrateMapper.allWinrateList();

        //판수 minPansoo 이하인 애들 제거
        for(Iterator<AllWinrate> iterator = allWinrateList.iterator(); iterator.hasNext();){
            AllWinrate awr = iterator.next();
            if(awr.getWhole()<minPansoo){
                iterator.remove();
            }
            awr.setEngAdc(lolSetting.convertKoToEng(awr.getAdc()));
            awr.setEngSup(lolSetting.convertKoToEng(awr.getSup()));

        }

        //정렬(1기준 승률, 2기준 판수)
        Collections.sort(allWinrateList);
        
        //상위n개만 리턴
        allWinrateList = allWinrateList.subList(0,lenList);
        return allWinrateList;
    }

    @Override
    public CombiReplyBoard mkViewCombiBoard(String adc, String sup, int lastReplyIndex) throws IOException { //영어이름으로 가져옴
        //한글챔프명 변수
        String adcKo = lolSetting.convertEngToKo(adc);
        String supKo = lolSetting.convertEngToKo(sup);

        //allwinrate 생성 및 영어이름 저장
        AllWinrate allWinrate = allWinrateMapper.getAllwinrate(adcKo,supKo);
        allWinrate.setEngAdc(adc);
        allWinrate.setEngSup(sup);

        CombiReplyBoard combiReplyBoard = new CombiReplyBoard();//리턴용 객체
        String manager = "관리자";
        String mkBoardMessage = allWinrate.getAdc()+" & "+allWinrate.getSup()+" 조합 테이블이 생성되었습니다 :)";

        //adc_sup 테이블 있나 확인하고
        int exsistInt = allWinrateMapper.tblExist("lol_data",allWinrate.getEngAdc()+"_"+allWinrate.getEngSup());
        log.info("테이블있나확인: {}",allWinrate.getEngAdc()+"_"+allWinrate.getEngSup());
        //없으면
        if(exsistInt==0) {
            log.info("{}&{}테이블 없어서 생성중",adc,sup);
            allWinrateMapper.createAwrTbl(allWinrate.getEngAdc(),allWinrate.getEngSup()); //만들고
            allWinrateMapper.writeAwlReply(allWinrate.getEngAdc(),allWinrate.getEngSup(),manager,mkBoardMessage); // 환영인사 남김
        }
        //대댓글용 db도 있나 확인후 없으면 생성함
        int existSecTbl = allWinrateMapper.tblExist("lol_data",allWinrate.getEngAdc()+"_"+allWinrate.getEngSup()+"_secreply");
        if(existSecTbl==0){
            allWinrateMapper.createAwrSecTbl(allWinrate.getEngAdc(),allWinrate.getEngSup()); //대댓db도 생성
        }




        //해당 조합 게시판 댓글들
        List<CombiReply> replyList = allWinrateMapper.combiReplyList(allWinrate.getEngAdc(), allWinrate.getEngSup());

        //댓,대댓,댓,대댓 순으로 만들기 시작
        List<CombiReply> replyAndSecReply = new ArrayList<>(); //댓대댓댓대댓대댓댓 순서로 만들어질 리스트
        for(CombiReply reply: replyList){
            reply.setIndent(0); //기본댓글이므로 얘네 인덴트는0임!
            replyAndSecReply.add(reply); //기본댓글을 결과리스트에 넣음<같은맥락1>

            //기본댓글rno을 참조하는 secReply 목록록
            List<CombiSecReply> listSecReply = allWinrateMapper.getListSecReplyByRno(adc,sup,reply.getRno());
            for(CombiSecReply secReply:listSecReply){
                //CombiSecReply를 CombiReply타입으로 변환
                CombiReply combiReply = new CombiReply();//이 새 객체가 secReply의 새 틀이 됨
                combiReply.setIndent(1);//대댓이므로 인덴트는 1칸 들어간 1임
                combiReply.setContent(secReply.getContent());
                combiReply.setRegDate(secReply.getRegDate());
                combiReply.setRno(secReply.getRno());
                combiReply.setWriter(secReply.getWriter());
                combiReply.setSecRnoBeforeConvert(secReply.getSecRno());

                replyAndSecReply.add(combiReply);//대댓글을 형변환하여 결과 리스트에 넣음<같은맥락1>
            }
        }
        replyList=replyAndSecReply; //기존 리턴값이었던 replyList를 MVC 찾아다니면서 바꿀 수 없으니 그냥 대체하겠음!
        if(lastReplyIndex>replyList.size()) lastReplyIndex=replyList.size();
        replyList=replyList.subList(0,lastReplyIndex);


        combiReplyBoard.setAllWinrate(allWinrate);
        combiReplyBoard.setReplyList(replyList);
        combiReplyBoard.setLastReplyIndex(lastReplyIndex);

        return combiReplyBoard;
    }

    @Override
    public void writeReply(String adc, String sup, String writer, String content) {
        int i = allWinrateMapper.writeAwlReply(adc,sup,writer,content);
        log.info("AllWinrateServiceImpl.writeReply return>>>{}",i);
    }

    @Override
    public int updateReply(String adcEng, String supEng, String writer, String content, int rno) {
        int i = allWinrateMapper.updateAwrTbl(adcEng, supEng, writer, content, rno);
        log.info("AllWinrateServiceImpl.updateReply return>>>{}",i);
        return i;

    }

    @Override
    public int deleteReply(String adcEng, String supEng, int rno) {
        int i = allWinrateMapper.deleteAwrTbl(adcEng, supEng, rno);
        log.info("AllWinrateServiceImpl.deleteReply return>>>{}",i);
        return i;
    }

    @Override
    public int writeSecReply(String adc, String sup, int rno, String writer, String content) {
        int i = allWinrateMapper.insertSecReply(adc, sup, rno, writer, content);
        log.info("writeSecReply success={}",i);
        return i;
    }

    @Override
    public int updateSecReply(String adcEng, String supEng, String writer, String content, int secRno, int rno) {
        int i = allWinrateMapper.updateSecReply(adcEng, supEng, writer, content, secRno, rno);
        return i;
    }

    @Override
    public int deleteSecReply(String adcEng, String supEng, int secRno, int rno) {
        int i = allWinrateMapper.deleteSecReply(adcEng, supEng, secRno, rno);
        return i;
    }


}
