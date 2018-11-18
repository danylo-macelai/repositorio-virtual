package br.com.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <b>Project:</b> virtual-common <br>
 *
 * @author macelai
 * @date: 22 de out de 2018
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class Domain implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private Long          id;

    @JsonIgnore
    @Column(name = "versao", nullable = false)
    private long          versao;

    @JsonIgnore
    @Column(name = "criacao", insertable = true, updatable = false, nullable = false)
    private LocalDateTime criacao;

    @JsonIgnore
    @Column(name = "alteracao", updatable = true, nullable = false)
    private LocalDateTime alteracao;

    public Domain() {

    }

    public Domain(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersao() {
        return versao;
    }

    public void setVersao(long versao) {
        this.versao = versao;
    }

    public LocalDateTime getCriacao() {
        return criacao;
    }

    public void setCriacao(LocalDateTime criacao) {
        this.criacao = criacao;
    }

    public LocalDateTime getAlteracao() {
        return alteracao;
    }

    public void setAlteracao(LocalDateTime alteracao) {
        this.alteracao = alteracao;
    }

    @PrePersist
    public void prePersist() {
        setCriacao(LocalDateTime.now());
        setAlteracao(LocalDateTime.now());
        setVersao(1);
    }

    @PreUpdate
    public void preUpdate() {
        setVersao(getVersao() + 1);
        setAlteracao(LocalDateTime.now());
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public final boolean equals(Object other) {
        if ((other == null) || !this.getClass().equals(other.getClass())) {
            return false;
        }
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(id, ((Domain) other).id);
        return builder.isEquals();
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public Domain clone() throws CloneNotSupportedException {
        return (Domain) ObjectUtils.cloneIfPossible(this);
    }

}
