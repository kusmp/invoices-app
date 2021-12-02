package pl.kusmp.stripe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kusmp.stripe.model.InvoiceDTO;
import pl.kusmp.stripe.model.InvoiceItemDTO;
import pl.kusmp.stripe.service.InvoiceService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService geoService;

    @Test
    void shouldSaveNewEntityWhenValidParametersProvided() throws Exception {
        var invoiceItemDTO = InvoiceItemDTO.builder().amount(10L).currency("pln").customer("customerId").build();
        var invoice = InvoiceDTO.builder().customer("customerId").invoiceItem(invoiceItemDTO).build();

        this.mockMvc.perform(post("/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
                        .content(new ObjectMapper().writeValueAsString(invoice)))
                .andExpect(status().isOk());

    }

    @Test
    void shouldReturnBadRequestStatusWhenMissingParameter() throws Exception {
        var invoiceItemDTO = InvoiceItemDTO.builder().amount(10L).currency("pln").customer("customerId").build();
        var invoice = InvoiceDTO.builder().invoiceItem(invoiceItemDTO).build();

        this.mockMvc.perform(post("/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
                        .content(new ObjectMapper().writeValueAsString(invoice)))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(get("/v1/in_1K2DkTE4fFl3AuLubyteASAe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
                        .content(new ObjectMapper().writeValueAsString(invoice)))
                .andExpect(status().isOk());

    }

    @Test
    void shouldReturnOKStatusWhenInvoiceFound() throws Exception {
        this.mockMvc.perform(get("/v1/in_1K2DkTE4fFl3AuLubyteASAe")
                        .header("Authorization", "Basic dXNlcjpwYXNzd29yZA=="))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedStatusWhenBadCredentials() throws Exception {
        this.mockMvc.perform(get("/v1/in_1K2DkTE4fFl3AuLubyteASAe")
                        .header("Authorization", "Basic dXNlcjpwYXNzd29=="))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}