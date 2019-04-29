package br.com.master.domain;

import br.com.common.domain.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RV_CONFIGURACAO")
public class ConfiguracaoTO extends Domain {

    // Tamanho em Quilobyte (kB)
    @Column(name = "tamanho_bloco", nullable = false)
    private Integer tamanhoBloco;

    @Column(name = "qtde_replicacao", nullable = false)
    private Integer qtdeReplicacao;

    public Integer getTamanhoBloco() {
        return tamanhoBloco;
    }

    public void setTamanhoBloco(Integer tamanhoBloco) {
        this.tamanhoBloco = tamanhoBloco;
    }

    public Integer getQtdeReplicacao() {
        return qtdeReplicacao;
    }

    public void setQtdeReplicacao(Integer qtdeReplicacao) {
        this.qtdeReplicacao = qtdeReplicacao;
    }

}
