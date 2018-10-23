package br.com.slave.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.common.domain.Domain;

/**
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 23 de out de 2018
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BLOCO")
public class BlocoTO extends Domain {

    @Column(name = "local")
    private String  local;

    @Column(name = "tamanho")
    private Integer tamanho;

    @Column(name = "disponibilidade")
    private Boolean disponibilidade;

    @Column(name = "hash")
    private String  hash;

    @Column(name = "acesso", nullable = false)
    private Date    acesso;

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getAcesso() {
        return acesso;
    }

    public void setAcesso(Date acesso) {
        this.acesso = acesso;
    }

}
