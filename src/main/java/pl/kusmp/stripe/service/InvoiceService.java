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
        var invoice = Invoice.retrieve(id);
        var invoiceItemDTO = InvoiceItemDTO.builder().amount(invoice.getAmountDue()).currency(invoice.getCurrency()).customer(invoice.getCustomer()).build();

        return createInvoiceDTO(invoice, invoiceItemDTO);
    }

    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) throws StripeException {
        Stripe.apiKey = stripeConfig.getStripeAppKey();
        var invoiceItem = invoiceElementService.createInvoiceParams(invoiceDTO);
        var invoiceParams =
                InvoiceCreateParams.builder()
                        .setCustomer(invoiceDTO.getCustomer())
                        .build();

        var stripeInvoice = Invoice.create(invoiceParams);

        var invoiceItemDTO = InvoiceItemDTO.builder().
                amount(invoiceItem.getAmount())
                .currency(invoiceItem.getCurrency()).
                customer(invoiceItem.getCustomer())
                .build();

        return createInvoiceDTO(stripeInvoice, invoiceItemDTO);
    }

    private InvoiceDTO createInvoiceDTO(Invoice invoice, InvoiceItemDTO invoiceItemDTO) {

        return InvoiceDTO.builder()
                .id(invoice.getId())
                .accountCountry(invoice.getAccountCountry())
                .amountDue(invoice.getAmountDue())
                .amountPaid(invoice.getAmountPaid())
                .customer(invoice.getCustomer())
                .invoiceItem(invoiceItemDTO)
                .build();
    }
}
