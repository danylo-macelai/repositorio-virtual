package br.com.master.wrappers;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 9 de abr de 2019
 * @version $
 */
@SuppressWarnings("serial")
public class Slave implements Serializable {

    private int    contem;
    private String instanceId;

    public Slave() {

    }

    public Slave usage() {
        contem++;
        return this;
    }

    public int getContem() {
        return contem;
    }

    public void setContem(int contem) {
        this.contem = contem;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public final boolean equals(Object other) {
        if ((other == null) || !this.getClass().equals(other.getClass())) {
            return false;
        }
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(instanceId, ((Slave) other).getInstanceId());
        return builder.isEquals();
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
        return result;
    }
}
