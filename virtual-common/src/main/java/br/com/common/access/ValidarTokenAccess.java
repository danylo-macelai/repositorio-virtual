package br.com.common.access;

import br.com.common.access.data.ValidarTokenData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 11 de ago de 2019
 * @version $
 */
@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidarTokenAccess extends Access {

    @JsonProperty("data")
    private ValidarTokenData data;

    public ValidarTokenAccess() {

    }

    public ValidarTokenData getData() {
        return data;
    }

    public void setData(ValidarTokenData data) {
        this.data = data;
    }

}
