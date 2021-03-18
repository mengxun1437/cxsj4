package online.mengxun.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class DbDriver {
    @Id
    private String driverId;
    //驱动名
    private String driverName;
    //驱动数据库类型
    private String driverType;
}
