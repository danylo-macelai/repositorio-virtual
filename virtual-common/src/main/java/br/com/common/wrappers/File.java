package br.com.common.wrappers;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 17 de dez de 2018
 */
@Embeddable
public class File {

    @Column(name = "uuid", updatable = false, nullable = false)
    private String uuid;

    @Column(name = "host", updatable = false, nullable = false)
    private String host;

    public File() {

    }

    public File(String uuid, String host) {
        this.uuid = uuid;
        this.host = host;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
