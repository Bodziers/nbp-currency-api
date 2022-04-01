package pl.finmatik.nbpcurrencyapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Generated("jsonschema2pojo")
public class Rate {
    private String currency;
    private String code;
    private Float mid;
}
