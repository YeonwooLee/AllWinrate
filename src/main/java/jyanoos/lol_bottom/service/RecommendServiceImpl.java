package jyanoos.lol_bottom.service;


import jyanoos.lol_bottom.domain.recommend.*;
import jyanoos.lol_bottom.mapper.RecommendMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RecommendServiceImpl implements RecommendService{
    private final RecommendMapper recommendMapper;

    public RecommendServiceImpl(RecommendMapper recommendMapper) {
        this.recommendMapper = recommendMapper;
    }


    @Override
    //원딜추천 - 아군서폿만 앎
    public List<RecommendAdcKnowMsup> recommendAdcKnowMsup(RecommendRequest recommendRequest) {

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
            String mAdc = combi.split("_")[0];
            recommendAdcKnowMsup.setMAdc(mAdc);
            resultList.add(recommendAdcKnowMsup);
        }

        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public List<RecommendAdcKnowMsupEadc> recommendAdcKnowMsupEadc(RecommendRequest recommendRequest) {
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
            recommendAdcKnowMsupEadc.setMAdc(mAdc);
            recommendAdcKnowMsupEadc.setMSup(mSup);
            resultList.add(recommendAdcKnowMsupEadc);
        }
        Collections.sort(resultList);
        log.info("resultList:{}",resultList.size());
        return resultList;
    }

    @Override
    public List<RecommendAdcKnow> recommendAdcKnow(RecommendRequest recommendRequest) {
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
            resultList.add(recommendAdcKnow);
        }
        Collections.sort(resultList);
        log.info("resultList:{}",resultList.size());
        return resultList;
    }

    @Override
    public List<RecommendAdcKnowMsupEsup> recommendAdcKnowMsupEsup(RecommendRequest recommendRequest) {
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
            recommendAdcKnowMsupEsup.setMSup(mSup);
            recommendAdcKnowMsupEsup.setESup(eSup);
            resultList.add(recommendAdcKnowMsupEsup);
        }
        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public List<RecommendAdcKnowMsupEadcEsup> recommendAdcKnowMsupEadcEsup(RecommendRequest recommendRequest) {
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
            recommendAdcKnowMsupEadcEsup.setMSup(mSup);
            recommendAdcKnowMsupEadcEsup.setESup(eSup);
            recommendAdcKnowMsupEadcEsup.setEAdc(eAdc);
            resultList.add(recommendAdcKnowMsupEadcEsup);
        }
        Collections.sort(resultList);
        return resultList;

    }

    @Override
    public List<RecommendAdcKnowEadc> recommendAdcKnowEadc(RecommendRequest recommendRequest) {
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
            recommendAdcKnowEadc.setEAdc(eAdc);
            resultList.add(recommendAdcKnowEadc);
        }
        Collections.sort(resultList);
        return resultList;

    }

    @Override
    public List<RecommendAdcKnowEsup> recommendAdcKnowEsup(RecommendRequest recommendRequest) {
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
            recommendAdcKnowEsup.setESup(eSup);
            resultList.add(recommendAdcKnowEsup);
        }
        Collections.sort(resultList);
        return resultList;

    }

    @Override
    public List<RecommendAdcKnowEadcEsup> recommendAdcKnowEadcEsup(RecommendRequest recommendRequest) {
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
            recommendAdcKnowEadcEsup.setESup(eSup);
            recommendAdcKnowEadcEsup.setEAdc(eAdc);
            resultList.add(recommendAdcKnowEadcEsup);
        }
        Collections.sort(resultList);
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
