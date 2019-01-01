package br.com.slave.domain;

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
@Table(name = "RV_VOLUME")
public class VolumeTO extends Domain {

    @Column(name = "localizacao", nullable = false)
    private String  localizacao;

    @Column(name = "capacidade", nullable = false)
    private Integer capacidade;

    @Column(name = "tamanho", nullable = false)
    private Integer tamanho;

    @Column(name = "contem", nullable = false)
    private Integer contem;

    @Column(name = "disponibilidade", nullable = false)
    private Boolean disponibilidade;

    public final void incrementar(int read) {
        contem++;
        tamanho += read;
    }

    public final void decrementar(int read) {
        contem--;
        tamanho -= read;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    public Integer getContem() {
        return contem;
    }

    public void setContem(Integer contem) {
        this.contem = contem;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

}
