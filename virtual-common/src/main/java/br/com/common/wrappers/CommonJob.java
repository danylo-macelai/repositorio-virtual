package br.com.common.wrappers;

import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 24 de abr de 2019
 */
public abstract class CommonJob extends QuartzJobBean {

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
