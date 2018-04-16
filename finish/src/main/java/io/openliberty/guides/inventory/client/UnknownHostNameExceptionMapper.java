// tag::mapper[]
package io.openliberty.guides.inventory.client;

import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Provider
public class UnknownHostNameExceptionMapper implements ResponseExceptionMapper<UnknownHostNameException> {
    Logger LOG = Logger.getLogger(UnknownHostNameExceptionMapper.class.getName());

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        LOG.info("status = " + status);
        return status == 404; //Not Found
    }

    @Override
    public UnknownHostNameException toThrowable(Response response) {
        return new UnknownHostNameException();
    }
}
// end::mapper[]
