package pl.finmatik.nbpcurrencyapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Generated("jsonschema2pojo")
public class CurrencyTable {
    private String table;
    private String no;
    private String effectiveDate;
    private List<Rate> rates = null;
}
