package online.mengxun.server.controller;


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
import java.util.UUID;

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
            return Response.success("获取用户OpenId失败",wxApiClient.getOpenIdByCode(appId,appSecretKey,code));

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
        if(userRepository.existsByOpenId(registerUser.getOpenId())){
            return Response.error("该用户已经存在");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setNickName(registerUser.getNickName());
        user.setOpenId(registerUser.getOpenId());
        user.setAvatar(registerUser.getAvatar());
        user.setCreated(new Date());
        user.setUpdated(new Date());


        if (userRepository.save(user) != null){
            return Response.success("add new user success");
        }else {
            return Response.error("add new user failed");
        }
    }

}
