package pl.kusmp.stripe.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class StripeConfig {
    @Value("${stripe.app.key}")
    private String stripeAppKey;
}
