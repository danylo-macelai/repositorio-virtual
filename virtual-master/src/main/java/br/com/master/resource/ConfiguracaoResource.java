package br.com.master.resource;

import br.com.common.wrappers.PatchForm;

import br.com.master.business.IConfiguracao;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ConfiguracaoTO;

import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <b>Description:</b> FIXME: Document this type <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 * @version $
 */
@RestController()
@Api(tags = { "Configuração" })
public class ConfiguracaoResource {

    @Autowired
    IConfiguracao business;

    @ApiOperation(value = "Consulta os metadados da configuração", nickname = "consulta", notes = "<p>A consulta é usada para recuperar os <strong>metadados</strong> da <strong>configuração</strong>. Se a <strong>configuração</strong> existir os <strong>metadados</strong> serão enviados no corpo da mensagem de resposta no formato <strong>json</strong> caso contrário, o status <strong>404</strong> indicando que a configuração não existe ou não foi localizado.</p>", response = ConfiguracaoTO.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum registro foi encontrado para a consulta.")
    })
    @GetMapping(value = "/configuracoes")
    public ResponseEntity<ConfiguracaoTO> consulta() {
        final ConfiguracaoTO configuracao = business.buscar();
        if (configuracao == null) {
            throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
        }
        return ResponseEntity.ok(configuracao);
    }

    @ApiOperation(value = "Altera os metadados da configuração", nickname = "alteracao", notes = "<p>A alteração é usada para manter os <strong>metadados</strong> da única configuração cadastrada. Se a operação for realizada com sucesso, o status <strong>204</strong> será retornado caso contrário, a mensagem de erro.</p>", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "<p>Regras de Negócio</p> <ul> <li>Nenhum registro foi encontrado para a consulta,</li> <li>Nao foi possível preencher a configuração.</li> </ul>") })
    @PatchMapping(value = "/configuracoes")
    public ResponseEntity<?> alteracao(
            @ApiParam(name = "patchs", value = "<p>Lista de objetos que contém as informações necessárias para a alteração das propriedades</p> <ul> <li><strong>OP:</strong> deverá ser informado o tipo de operação realizada: <ul> <li><em>ADD</em> para incluir um valor</li> <li><em>REMOVE</em> para excluir o valor</li> <li><em>REPLACE</em> para alterar o valor</li> </ul> </li> <li><strong>PATH:</strong> nome da propriedade a ser alterada;</li> <li><strong>VALUE:</strong> o valor que será atribuído.</li> </ul>", required = true) @RequestBody PatchForm[] patchs) {
        ConfiguracaoTO configuracao = business.buscar();
        if (configuracao == null) {
            throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
        }
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode patch = mapper.valueToTree(patchs);
        final JsonNode domain = mapper.valueToTree(configuracao);
        try {
            final JsonNode merge = JsonPatch.apply(patch, domain);
            configuracao = mapper.readerForUpdating(configuracao).readValue(merge);
        } catch (final Exception e) {
            throw new MasterException("slave.preencher.patchs.obj", e);
        }
        business.alterar(configuracao);
        return ResponseEntity.noContent().build();
    }

}
