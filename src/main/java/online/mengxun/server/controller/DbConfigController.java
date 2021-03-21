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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public Response addNewDatabaseConfig(@Valid @RequestBody DbConfigModel.AddNewDatabase dbConfig) {
        try {

            //用户校验
            if (!userRepository.existsById(dbConfig.getOpenId())) {
                return Response.error("此用户不存在");
            }

            //driverId校验
            if (!dbDriverRepository.existsById(dbConfig.getDbDriverId())) {
                return Response.error("driverId不合法");
            }

            //添加数据库
            DbConfig database = new DbConfig();
            String dbId = UUID.randomUUID().toString();
            database.setDbId(dbId);
            BeanUtils.copyProperties(dbConfig, database);
            database.setCreated(new Date());
            database.setUpdated(database.getCreated());

            if (dbConfigRepository.save(database) != null) {
                JSONObject res = new JSONObject();
                res.put("dbId", dbId);
                res.put("openId", database.getOpenId());
                res.put("created", database.getCreated());
                res.put("updated", database.getUpdated());

                return Response.success("新增数据库配置信息成功", res);
            } else {
                return Response.error("新增数据库配置信息失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("新增数据库配置信息失败");
        }
    }


    @ApiOperation(value = "获取用户所有的数据库配置信息")
    @GetMapping("/")
    public Response getAllDatabaseConfig(@RequestHeader("Authorization") String openId) {
        try {

            List<DbConfig> lists = dbConfigRepository.findAllByOpenId(openId);
            List<JSONObject> resData = new ArrayList<>();
            for (DbConfig list : lists) {
                JSONObject resDataItem = new JSONObject();
                resDataItem.put("dbId", list.getDbId());
                resDataItem.put("shortName", list.getShortName());
                resDataItem.put("created", list.getCreated());
                resDataItem.put("updated", list.getUpdated());
                resData.add(resDataItem);
            }
            return Response.success(resData);

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取用户所有数据库配置信息失败");
        }
    }

    @ApiOperation(value = "通过dbId获取数据库的具体信息")
    @GetMapping("/{dbId}")
    public Response getDatabaseConfigByDbId(@PathVariable("dbId") String dbId,@RequestHeader("Authorization") String openId) {
        try {

            //数据库id校验
            if (!dbConfigRepository.existsById(dbId)) {
                return Response.error("该数据库配置不存在");
            }

            if (!dbConfigRepository.existsByDbIdAndOpenId(dbId,openId)){
                return Response.error("您没有获取数据库的配置信息的权限");
            }


            return Response.success(dbConfigRepository.findById(dbId));

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error("获取数据库信息失败");
        }
    }


    @ApiOperation(value = "通过dbId修改数据库的具体信息")
    @PutMapping("/{dbId}")
    public Response modifyDatabaseConfigByDbId(@PathVariable("dbId") String dbId,@RequestHeader("Authorization") String openId,@Valid @RequestBody DbConfigModel.ModifyDatabase modifyBody){
        try{
            //数据库id校验
            if (!dbConfigRepository.existsById(dbId)) {
                return Response.error("该数据库配置不存在");
            }

            if (!dbConfigRepository.existsByDbIdAndOpenId(dbId,openId)){
                return Response.error("您没有获取数据库的配置信息的权限");
            }

            DbConfig dbConfig = dbConfigRepository.getOne(dbId);

            //driverId校验
            if (!dbDriverRepository.existsById(modifyBody.getDbDriverId())) {
                return Response.error("driverId不合法");
            }


            dbConfig.setUpdated(new Date());

            BeanUtils.copyProperties(modifyBody,dbConfig);

            if (dbConfigRepository.save(dbConfig)!=null){
                return Response.success("修改信息成功",dbConfigRepository.findById(dbId));
            }else {
                return Response.error("修改信息失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.error("修改数据库信息失败");
        }
    }

    @ApiOperation(value = "修改数据库配置信息的密码")
    @PutMapping("/password")
    public Response modifyDatabasePwd(@RequestHeader("Authorization") String openId,@Valid @RequestBody DbConfigModel.ModifyDatabasePwd modifyPwdBody){
        try{
            if (!dbConfigRepository.existsById(modifyPwdBody.getDbId())){
                return Response.error("该数据库配置不存在");
            }

            //校验
            if (!dbConfigRepository.existsByDbIdAndOpenId(modifyPwdBody.getDbId(),openId)){
                return Response.error("您没有修改这个数据库配置信息密码的权限");
            }

            DbConfig dbConfig = dbConfigRepository.getOne(modifyPwdBody.getDbId());
            dbConfig.setDbPwd(modifyPwdBody.getDbPwd());
            dbConfig.setUpdated(new Date());

            if (dbConfigRepository.save(dbConfig)!=null){
                return Response.success("修改密码成功");
            }else {
                return Response.error("修改密码失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.error("修改密码失败");
        }
    }

    @ApiOperation(value = "通过dbId删除数据库配置信息")
    @DeleteMapping("/{dbId}")
    public Response deleteDatabaseConfigById(@PathVariable("dbId") String dbId,@RequestHeader("Authorization") String openId){
        try{
            //数据库id校验
            if (!dbConfigRepository.existsById(dbId)) {
                return Response.error("该数据库配置不存在");
            }

            if (!dbConfigRepository.existsByDbIdAndOpenId(dbId,openId)){
                return Response.error("您没有获取数据库的配置信息的权限");
            }

            dbConfigRepository.deleteById(dbId);

            return Response.success("删除数据库配置信息成功");

        }catch (Exception e){
            e.printStackTrace();
            return Response.error("删除数据库配置信息失败");
        }
    }



}
