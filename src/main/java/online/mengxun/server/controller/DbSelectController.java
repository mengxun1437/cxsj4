package online.mengxun.server.controller;

import io.swagger.annotations.ApiOperation;
import online.mengxun.server.client.DatabaseClient;
import online.mengxun.server.entity.DbConfig;
import online.mengxun.server.entity.DbDriver;
import online.mengxun.server.entity.DbSearchRecord;
import online.mengxun.server.model.DbSelectModel;
import online.mengxun.server.reposity.DbConfigRepository;
import online.mengxun.server.reposity.DbDriverRepository;
import online.mengxun.server.reposity.DbSearchRecordRepository;
import online.mengxun.server.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/select")
public class DbSelectController {

    @Autowired
    private DbConfigRepository dbConfigRepository;

    @Autowired
    private DbDriverRepository dbDriverRepository;

    @Autowired
    private DbSearchRecordRepository dbSearchRecordRepository;

    @ApiOperation(value = "获取查询结果")
    @PostMapping("/")
    public Response getSelectResult(@RequestHeader("Authorization") String openId, @Valid @RequestBody DbSelectModel.GetSelectResult select){
       try{
           String dbId = select.getDbId();
           String srSql = select.getSrSql();

           //数据库id校验
           if (!dbConfigRepository.existsById(dbId)) {
               return Response.error("该数据库配置不存在");
           }

           if (!dbConfigRepository.existsByDbIdAndOpenId(dbId,openId)){
               return Response.error("您没有获取数据库的配置信息的权限");
           }

           DbConfig dbConfig = dbConfigRepository.getOne(dbId);
           DbDriver dbDriver = dbDriverRepository.getOne(dbConfig.getDbDriverId());

           DbSearchRecord dbSearchRecord = new DbSearchRecord();
           Connection conn = DatabaseClient.getConnection(dbConfig.getDbIp(),dbConfig.getDbPort().toString(),dbConfig.getDbName(),dbConfig.getDbUser(),dbConfig.getDbPwd(),dbDriver.getDriverName(),dbDriver.getDriverType());
           if (conn == null){
               dbSearchRecord.setSrId(UUID.randomUUID().toString());
               dbSearchRecord.setSrConnected(false);
               dbSearchRecord.setOpenId(openId);
               dbSearchRecord.setSrSql(srSql);
               dbSearchRecord.setCreated(new Date());
               dbSearchRecord.setSrResult(false);
               dbSearchRecordRepository.save(dbSearchRecord);
               return Response.error("数据库连接失败");
           }

           List selectResultData = DatabaseClient.selectData(conn,srSql);
           System.out.println(selectResultData);
           dbSearchRecord.setSrId(UUID.randomUUID().toString());
           dbSearchRecord.setSrConnected(true);
           dbSearchRecord.setOpenId(openId);
           dbSearchRecord.setSrSql(srSql);
           dbSearchRecord.setCreated(new Date());
           dbSearchRecord.setSrResult(true);
           dbSearchRecordRepository.save(dbSearchRecord);
           return Response.success("查询成功",selectResultData);

       }catch (Exception e){
           e.printStackTrace();
           return Response.error("查询失败");
       }
    }

}
