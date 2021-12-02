package pl.kusmp.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.param.InvoiceCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kusmp.stripe.config.StripeConfig;
import pl.kusmp.stripe.model.InvoiceDTO;
import pl.kusmp.stripe.model.InvoiceItemDTO;

@Service
public class InvoiceService {

    private final CustomerService clientService;
    private final InvoiceElementService invoiceElementService;
    private final StripeConfig stripeConfig;

    @Autowired
    public InvoiceService(CustomerService clientService, InvoiceElementService invoiceElementService, StripeConfig stripeConfig) {
        this.clientService = clientService;
        this.invoiceElementService = invoiceElementService;
        this.stripeConfig = stripeConfig;
    }

    public InvoiceDTO getInvoice(String id) throws StripeException {
        Stripe.apiKey = stripeConfig.getStripeAppKey();
        var invoiceItem = Invoice.retrieve(id);
        var invoiceItemDTO = InvoiceItemDTO.builder().amount(invoiceItem.getAmountDue()).currency(invoiceItem.getCurrency()).customer(invoiceItem.getCustomer()).build();

        return InvoiceDTO.builder().id(invoiceItem.getId()).accountCountry(invoiceItem.getAccountCountry()).amountDue(invoiceItem.getAmountDue()).amountPaid(invoiceItem.getAmountPaid()).customer(invoiceItem.getCustomer()).invoiceItem(invoiceItemDTO).build();
    }

    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) throws StripeException {
        Stripe.apiKey = stripeConfig.getStripeAppKey();
        var invoiceItem = invoiceElementService.createInvoiceParams(invoiceDTO);
        var invoiceParams =
                InvoiceCreateParams.builder()
                        .setCustomer(invoiceDTO.getCustomer())
                        .build();
        var stripeInvoice = Invoice.create(invoiceParams);
        var invoiceItemDTO = InvoiceItemDTO.builder().amount(invoiceItem.getAmount()).currency(invoiceItem.getCurrency()).customer(invoiceItem.getCustomer()).build();
        return InvoiceDTO.builder().id(stripeInvoice.getId()).accountCountry(stripeInvoice.getAccountCountry()).amountDue(stripeInvoice.getAmountDue()).amountPaid(stripeInvoice.getAmountPaid()).customer(stripeInvoice.getCustomer()).invoiceItem(invoiceItemDTO).build();
    }

    public String createCustomer() throws StripeException {
        return clientService.createNewCustomer().getId();
    }
}
