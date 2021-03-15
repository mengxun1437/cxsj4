package online.mengxun.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger2.host:127.0.0.1:8085}")
    private String swaggerHost;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerHost)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("online.mengxun.server.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("cxsj4 Api文档")
                .description("cxsj4 SQL智能查询 Api文档")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
