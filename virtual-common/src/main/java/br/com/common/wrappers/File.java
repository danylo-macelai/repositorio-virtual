package br.com.common.wrappers;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 17 de dez de 2018
 */
@SuppressWarnings("serial")
@Embeddable
public class File implements Serializable {

    @Column(name = "uuid", updatable = false, nullable = false)
    private String  uuid;

    @Column(name = "tamanho", updatable = false, nullable = false)
    private Integer tamanho;

    @Column(name = "instance_id", updatable = false, nullable = false)
    private String  instanceId;

    @Column(name = "replica", updatable = false, nullable = false)
    private Boolean replica;

    public File() {

    }

    public File(String uuid, Integer tamanho, String instanceId) {
        this(uuid, tamanho, instanceId, false);
    }

    public File(String uuid, Integer tamanho, String instanceId, Boolean replica) {
        this.uuid = uuid;
        this.tamanho = tamanho;
        this.instanceId = instanceId;
        this.replica = replica;
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

    public Boolean getReplica() {
        return replica;
    }

    public void setReplica(Boolean replica) {
        this.replica = replica;
    }

}
