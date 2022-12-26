package com.kakaoinsurance.payment.web.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.Optional;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class CancelRequestDto {

    @Length(min = 20, max = 20, message = "관리번호는 20자리입니다.")
    @NotBlank(message = "관리번호는 필수 입력값입니다.")
    private String controlNumber;   // 관리번호

    @Min(value = 100, message = "금액은 100원 이상입니다.")
    @Max(value = 1000000000, message = "금액은 10억 이하입니다.")
    @NonNull
    private int cancelPrice;    // 취소금액

    @Setter
    private Optional<Integer> vat;    // 부가가치세

    public CancelRequestDto(){

    }
    public CancelRequestDto(String controlNumber, int cancelPrice) {
        this.controlNumber = controlNumber;
        this.cancelPrice = cancelPrice;
    }
}
