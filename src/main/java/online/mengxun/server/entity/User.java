package online.mengxun.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class User {
    @Id
    private String id;
    private String openId;
    private String nickName;
    private String avatar;
    private Date created;
    private Date updated;
}
