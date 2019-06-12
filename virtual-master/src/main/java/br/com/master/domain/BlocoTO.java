package br.com.master.domain;

import br.com.common.domain.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(name = "RV_BLOCO", uniqueConstraints = {
        @UniqueConstraint(name = "UNIQ_UUID__INSTANCE_ID_IN_RV_BLOCO", columnNames = { "uuid", "instance_id" })
}, indexes = {
        @Index(name = "IDX_UUID_IN_RV_BLOCO", columnList = "uuid", unique = true),
        @Index(name = "IDX_INSTANCE_ID_IN_RV_BLOCO", columnList = "instance_id", unique = true)
})
public class BlocoTO extends Domain {

    @Column(name = "numero", updatable = false, nullable = false)
    private Integer numero;

    @Column(name = "uuid", updatable = false, nullable = false)
    private String uuid;

    @Column(name = "tamanho", updatable = false, nullable = false)
    private Integer tamanho;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "dir_off_line", updatable = false, nullable = false)
    private String diretorioOffLine;

    @Column(name = "replica", updatable = false, nullable = false)
    private Boolean replica;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "arquivo_id", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "FKEY_ARQUIVO_ID_IN_RV_BLOCO"))
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
