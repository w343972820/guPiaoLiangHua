package vicoMain;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import utils.GuPiaoContent;
import utils.JdbcUtils;

import java.util.ArrayList;


public class Sql_Config_Log_Web_Time  extends AbstractVerticle {
    //获取某股最新行情
    //http://hq.sinajs.cn/list=sh601006

    InternalLogger logger = Log4JLoggerFactory.getInstance(Sql_Config_Log_Web_Time.class);
    JDBCClient jdbcClient;
    VicoDao vicoDao;
    int onlineIndex=0;
    @Override
    public void start() throws Exception {
        ReadHtml readHtml = new ReadHtml();
        JdbcUtils jdbcUtils = new JdbcUtils();

        jdbcUtils.setDbClient(vertx,hands->{
            if (hands.succeeded()){
                jdbcClient = jdbcUtils.getDbClient();
                vicoDao = new VicoDao(jdbcClient);
                logger.info("jdbcClient2:"+jdbcClient);
                //从聚合数据拿股票代码
                //this.toGetGuPiaoInfo(readHtml);
                //从数据库找股票代码，找到股票代码读新浪股票分红页面，获取分红数据写入分红数据库表
                //this.findGuPiaoCode(readHtml);
                //给分红表加上价格及股票名，及年化，年化 = paixi / jiage / 10
                this.getGuPiaoCurryJiaAndMing(readHtml);

            }
        });

    }

    public void findGuPiaoCode(ReadHtml readHtml){
        vicoDao.qureyCode(hand->{
            if (hand.succeeded()){
                ArrayList<GuPiaoContent> contentList = hand.result();
                onlineIndex = 0;
                diTuiList(contentList,readHtml);
            }else{
                logger.info("findGuPiaoCode error");
            }
        });
    }
    public void getGuPiaoCurryJiaAndMing(ReadHtml readHtml){
        vicoDao.qureyFenHongCode(hand->{
            if (hand.succeeded()){
                ArrayList<String> codes = hand.result();
                onlineIndex = 0;
                diTuiStringList(codes,readHtml);


            }else{
                logger.info("findFenHongCode error");
            }
        });



    }

    public void diTuiStringList(ArrayList<String> codes,ReadHtml readHtml){
        if (onlineIndex >= codes.size()){
            logger.info("end...");
            return;
        }
        this.vertx.setTimer(3000,readWeb->{
            String code=codes.get(onlineIndex);
            onlineIndex++;
            logger.info("code:"+code);
            GuPiaoContent content = readHtml.getSinLanGuPiaoInfo(code);
            //直接上数据库存吧
            vicoDao.updateFenHong(content);
            diTuiStringList(codes,readHtml);
        });

    }

    public void diTuiList(ArrayList<GuPiaoContent> contentList,ReadHtml readHtml){
        if (onlineIndex >= contentList.size()){
            logger.info("end...");
            return;
        }
        this.vertx.setTimer(2000,readWeb->{
            String code=contentList.get(onlineIndex).getCode();
            onlineIndex++;
            logger.info("code:"+code);
            toSetFenHongShuJu(readHtml,code.substring(2));
            diTuiList(contentList,readHtml);
        });





    }

    public void toGetGuPiaoInfo(ReadHtml readHtml){
        ArrayList<GuPiaoContent> goPiaoInfo = readHtml.getGuPiaoShuJuToSql();
        logger.info("长度："+goPiaoInfo.size());
        vicoDao.insertGuPiaoInfoToSql(goPiaoInfo);
    }

    public void toSetFenHongShuJu(ReadHtml readHtml,String code){
        try{
            //去读取数据
            logger.info("拿到数据库连接了...");
            //String code = "000651";
            ArrayList<String> resultsList = readHtml.getFenHong(code);
            //先查询数所库有没这个值
            vicoDao.goPiaofenHongInsertKu(code,resultsList);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        //调用vertx
        Vertx.vertx().deployVerticle(new Sql_Config_Log_Web_Time());
    }


}
