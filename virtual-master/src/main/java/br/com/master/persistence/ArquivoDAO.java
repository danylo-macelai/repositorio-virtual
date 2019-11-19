package br.com.master.persistence;

import br.com.common.access.property.ValidarToken;

import br.com.master.domain.ArquivoTO;
import br.com.master.wrappers.SearchTab;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
public interface ArquivoDAO extends JpaRepository<ArquivoTO, Long> {

    /**
     * Consulta o arquivo por nome
     *
     * @param nome - Nome do arquivo
     * @return List<ArquivoTO>
     */
    List<ArquivoTO> findAllByNomeIgnoreCaseContaining(String nome);

    /**
     * Consulta o arquivo por nome
     *
     * @param nome - Nome do arquivo
     * @param pageable - Configuracao de pagina e quantidade de itens para retorno
     * @return Page<ArquivoTO>
     */
    Page<ArquivoTO> findAllByNomeIgnoreCaseContaining(String nome, Pageable pageable);

    /**
     * Consulta o arquivo por nome
     *
     * @param tab - Grupo do arquivo
     * @param nome - Nome do arquivo
     * @return List<ArquivoTO>
     */
    List<ArquivoTO> findAllBySearchTabAndNomeIgnoreCaseContaining(SearchTab tab, String nome);

    /**
     * Consulta o arquivo por nome
     *
     * @param tab - Grupo do arquivo
     * @param nome - Nome do arquivo
     * @param pageable - Configuracao de pagina e quantidade de itens para retorno
     * @return Page<ArquivoTO>
     */
    Page<ArquivoTO> findAllBySearchTabAndNomeIgnoreCaseContaining(SearchTab tab, String nome, Pageable pageable);

    /**
     * Consulta os arquivos do usuario
     *
     * @apiNote $
     *
     * @param token
     * @return Page<ArquivoTO>
     */
    Page<ArquivoTO> findAllByToken(ValidarToken token, Pageable pageable);

}
