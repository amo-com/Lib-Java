package amo.lib.common;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private Boolean swaggerEnabled;

    @Value("${swagger.title}")
    private String swaggerTitle;

    @Value("${swagger.host}")
    private String SERVER_HOST;

    public List<Parameter> parameters = getDefaultParameters();

    /**
     * @Bean托管,需要配置使用
     * @return
     */
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(this.SERVER_HOST)
                .globalOperationParameters(parameters)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(swaggerEnabled ? PathSelectors.any() : PathSelectors.none())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(this.swaggerTitle)
                .description(this.swaggerTitle)
                .termsOfServiceUrl("Terms Of ServiceUrl")
                .version("1.0")
                .build();
    }

    protected List<Parameter> getDefaultParameters(){
        List<Parameter> parameterList = new ArrayList<Parameter>();
        ParameterBuilder siteParam = new ParameterBuilder();
        siteParam.name("site")
                .description("Site")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(true);
        parameterList.add(siteParam.build());

        return parameterList;
    }
}