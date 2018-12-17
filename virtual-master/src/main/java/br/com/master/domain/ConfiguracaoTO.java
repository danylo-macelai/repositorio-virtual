package br.com.master.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.common.domain.Domain;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RV_CONFIGURACAO")
public class ConfiguracaoTO extends Domain {

    @Column(name = "tamanho_bloco", nullable = false)
    private Long    tamanhoBloco;

    @Column(name = "qtde_replicacao", nullable = false)
    private Integer qtdeReplicacao;

    public Long getTamanhoBloco() {
        return tamanhoBloco;
    }

    public void setTamanhoBloco(Long tamanhoBloco) {
        this.tamanhoBloco = tamanhoBloco;
    }

    public Integer getQtdeReplicacao() {
        return qtdeReplicacao;
    }

    public void setQtdeReplicacao(Integer qtdeReplicacao) {
        this.qtdeReplicacao = qtdeReplicacao;
    }

}
