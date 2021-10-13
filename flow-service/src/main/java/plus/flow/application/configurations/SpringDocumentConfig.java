package plus.flow.application.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "流程服务",
                description = "",
                contact = @Contact(
                        name = "Hansin",
                        email = "hansin@goodvoice.com"
                ),
                version = "v1"
        )

)
@SecuritySchemes(value = @SecurityScheme(name = "auth",
        type = SecuritySchemeType.OAUTH2,
        in= SecuritySchemeIn.HEADER,
        scheme = "Bearer",
        flows =  @OAuthFlows(
                implicit = @OAuthFlow(
                        authorizationUrl="${plus.flow.authorization-endpoint}")
        )
)
)
public class SpringDocumentConfig {
}
