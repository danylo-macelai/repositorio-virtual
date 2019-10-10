package br.com.master.domain;

import br.com.common.access.property.ValidarToken;
import br.com.common.domain.Domain;

import br.com.master.wrappers.SearchTab;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 * @version $
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RV_ARQUIVO", indexes = {
        @Index(name = "IDX_NOME_IN_RV_ARQUIVO", columnList = "nome", unique = false)
})
public class ArquivoTO extends Domain {

    @Column(name = "tamanho", updatable = false, nullable = false)
    private Integer tamanho;

    @Column(name = "pecas", updatable = false, nullable = false)
    private Integer pecas;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "mime_type", updatable = false, nullable = false)
    private String mimeType;

    @Column(name = "search_tab", updatable = false, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SearchTab searchTab;

    @Embedded
    private ValidarToken token;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "arquivo")
    @OrderBy("numero ASC")
    private Set<BlocoTO> blocos;

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    public Integer getPecas() {
        return pecas;
    }

    public void setPecas(Integer pecas) {
        this.pecas = pecas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public SearchTab getSearchTab() {
        return searchTab;
    }

    public void setSearchTab(SearchTab searchTab) {
        this.searchTab = searchTab;
    }

    public ValidarToken getToken() {
        return token;
    }

    public void setToken(ValidarToken token) {
        this.token = token;
    }

    public Set<BlocoTO> getBlocos() {
        if (blocos == null) {
            blocos = new HashSet<>();
        }
        return blocos;
    }

    public void setBlocos(Set<BlocoTO> blocos) {
        this.blocos = blocos;
    }

}
