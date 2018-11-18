package br.com.master.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "BLOCO")
public class BlocoTO extends Domain {

    @Column(name = "uuid", updatable = false, nullable = false)
    private String    uuid;

    @Column(name = "numero", updatable = false, nullable = false)
    private Integer   numero;

    @Column(name = "host", updatable = false, nullable = false)
    private String    host;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_arquivo", updatable = false, nullable = false)
    private ArquivoTO arquivo;

    public BlocoTO(ArquivoTO arquivo, String host, Integer numero, String uuid) {
        this.arquivo = arquivo;
        this.host = host;
        this.numero = numero;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ArquivoTO getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoTO arquivo) {
        this.arquivo = arquivo;
    }

}
