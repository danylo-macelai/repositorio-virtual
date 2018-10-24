package br.com.slave.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.slave.business.IBloco;

/**
 * <b>Description:</b>  <br>
 * <b>Project:</b> virtual-slave <br>

 * @author macelai
 * @date: 24 de out de 2018
 */
@Component
@Path("/blocos")
public class BlocoResource {

	@Autowired
	private IBloco business;

	@GET
	@Path("/")
	public Response listagem() {
		return Response.status(Response.Status.ACCEPTED).entity(business.carregarTodos()).build();
	}

}
