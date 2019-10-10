package br.com.common.access.property;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
        "id",
        "nome",
        "email",
        "perfilType"
})
@Embeddable
public class ValidarToken implements Serializable {

    @JsonProperty("id")
    @Column(name = "access_id", updatable = false, nullable = false)
    private String accessId;

    @JsonProperty("nome")
    @Column(name = "access_nome", updatable = false, nullable = false)
    private String accessNome;

    @JsonProperty("email")
    @Column(name = "access_email", updatable = false, nullable = false)
    private String accessEmail;

    @JsonProperty("perfilType")
    @Column(name = "access_perfil_type", updatable = false, nullable = false)
    private String accessPerfilType;

    public ValidarToken() {
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessNome() {
        return accessNome;
    }

    public void setAccessNome(String accessNome) {
        this.accessNome = accessNome;
    }

    public String getAccessEmail() {
        return accessEmail;
    }

    public void setAccessEmail(String accessEmail) {
        this.accessEmail = accessEmail;
    }

    public String getAccessPerfilType() {
        return accessPerfilType;
    }

    public void setAccessPerfilType(String accessPerfilType) {
        this.accessPerfilType = accessPerfilType;
    }

}
