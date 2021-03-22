package online.mengxun.server.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

public class DbSelectModel {

    @Data
    public static class GetSelectResult{
        @NotBlank(message = "dbId不可以为空")
        private String dbId;
        @NotBlank(message = "查询语句不能为空")
        private String srSql;
    }
}
