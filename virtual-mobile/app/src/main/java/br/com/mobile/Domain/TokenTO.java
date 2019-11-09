package br.com.mobile.Domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 30/10/2019 > _@version $$ >
 */
public class TokenTO implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("PerfilType")
    private String perfilType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfilType() {
        return perfilType;
    }

    public void setPerfilType(String perfilType) {
        this.perfilType = perfilType;
    }
}
