package online.mengxun.server.interceptor;

import lombok.extern.slf4j.Slf4j;
import online.mengxun.server.reposity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *自定义拦截器，除了登录接口，其他接口调用的时候header中要加入参数
 * Authorization
 */
@Component
@Slf4j
public class HeaderInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            throw new Exception("Header Authorization参数缺失");
        }else{
            try {
                if (userRepository.existsById(authorization)){
                    return true;
                }else {
                    throw new Exception("您没有访问接口的权限");
                }

            }catch (Exception e){
                throw new Exception("Authorization验证错误");
            }

        }
    }
}
