package online.mengxun.server.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DbConfigModel {

    /**
     * 数据库配置信息注册model
     */
    @Data
    public static class AddNewDatabase{
        @NotBlank(message = "openId不可以为空")
        private String openId;
        @NotBlank(message = "shortName不可以为空")
        private String shortName;
        @NotBlank(message = "数据库名不能为空")
        private String dbName;
        @NotBlank(message = "数据库用户名不可以为空")
        private String dbUser;
        @NotBlank(message = "数据库密码不可以空")
        private String dbPwd;
        @NotBlank(message = "数据库主机ip不可以为空")
        private String dbIp;
        @NotNull(message = "端口号不能为空")
        private Integer dbPort;
        @NotNull(message = "数据库的驱动选择不可以为空")
        private String dbDriverId;
    }

    /**
     * 数据库修改model
     */

    @Data
    public static class ModifyDatabase{
        @NotBlank(message = "shortName不可以为空")
        private String shortName;
        @NotBlank(message = "数据库名不能为空")
        private String dbName;
        @NotBlank(message = "数据库用户名不可以为空")
        private String dbUser;
        @NotBlank(message = "数据库主机ip不可以为空")
        private String dbIp;
        @NotNull(message = "端口号不能为空")
        private Integer dbPort;
        @NotNull(message = "数据库的驱动选择不可以为空")
        private String dbDriverId;
    }

    /**
     * 数据库配置修改密码model
     */
    @Data
    public static class ModifyDatabasePwd{
        @NotBlank(message = "dbId不可以为空")
        private String dbId;
        @NotBlank(message = "修改后密码不可以为空")
        private String dbPwd;
    }


}
