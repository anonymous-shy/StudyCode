package xyz.shy.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shy on 2018/5/28
 */

public class JacksonTest {

    private ObjectMapper mapper = new ObjectMapper()
            //类级别的设置，JsonInclude.Include.NON_EMPTY标识只有非NULL的值才会被纳入json string之中，其余的都将被忽略
//            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            //禁止使用出现未知属性之时，抛出异常
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            //转化后的json的key命名格式
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    @Test
    public void testJson1() throws IOException {
        Goods goods = new Goods(1001, "AAA", true, LocalDateTime.now().toString(), 11111L);
        String value = mapper.writeValueAsString(goods);
        Map<String, Object> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", true);
        map.put("C", 1001L);
        map.put("D", "a,b,c");
        map.put("E", "");
        map.put("F", null);
        System.out.println(value);
        System.out.println(mapper.writeValueAsString(map));

        JsonNode jsonNode = mapper.readTree(value);
        JsonNode ctime = jsonNode.findValue("ctime");
        System.out.println(ctime.asText());
    }

    @Test
    public void testJson2() throws IOException {
        String jsonStr1 = "{\"img_location\": [{\"img_type\": null, \"img_size\": null, \"img_path\": \"/data/sharedata/XY-TGBUS-1/878712075d99873600d675132f08aaa4.JPEG\", \"checksum\": \"878712075d99873600d675132f08aaa4\", \"img_width\": 670, \"img_desc\": null, \"img_height\": 377, \"img_src\": \"http://ps4.tgbus.bnbsky.com/UploadFiles/201803/2018030111250970.jpg\", \"img_format\": \"JPEG\", \"img_index\": 1}, {\"img_type\": null, \"img_size\": null, \"img_path\": \"/data/sharedata/XY-TGBUS-1/b4a88754082cdbd8d350836208e7c445.JPEG\", \"checksum\": \"b4a88754082cdbd8d350836208e7c445\", \"img_width\": 800, \"img_desc\": null, \"img_height\": 450, \"img_src\": \"http://ps4.tgbus.bnbsky.com/UploadFiles/201803/2018030111252532.jpg\", \"img_format\": \"JPEG\", \"img_index\": 2}, {\"img_type\": null, \"img_size\": null, \"img_path\": \"/data/sharedata/XY-TGBUS-1/3998e9abbbfa115d7a6b9a892a2647e5.JPEG\", \"checksum\": \"3998e9abbbfa115d7a6b9a892a2647e5\", \"img_width\": 640, \"img_desc\": null, \"img_height\": 360, \"img_src\": \"http://ps4.tgbus.bnbsky.com/UploadFiles/201803/2018030111253895.jpg\", \"img_format\": \"JPEG\", \"img_index\": 3}], \"data_source_sub_id\": \"2079\", \"crawlid\": \"28\", \"data_source_unique_id\": \"XY-TGBUS-1#186-76828\", \"status_code\": 200, \"data_source_key\": \"XY-TGBUS\", \"video_location_count\": null, \"repost_count\": null, \"toutiao_refer_url\": null, \"parsed_content_main_body\": \"\\u7d22\\u5c3c\\u5728\\u4eca\\u5929\\u516c\\u5e03\\u4e86\\u6b27\\u7f8e\\u670d\\u548c\\u65e5\\u670d\\u7684PS +\\u4f1a\\u54583\\u6708\\u4efd\\u7684\\u4f1a\\u514d\\u6e38\\u620f\\u540d\\u5355\\uff0c\\u4ee4\\u4eba\\u5174\\u594b\\u7684\\u662f\\uff0cPS4\\u72ec\\u5360\\u5927\\u4f5c\\u300a\\u8840\\u6e90\\u8bc5\\u5492\\u300b\\u5c31\\u5728\\u8fd9\\u6b21\\u7684\\u4f1a\\u514d\\u6e38\\u620f\\u9635\\u5bb9\\u4e4b\\u4e2d\\u3002\\u6b27\\u7f8e\\u670d3\\u6708\\u4f1a\\u514d\\u9635\\u5bb9\\uff1a\\u300a\\u8840\\u6e90\\u8bc5\\u5492\\u300bBloodborne(PS4)\\u300a\\u745e\\u5947\\u4e0e\\u53ee\\u5f53\\u300bRatchet & Clank(PS4)\\u300a\\u51ef\\u4e4b\\u4f20\\u5947:\\u5468\\u5e74\\u7248\\u300bLegend of Kay (PS3)\\u300a\\u514b\\u83b1\\u5c14 \\u52a0\\u957f\\u7248\\u300bClaire: Extended Cut(PS4/PS Vita)\\u300a\\u7206\\u7834\\u6df7\\u86cb\\u300bBombing Busters(PS4/PS Vita)\\u65e5\\u670d3\\u6708\\u4f1a\\u514d\\u9635\\u5bb9\\uff1a\\u300a\\u8840\\u6e90\\u8bc5\\u5492\\u300bBloodborne(PS4)\\u300a\\u602a\\u7269\\u730e\\u4eba\\uff1a\\u4e16\\u754c\\u300bPS4\\u4e3b\\u9898\\u4e0e\\u5934\\u50cf\\u9664\\u4e86\\u516c\\u5e033\\u6708\\u7684\\u4f1a\\u514d\\u6e38\\u620f\\u4e4b\\u5916\\uff0c\\u7d22\\u5c3c\\u8fd8\\u540c\\u65f6\\u5ba3\\u5e03\\uff0c\\u4ece\\u660e\\u5e74\\u76843\\u67088\\u65e5\\u8d77\\uff0c\\u6bcf\\u6708\\u7684PS+\\u4f1a\\u514d\\u6e38\\u620f\\u5c06\\u4e0d\\u518d\\u63d0\\u4f9bPS3\\u548cPSV\\u5e73\\u53f0\\u7684\\u6e38\\u620f\\uff0c\\u6216\\u8bb8\\u8fd9\\u4ee3\\u8868\\u4e86\\u7d22\\u5c3c\\u5c06\\u9010\\u6e10\\u653e\\u5f03PS3\\u548cPSV\\u7684\\u4f1a\\u5458\\u670d\\u52a1\\u652f\\u6301\\uff0c\\u8fd9\\u5bf9\\u4e24\\u4e2a\\u5e73\\u53f0\\u7684\\u7528\\u6237\\u662f\\u4e00\\u4e2a\\u4e0d\\u5c0f\\u7684\\u6253\\u51fb\\u3002\", \"frequency\": null, \"toutiao_category_class\": null, \"toutiao_category_class_id\": null, \"data_source_type\": \"\\u5782\\u76f4\\u7ad9\", \"id\": \"http://61.142.208.146:11011/data.php?ut=1519833600&plat=tgbuswww&source=ps2&nav=slg\", \"cookiejar\": null, \"original_url\": null, \"audio_location_count\": null, \"sub_channel\": \"PS4_\\u8d44\\u8baf\\u4e2d\\u5fc3\", \"parser_info\": \"dmt_scrapy_redis.page_parser.tgbus_parser.TGBusSpider\", \"discovery_time\": \"2018-05-24 20:32:07\", \"small_img_location_count\": 1, \"comment_count\": null, \"authorized\": null, \"batch_id\": \"XY-TGBUS-1#186-76828#0524193726#1\", \"info_source_url\": null, \"data_source_id\": \"XY-TGBUS-1\", \"media\": \"TGBUS\", \"update_time\": 1519874473, \"publish_time\": 1519874473, \"audio_location\": null, \"response_url\": \"http://ps4.tgbus.bnbsky.com/news/201803/20180301112113.shtml\", \"timestamp\": \"2018-05-24 20:48:28\", \"appid\": \"crawl_big_media_redis\", \"post_request\": null, \"parsed_content\": \"<p>\\u7d22\\u5c3c\\u5728\\u4eca\\u5929\\u516c\\u5e03\\u4e86\\u6b27\\u7f8e\\u670d\\u548c\\u65e5\\u670d\\u7684PS +\\u4f1a\\u54583\\u6708\\u4efd\\u7684\\u4f1a\\u514d\\u6e38\\u620f\\u540d\\u5355\\uff0c\\u4ee4\\u4eba\\u5174\\u594b\\u7684\\u662f\\uff0cPS4\\u72ec\\u5360\\u5927\\u4f5c\\u300a\\u8840\\u6e90\\u8bc5\\u5492\\u300b\\u5c31\\u5728\\u8fd9\\u6b21\\u7684\\u4f1a\\u514d\\u6e38\\u620f\\u9635\\u5bb9\\u4e4b\\u4e2d\\u3002</p><p><img src=\\\"/data/sharedata/XY-TGBUS-1/878712075d99873600d675132f08aaa4.JPEG\\\"/></p><p>\\u6b27\\u7f8e\\u670d3\\u6708\\u4f1a\\u514d\\u9635\\u5bb9\\uff1a</p><p>\\u300a\\u8840\\u6e90\\u8bc5\\u5492\\u300bBloodborne(PS4)</p><p>\\u300a\\u745e\\u5947\\u4e0e\\u53ee\\u5f53\\u300bRatchet & Clank(PS4)</p><p><img src=\\\"/data/sharedata/XY-TGBUS-1/b4a88754082cdbd8d350836208e7c445.JPEG\\\"/></p><p>\\u300a\\u51ef\\u4e4b\\u4f20\\u5947:\\u5468\\u5e74\\u7248\\u300bLegend of Kay (PS3)</p><p>\\u300a\\u514b\\u83b1\\u5c14 \\u52a0\\u957f\\u7248\\u300bClaire: Extended Cut(PS4/PS Vita)</p><p>\\u300a\\u7206\\u7834\\u6df7\\u86cb\\u300bBombing Busters(PS4/PS Vita)</p><p>\\u65e5\\u670d3\\u6708\\u4f1a\\u514d\\u9635\\u5bb9\\uff1a</p><p>\\u300a\\u8840\\u6e90\\u8bc5\\u5492\\u300bBloodborne(PS4)</p><p>\\u300a\\u602a\\u7269\\u730e\\u4eba\\uff1a\\u4e16\\u754c\\u300bPS4\\u4e3b\\u9898\\u4e0e\\u5934\\u50cf</p><p><img src=\\\"/data/sharedata/XY-TGBUS-1/3998e9abbbfa115d7a6b9a892a2647e5.JPEG\\\"/></p><p>\\u9664\\u4e86\\u516c\\u5e033\\u6708\\u7684\\u4f1a\\u514d\\u6e38\\u620f\\u4e4b\\u5916\\uff0c\\u7d22\\u5c3c\\u8fd8\\u540c\\u65f6\\u5ba3\\u5e03\\uff0c\\u4ece\\u660e\\u5e74\\u76843\\u67088\\u65e5\\u8d77\\uff0c\\u6bcf\\u6708\\u7684PS+\\u4f1a\\u514d\\u6e38\\u620f\\u5c06\\u4e0d\\u518d\\u63d0\\u4f9bPS3\\u548cPSV\\u5e73\\u53f0\\u7684\\u6e38\\u620f\\uff0c\\u6216\\u8bb8\\u8fd9\\u4ee3\\u8868\\u4e86\\u7d22\\u5c3c\\u5c06\\u9010\\u6e10\\u653e\\u5f03PS3\\u548cPSV\\u7684\\u4f1a\\u5458\\u670d\\u52a1\\u652f\\u6301\\uff0c\\u8fd9\\u5bf9\\u4e24\\u4e2a\\u5e73\\u53f0\\u7684\\u7528\\u6237\\u662f\\u4e00\\u4e2a\\u4e0d\\u5c0f\\u7684\\u6253\\u51fb\\u3002</p>\", \"toutiao_out_url\": null, \"para_info\": null, \"parsed_content_char_count\": 382, \"tags\": null, \"desc\": \"PS+\\u6b27\\u7f8e\\u65e5\\u670d3\\u6708\\u4f1a\\u514d\\u516c\\u5e03&nbsp;\\u300a\\u8840\\u6e90\\u300b\\u91cd\\u78c5\\u52a0\\u5165\", \"url_domain\": \"61.142.208.146:11011\", \"article_genre\": \"article\", \"parse_function\": \"parse_detail_page\", \"click_count\": null, \"url\": \"http://61.142.208.146:11011/data.php?ut=1519833600&plat=tgbuswww&source=ps2&nav=slg\", \"author\": \"\\u7ae0\\u9c7c\\u4e0a\\u6821\", \"saved_data_location\": \"test#game_lib#tgbus_article\", \"video_location\": null, \"channel_id\": null, \"task_priority\": 2000, \"like_count\": null, \"title\": \"PS+\\u6b27\\u7f8e\\u65e5\\u670d3\\u6708\\u4f1a\\u514d\\u516c\\u5e03&nbsp;\\u300a\\u8840\\u6e90\\u300b\\u91cd\\u78c5\\u52a0\\u5165\", \"channel\": \"\\u9891\\u9053\", \"task_run_state_time\": {\"list_inqueue\": \"2018-05-24 20:31:35\", \"list_outqueue\": \"2018-05-24 20:31:35\", \"detail_outqueue\": \"2018-05-24 20:48:07\", \"detail_inqueue\": \"2018-05-24 20:32:07\", \"sccuess_indb\": null}, \"dont_filter\": true, \"small_img_location\": [{\"img_size\": null, \"img_path\": \"/data/sharedata/XY-TGBUS-1/878712075d99873600d675132f08aaa4.JPEG\", \"checksum\": \"878712075d99873600d675132f08aaa4\", \"img_width\": 670, \"img_height\": 377, \"img_src\": \"http://ps4.tgbus.bnbsky.com/UploadFiles/201803/2018030111250970.jpg\", \"img_format\": \"JPEG\", \"img_index\": 1}], \"info_source\": \"TGBUS\\u7f16\\u8bd1\", \"img_location_count\": 3}";
        String jsonStr2 = "{\"status\":10001,\"code\":\"HDkGzI\",\"pubkey\":\"DBCEEECFD3F6808C85254B1\",\"servertime\":1475741518}";
        Map map = mapper.readValue(jsonStr2, Map.class);
        map.put("name", "AnonYmous");
        System.out.println(map);
        System.out.println(mapper.writeValueAsString(map));
        JsonNode jsonNode = mapper.readTree(jsonStr1);
        System.out.println(jsonNode);
        System.out.println(jsonNode.findValue("parsed_content_main_body").asText());
        System.out.println(jsonNode.findValue("img_location"));
    }
}
