package pl.kusmp.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kusmp.stripe.config.StripeConfig;
import pl.kusmp.stripe.model.InvoiceDTO;
import pl.kusmp.stripe.model.InvoiceItemDTO;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    private static final String API_KEY = "provide-valid-api-key";
    @Mock
    private CustomerService clientService;
    @Mock
    private InvoiceElementService invoiceElementService;
    @Mock
    private StripeConfig stripeConfig;
    @InjectMocks
    private InvoiceService tested;

    @Test
    void shouldGetInvoice() throws StripeException {
        //given
        Stripe.apiKey = API_KEY;
        var invoice = new Invoice();
        invoice.setId("id");
        Mockito.when(stripeConfig.getStripeAppKey()).thenReturn(API_KEY);

        //when
        tested.getInvoice(createInvoiceDTO().getId());

        //then
        Mockito.verify(stripeConfig).getStripeAppKey();
    }

    @Test
    void shouldGeneratenewInvoice() throws StripeException {
        //given
        Stripe.apiKey = API_KEY;

        Mockito.when(stripeConfig.getStripeAppKey()).thenReturn(API_KEY);
        Mockito.when(invoiceElementService.createInvoiceParams(createInvoiceDTO())).thenReturn(createInvoiceItem(createInvoiceDTO()));

        //when
        tested.createInvoice(createInvoiceDTO());

        //then
        Mockito.verify(invoiceElementService).createInvoiceParams(createInvoiceDTO());

    }

    @Test
    void shouldReturnInvalidRequestExceptionWhenFieldsAreNotValid() throws StripeException {
        //given
        Stripe.apiKey = API_KEY;
        Mockito.when(stripeConfig.getStripeAppKey()).thenReturn(API_KEY);
        var invoiceDTO = createInvoiceDTO();
        invoiceDTO.setCustomer(null);
        Mockito.when(invoiceElementService.createInvoiceParams(invoiceDTO)).thenReturn(createInvoiceItem(createInvoiceDTO()));

        //when

        //then
        Throwable exception = assertThrows(InvalidRequestException.class,
                () -> tested.createInvoice(invoiceDTO));
    }

    private InvoiceDTO createInvoiceDTO() {
        var invoiceItemDTO = InvoiceItemDTO.builder().amount(10L).currency("pln").customer("cus_KhcpGwb3BfFAFd").build();
        return InvoiceDTO.builder().id("in_1K2DkTE4fFl3AuLubyteASAe").customer("cus_KhcpGwb3BfFAFd").invoiceItem(invoiceItemDTO).build();
    }

    private InvoiceItem createInvoiceItem(InvoiceDTO invoiceDTO) throws StripeException {
        var params = new HashMap<String, Object>();
        params.put("customer", invoiceDTO.getInvoiceItem().getCustomer());
        params.put("amount", invoiceDTO.getInvoiceItem().getAmount());
        params.put("currency", invoiceDTO.getInvoiceItem().getCurrency());

        return InvoiceItem.create(params);
    }

}