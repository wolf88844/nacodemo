package com.example.auth2.config.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;

/**
 * @ClassName MssWebResponseExceptionTranslator
 * @Author LIUHANPENG
 * @Date 2020/1/2 0002 15:59
 **/
public class MssWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase = (Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class,causeChain);
        if(ase != null){
            return handleOAuth2Exception((OAuth2Exception)ase);
        }
        ase = (Exception) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,causeChain);
        if(ase!=null){
            return handleOAuth2Exception(new MssWebResponseExceptionTranslator.UnauthorizedException(e.getMessage(),e));
        }

        ase = (AccessDeniedException)throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class,causeChain);
        if(ase instanceof AccessDeniedException){
            return handleOAuth2Exception(new MssWebResponseExceptionTranslator.ForbiddenException(ase.getMessage(),ase));
        }

        ase = (HttpRequestMethodNotSupportedException)throwableAnalyzer.getFirstThrowableOfType(HttpRequestMethodNotSupportedException.class,causeChain);
        if(ase instanceof HttpRequestMethodNotSupportedException){
            return handleOAuth2Exception(new MssWebResponseExceptionTranslator.MethodNotAllowed(ase.getMessage(),ase));
        }

        return handleOAuth2Exception(new MssWebResponseExceptionTranslator.ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),e));
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e){
        int status = e.getHttpErrorCode();
        String error = e.getOAuth2ErrorCode();
        String summy = e.getSummary();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cache-Control","no-store");
        httpHeaders.set("Pragma","no-cache");
        if(status== HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)){
            httpHeaders.set("WWW-Authenticate",String.format("%s,%s", OAuth2AccessToken.BEARER_TYPE,e.getSummary()));
        }
        ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(e,httpHeaders,HttpStatus.valueOf(status));
        return response;
    }

    public void setThrowableAnalyzer(ThrowableAnalyzer throwableAnalyzer){
        this.throwableAnalyzer = throwableAnalyzer;
    }

    @SuppressWarnings("serial")
    private static class ForbiddenException extends OAuth2Exception{
        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "access_denied";
        }

        @Override
        public int getHttpErrorCode() {
            return 403;
        }
    }

    @SuppressWarnings("serial")
    private static class ServerErrorException extends OAuth2Exception{
        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }

        @Override
        public int getHttpErrorCode() {
            return 500;
        }
    }

    @SuppressWarnings("serial")
    private static class UnauthorizedException extends OAuth2Exception {

        public UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "unauthorized";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }

    }

    @SuppressWarnings("serial")
    private static class MethodNotAllowed extends OAuth2Exception {

        public MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "method_not_allowed";
        }

        @Override
        public int getHttpErrorCode() {
            return 405;
        }

    }
}
