package pl.kusmp.stripe.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class InvoiceDTO {
    private String id;
    @NotBlank
    private String customer;
    private String accountCountry;
    private Long amountDue;
    private Long amountPaid;
    private InvoiceItemDTO invoiceItem;
}
