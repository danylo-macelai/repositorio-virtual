package br.com.master.business;

import br.com.common.access.property.ValidarToken;
import br.com.common.business.IBusiness;
import br.com.common.domain.Domain;

import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.wrappers.SearchTab;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
public interface IArquivo extends IBusiness<ArquivoTO> {

    /**
     * Consulta o arquivo por nome
     *
     * @param nome - Nome do arquivo
     * @param searchTab - Grupo do arquivo
     *
     * @return List<ArquivoTO>
     * @throws MasterException
     */
    List<ArquivoTO> carregarPor(String nome, SearchTab searchTab) throws MasterException;

    /**
     * Consulta o arquivo por nome
     *
     * @param nome - Nome do arquivo
     * @param searchTab - Grupo do arquivo
     * @param page - Pagina inicial da consulta
     *
     * @return List<ArquivoTO>
     * @throws MasterException
     */
    Page<ArquivoTO> carregarPor(String nome, SearchTab searchTab, Integer page) throws MasterException;

    /**
     * Realiza o upload e grava o arquivo enviado
     *
     * @param access
     * @param file
     */
    ArquivoTO gravar(ValidarToken access, MultipartFile file) throws MasterException;

    /**
     * Realiza o download do arquivo enviado
     *
     * @param arquivo
     * @return InputStreamResource
     * @throws MasterException
     */
    InputStreamResource ler(ArquivoTO arquivo) throws MasterException;

    /**
     * O método {@link IBusiness#excluir(Domain)} exclui apenas o registro informado.
     *
     * @param access
     * @param id
     * @throws MasterException
     */
    void excluir(ValidarToken access, long id) throws MasterException;

    /**
     * Consulta os arquivos do usuario
     *
     * @apiNote $
     *
     * @param access
     * @param page
     */
    Page<ArquivoTO> carregarPor(ValidarToken access, Integer page);
}
