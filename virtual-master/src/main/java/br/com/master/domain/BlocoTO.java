package br.com.master.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.common.domain.Domain;
import br.com.common.wrappers.File;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 16 de nov de 2018
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RV_BLOCO")
public class BlocoTO extends Domain {

    @Column(name = "numero", updatable = false, nullable = false)
    private Integer   numero;

    @Embedded
    private File      file;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_arquivo", updatable = false, nullable = false)
    private ArquivoTO arquivo;

    public BlocoTO() {

    }

    public BlocoTO(Integer numero, File file, ArquivoTO arquivo) {
        this.numero = numero;
        this.file = file;
        this.arquivo = arquivo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArquivoTO getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoTO arquivo) {
        this.arquivo = arquivo;
    }

}
