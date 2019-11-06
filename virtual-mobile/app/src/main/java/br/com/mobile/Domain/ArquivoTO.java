package br.com.mobile.Domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 30/10/2019 > _@version $$ >
 */
public class ArquivoTO implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("nome")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @SerializedName("tamanho")
    private int tamanho;

    @SerializedName("pecas")
    private int pecas;

    @SerializedName("mimeType")
    private String mimeType;

    @SerializedName("searchTab")
    private String searchTab;

    @SerializedName("token")
    private TokenTO token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getPecas() {
        return pecas;
    }

    public void setPecas(int pecas) {
        this.pecas = pecas;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSearchTab() {
        return searchTab;
    }

    public void setSearchTab(String searchTab) {
        this.searchTab = searchTab;
    }

    public TokenTO getToken() {
        return token;
    }

    public void setToken(TokenTO token) {
        this.token = token;
    }
}
