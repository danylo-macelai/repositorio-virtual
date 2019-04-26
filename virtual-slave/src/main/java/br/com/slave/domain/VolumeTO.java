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

    // Tamanho em Megabytes (MB)
    @Column(name = "capacidade", nullable = false)
    private Long    capacidade;

    // Tamanho em Megabytes (MB)
    @Column(name = "tamanho", nullable = false)
    private Long    tamanho;

    @Column(name = "contem", nullable = false)
    private Long    contem;

    @Column(name = "disponibilidade", nullable = false)
    private Boolean disponibilidade;

    public final void reset(long count, long size) {
        contem = count;
        tamanho = ((size / 1024) / 1024);
    }

    public final void incrementar(int bytes) {
        contem++;
        // Converter Bytes em Megabytes (byte → MB)
        tamanho += ((bytes / 1024) / 1024);
    }

    public final void decrementar(int bytes) {
        contem--;
        // Converter Bytes em Megabytes (byte → MB)
        tamanho -= ((bytes / 1024) / 1024);
    }

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

    public Long getContem() {
        return contem;
    }

    public void setContem(Long contem) {
        this.contem = contem;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

}
