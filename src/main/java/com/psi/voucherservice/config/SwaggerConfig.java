package com.psi.voucherservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerBigCommerceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("VoucherService")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.psi.voucherservice.controller"))
                .paths(regex("/.*"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }


    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Spring Boot REST API")
                .description("Voucher Service REST API")
                .contact(new Contact("Vamshi Kiran Gullapelly", "www.psionline.com", "vamshi.gp81@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}
