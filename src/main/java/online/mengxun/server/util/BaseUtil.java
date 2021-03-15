package online.mengxun.server.util;

import com.alibaba.fastjson.JSONObject;

public class BaseUtil {
    public static JSONObject JSONStringToJSONObject(String jsonStr){
        try{
            return JSONObject.parseObject(jsonStr);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
