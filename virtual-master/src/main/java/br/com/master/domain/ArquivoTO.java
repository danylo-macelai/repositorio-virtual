package br.com.master.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import br.com.common.domain.Domain;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RV_ARQUIVO")
public class ArquivoTO extends Domain {

    @Column(name = "tamanho", updatable = false, nullable = false)
    private Long         tamanho;

    @Column(name = "nome", nullable = false)
    private String       nome;

    @Column(name = "mime_type", updatable = false, nullable = false)
    private String       mimeType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "arquivo")
    @OrderBy("numero ASC")
    private Set<BlocoTO> blocos;

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
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
