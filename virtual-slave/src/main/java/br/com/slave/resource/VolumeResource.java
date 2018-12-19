package br.com.slave.resource;

import static br.com.slave.resource.VolumeResource.RESOURCE_ROOT_URL;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wso2.msf4j.Request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;

import br.com.common.wrappers.File;
import br.com.common.wrappers.PatchForm;
import br.com.slave.business.IVolume;
import br.com.slave.configuration.SlaveException;
import br.com.slave.domain.VolumeTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

/**
 * <b>Description:</b> <br>
 * <b>Project:</b> virtual-slave <br>
 *
 * @author macelai
 * @date: 24 de out de 2018
 */
@Api(value = "volumes")
@SwaggerDefinition(
        info = @Info(
                title = "Volumes RESTful Web Services",
                version = "1.0",
                description = "Manual de Integração",
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(
                        name = "Danylo Macelai",
                        email = "danylomacelai@gmail.com",
                        url = "http://danylomacelai.com")
                )
        )
@Component
@Path(RESOURCE_ROOT_URL)
public class VolumeResource {

    public static final String RESOURCE_ROOT_URL   = "/volumes";

    @Autowired
    private IVolume business;

    @GET
    @Path("/healthCheck")
    public Response healthCheck() {
        return Response.status(200)
                .entity("alive")
                .build();
    }

    @GET
    @Path("/status")
    public Response status() {
        return Response.status(200)
                .build();
    }

    @OPTIONS
    @Path("/")
    public Response options() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With")
                .build();
    }

    @GET
    @Path("/")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Consulta os volume",
            nickname = "consulta",
            notes = "Retorna um item de VolumeTO",
            response = VolumeTO.class,
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Volume localizado com sucesso", response = VolumeTO.class),
            @ApiResponse(code = 404, message = "Volume não existe ou não foi localizado")})
    public Response consulta() {
        VolumeTO volume = business.buscar();
        if (volume == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(volume).build();
    }

    @PUT
    @Path("/{id:\\d+}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Altera o volume",
            nickname = "edicao",
            notes = "Altera as informações do VolumeTO",
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Volume alterado com sucesso", response = VolumeTO.class),
            @ApiResponse(code = 404, message = "Volume não existe ou não foi localizado")})
    public Response edicao(@PathParam("id") final int id, PatchForm[] patchs) {
        VolumeTO volume = business.ache(id);
        if (volume == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patch = mapper.valueToTree(patchs);
        JsonNode domain = mapper.valueToTree(volume);
        JsonNode merge = JsonPatch.apply(patch, domain);
        try {
            volume = mapper.readerForUpdating(volume).readValue(merge);
        } catch (IOException e) {
            throw new SlaveException("volume.preencher.erro");
        }
        business.alterar(volume);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/upload")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Upload de Blocos",
            nickname = "upload",
            notes = "Realiza o upload dos blocos",
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Upload realizado com sucesso", response = String.class),
            @ApiResponse(code = 409, message = "O identificador gerado já existe")})
    public Response upload(@Context Request request) {
        File file = business.upload(request);
        return Response.status(Response.Status.OK).entity(file).build();
    }

    @GET
    @Path("/download/{uuid}")
    @ApiOperation(
            value = "Download de Bloco",
            nickname = "download",
            notes = "Realiza o download dos blocos",
            produces = MediaType.TEXT_HTML)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Download realizado com sucesso", response = String.class)})
    public Response download(@PathParam("uuid") String uuid) {
        StreamingOutput stream = business.download(uuid);
        return Response.status(Response.Status.OK).entity(stream).build();
    }

    @POST
    @Path("/replicacao/{uuid}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(
            value = "Replicação de Bloco",
            nickname = "download",
            notes = "Cria uma cópia do bloco",
            produces = MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Replicação realizada com sucesso", response = String.class)})
    public Response replicacao(@PathParam("uuid") String uuid) throws Exception {
        File file = business.replicacao(uuid);
        return Response.status(Response.Status.OK).entity(file).build();
    }

}
