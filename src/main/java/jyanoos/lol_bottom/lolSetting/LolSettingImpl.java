package jyanoos.lol_bottom.lolSetting;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class LolSettingImpl implements LolSetting{
    @Override
    public String convertKoToEng(String korChamp) throws IOException {
        Map<String,String> koToEngMap = koToEngMap();
        return koToEngMap.get(korChamp);

    }

    @Override
    public Map<String, String> koToEngMap() throws IOException {
        InputStream getLocalJsonFile = new FileInputStream("C:\\Users\\yeonw\\inflearn\\myproject\\lol_bottom\\src\\main\\java\\jyanoos\\lol_bottom\\lolSetting\\KoToEng"+version+".json");

        HashMap<String,Object> jsonMap = new ObjectMapper().readValue(getLocalJsonFile, HashMap.class);
        HashMap<String, String> koToEngMap = new HashMap<>();
        jsonMap.keySet().iterator().
                forEachRemaining(key->koToEngMap.put(key,(String)jsonMap.get(key)));
        return koToEngMap;
    }
}
