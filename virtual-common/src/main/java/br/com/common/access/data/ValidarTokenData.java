package br.com.common.access.data;

import br.com.common.access.property.ValidarToken;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonPropertyOrder({
    "validarToken"
})
public class ValidarTokenData implements Serializable {

    @JsonProperty("validarToken")
    private ValidarToken validarToken;

    public ValidarTokenData() {
    }

    public ValidarToken getValidarToken() {
        return validarToken;
    }

    public void setValidarToken(ValidarToken validarToken) {
        this.validarToken = validarToken;
    }

}
