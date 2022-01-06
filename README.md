# BottomOfLegend - AllWinrate(총전적 관련)



## CLASS 설명



#### AllWinrateController.class

> 컨트롤러
>
> ``` java
> @RequestMapping("/awrmain") //awr main page 
> ```





#### AllWinrate.class

> 조합별 총전적 vo 
>
> ``` java
> String bot_combi; //봇 조합
> String adc; 
> String sup;
> 
> //영문 원딜서폿은 캐릭터 그림 가져올 때 필요
> String engAdc;
> String engSup;
> int whole;
> int win;
> int lose;
> float win_rate;
> ```
>
> #splitCombi
>
> ``` java
> //조합을 원딜과 서폿으로 분류하는 메소드
> //AllWinrate.class 생성자에서 호출하여 사용
> 
> //input: {adc}_{sup}_ver_sion ex)이즈리얼_소나_11_24
> //return: void, AllWinrate 인스턴스에 adc, sup 배정
> 
> public void splitCombi(String bot_combi){
>     String[] combiList = bot_combi.split("_");
>     adc=combiList[0];
>     sup=combiList[1];
> }
> ```









#### LolSetting.interface

> 역할1. 사용할 게임 버전, 티어 등 설정 변수를 지정
>
> 역할2. 각종 롤 관련 자료 ex)한글챔프명 -> 영문챔프명
>
> LolSettingImpl.class에서 구현
>
> ```java
> String convertKoToEng(String korChamp) throws IOException; //한글챔프명 -> 영문챔프명
> Map<String,String> koToEngMap() throws IOException; // Map<한글챔프명,영문챔프명>
> ```





#### AllWinrateMapper.interface

> sql mapper
>
> 
>
> #allWinrateList()
>
> ​	조합별 총전적표 
>
> ​	input: x
> ​	return: List<AllWinrate>
>
> ```java
> @Select("select * from all_winrate_"+version)
> List<AllWinrate> allWinrateList();
> ```





#### AllWinrateService.interface

> allWinrate 페이지 service
>
> AllWinrateServiceImpl 에서 구현
>
> 
>
> #minpansoo: 등장 횟수 minpansoo 미만의 조합은 거름
>
> #lenList:  승률 상위 lenList개의 AllWinrate만 List에 넣어 리턴함
>
> ```java
> //db에서 조합별 총승률 리스트 가져옴
> List<AllWinrate> mkAllWinrateList(int minPansoo, int lenList) throws IOException;
> ```

# allwinrate
