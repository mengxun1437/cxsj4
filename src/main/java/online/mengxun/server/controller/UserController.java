package online.mengxun.server.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.mengxun.server.client.WxApiClient;
import online.mengxun.server.config.GlobalDataConfig;
import online.mengxun.server.entity.User;
import online.mengxun.server.model.UserModel;
import online.mengxun.server.reposity.UserRepository;
import online.mengxun.server.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

import static online.mengxun.server.util.BaseUtil.JSONStringToJSONObject;

@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
@Validated
public class UserController{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WxApiClient wxApiClient;

    private String appId = GlobalDataConfig.appId;
    private String appSecretKey =GlobalDataConfig.appSecretKey;

    @ApiOperation(value = "获取OpenId")
    @GetMapping("/openId")
    public Response getUserOpenid(@RequestParam("code") String code){
        try{
            JSONObject res = JSONStringToJSONObject(wxApiClient.getOpenIdByCode(appId,appSecretKey,code));
            /*
            如果返回openid就返回成功
            * */
            if(res.containsKey("openid")) {
                JSONObject resSuc = new JSONObject();
                resSuc.put("openId",res.getString("openid"));
                return Response.success("获取用户OpenId成功", resSuc);
            }else {
                return Response.error("获取用户OpenId失败",res);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error("获取OpenId发生异常");
        }
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/")
    public Response registerUser(@Valid @RequestBody UserModel.RegisterUser registerUser) {
        /*
        * 用户存在返回存在
        * */
        if(userRepository.existsById(registerUser.getOpenId())){
            return Response.error("该用户已经存在");
        }
        User user = new User();
        user.setNickName(registerUser.getNickName());
        user.setOpenId(registerUser.getOpenId());
        user.setAvatar(registerUser.getAvatar());
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());
        if (userRepository.save(user) != null){
            JSONObject resSuc = new JSONObject();
            resSuc.put("openid",registerUser.getOpenId());
            resSuc.put("created",user.getCreated());
            resSuc.put("updated",user.getUpdated());
            return Response.success("新增用户成功",resSuc);
        }else {
            return Response.error("新增用户失败");
        }
    }




}
