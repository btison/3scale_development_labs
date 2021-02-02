package com.redhat.coolstore.products.rest;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class LoggingFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Context
    UriInfo info;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {

        if (log.isDebugEnabled()) {
            final String method = context.getMethod();
            final String path = info.getPath();

            log.debug("Request for {} {}", method, path);
            log.debug("Request headers:");
            MultivaluedMap<String, String> rh = context.getHeaders();
            rh.forEach((key, value) -> log.debug("{} = {}", key, value));
        }
    }
}
