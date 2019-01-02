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
            notes = "<p>A consulta é usada para recuperar os <strong>metadados</strong> dos <strong>arquivos</strong> através do <strong>nome</strong>, caso seja localizado um ou mais registros será retornado os <strong>metadados</strong> no corpo da mensagem da resposta no formato <strong>json</strong> caso contrário, o status <strong>404</strong> indicando que o arquivo não existe ou não foi localizado.</p>",
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
            throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
        }
        return ResponseEntity.ok(arquivos);
    }


    @GetMapping(value = "/arquivos/leitura/{id:\\d+}")
    @ApiOperation(
            value = "Carrega o arquivo do volume",
            nickname = "leitura",
            notes = "<p>A leitura é usada para fazer o <strong>download</strong> do <strong>arquivo</strong> no servidor, através do <strong>id</strong>. O arquivo será reconstruído como os <strong>blocos</strong> que estão espalhados entre os servidores <strong>slave</strong> registrados no <strong>service Discovery</strong> se a operação for realizada com sucesso, retorna o <strong>binário</strong> no corpo da mensagem caso contrário, a mensagem de erro.</p>",
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
            throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
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
            notes = "<p>A gravação é usada para fazer o <strong>upload</strong> do <strong>arquivo</strong> para o volume, que será divido em <strong>blocos</strong> de tamanho fixo pré-configurado e enviados aos servidores <strong>slave</strong> para o armazenamento, caso esteja configurado também serão replicados em outros servidores registrados no <strong>service discovery</strong>. Se a operação for realizada com sucesso, retorna os <strong>metadados</strong> do arquivo caso contrário, a mensagem de erro</p>",
            response = ArquivoTO.class,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "<p>Regras de Negócio:</p> <ul> <li>Não existe serviço registrado no service discovery</li> <li>A localização (C:/.../.../m1) do volume é invalida.</li> </ul>")}
            )
    public ResponseEntity<ArquivoTO> gravacao(
            @ApiParam(
                    name = "file",
                    value = "<p>File é o binário que será enviado ao servidor pode ser um arquivo de texto, planilha, livro, vídeo, música e etc..</p>",
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
            notes = "<p>A exclusão é usada para <strong>remover</strong> o <strong>arquivo</strong> do volume, através do <strong>id</strong>. Todos os <strong>blocos</strong> do arquivo serão removidos do volume em seguida os <strong>metadados</strong> se a operação for realizada com sucesso, retorna o status <strong>204</strong> caso contrário, a mensagem de erro.</p>",
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
            throw new MasterException("slave.obj.nao.localizado").status(Status.NOT_FOUND);
        }

        arquivoBusiness.excluir(arquivo);

        return ResponseEntity.noContent().build();
    }

}
