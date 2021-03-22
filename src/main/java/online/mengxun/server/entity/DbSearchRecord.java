package online.mengxun.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class DbSearchRecord {
    @Id
    private String srId;
    private String openId;
    private String srSql;
    private Boolean srConnected;
    private Boolean srResult;
    private Date created;
}
