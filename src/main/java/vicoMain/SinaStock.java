package vicoMain;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.Vertx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SinaStock {
    InternalLogger logger = Log4JLoggerFactory.getInstance(SinaStock.class);
    List<String> codes = new ArrayList<String>();
    ArrayList<String> mysqlCodes = new ArrayList<>();

    // 获取新浪78也的所有股票代码
    public void getAllStackCodes(int index, int size, Vertx vertx, VicoDao vicoDao) {
        if (index > size) {
            for (String aa : codes) {
                if (aa.startsWith("sh") || aa.startsWith("sz")) {
                    mysqlCodes.add(aa);
                }
            }
            //存入数据库
            logger.info("获取数据成功，存入数据库,codes长度：" + mysqlCodes.size());
            vicoDao.insertFenHong(mysqlCodes);
               return;
        }
        vertx.setTimer(2000, hand -> {
            try {
                int c = index;
                c++;
                URL url = null;
                url = new URL("http://vip.stock.finance.sina.com.cn/q/go.php/vIR_CustomSearch/index.phtml?p=" + index + "&sr_p=-1");
                String code = null;
                code = getBatchStackCodes(url);
                System.out.println("code=" + index);
                codes.addAll(handleStockCode(code));
                getAllStackCodes(c, size, vertx, vicoDao);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // 解析一组股票代码字符串 把code中包括的所有股票代码放入List中
    public List<String> handleStockCode(String code) {
        List<String> codes = null;
        int end = code.indexOf(";");
        code = code.substring(0, end);
        int start = code.lastIndexOf("=");
        code = code.substring(start);
        code = code.substring(code.indexOf("\"") + 1, code.lastIndexOf("\""));
        codes = Arrays.asList(code.split(","));
        return codes;
    }

    // 返回的值是一个js代码段 包括指定url页面包含的所有股票代码
    public String getBatchStackCodes(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(30000);
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = null;
        StringBuffer sb = new StringBuffer();
        boolean flag = false;
        while ((line = br.readLine()) != null) {
            if (line.contains("<script type=\"text/javascript\">") || flag) {
                sb.append(line);
                flag = true;
            }
            if (line.contains("</script>")) {
                flag = false;
                if (sb.length() > 0) {
                    if (sb.toString().contains("code_list") && sb.toString().contains("element_list")) {
                        break;
                    } else {
                        sb.setLength(0);
                    }
                }
            }
        }
        if (br != null) {
            br.close();
            br = null;
        }
        return sb.toString();
    }

}
