package vicoMain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.web.RoutingContext;
import utils.GuPiaoContent;
import utils.HttpUtils;
import utils.JdbcUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VicoDao {
    InternalLogger logger = Log4JLoggerFactory.getInstance(VicoDao.class);
    JDBCClient jdbcClient;
    public VicoDao(JDBCClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }
    public void chuLiWeb(RoutingContext req){
        //对客户端传入的参数做json处理
        JsonObject resultsJson = JdbcUtils.param(req);
        //判断服务器传过来的json
        String putuuid = resultsJson.getString("putuuid");
        //可以先对putuuid判断，在响应相应的值
        sqlQury(putuuid).setHandler(h->{
            if (h.succeeded()){
                System.out.println("查看数据库成功。。。");
                if (h.result()){
                    req.response()
                            .putHeader("content-type", "application/json")
                            .end(new JsonObject().put("code","2000").put("msg","找到相同:"+putuuid).toString());
                }else{
                    req.response()
                            .putHeader("content-type", "application/json")
                            .end(new JsonObject().put("code","2000").put("msg","末在数据库内找到相同UUID").toString());
                }
            }else{
                System.out.println("数据库查询出错");
                req.response()
                        .putHeader("content-type", "application/json")
                        .end(new JsonObject().put("code","1000").put("msg","数据库异常"+h.cause()).toString());
            }
        });
    }
    //用future返回回调，以保证请求及处理同步问题
    public Future<Boolean> sqlQury(String code){
        Future<Boolean> future = Future.future();
        String sqlUid = "select code from GuPiaoFenHong where code="+code;
        //String sql = "insert into tradeWithDraw(putuuid,cointypename,putnum,apitype,putaddress,status,createTime) values (?,?,?,?,?,0,?)";
        //String updatesqls = "update tradeWithDraw set status = "+status+",msg=? where putuuid = \'" +txID +"\'";
        //JsonArray params = new JsonArray().add(msg);
        jdbcClient.query(sqlUid,qurSql->{
            if (qurSql.succeeded()){
                logger.info("已查到数据库了");
                // 获取到查询的结果，Vert.x对ResultSet进行了封装
                ResultSet resultSet = qurSql.result();
                // 把ResultSet转为List<JsonObject>形式
                List<JsonObject> resultsList = resultSet.getRows();
                if (resultsList.size()>0){
                    //如果有数所据，就修改
                    logger.info("去修改数据库");

                    future.complete(true);
                }else{
                    logger.info("去插入数据库..");
                    future.complete(false);
                }
            }else{
                logger.info("没查到数据库..");
                logger.error(qurSql.cause());
                future.failed();
            }
        });
        return future;
    }
    public void goPiaofenHongInsertKu(String code,ArrayList<String> guPiaoList){
        String sqlUid = "select code from GuPiaoFenHong where code="+code;
        jdbcClient.query(sqlUid,qurSql->{
            if (qurSql.succeeded()){
                logger.info("已查到数据库了");
                // 获取到查询的结果，Vert.x对ResultSet进行了封装
                ResultSet resultSet = qurSql.result();
                // 把ResultSet转为List<JsonObject>形式
                List<JsonObject> resultsList = resultSet.getRows();
                if (resultsList.size()>0){
                    //如果有数所据，就修改
                    logger.info("去修改数据库");
                }else{
                    logger.info("去插入数据库..");
                    String newTime = guPiaoList.get(0);
                    String songGu = guPiaoList.get(1);
                    String zhuanZheng = guPiaoList.get(2);
                    String paiXi = guPiaoList.get(3);
                    String jingDu = guPiaoList.get(4);
                    String guQuanDengJiRi = guPiaoList.get(6);
                   /* if (guQuanDengJiRi.equals("--")){
                        guQuanDengJiRi="";
                    }*/
                   logger.info("newTime:"+newTime+" songGu:"+songGu+" paiXi:"+paiXi);
                    String times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    String sql = "insert into GuPiaoFenHong(code,newTime,songGu,zhuanZheng,paiXi,jingDu,guQuanDengJiRi,times) values (?,?,?,?,?,?,?,?)";
                    JsonArray params = new JsonArray().add(code);
                    params.add(newTime);
                    params.add(songGu);
                    params.add(zhuanZheng);
                    params.add(paiXi);
                    params.add(jingDu);
                    params.add(guQuanDengJiRi);
                    params.add(times);
                    jdbcClient.updateWithParams(sql,params,inserSql->{
                        if (inserSql.succeeded()){
                            logger.info("插入数据库成功");
                        }else{
                            logger.info("插入数据库失败");
                        }
                    });
                }
            }else{
                logger.info("没查到数据库..");
            }
        });
    }
    public void insertGuPiaoInfoToSql(ArrayList<GuPiaoContent> contentsGuPiao){
        for(int i=0;i<contentsGuPiao.size();i++){
            GuPiaoContent contentResults = contentsGuPiao.get(i);
            String sql = "insert into ShenGuPiaoInfo(code,name,trade,times) values (?,?,?,?)";
            JsonArray params = new JsonArray().add(contentResults.getCode());
            params.add(contentResults.getName());
            params.add(contentResults.getTrade());
            params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            jdbcClient.updateWithParams(sql,params,inserSql->{
                if (inserSql.succeeded()){
                    logger.info("插入数据库成功");
                }else{
                    logger.info("插入数据库失败");
                }
            });
        }
    }

    //查到股票代码后，回调过去给调用的人用
    public void qureyCode(Handler<AsyncResult<ArrayList<GuPiaoContent>>> handler){
        Future<ArrayList<GuPiaoContent>> future = Future.future();
        future.setHandler(handler);
        String sqlUid = "select code,name,trade from HuGuPiaoInfo";
        ArrayList<GuPiaoContent> contentList=new ArrayList<>();
        jdbcClient.query(sqlUid,qurSql->{
            if (qurSql.succeeded()){
                ResultSet resultSet = qurSql.result();
                // 把ResultSet转为List<JsonObject>形式
                List<JsonObject> resultsList = resultSet.getRows();
                for(JsonObject json:resultsList){
                    String code = json.getString("code");
                    String name = json.getString("name");
                    String trade = json.getString("trade");
                    GuPiaoContent content = new GuPiaoContent(name,code,trade);
                    contentList.add(content);
                }
                future.complete(contentList);
            }else{
                future.failed();
                logger.info("HuGuPiaoInfo表查询出错。。");
            }
        });
    }
    //查分红表的股票代码，拿代码去网站找价格及名字，在算出年化存入数据库
    public void qureyFenHongCode(Handler<AsyncResult<ArrayList<String>>> handler){
        Future<ArrayList<String>> future = Future.future();
        future.setHandler(handler);
        String sqlUid = "select code from GuPiaoFenHong";
        ArrayList<String> contentList=new ArrayList<>();
        jdbcClient.query(sqlUid,qurSql->{
            if (qurSql.succeeded()){
                ResultSet resultSet = qurSql.result();
                List<JsonObject> resultsList = resultSet.getRows();
                for(JsonObject json:resultsList){
                    String code = json.getString("code");
                    contentList.add(code);
                }
                future.complete(contentList);

            }else{
                future.failed();
                logger.info("GuPiaoFenHong表查询出错。。");
            }
        });
    }
    public void updateFenHong(GuPiaoContent content){

        //先查出paixi计算出年化在存数据库
        String paixiSql="select paiXi from GuPiaoFenHong where code=?";
        JsonArray par = new JsonArray().add(content.getCode());
        jdbcClient.queryWithParams(paixiSql,par,qurSql->{
            if (qurSql.succeeded()){
                ResultSet resultSet = qurSql.result();
                List<JsonObject> resultsList = resultSet.getRows();
                String paixiShu = resultsList.get(0).getString("paiXi");
                logger.info("派息："+paixiShu);
                double nianHua = Double.parseDouble(paixiShu) / Double.parseDouble(content.getTrade()) / 10;

                DecimalFormat df = new DecimalFormat("0.000");
                String str = df.format(nianHua);
                logger.info("年化："+str);
                String sqlUid = "update GuPiaoFenHong set name =?,newJiaGe=?,nianHua=? where code = ?";
                JsonArray params = new JsonArray().add(content.getName());
                params.add(content.getTrade());
                params.add(str);
                params.add(content.getCode());
                jdbcClient.updateWithParams(sqlUid,params,updateSql->{
                    if (updateSql.succeeded()){
                        logger.info("修改成功");
                    }else{
                        logger.info("修改失败。。");
                    }
                });
            }else{
                logger.info("查派息出问题了。。");
            }


        });




    }
}
