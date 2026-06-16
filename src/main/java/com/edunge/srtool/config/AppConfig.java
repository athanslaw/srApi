package com.edunge.srtool.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SR Tool",
                version = "1.0.0",
                description = "SR Tool - Election/Incidents monitoring REST API",
                contact = @Contact(
                        name = "Adewale Adeleye",
                        email = "adewaleadeleye26@gmail.com",
                        url = "www.walenotes.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class AppConfig {
}