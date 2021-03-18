package online.mengxun.server.exception;

import online.mengxun.server.response.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {

    /*
     * 全局异常
     * */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response handleGlobalDataException(Exception ex) {
        System.out.println(ex);
        try {
            /*
            请求方式不允许
            * */
            if (ex instanceof HttpRequestMethodNotSupportedException){
                return Response.error("该接口不支持此请求方式");
            }

            /*
            * 空指针错误
            * */
            else if (ex instanceof NullPointerException){
                return Response.error("发生了空指针错误");
            }

            /*
            * 数据校验异常
            * */
            else if (ex instanceof MethodArgumentNotValidException) {
                BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
                if (bindingResult.hasErrors()) {
                    return Response.error(bindingResult.getFieldError().getDefaultMessage());
                }
                return Response.error("数据校验错误");
            }
            /*
            * 约束异常
            * */
            else if (ex instanceof DataIntegrityViolationException) {
                return Response.error("数据库更新失败");
            }

            return Response.error(ex.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(e.getMessage());
        }
    }
}
