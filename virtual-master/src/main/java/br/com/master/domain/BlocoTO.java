package br.com.master.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(name = "RV_BLOCO", uniqueConstraints = { @UniqueConstraint(columnNames = { "uuid", "instance_id" }) })
public class BlocoTO extends Domain {

    @Column(name = "numero", updatable = false, nullable = false)
    private Integer   numero;

    @Column(name = "uuid", updatable = false, nullable = false)
    private String    uuid;

    @Column(name = "tamanho", updatable = false, nullable = false)
    private Integer   tamanho;

    @Column(name = "instance_id")
    private String    instanceId;

    @Column(name = "dir_off_line", updatable = false, nullable = false)
    private String    diretorioOffLine;

    @Column(name = "replica", updatable = false, nullable = false)
    private Boolean   replica;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_arquivo", updatable = false, nullable = false)
    private ArquivoTO arquivo;

    public BlocoTO() {

    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDiretorioOffLine() {
        return diretorioOffLine;
    }

    public void setDiretorioOffLine(String diretorioOffLine) {
        this.diretorioOffLine = diretorioOffLine;
    }

    public Boolean getReplica() {
        return replica;
    }

    public void setReplica(Boolean replica) {
        this.replica = replica;
    }

    public ArquivoTO getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoTO arquivo) {
        this.arquivo = arquivo;
    }

}
