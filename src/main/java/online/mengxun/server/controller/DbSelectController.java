package online.mengxun.server.controller;

import io.swagger.annotations.Api;
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
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Api(tags = "数据库查询模块")
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
    public Response getSelectResult(@RequestHeader("Authorization") String openId, @Valid @RequestBody DbSelectModel.GetSelectResult select) {
        try {
            String dbId = select.getDbId();
            String srSql = select.getSrSql();

            //数据库id校验
            if (!dbConfigRepository.existsById(dbId)) {
                return Response.error("该数据库配置不存在");
            }

            if (!dbConfigRepository.existsByDbIdAndOpenId(dbId, openId)) {
                return Response.error("您没有获取数据库的配置信息的权限");
            }

            //数据库语句校验,只能查询
            if (!select.getSrSql().substring(0, 6).equalsIgnoreCase("SELECT")) {
                return Response.error("只能提交select语句");
            }

            if(!srSql.matches("select (.*) from (.*)")){
                return Response.error("sql语句格式错误;正确格式=>select () from ()");
            }

            DbConfig dbConfig = dbConfigRepository.getOne(dbId);
            DbDriver dbDriver = dbDriverRepository.getOne(dbConfig.getDbDriverId());

            DbSearchRecord dbSearchRecord = new DbSearchRecord();
            Connection conn = DatabaseClient.getConnection(dbConfig.getDbIp(), dbConfig.getDbPort().toString(), dbConfig.getDbName(), dbConfig.getDbUser(), dbConfig.getDbPwd(), dbDriver.getDriverName(), dbDriver.getDriverType());
            if (conn == null) {
                dbSearchRecord.setSrId(UUID.randomUUID().toString());
                dbSearchRecord.setSrConnected(false);
                dbSearchRecord.setOpenId(openId);
                dbSearchRecord.setSrSql(srSql);
                dbSearchRecord.setCreated(new Date());
                dbSearchRecord.setSrResult(false);
                dbSearchRecordRepository.save(dbSearchRecord);
                return Response.error("数据库连接失败");
            }

            dbSearchRecord.setSrId(UUID.randomUUID().toString());
            dbSearchRecord.setSrConnected(true);
            dbSearchRecord.setOpenId(openId);
            dbSearchRecord.setSrSql(srSql);
            dbSearchRecord.setCreated(new Date());


            try{
                List selectResultData = DatabaseClient.selectData(conn, srSql);

                dbSearchRecord.setSrResult(true);
                dbSearchRecordRepository.save(dbSearchRecord);
                return Response.success("查询成功", selectResultData);
            }catch (SQLException e) {
                dbSearchRecord.setSrResult(false);
                dbSearchRecordRepository.save(dbSearchRecord);
                return Response.error(e.getMessage());
            }



        } catch (Exception e) {

            return Response.error("Unknown Select Error");
        }
    }
}
