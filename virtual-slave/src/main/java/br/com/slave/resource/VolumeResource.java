package br.com.slave.resource;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.slave.business.IVolume;
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
 * <b>Description:</b>  <br>
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
@Path("/volumes")
public class VolumeResource {

    @Autowired
    private IVolume business;

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
    @Produces({"application/json"})
    @ApiOperation(
            value = "Consulta os volume",
            nickname = "consulta",
            notes = "Retorna um item de VolumeTO",
            response = VolumeTO.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Volume localizado com sucesso", response = VolumeTO.class),
            @ApiResponse(code = 404, message = "Volume não existe ou não foi localizado")})
    public Response consulta() {
        VolumeTO volume = business.buscar();;
        if (volume == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(volume).build();
    }

}