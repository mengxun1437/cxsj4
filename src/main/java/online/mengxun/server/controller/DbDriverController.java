package online.mengxun.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.mengxun.server.entity.DbDriver;
import online.mengxun.server.reposity.DbDriverRepository;
import online.mengxun.server.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "数据库Driver模块")
@RestController
@RequestMapping("/driver")
public class DbDriverController {

    @Autowired
    private DbDriverRepository dbDriverRepository;

    @ApiOperation(value = "获取driver列表")
    @GetMapping("/")
    public Response getAllDbDrivers(){
        try{
            List<DbDriver> dbDriverList = dbDriverRepository.findAll();
            return Response.success(dbDriverList);
        }catch (Exception e){
            return Response.error("获取数据库驱动列表失败");
        }
    }
}
