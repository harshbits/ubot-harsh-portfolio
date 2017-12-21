package com.harshbits.ubot.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) throws IOException {

		logRequest(request, body);

		ClientHttpResponse response = execution.execute(request, body);
		final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
		logResponse(responseCopy);
		return responseCopy;
	}

	private void logRequest(final HttpRequest request, final byte[] body) {

		final StringBuilder logRequest = new StringBuilder("Outgoing REST Request - ");
		
		logRequest.append("\n[HTTP METHOD:").append(request.getMethod().toString());
		logRequest.append("]\n[URI:").append(request.getURI().toString());
		if (request.getHeaders().getContentType() == MediaType.APPLICATION_JSON
				|| request.getHeaders().getContentType() == MediaType.APPLICATION_JSON_UTF8) {
			logRequest.append("]\n[REQUEST BODY:").append(new String(body)).append("]");	
		}else {
			logRequest.append("]\n[REQUEST BODY: NOT JSON or JSON/UTF-8 Type]");	
		}
		log.info(logRequest.toString());
	}

	private void logResponse(ClientHttpResponse response) throws IOException {

		log.info("Incoming Response: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
	}

}