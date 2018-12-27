package br.com.master.resource;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.master.business.IArquivo;
import br.com.master.configuration.MasterException;
import br.com.master.domain.ArquivoTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-master <br>
 *
 * @author macelai
 * @date: 18 de nov de 2018
 */
@RestController()
@Api(tags = { "Arquivos" })
public class ArquivoResource {

    @Autowired
    IArquivo arquivoBusiness;

    @GetMapping(value = "/arquivos/{nome}")
    @ApiOperation(
            value = "Consulta os metadados do arquivo",
            nickname = "consulta",
            notes = "Retorna uma lista de metadados de arquivos que possuem o nome informado",
            response = List.class,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum arquivo foi localizado!")
            }
    )
    public ResponseEntity<List<ArquivoTO>> consulta(
            @ApiParam(
                    name = "nome",
                    value = "Nome é a identificação do arquivo digital deverá conter a extensão Exemplo: remessa.zip, remessa.xls, remessa.png, remessa.avi, etc.",
                    example = "remessa.txt",
                    required = true
            )
            @PathVariable String nome) {
        List<ArquivoTO> arquivos = arquivoBusiness.carregarPor(nome);
        if (arquivos.isEmpty()) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }
        return ResponseEntity.ok(arquivos);
    }


    @GetMapping(value = "/arquivos/leitura/{id:\\d+}")
    @ApiOperation(
            value = "Transfere o arquivo para o computador local",
            nickname = "leitura",
            notes = "Retorna uma cópia do arquivo que está no servidor Master para um computador local",
            response = Resource.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum arquivo foi localizado!")
            }
    )
    public ResponseEntity<Resource> leitura(
            @ApiParam(
                    name = "id",
                    value = "Id é um número utilizado para a identificação do arquivo que será transferido",
                    example = "1234",
                    required = true
            )
            @PathVariable("id") long id) {
        ArquivoTO arquivo = arquivoBusiness.ache(id);
        if (arquivo == null) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }

        InputStreamResource stream = arquivoBusiness.download(arquivo);

        return ResponseEntity.ok()
                .contentLength(arquivo.getTamanho())
                .contentType(MediaType.parseMediaType(arquivo.getMimeType()))
                .body(stream);
    }

    @PostMapping("/arquivos/gravacao")
    @ApiOperation(
            value = "Envia o arquivo para o servidor remoto",
            nickname = "gravacao",
            notes = "Retorna os metadados de arquivos que foi enviado ao servidor Master",
            response = ArquivoTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArquivoTO> gravacao(
            @ApiParam(
                    name = "file",
                    value = "file é o arquivo que será enviado ao servidor",
                    required = true
            )
            @RequestParam("file") MultipartFile file) {
        ArquivoTO arquivo = arquivoBusiness.upload(file);
        return ResponseEntity.ok(arquivo);
    }

    @DeleteMapping(value = "/arquivos/exclusao/{id:\\d+}")
    @ApiOperation(
            value = "Remove o arquivo do servidor remoto",
            nickname = "exclusao",
            notes = "Retorna o status 204 indicando que a exclusão foi bem sucedida",
            response = Response.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum arquivo foi localizado!")
            }
    )
    public ResponseEntity<Response> exclusao(
            @ApiParam(
                    name = "id",
                    value = "Id é um número utilizado para a identificação do arquivo que será removido",
                    example = "1234",
                    required = true
            )
            @PathVariable("id") long id) {

        ArquivoTO arquivo = arquivoBusiness.ache(id);
        if (arquivo == null) {
            throw new MasterException("no.result.exception").status(Status.NOT_FOUND);
        }

        arquivoBusiness.excluir(arquivo);

        return ResponseEntity.noContent().build();
    }

}
