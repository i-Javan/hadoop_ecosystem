package com.task;

import com.alibaba.fastjson.JSONObject;
import com.dao.EntityDao;
import com.entity.AirQualityFacts;
import com.entity.ExchangeRate;
import com.entity.Gold;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 汇率获取定时器
 */
@Slf4j
@Component
public class LogTask {
    @Value("${zx.apikey.130.211}")
    private String exchangeratekey;
    @Value("${zx.apikey.130.210}")
    private String stockkey;
    @Value("${zx.apikey.130.222}")
    private String hlkey2;
    @Value("${zx.apikey.158.223}")
    private String hlkey1;
    @Value("${zx.apikey.158.224}")
    private String gpldhlkey;
    @Value("${envicloud.apikey}")
    private String envicloudKey;

    @Autowired
    private HttpClient httpClient;
    @Autowired
    private EntityDao entityDao;

    @Autowired
    private GoldTask goldTask;

    // 环境数据的map
    Map enviroMap = new HashMap();

    Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHH").create();

    SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd HH");

    SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");

    //private static final Logger logger = LoggerFactory.getLogger(LogTask.class);

    private int count = 0;

    @Scheduled(fixedRate = 600000) //十分钟执行一次
    //@Scheduled(cron = "0 0 10,14,16 * * ?") //每天 十点，下午两点，下午四点执行
    //0 0 12 ? * WED // 表示每个星期三中午12点
    //0 0 12 * * ? // 每天中午12点触发
    private void process() throws ParseException {
        //MultiValueMap m = new LinkedMultiValueMap<String,Object>();
        //System.err.println(httpClient.client("https://story.hhui.top/detail?id=666106231640", HttpMethod.GET,m));
        //m.add("a","asdf");
        //System.err.println(httpClient.client("http://172.16.92.182:8088/postMan", HttpMethod.POST,m));

        //结果为“0”是上午 结果为“1”是下午
        GregorianCalendar ca = new GregorianCalendar();
        String key = GregorianCalendar.AM_PM == 0 ? hlkey1 : hlkey2;
        System.err.println("key:" + key);
        Map map = httpClient.get("http://zhouxunwang.cn/data/?id=54&key=" + key + "&ske=1");
        List<Map<String, Map<String, String>>> result = (List<Map<String, Map<String, String>>>) map.get("result");
        Map<String, Map<String, String>> datasouce = result.get(0);

        for (String dateKey : datasouce.keySet()) {
            Map<String, String> data = datasouce.get(dateKey);
            ExchangeRate er = new ExchangeRate();
            er.setBankConversionPri(data.get("bankConversionPri"));
            er.setDate(formatterDate.parse(data.get("date")));
            er.setTime(formatterTime.parse(data.get("time")));
            er.setFBuyPri(data.get("fBuyPri"));
            er.setFSellPri(data.get("fSellPri"));
            er.setMBuyPri(data.get("mBuyPri"));
            er.setMSellPri(data.get("mSellPri"));
            er.setName(data.get("name"));
            log.info("插入信息：" + er.toString());
            entityDao.save(er);
        }
        log.info("定时器调用次数：" + (++count));
    }


    /**
     * 黄金获取
     */
    @Scheduled(fixedRate = 3600000) //一小时执行一次
    private void goldTaskProcess() {
        try {
            Map<String, String> pm = new HashMap<>();
            pm.put("id", "68");
            pm.put("key", gpldhlkey);
            pm.put("fid", "1");
            goldTask.getRequest(Gold.class, pm);
            log.error("黄金获取定时器成功！");
        } catch (Exception e) {
            log.error("黄金获取定时器出错：" + e);
        }
    }
    /**
     *  汇率获取
     */
//    @Scheduled(fixedRate = 180000) //三分钟执行一次
//    private void exchangeRateTaskProcess(){
//        try {
//            Map<String,String> pm = new HashMap<>();
//            pm.put("id","54");
//            pm.put("key",GregorianCalendar.AM_PM == 0 ? hlkey1 : hlkey2);
//            pm.put("ske","1");
//            goldTask.getRequest(ExchangeRate.class,pm);
//            logger.error("汇率获取定时器成功！");
//        }catch (Exception e){
//            logger.error("汇率获取定时器出错："+e);
//        }
//    }
    /*
     * @Description 城市空气质量实况
     * @Author LiangZF
     * @param
     * @Date 2019/6/6 14:32
     * @return void
     */
    @Scheduled(fixedRate = 3600000) //一小时执行一次
    private void airQualityFacts(){
        long time1=System.currentTimeMillis();
        // 遍历城市数据
        StringBuffer sql = new StringBuffer("SELECT areaid FROM t_city");
        List<Object> params = new ArrayList<>();
        List<Map<String, Object>> scrollDataMap = entityDao.getScrollDataMap(sql.toString(), params.toArray());
        for(Map<String, Object> areaMap : scrollDataMap){
            Integer areaid =(Integer)areaMap.get("areaid");
            if(!StringUtils.isEmpty(areaid)){
                String urls = "http://service.envicloud.cn:8082/v2/cityairlive/"+envicloudKey+"/"+areaid;
                JSONObject jsonObject = com.common.HttpClientUtils.httpGet(urls);
                if(jsonObject == null){
                    continue;
                }
                enviroMap = jsonObject;
                String jsonObj = gson.toJson(enviroMap);
                AirQualityFacts t = gson.fromJson(jsonObj, AirQualityFacts.class);
                if("200".equals(t.getRcode())){
                    entityDao.save(t);
                    log.info("插入数据："+t.toString());
                    count++;
                }
            }else{
                log.info("城市id为空!");
            }
        }
        long time2=System.currentTimeMillis();
        log.info("城市数据插入完毕！"+sDateFormat.format(new  java.util.Date()) +"本时段一共插入"+count+"条数据,用时："+(time2-time1)+"ms");
    }
}
