package br.com.master.configuration;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 20 de dez de 20182
 */
@Configuration
@EnableSwagger2
public class MasterSpringFox {

    static List<ResponseMessage> listGlobalResponseMessage = new ArrayList<>();

    {
        listGlobalResponseMessage.add(new ResponseMessageBuilder().code(401).message("Não autorizado").build());
        listGlobalResponseMessage.add(new ResponseMessageBuilder().code(403).message("Proibido").build());
    }

    @Bean
    public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.master.resource")) // .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag(
                        "Arquivos",
                        "O serviço de arquivos envolve a leitura, gravação e exclusão.")
                )
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.DELETE, listGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.GET, listGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.PATCH, listGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.POST, listGlobalResponseMessage)
                .globalResponseMessage(RequestMethod.PUT, listGlobalResponseMessage)
                .ignoredParameterTypes(File.class, InputStream.class, Resource.class, URI.class, URL.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Master RESTful Web Services" ,                                                         /* title */
                "Manual de Integração." ,                                                               /* description */
                "1.0",                                                                                  /* version */
                "Terms of service" ,                                                                    /* termsOfServiceUrl */
                new Contact("Danylo Macelai", "http://danylomacelai.com", "danylomacelai@gmail.com") ,  /* contactName */
                "Apache 2.0",                                                                           /* license */
                "http://www.apache.org/licenses/LICENSE-2.0",                                           /* licenseUrl */
                Collections.emptyList()                                                                 /* vendorExtensions */
                );
    }

}
