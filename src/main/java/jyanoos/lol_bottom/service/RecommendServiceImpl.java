package jyanoos.lol_bottom.service;


import jyanoos.lol_bottom.domain.recommend.*;
import jyanoos.lol_bottom.lolSetting.LolSetting;
import jyanoos.lol_bottom.mapper.RecommendMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RecommendServiceImpl implements RecommendService{
    private final RecommendMapper recommendMapper;
    private final LolSetting lolSetting;

    public RecommendServiceImpl(RecommendMapper recommendMapper, LolSetting lolSetting) {
        this.recommendMapper = recommendMapper;
        this.lolSetting = lolSetting;
    }


    @Override
    //원딜추천 - 아군서폿만 앎
    public List<RecommendAdcKnowMsup> recommendAdcKnowMsup(RecommendRequest recommendRequest) throws IOException {

        //아군 서폿이 포함된 게임 리스트 생성(mapper에서 버전 관리됨)
        String mSup = recommendRequest.getMSup();
        List<Game> games = recommendMapper.supVs(mSup);

        //조합별 전적 임시 저장용 map
        ConcurrentHashMap<String,List<Integer>> temp = new ConcurrentHashMap<>(); //<조합명,[총등장,승수]
        List<RecommendAdcKnowMsup> resultList = new ArrayList<>();

        for(Game game:games){
            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String bCombi =bAdc+"_"+bSup;
            String rCombi =rAdc+"_"+rSup;
            String winTeam;
            if(game.getBwin()==1){
                winTeam="blue";
            }else{
                winTeam="red";
            }

            if(winTeam.equals("blue")){//블루승
                setMap(mSup, temp, bSup, rCombi, bCombi);
            }else{//레드승
                setMap(mSup, temp, rSup, bCombi, rCombi);
            }
        }
        for(String combi : temp.keySet()){
            int whole = temp.get(combi).get(0);
            int win = temp.get(combi).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            //log.info("whole{} win{} winrate{}",whole,win,(float) (Math.round(win/whole*10000)/100.0));
            RecommendAdcKnowMsup recommendAdcKnowMsup = new RecommendAdcKnowMsup(whole,win,winrate);
            recommendAdcKnowMsup.setMSup(mSup);
            recommendAdcKnowMsup.setMSupE(lolSetting.convertKoToEng(mSup));

            String mAdc = combi.split("_")[0];
            recommendAdcKnowMsup.setMAdc(mAdc);
            recommendAdcKnowMsup.setMAdcE(lolSetting.convertKoToEng(mAdc));
            resultList.add(recommendAdcKnowMsup);
        }

        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public List<RecommendAdcKnowMsupEadc> recommendAdcKnowMsupEadc(RecommendRequest recommendRequest) throws IOException {
        log.info("recommendAdcKnowMsupEadc 시작");
        String mSup = recommendRequest.getMSup();
        String eAdc = recommendRequest.getEAdc();
        List<Game> gameList = recommendMapper.adcVsSup(eAdc, mSup); //아는정보(msup,eadc)로 게임 리스트 생성
        log.info("gamelist{}",gameList);
        //<조합명:[총전적,승수]> 쌍의 map --> 조합별 전적 기록해서 차후에 recommendAdcKnowMsupEadc 객체 생성에 사용
        ConcurrentHashMap<String,List<Integer>> temp = new ConcurrentHashMap<>();
        //위 map 내용을 객체화하여 여기에 저장
        List<RecommendAdcKnowMsupEadc> resultList = new ArrayList<>();

        //우리팀, 상대팀 포지션별 유닛 배정 및 전적 반영
        for(Game game:gameList){
            String mAdc;
            String eSup;
            //String mSup << 이미 존재
            //String eEad << 이미 존재
            String winTeam;
            String myTeam;

            //아군상대 조합 구분
            if(game.getBsup().equals(mSup)){
                myTeam="blue";
                mAdc=game.getBadc();
                eSup=game.getRsup();
            }else{
                myTeam="red";
                mAdc=game.getRadc();
                eSup=game.getBsup();
            }
            //승패팀 구분
            if(game.getBwin()==1){
                winTeam="blue";
            }else{
                winTeam="red";
            }

            //우리팀 이겼으면
            if(myTeam.equals(winTeam)){
                if (temp.containsKey(mAdc)){
                    List<Integer> integers = temp.get(mAdc);
                    integers.set(0, integers.get(0)+1);//등장수+1
                    integers.set(1, integers.get(1)+1);//승수+1
                    temp.replace(mAdc,integers);
                }else{
                    List<Integer> integers = new ArrayList<>();
                    integers.add(1); //총등장1
                    integers.add(1);//승수1
                    temp.put(mAdc,integers);
                }
            }//우리팀 졌으면
            else{
                if (temp.containsKey(mAdc)){
                    List<Integer> integers = temp.get(mAdc);
                    integers.set(0, integers.get(0)+1);//등장수+1
                    temp.replace(mAdc,integers);
                }else{
                    List<Integer> integers = new ArrayList<>();
                    integers.add(1); //총등장1
                    integers.add(0);//승수0
                    temp.put(mAdc,integers);
                }
            }




        }
        for(String key:temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            String mAdc = key;
            RecommendAdcKnowMsupEadc recommendAdcKnowMsupEadc = new RecommendAdcKnowMsupEadc(whole,win,winrate);
            recommendAdcKnowMsupEadc.setEAdc(eAdc);
            recommendAdcKnowMsupEadc.setEAdcE(lolSetting.convertKoToEng(eAdc));

            recommendAdcKnowMsupEadc.setMAdc(mAdc);
            recommendAdcKnowMsupEadc.setMAdcE(lolSetting.convertKoToEng(mAdc));

            recommendAdcKnowMsupEadc.setMSup(mSup);
            recommendAdcKnowMsupEadc.setMSupE(lolSetting.convertKoToEng(mSup));
            resultList.add(recommendAdcKnowMsupEadc);
        }
        Collections.sort(resultList);
        log.info("resultList:{}",resultList.size());
        return resultList;
    }

    @Override
    public List<RecommendAdcKnow> recommendAdcKnow(RecommendRequest recommendRequest) throws IOException {
        log.info("recommendAdcKnow 시작");
        List<Game> gameList = recommendMapper.vs(); //아는정보없음
        //log.info("gamelist{}",gameList);
        //<조합명:[총전적,승수]> 쌍의 map --> 조합별 전적 기록해서 차후에 RecommendAdcKnow 객체 생성에 사용
        ConcurrentHashMap<String,List<Integer>> temp = new ConcurrentHashMap<>();
        //위 map 내용을 객체화하여 여기에 저장
        List<RecommendAdcKnow> resultList = new ArrayList<>();

        //우리팀, 상대팀 포지션별 유닛 배정 및 전적 반영
        for(Game game:gameList){
            String bAdc = game.getBadc();
            String rAdc = game.getRadc();
            String winTeam;
            if (game.getBwin()==1){
                winTeam="blue";
            }else{
                winTeam="red";
            }
            if(winTeam.equals("blue")){
                setKnowNothing(temp, rAdc, bAdc);
            }else{
                setKnowNothing(temp, bAdc, rAdc);
            }
        }
        for(String key:temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            String mAdc = key;
            RecommendAdcKnow recommendAdcKnow = new RecommendAdcKnow(whole,win,winrate);
            recommendAdcKnow.setMAdc(mAdc);
            recommendAdcKnow.setMAdcE(lolSetting.convertKoToEng(mAdc));

            resultList.add(recommendAdcKnow);
        }
        Collections.sort(resultList);
        log.info("resultList:{}",resultList.size());
        return resultList;
    }

    @Override
    public List<RecommendAdcKnowMsupEsup> recommendAdcKnowMsupEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendAdcKnowMsupEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String mSup = recommendRequest.getMSup();
        String eSup = recommendRequest.getESup();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.supVsSup(mSup,eSup);

        //map구성
        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mAdc;
            String myTeam = bSup.equals(mSup) ? "blue":"red"; //받아온 msup이랑 bsup이랑 일치시 우리팀 블루팀, 아니면 레드팀
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mAdc)){//adc가 맵에 있으면
                List<Integer> integers = temp.get(mAdc);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mAdc,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mAdc,integers);
            }
        }
        for (String key: temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendAdcKnowMsupEsup recommendAdcKnowMsupEsup = new RecommendAdcKnowMsupEsup(whole,win,winrate);
            recommendAdcKnowMsupEsup.setMAdc(key);
            recommendAdcKnowMsupEsup.setMAdcE(lolSetting.convertKoToEng(key));
            recommendAdcKnowMsupEsup.setMSup(mSup);
            recommendAdcKnowMsupEsup.setMSupE(lolSetting.convertKoToEng(mSup));
            recommendAdcKnowMsupEsup.setESup(eSup);
            recommendAdcKnowMsupEsup.setESupE(lolSetting.convertKoToEng(eSup));
            resultList.add(recommendAdcKnowMsupEsup);
        }
        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public List<RecommendAdcKnowMsupEadcEsup> recommendAdcKnowMsupEadcEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendAdcKnowMsupEadcEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String mSup = recommendRequest.getMSup();
        String eSup = recommendRequest.getESup();
        String eAdc = recommendRequest.getEAdc();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcSupVsSup(eAdc,eSup,mSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mAdc;
            String myTeam = bSup.equals(mSup) ? "blue":"red"; //받아온 msup이랑 bsup이랑 일치시 우리팀 블루팀, 아니면 레드팀
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mAdc)){//adc가 맵에 있으면
                List<Integer> integers = temp.get(mAdc);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mAdc,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mAdc,integers);
            }
        }
        for (String key: temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendAdcKnowMsupEadcEsup recommendAdcKnowMsupEadcEsup = new RecommendAdcKnowMsupEadcEsup(whole,win,winrate);
            recommendAdcKnowMsupEadcEsup.setMAdc(key);
            recommendAdcKnowMsupEadcEsup.setMAdcE(lolSetting.convertKoToEng(key));
            recommendAdcKnowMsupEadcEsup.setMSup(mSup);
            recommendAdcKnowMsupEadcEsup.setMSupE(lolSetting.convertKoToEng(mSup));
            recommendAdcKnowMsupEadcEsup.setESup(eSup);
            recommendAdcKnowMsupEadcEsup.setESupE(lolSetting.convertKoToEng(eSup));
            recommendAdcKnowMsupEadcEsup.setEAdc(eAdc);
            recommendAdcKnowMsupEadcEsup.setEAdcE(lolSetting.convertKoToEng(eAdc));
            resultList.add(recommendAdcKnowMsupEadcEsup);
        }
        Collections.sort(resultList);
        return resultList;

    }

    @Override
    public List<RecommendAdcKnowEadc> recommendAdcKnowEadc(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendAdcKnowEadc> resultList = new ArrayList<>();

        //아는 정보 기입
        String eAdc = recommendRequest.getEAdc();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcVs(eAdc);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mAdc;
            String myTeam = !bAdc.equals(eAdc) ? "blue":"red"; //내팀구분
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mAdc)){//madc가 맵에 있으면
                List<Integer> integers = temp.get(mAdc);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mAdc,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mAdc,integers);
            }
        }
        for (String key: temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendAdcKnowEadc recommendAdcKnowEadc = new RecommendAdcKnowEadc(whole,win,winrate);
            recommendAdcKnowEadc.setMAdc(key);
            recommendAdcKnowEadc.setMAdcE(lolSetting.convertKoToEng(key));
            recommendAdcKnowEadc.setEAdc(eAdc);
            recommendAdcKnowEadc.setEAdcE(lolSetting.convertKoToEng(eAdc));
            resultList.add(recommendAdcKnowEadc);
        }
        Collections.sort(resultList);
        return resultList;

    }

    @Override
    public List<RecommendAdcKnowEsup> recommendAdcKnowEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendAdcKnowEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String eSup = recommendRequest.getESup();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.supVs(eSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mAdc;
            String myTeam = !bSup.equals(eSup) ? "blue":"red"; //내팀구분
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;//내원딜확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mAdc)){//madc가 맵에 있으면
                List<Integer> integers = temp.get(mAdc);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mAdc,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mAdc,integers);
            }
        }
        for (String key: temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendAdcKnowEsup recommendAdcKnowEsup = new RecommendAdcKnowEsup(whole,win,winrate);
            recommendAdcKnowEsup.setMAdc(key);
            recommendAdcKnowEsup.setMAdcE(lolSetting.convertKoToEng(key));
            recommendAdcKnowEsup.setESup(eSup);
            recommendAdcKnowEsup.setESupE(lolSetting.convertKoToEng(eSup));
            resultList.add(recommendAdcKnowEsup);
        }
        Collections.sort(resultList);
        return resultList;

    }

    @Override
    public List<RecommendAdcKnowEadcEsup> recommendAdcKnowEadcEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendAdcKnowEadcEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String eSup = recommendRequest.getESup();
        String eAdc = recommendRequest.getEAdc();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcSupVs(eAdc,eSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mAdc;
            String myTeam = !bSup.equals(eSup) ? "blue":"red"; //내팀구분
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;//내원딜확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mAdc)){//madc가 맵에 있으면
                List<Integer> integers = temp.get(mAdc);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mAdc,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mAdc,integers);
            }
        }
        for (String key: temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendAdcKnowEadcEsup recommendAdcKnowEadcEsup = new RecommendAdcKnowEadcEsup(whole,win,winrate);
            recommendAdcKnowEadcEsup.setMAdc(key);
            recommendAdcKnowEadcEsup.setMAdcE(lolSetting.convertKoToEng(key));
            recommendAdcKnowEadcEsup.setESup(eSup);
            recommendAdcKnowEadcEsup.setESupE(lolSetting.convertKoToEng(eSup));
            recommendAdcKnowEadcEsup.setEAdc(eAdc);
            recommendAdcKnowEadcEsup.setEAdcE(lolSetting.convertKoToEng(eAdc));
            resultList.add(recommendAdcKnowEadcEsup);
        }
        Collections.sort(resultList);
        return resultList;

    }

    @Override
    public List<RecommendSupKnow> recommendSupKnow(RecommendRequest recommendRequest) throws IOException {
        log.info("RecommendSupKnow 시작");
        List<Game> gameList = recommendMapper.vs(); //아는정보없음
        //log.info("gamelist{}",gameList);
        //<조합명:[총전적,승수]> 쌍의 map --> 조합별 전적 기록해서 차후에 RecommendAdcKnow 객체 생성에 사용
        ConcurrentHashMap<String,List<Integer>> temp = new ConcurrentHashMap<>();
        //위 map 내용을 객체화하여 여기에 저장
        List<RecommendSupKnow> resultList = new ArrayList<>();

        //우리팀, 상대팀 포지션별 유닛 배정 및 전적 반영
        for(Game game:gameList){
            String bSup = game.getBsup();
            String rSup = game.getRsup();
            String winTeam;
            if (game.getBwin()==1){
                winTeam="blue";
            }else{
                winTeam="red";
            }
            if(winTeam.equals("blue")){
                setKnowNothing(temp, rSup, bSup);
            }else{
                setKnowNothing(temp, bSup, rSup);
            }
        }
        for(String key:temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            String mSup = key;//dd
            RecommendSupKnow recommendSupKnow = new RecommendSupKnow(whole,win,winrate);//dd
            recommendSupKnow.setMSup(mSup);//dd
            recommendSupKnow.setMSupE(lolSetting.convertKoToEng(mSup));//dd
            resultList.add(recommendSupKnow);//dd
        }
        Collections.sort(resultList);
        log.info("resultList:{}",resultList.size());
        return resultList;
    }

    @Override
    public List<RecommendSupKnowMadc> recommendSupKnowMadc(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendSupKnowMadc> resultList = new ArrayList<>();

        //아는 정보 기입
        String mAdc = recommendRequest.getMAdc();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcVs(mAdc);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String myTeam = bAdc.equals(mAdc) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mSup)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mSup);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mSup,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mSup,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendSupKnowMadc recommendSupKnowMadc = new RecommendSupKnowMadc(whole,win,winrate);
            recommendSupKnowMadc.setMSup(key);
            recommendSupKnowMadc.setMSupE(lolSetting.convertKoToEng(key));
            recommendSupKnowMadc.setMAdc(mAdc);
            recommendSupKnowMadc.setMAdcE(lolSetting.convertKoToEng(mAdc));

            resultList.add(recommendSupKnowMadc);
        }
//        for(RecommendSupKnowMadc re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendSupKnowMadcEadc> recommendSupKnowMadcEadc(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendSupKnowMadcEadc> resultList = new ArrayList<>();

        //아는 정보 기입
        String mAdc = recommendRequest.getMAdc();
        String eAdc = recommendRequest.getEAdc();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcVsAdc(mAdc,eAdc);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String myTeam = bAdc.equals(mAdc) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mSup)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mSup);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mSup,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mSup,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendSupKnowMadcEadc recommendSupKnowMadcEadc = new RecommendSupKnowMadcEadc(whole,win,winrate);
            recommendSupKnowMadcEadc.setMSup(key);
            recommendSupKnowMadcEadc.setMSupE(lolSetting.convertKoToEng(key));
            recommendSupKnowMadcEadc.setMAdc(mAdc);
            recommendSupKnowMadcEadc.setMAdcE(lolSetting.convertKoToEng(mAdc));
            recommendSupKnowMadcEadc.setEAdc(eAdc);
            recommendSupKnowMadcEadc.setEAdcE(lolSetting.convertKoToEng(eAdc));

            resultList.add(recommendSupKnowMadcEadc);
        }
//        for(RecommendSupKnowMadc re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendSupKnowMadcEsup> recommendSupKnowMadcEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendSupKnowMadcEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String mAdc = recommendRequest.getMAdc();
        String eSup = recommendRequest.getESup();

        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcVsSup(mAdc,eSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String myTeam = bAdc.equals(mAdc) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mSup)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mSup);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mSup,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mSup,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendSupKnowMadcEsup recommendSupKnowMadcEsup = new RecommendSupKnowMadcEsup(whole,win,winrate);
            recommendSupKnowMadcEsup.setMSup(key);
            recommendSupKnowMadcEsup.setMSupE(lolSetting.convertKoToEng(key));
            recommendSupKnowMadcEsup.setMAdc(mAdc);
            recommendSupKnowMadcEsup.setMAdcE(lolSetting.convertKoToEng(mAdc));
            recommendSupKnowMadcEsup.setESup(eSup);
            recommendSupKnowMadcEsup.setESupE(lolSetting.convertKoToEng(eSup));


            resultList.add(recommendSupKnowMadcEsup);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendSupKnowMadcEadcEsup> recommendSupKnowMadcEadcEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendSupKnowMadcEadcEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String mAdc = recommendRequest.getMAdc();
        String eAdc = recommendRequest.getEAdc();
        String eSup = recommendRequest.getESup();


        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcSupVsAdc(eAdc,eSup,mAdc);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String myTeam = bAdc.equals(mAdc) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mSup)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mSup);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mSup,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mSup,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendSupKnowMadcEadcEsup recommendSupKnowMadcEadcEsup = new RecommendSupKnowMadcEadcEsup(whole,win,winrate);
            recommendSupKnowMadcEadcEsup.setMSup(key);
            recommendSupKnowMadcEadcEsup.setMSupE(lolSetting.convertKoToEng(key));
            recommendSupKnowMadcEadcEsup.setMAdc(mAdc);
            recommendSupKnowMadcEadcEsup.setMAdcE(lolSetting.convertKoToEng(mAdc));
            recommendSupKnowMadcEadcEsup.setEAdc(eAdc);
            recommendSupKnowMadcEadcEsup.setEAdcE(lolSetting.convertKoToEng(eAdc));
            recommendSupKnowMadcEadcEsup.setESup(eSup);
            recommendSupKnowMadcEadcEsup.setESupE(lolSetting.convertKoToEng(eSup));


            resultList.add(recommendSupKnowMadcEadcEsup);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendSupKnowEadc> recommendSupKnowEadc(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendSupKnowEadc> resultList = new ArrayList<>();

        //아는 정보 기입
        String eAdc = recommendRequest.getEAdc();



        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcVs(eAdc);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String myTeam = !bAdc.equals(eAdc) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mSup)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mSup);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mSup,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mSup,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendSupKnowEadc recommendSupKnowEadc = new RecommendSupKnowEadc(whole,win,winrate);
            recommendSupKnowEadc.setMSup(key);
            recommendSupKnowEadc.setMSupE(lolSetting.convertKoToEng(key));
            recommendSupKnowEadc.setEAdc(eAdc);
            recommendSupKnowEadc.setEAdcE(lolSetting.convertKoToEng(eAdc));



            resultList.add(recommendSupKnowEadc);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendSupKnowEsup> recommendSupKnowEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendSupKnowEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String eSup = recommendRequest.getESup();



        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.supVs(eSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String myTeam = !bSup.equals(eSup) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mSup)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mSup);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mSup,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mSup,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendSupKnowEsup recommendSupKnowEsup = new RecommendSupKnowEsup(whole,win,winrate);
            recommendSupKnowEsup.setMSup(key);
            recommendSupKnowEsup.setMSupE(lolSetting.convertKoToEng(key));
            recommendSupKnowEsup.setESup(eSup);
            recommendSupKnowEsup.setESupE(lolSetting.convertKoToEng(eSup));



            resultList.add(recommendSupKnowEsup);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendSupKnowEadcEsup> recommendSupKnowEadcEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendSupKnowEadcEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String eAdc = recommendRequest.getEAdc();
        String eSup = recommendRequest.getESup();



        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcSupVs(eAdc,eSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String myTeam = !bSup.equals(eSup) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mSup)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mSup);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mSup,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mSup,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendSupKnowEadcEsup recommendSupKnowEadcEsup = new RecommendSupKnowEadcEsup(whole,win,winrate);
            recommendSupKnowEadcEsup.setMSup(key);
            recommendSupKnowEadcEsup.setMSupE(lolSetting.convertKoToEng(key));
            recommendSupKnowEadcEsup.setESup(eSup);
            recommendSupKnowEadcEsup.setESupE(lolSetting.convertKoToEng(eSup));
            recommendSupKnowEadcEsup.setEAdc(eAdc);
            recommendSupKnowEadcEsup.setEAdcE(lolSetting.convertKoToEng(eAdc));

            resultList.add(recommendSupKnowEadcEsup);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendCombiKnow> recommendCombiKnow(RecommendRequest recommendRequest, int minPansoo) throws IOException {
        log.info("RecommendCombiKnow 시작");
        List<Game> gameList = recommendMapper.vs(); //아는정보없음
        //log.info("gamelist{}",gameList);
        //<조합명:[총전적,승수]> 쌍의 map --> 조합별 전적 기록해서 차후에 RecommendAdcKnow 객체 생성에 사용
        ConcurrentHashMap<String,List<Integer>> temp = new ConcurrentHashMap<>();
        //위 map 내용을 객체화하여 여기에 저장
        List<RecommendCombiKnow> resultList = new ArrayList<>();

        //우리팀, 상대팀 포지션별 유닛 배정 및 전적 반영
        for(Game game:gameList){
            String wAdc, wSup, lAdc,lSup,wCombi,lCombi;
            String winTeam = game.getBwin()==1?"blue":"red";

            wAdc = winTeam.equals("blue")?game.getBadc():game.getRadc();
            wSup = winTeam.equals("blue")?game.getBsup():game.getRsup();

            lAdc = winTeam.equals("blue")?game.getRadc():game.getBadc();
            lSup = winTeam.equals("blue")?game.getRsup():game.getBsup();

            wCombi=wAdc+"_"+wSup;
            lCombi=lAdc+"_"+lSup;

            //승팀반영
            if(temp.containsKey(wCombi)){
                List<Integer> integers = temp.get(wCombi);
                integers.set(0,integers.get(0)+1);
                integers.set(1,integers.get(1)+1);
                temp.replace(wCombi,integers);
            }else{
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                integers.add(1);
                temp.put(wCombi,integers);
            }

            //패팀반영
            if(temp.containsKey(lCombi)){
                List<Integer> integers = temp.get(lCombi);
                integers.set(0,integers.get(0)+1);
                temp.replace(lCombi,integers);
            }else{
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                integers.add(0);
                temp.put(lCombi,integers);
            }



        }
        for(String key:temp.keySet()){
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            String mCombi = key;//dd
            String mAdc,mSup;
            mAdc = mCombi.split("_")[0];
            mSup = mCombi.split("_")[1];

            RecommendCombiKnow recommendCombiKnow = new RecommendCombiKnow(whole,win,winrate);//dd
            recommendCombiKnow.setMCombi(mCombi);//dd
            //recommendCombiKnow.setMCombiE(lolSetting.convertKoToEng(mCombi));//dd
            recommendCombiKnow.setMAdc(mAdc);//dd
            recommendCombiKnow.setMAdcE(lolSetting.convertKoToEng(mAdc));//dd
            recommendCombiKnow.setMSup(mSup);//dd
            recommendCombiKnow.setMSupE(lolSetting.convertKoToEng(mSup));//dd

            if(whole>=minPansoo) {
                resultList.add(recommendCombiKnow);//dd
            }
        }
        Collections.sort(resultList);
        log.info("resultList:{}",resultList.size());
        return resultList;
    }

    @Override
    public List<RecommendCombiKnowEadc> recommendCombiKnowEadc(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendCombiKnowEadc> resultList = new ArrayList<>();

        //아는 정보 기입
        String eAdc = recommendRequest.getEAdc();



        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcVs(eAdc);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String mAdc;
            String mCombi;

            String myTeam = !bAdc.equals(eAdc) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;//내서폿확인
            mCombi = mAdc+"_"+mSup;

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mCombi)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mCombi);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mCombi,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mCombi,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendCombiKnowEadc recommendCombiKnowEadc = new RecommendCombiKnowEadc(whole,win,winrate);
            recommendCombiKnowEadc.setMCombi(key);//<<추천받을포지션
            //recommendCombiKnowEadc.setMCombiE(lolSetting.convertKoToEng(key));//<<추천받을포지션
            String mAdc, mSup;
            mAdc=key.split("_")[0];
            mSup=key.split("_")[1];
            
            //추천시고정자료, 가령 상대원딜 알 때 아군 서폿 추천이면 상대원딜은 고정이니까 여기 추가
            recommendCombiKnowEadc.setEAdc(eAdc);
            recommendCombiKnowEadc.setEAdcE(lolSetting.convertKoToEng(eAdc));
            recommendCombiKnowEadc.setMAdc(mAdc);
            recommendCombiKnowEadc.setMAdcE(lolSetting.convertKoToEng(mAdc));
            recommendCombiKnowEadc.setMSup(mSup);
            recommendCombiKnowEadc.setMSupE(lolSetting.convertKoToEng(mSup));

            resultList.add(recommendCombiKnowEadc);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendCombiKnowEadcEsup> recommendCombiKnowEadcEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendCombiKnowEadcEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String eAdc = recommendRequest.getEAdc();
        String eSup = recommendRequest.getESup();



        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.adcSupVs(eAdc,eSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String mAdc;
            String mCombi;

            String myTeam = !bAdc.equals(eAdc) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;//내서폿확인
            mCombi = mAdc+"_"+mSup;

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mCombi)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mCombi);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mCombi,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mCombi,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendCombiKnowEadcEsup recommendCombiKnowEadcEsup = new RecommendCombiKnowEadcEsup(whole,win,winrate);
            recommendCombiKnowEadcEsup.setMCombi(key);//<<추천받을포지션
            //recommendCombiKnowEadcEsup.setMCombiE(lolSetting.convertKoToEng(key));//<<추천받을포지션
            String mAdc, mSup;
            mAdc=key.split("_")[0];
            mSup=key.split("_")[1];
            String eCombi = eAdc+"_"+eSup;

            //추천시고정자료, 가령 상대원딜 알 때 아군 서폿 추천이면 상대원딜은 고정이니까 여기 추가
            recommendCombiKnowEadcEsup.setEAdc(eAdc);
            recommendCombiKnowEadcEsup.setEAdcE(lolSetting.convertKoToEng(eAdc));
            recommendCombiKnowEadcEsup.setMAdc(mAdc);
            recommendCombiKnowEadcEsup.setMAdcE(lolSetting.convertKoToEng(mAdc));
            recommendCombiKnowEadcEsup.setMSup(mSup);
            recommendCombiKnowEadcEsup.setMSupE(lolSetting.convertKoToEng(mSup));
            recommendCombiKnowEadcEsup.setESup(eSup);
            recommendCombiKnowEadcEsup.setESupE(lolSetting.convertKoToEng(eSup));
            recommendCombiKnowEadcEsup.setECombi(eCombi);
            //recommendCombiKnowEadcEsup.setECombiE(lolSetting.convertKoToEng(eCombi));

            resultList.add(recommendCombiKnowEadcEsup);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    @Override
    public List<RecommendCombiKnowEsup> recommendCombiKnowEsup(RecommendRequest recommendRequest) throws IOException {
        //정보 저장할 임시map
        ConcurrentHashMap<String, List<Integer>> temp = new ConcurrentHashMap<>();

        //최종 리턴용 리스트
        List<RecommendCombiKnowEsup> resultList = new ArrayList<>();

        //아는 정보 기입
        String eSup = recommendRequest.getESup();



        //아는 정보로 게임 가져옴
        List<Game> gameList =  recommendMapper.supVs(eSup);

        for(Game game:gameList){

            String bAdc = game.getBadc();
            String bSup = game.getBsup();
            String rAdc = game.getRadc();
            String rSup = game.getRsup();
            String mSup;
            String mAdc;
            String mCombi;

            String myTeam = !bSup.equals(eSup) ? "blue":"red"; //내팀구분
            mSup = myTeam.equals("blue") ? bSup:rSup;//내서폿확인
            mAdc = myTeam.equals("blue") ? bAdc:rAdc;//내서폿확인
            mCombi = mAdc+"_"+mSup;

            String winTeam = game.getBwin()==1 ? "blue":"red"; // 승팀판정
            boolean meWin = winTeam.equals(myTeam)?true:false;

            if(temp.containsKey(mCombi)){//msup가 맵에 있으면
                List<Integer> integers = temp.get(mCombi);
                integers.set(0, integers.get(0)+1); //등장+1
                if(meWin){
                    integers.set(1, integers.get(1)+1); //이겼으면 승수도 +1
                }
                temp.replace(mCombi,integers); //map replace
            }else{//맵에 없으면
                List<Integer> integers = new ArrayList<>();
                integers.add(1);
                if(meWin){
                    integers.add(1);//이겼으면 [1,1]
                }else {
                    integers.add(0); //졌으면[1,0]
                }
                temp.put(mCombi,integers);
            }
        }
        //최종리턴용리스트 구성부분
        for (String key: temp.keySet()){//key는 추천받을 포지션임
            int whole = temp.get(key).get(0);
            int win = temp.get(key).get(1);
            float winrate = (float) (Math.round((float)win/whole*10000)/100.0);
            RecommendCombiKnowEsup recommendCombiKnowEsup = new RecommendCombiKnowEsup(whole,win,winrate);
            recommendCombiKnowEsup.setMCombi(key);//<<추천받을포지션
            //recommendCombiKnowEsup.setMCombiE(lolSetting.convertKoToEng(key));//<<추천받을포지션
            String mAdc, mSup;
            mAdc=key.split("_")[0];
            mSup=key.split("_")[1];

            //추천시고정자료, 가령 상대원딜 알 때 아군 서폿 추천이면 상대원딜은 고정이니까 여기 추가
            recommendCombiKnowEsup.setESup(eSup);
            recommendCombiKnowEsup.setESupE(lolSetting.convertKoToEng(eSup));
            recommendCombiKnowEsup.setMAdc(mAdc);
            recommendCombiKnowEsup.setMAdcE(lolSetting.convertKoToEng(mAdc));
            recommendCombiKnowEsup.setMSup(mSup);
            recommendCombiKnowEsup.setMSupE(lolSetting.convertKoToEng(mSup));

            resultList.add(recommendCombiKnowEsup);
        }
//        for(RecommendSupKnowMadcEsup re:resultList){
//            log.info("{}",re);
//
//        }
        Collections.sort(resultList);
//        log.info("여기까진옴2");
        return resultList;

    }

    private void setKnowNothing(ConcurrentHashMap<String, List<Integer>> temp, String bAdc, String rAdc) {
        if(temp.containsKey(rAdc)){
            List<Integer> integers = temp.get(rAdc);
            integers.set(0,integers.get(0)+1);
            integers.set(1,integers.get(1)+1);
            temp.replace(rAdc,integers);
        }else{
            List<Integer> integers = new ArrayList<>();
            integers.add(1); //총등장1
            integers.add(1);//승수0
            temp.put(rAdc,integers);
        }
        if(temp.containsKey(bAdc)){
            List<Integer> integers = temp.get(bAdc);
            integers.set(0,integers.get(0)+1);
            //integers.set(1,integers.get(1)+1);
            temp.replace(bAdc,integers);
        }else{
            List<Integer> integers = new ArrayList<>();
            integers.add(1); //총등장1
            integers.add(0);//승수0
            temp.put(bAdc,integers);
        }
    }


    private void setMap(String mSup, ConcurrentHashMap<String, List<Integer>> temp, String rSup, String bCombi, String rCombi) {
        if(mSup.equals(rSup)){//아군이 레드팀
            if(temp.containsKey(rCombi)){//맵에 있음
                List<Integer> integers = temp.get(rCombi);//[총등장,승]
                int whole = integers.get(0); //총등장
                int win = integers.get(1);//승
                integers.set(0,whole+1);//총등장+1
                integers.set(1,win+1);//승+1
                temp.replace(rCombi,integers);//맵값변경
            }else{//맵에 없음
                List<Integer> integers = new ArrayList<>();
                integers.add(1); //총등장1
                integers.add(1);//승수1
                temp.put(rCombi,integers); //map에 추가
            }
        }else{ //아군이 블루팀
            if (temp.containsKey(bCombi)){//맵에 있음
                List<Integer> integers = temp.get(bCombi);//[총등장,승]
                int whole = integers.get(0); //총등장
                integers.set(0,whole+1);//총등장+1
                temp.replace(bCombi,integers);//맵값변경
            }else{
                List<Integer> integers = new ArrayList<>();
                integers.add(1); //총등장1
                integers.add(0);//승수0
                temp.put(bCombi,integers); //map에 추가
            }

        }
    }


}
