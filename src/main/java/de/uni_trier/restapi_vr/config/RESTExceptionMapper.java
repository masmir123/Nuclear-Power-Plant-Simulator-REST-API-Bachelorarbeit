package de.uni_trier.restapi_vr.config;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class RESTExceptionMapper implements ExceptionMapper<Exception> {

    private static final Map<Class<? extends Exception>, Response.Status> exceptionStatus = Map.of(
            IllegalArgumentException.class, Response.Status.BAD_REQUEST,
            BadRequestException.class, Response.Status.BAD_REQUEST,
            RuntimeException.class, Response.Status.INTERNAL_SERVER_ERROR
    );

    private class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }

    @Override
    public Response toResponse(Exception e) {
        ErrorResponse error = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
        Response.Status status = exceptionStatus.getOrDefault(e.getClass(), Response.Status.INTERNAL_SERVER_ERROR);
        return Response.status(status).entity(error).build();
    }
}