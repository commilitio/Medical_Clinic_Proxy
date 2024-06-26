package medical_clinic_proxy.error;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import medical_clinic_proxy.exception.InternalServerErrorException;
import medical_clinic_proxy.exception.ObjectNotFoundException;

public class ClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);
        int status = response.status();
        switch (status) {
            case 404:
                return new ObjectNotFoundException(exception.getMessage() != null ? exception.getMessage() : "Object Not Found");
            case 500:
                return new InternalServerErrorException(exception.getMessage() != null ? exception.getMessage() : "Internal Server Error");
            case 503:
                return new RetryableException(
                        status,
                        exception.getMessage(),
                        response.request().httpMethod(),
                        exception,
                        100L,
                        response.request());
            default:
                return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}