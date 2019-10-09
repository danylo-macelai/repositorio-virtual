package br.com.master.business;

import br.com.common.access.property.ValidarToken;
import br.com.common.business.IBusiness;
import br.com.common.domain.Domain;

import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import br.com.master.wrappers.SearchTab;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
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
}
