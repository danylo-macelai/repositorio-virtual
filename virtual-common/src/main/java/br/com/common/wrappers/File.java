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

    @Column(name = "host", updatable = false, nullable = false)
    private String  host;

    public File() {

    }

    public File(String uuid, Integer tamanho, String host) {
        this.uuid = uuid;
        this.tamanho = tamanho;
        this.host = host;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
