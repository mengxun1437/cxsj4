package online.mengxun.server.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.mengxun.server.entity.DbConfig;
import online.mengxun.server.model.DbConfigModel;
import online.mengxun.server.reposity.DbConfigRepository;
import online.mengxun.server.reposity.DbDriverRepository;
import online.mengxun.server.reposity.UserRepository;
import online.mengxun.server.response.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

@Api(tags = "数据库配置模块")
@RestController
@RequestMapping("/database")
public class DbConfigController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DbConfigRepository dbConfigRepository;

    @Autowired
    private DbDriverRepository dbDriverRepository;

    @ApiOperation(value = "新增用户数据库配置信息")
    @PostMapping("/")
    public Response addNewDatabaseConfig(@Valid @RequestBody DbConfigModel.AddNewDatabase dbConfig){
        try{

            //用户校验
            if (!userRepository.existsById(dbConfig.getOpenId())){
                return Response.error("此用户不存在");
            }

            //driverId校验
            if (!dbDriverRepository.existsById(dbConfig.getDbDriverId())){
                return Response.error("driverId不合法");
            }

            //添加数据库
            DbConfig database = new DbConfig();
            String dbId = UUID.randomUUID().toString();
            database.setDbId(dbId);
            BeanUtils.copyProperties(dbConfig,database);
            database.setCreated(new Date());
            database.setUpdated(database.getCreated());

            if (dbConfigRepository.save(database)!=null){
                JSONObject res = new JSONObject();
                res.put("dbId",dbId);
                res.put("openId",database.getOpenId());
                res.put("created",database.getCreated());
                res.put("updated",database.getUpdated());

                return Response.success("新增数据库配置信息成功",res);
            }
            else {
                return Response.error("新增数据库配置信息失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.error("新增数据库配置信息失败");
        }
    }
}
