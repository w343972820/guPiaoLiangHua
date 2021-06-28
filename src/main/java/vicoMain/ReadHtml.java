package vicoMain;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;

import utils.GuPiaoContent;
import utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadHtml {
    InternalLogger logger = Log4JLoggerFactory.getInstance(ReadHtml.class);

    public ArrayList<String>  getFenHong(String code) throws IOException {
        String results = HttpUtils.doGet("http://vip.stock.finance.sina.com.cn/corp/go.php/vISSUE_ShareBonus/stockid/" + code + ".phtml");
        //去解析吧
        int startString = results.indexOf("sharebonus_1");
        int endString = results.indexOf("target=\"_blank\"",startString);
        String okString = results.substring(startString,endString);
        startString = okString.indexOf("<tbody>");
        endString = okString.indexOf("<td><a",startString);
        okString = okString.substring(startString,endString);
        Pattern p =Pattern.compile("\\<td>(.*)\\</td>");
        Matcher m =p.matcher(okString);
        ArrayList<String> matches= new ArrayList();
        while(m.find())
        {
            matches.add(m.group(1));

        }
        for(String ss:matches){
            logger.info(ss);
        }
        return matches;
    }
    public ArrayList<GuPiaoContent> getGuPiaoShuJuToSql(){
        String uri="http://web.juhe.cn:8080/finance/stock/shall?stock=&page=4&type=20&key=6b4d4bcc186813da2fd5988a1b6839a2";
        ArrayList<GuPiaoContent> contentsGuPiao= new ArrayList<>();
        try{
            for(int i=15;i<=124;i++){
                String results = HttpUtils.doGet2("http://web.juhe.cn:8080/finance/stock/szall?stock=&page="+i+"&type=20&key=6b4d4bcc186813da2fd5988a1b6839a2");
                JSONObject json = JSONObject.parseObject(results);
                JSONObject resultJson = json.getJSONObject("result");
                JSONArray dataArray = resultJson.getJSONArray("data");
                for(int j=0;j<dataArray.size();j++){
                    String stringArray = dataArray.get(j).toString();
                    JSONObject endsJson= JSONObject.parseObject(stringArray);
                    String codes = endsJson.getString("symbol");
                    String name = endsJson.getString("name");
                    String trade = endsJson.getString("trade");
                    contentsGuPiao.add(new GuPiaoContent(name,codes,trade));
                }
            }
        }catch (Exception e){
            throw e;
        }
        return contentsGuPiao;
    }

    public GuPiaoContent getSinLanGuPiaoInfo(String code){
        String uri="http://hq.sinajs.cn/list=sh"+code;
        GuPiaoContent content = new GuPiaoContent();
        try{
            String results = HttpUtils.doGet(uri);
            String[] fengeshu = results.split("\"");
            String[] douhaofenge = fengeshu[1].split(",");
            content.setCode(code);
            content.setName(douhaofenge[0]);
            content.setTrade(douhaofenge[3]);
            logger.info(results);
        }catch (Exception e){
            throw e;
        }
        return content;
    }




}
