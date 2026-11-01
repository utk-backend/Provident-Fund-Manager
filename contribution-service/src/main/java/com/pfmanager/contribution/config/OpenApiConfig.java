package com.pfmanager.contribution.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI contributionServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PF Manager — Contribution Service API")
                        .description("""
                                REST API for the Provident Fund Manager Contribution Service.
                                
                                Manages an immutable contribution ledger with support for:
                                - Creating contribution entries (CONTRIBUTION / REVERSAL)
                                - Retrieving individual entries by transaction ID
                                - Listing paginated history per participant
                                - Deriving participant balances from ledger entries
                                - Idempotent POST operations via Idempotency-Key header
                                
                                Phase 1 scope: INR only, no future-dated entries, full reversals only.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PF Manager Team"))
                        .license(new License()
                                .name("Private — Internal Use Only")));
    }
}
