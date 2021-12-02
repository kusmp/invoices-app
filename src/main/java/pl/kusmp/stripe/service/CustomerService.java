package pl.kusmp.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.springframework.stereotype.Service;
import pl.kusmp.stripe.config.StripeConfig;

@Service
public class CustomerService {

    private final StripeConfig stripeConfig;

    public CustomerService(StripeConfig stripeConfig) {
        this.stripeConfig = stripeConfig;
    }

    public Customer createNewCustomer() throws StripeException {
        Stripe.apiKey = stripeConfig.getStripeAppKey();
        var params = CustomerCreateParams.builder().build();
        return Customer.create(params);
    }
}
