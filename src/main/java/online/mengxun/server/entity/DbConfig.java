package online.mengxun.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DbConfig {
    @Id
    private String dbId;
    private String openId;
    private String shortName;
    private String dbName;
    private String dbUser;
    @JsonIgnore
    private String dbPwd;
    private String dbIp;
    private Integer dbPort;
    private String dbDriverId;
    private Date created;
    private Date updated;
}
