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
            notes = "<p>A consulta &eacute; usada para recuperar os <strong>metadados</strong> dos <strong>arquivos</strong> atrav&eacute;s do <strong>nome</strong>, caso seja localizado um ou mais registros ser&aacute; retornado os <strong>metadados</strong> no corpo da mensagem da resposta no formato <strong>json</strong> caso contr&aacute;rio, o status <strong>404</strong> indicando que o arquivo n&atilde;o existe ou n&atilde;o foi localizado.</p>",
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
                    value = "Nome é a identificação do arquivo digital deverá conter a extensão.",
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
            value = "Carrega o arquivo do volume",
            nickname = "leitura",
            notes = "<p>A leitura &eacute; usada para fazer o <strong>download</strong> do <strong>arquivo</strong> no servidor, atrav&eacute;s do <strong>id</strong>. O arquivo ser&aacute; reconstru&iacute;do como os <strong>blocos</strong> que est&atilde;o espalhados entre os servidores <strong>slave</strong> registrados no <strong>service Discovery</strong> se a opera&ccedil;&atilde;o for realizada com sucesso, retorna o <strong>bin&aacute;rio</strong> no corpo da mensagem caso contr&aacute;rio, a mensagem de erro.</p>",
            response = Resource.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nenhum registro foi encontrado para a consulta.")
            }
    )
    public ResponseEntity<Resource> leitura(
            @ApiParam(
                    name = "id",
                    value = "Id é um número utilizado para a identificação do arquivo",
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
            value = "Envia um arquivo para o volume",
            nickname = "gravacao",
            notes = "<p>A grava&ccedil;&atilde;o &eacute; usada para fazer o <strong>upload</strong> do <strong>arquivo</strong> para o volume, que ser&aacute; divido em <strong>blocos</strong> de tamanho fixo pr&eacute;-configurado e enviados aos servidores <strong>slave</strong> para o armazenamento, caso esteja configurado tamb&eacute;m ser&atilde;o replicados em outros servidores registrados no <strong>service discovery</strong>. Se a opera&ccedil;&atilde;o for realizada com sucesso, retorna os <strong>metadados</strong> do arquivo caso contr&aacute;rio, a mensagem de erro</p>",
            response = ArquivoTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "<p>Regras de Neg&oacute;cio:</p> <ul> <li>N&atilde;o existe servi&ccedil;o registrado no service discovery</li> <li>A localiza&ccedil;&atilde;o (<span style=\"color: #555; font-weight: bold;\">C</span>:<span style=\"color: #555;\">/....</span>/<span style=\"color: #555; font-weight: bold;\">m1</span>) do volume &eacute; invalida.</li> </ul>")}
            )
    public ResponseEntity<ArquivoTO> gravacao(
            @ApiParam(
                    name = "file",
                    value = "<p>File &eacute; o bin&aacute;rio que ser&aacute; enviado ao servidor pode ser um arquivo de texto, planilha, livro, v&iacute;deo, m&uacute;sica e etc..</p>",
                    required = true
            )
            @RequestParam("file") MultipartFile file) {
        ArquivoTO arquivo = arquivoBusiness.upload(file);
        return ResponseEntity.ok(arquivo);
    }

    @DeleteMapping(value = "/arquivos/exclusao/{id:\\d+}")
    @ApiOperation(
            value = "Remove o bloco do volume",
            nickname = "exclusao",
            notes = "<p>A exclus&atilde;o &eacute; usada para <strong>remover</strong> o <strong>arquivo</strong> do volume, atrav&eacute;s do <strong>id</strong>. Todos os <strong>blocos</strong> do arquivo ser&atilde;o removidos do volume em seguida os <strong>metadados</strong> se a opera&ccedil;&atilde;o for realizada com sucesso, retorna o status <strong>204</strong> caso contr&aacute;rio, a mensagem de erro.</p>",
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
