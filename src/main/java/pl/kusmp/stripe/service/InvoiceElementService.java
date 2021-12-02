package pl.kusmp.stripe.service;

import com.stripe.exception.StripeException;
import com.stripe.model.InvoiceItem;
import org.springframework.stereotype.Service;
import pl.kusmp.stripe.model.InvoiceDTO;

import java.util.HashMap;

@Service
public class InvoiceElementService {

    public InvoiceItem createInvoiceParams(InvoiceDTO invoiceDTO) throws StripeException {
        var params = new HashMap<String, Object>();
        params.put("customer", invoiceDTO.getInvoiceItem().getCustomer());
        params.put("amount", invoiceDTO.getInvoiceItem().getAmount());
        params.put("currency", invoiceDTO.getInvoiceItem().getCurrency());

        return InvoiceItem.create(params);
    }
}
