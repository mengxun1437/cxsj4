package online.mengxun.server.model;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

public class UserModel {

    /*
    * 用户注册接口请求体 registerUser
    * */

    @Data
    public static class RegisterUser {
        @NotBlank(message = "昵称不可以为空")
        private String nickName;

        @NotBlank(message = "微信ID不可以为空")
        private String openId;

        @URL(message = "头像链接存在问题")
        @NotBlank(message = "头像不可以为空")
        private String avatar;
    }
}
