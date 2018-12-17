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
    private Long    capacidade;

    @Column(name = "tamanho", nullable = false)
    private Long    tamanho;

    @Column(name = "contem", nullable = false)
    private Integer contem;

    @Column(name = "disponibilidade", nullable = false)
    private Boolean disponibilidade;

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Long getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Long capacidade) {
        this.capacidade = capacidade;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
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
