package com.kakaoinsurance.payment.web.dto;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Optional;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PayRequestDto {

    @Length(min = 10, max = 16, message = "카드번호는 10 ~ 16자리입니다.")
    @NotBlank(message = "카드번호는 필수 입력값입니다.")
    private String cardNumber;

    @Length(min = 4,max = 4, message="유효기간은 4자리입니다.")
    @NotBlank(message = "유효기간은 필수 입력값입니다.")
    @Pattern(regexp = "^((0[1-9])|(1[0-2]))(\\d{2})$", message = "유효기간은 mmyy 이어야합니다.")
    private String expiryDate;

    @NotBlank(message = "cvc는 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9]{3}$", message = "cvc는 000 ~ 999 값입니다.")
    private String cvc;

    @NotNull(message = "할부개월 수는 필수 입력값입니다.")
    @Min(value = 0, message = "할부개월의 최소값은 0입니다.")
    @Max(value = 12, message = "할부개월의 최대값은 12입니다.")
    private int installment;

    @Min(value = 100, message = "결제 금액은 100원 이상입니다.")
    @Max(value = 1000000000, message = "결제 금액은 10억 이하입니다.")
    @NonNull
    private int price;


    @Setter
    private Optional<Integer> vat;

    public PayRequestDto(){

    }
    public PayRequestDto(String cardNumber, String expiryDate, String cvc, int installment, int price) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvc = cvc;
        this.installment = installment;
        this.price = price;
    }
}
