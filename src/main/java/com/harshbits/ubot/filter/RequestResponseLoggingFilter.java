package com.harshbits.ubot.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.log4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link javax.servlet.http.HttpServletRequest} wrapper that caches all content
 * read from the {@linkplain #getInputStream() input stream} and
 * {@linkplain #getReader() reader}, and allows this content to be retrieved via
 * a {@link #getContentAsByteArray() byte array}.
 * <p>
 * 
 * @author harshbhavsar
 * @since 1.0.0
 *        <p>
 * @see ContentCachingRequestWrapper
 * @see ContentCachingResponseWrapper
 * @see Filter
 */
@Slf4j
public class RequestResponseLoggingFilter implements Filter {

	private List<String> ignoreResponseMethods;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		String ignoreMethods = filterConfig.getInitParameter("ignoreResponseMethods");
		if (!StringUtils.isEmpty(ignoreMethods)) {
			
			ignoreResponseMethods = Arrays.asList(ignoreMethods.split(",")).stream().map(String::trim)
					.collect(Collectors.toList());
		} else {
			ignoreResponseMethods = new ArrayList<>();
		}
	}
	
	/**
	 * <p>
	 * Default override method from {@link Filter} interface to process
	 * {@link HttpServletRequest} and {@link HttpServletResponse}.
	 * <p>
	 * 
	 * @see #getRequestData
	 * @see #getResponseData
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		
		if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			
			// Setting request headers to logs
			setRequestHeadersToMDC(httpServletRequest);
			
			Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
			BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);
			BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);

			final StringBuilder logRequest = new StringBuilder("Incoming REST Request - ");
			logRequest.append("\n[PATH:").append(httpServletRequest.getRequestURI());
			logRequest.append("]\n[HTTP METHOD:").append(httpServletRequest.getMethod());
			logRequest.append("]\n[HEADERS:").append(getRequestHeaders(httpServletRequest));
			logRequest.append("]\n[REQUEST PARAMETERS:").append(requestMap);
			logRequest.append("]\n[REQUEST BODY:").append(bufferedReqest.getRequestBody()).append("]");
			log.info(logRequest.toString());

			chain.doFilter(bufferedReqest, bufferedResponse);
			if(!ignoreResponseMethods.contains(httpServletRequest.getMethod())){
				final StringBuilder logResponse = new StringBuilder("Outgoing REST Response -")
						.append(bufferedResponse.getContent());
				log.info(logResponse.toString());
			}
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	@Override
	public void destroy() {
	}
	
	private void setRequestHeadersToMDC(HttpServletRequest httpServletRequest) {
		MDC.put("CORRELATIONID", httpServletRequest.getHeader("correlationid"));
		MDC.put("COUNTRY", httpServletRequest.getHeader("country"));
		MDC.put("SOURCE", httpServletRequest.getHeader("source"));
	}
	
	private String getRequestHeaders(HttpServletRequest httpServletRequest){
		
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		StringBuffer headerData = new StringBuffer();
		headerData.append("{");
		while(headerNames.hasMoreElements()){
			
			String headerName = headerNames.nextElement();
			headerData.append(headerName);
			headerData.append(":");
			headerData.append(httpServletRequest.getHeader(headerName));
			headerData.append(",");
		}
		headerData.append("}");
		return headerData.toString();
	}

	private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements()) {
			String requestParamName = (String) requestParamNames.nextElement();
			String requestParamValue = request.getParameter(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}
		return typesafeRequestMap;
	}

	private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

		private ByteArrayInputStream bais = null;
		private ByteArrayOutputStream baos = null;
		private BufferedServletInputStream bsis = null;
		private byte[] buffer = null;

		public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
			super(req);
			// Read InputStream and store its content in a buffer.
			InputStream is = req.getInputStream();
			this.baos = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int letti;
			while ((letti = is.read(buf)) > 0) {
				this.baos.write(buf, 0, letti);
			}
			this.buffer = this.baos.toByteArray();
		}

		@Override
		public ServletInputStream getInputStream() {
			this.bais = new ByteArrayInputStream(this.buffer);
			this.bsis = new BufferedServletInputStream(this.bais);
			return this.bsis;
		}

		String getRequestBody() throws IOException {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
			String line = null;
			StringBuilder inputBuffer = new StringBuilder();
			do {
				line = reader.readLine();
				if (null != line) {
					inputBuffer.append(line.trim());
				}
			} while (line != null);
			reader.close();
			return inputBuffer.toString().trim();
		}

	}

	private static final class BufferedServletInputStream extends ServletInputStream {

		private ByteArrayInputStream bais;

		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		@Override
		public int available() {
			return this.bais.available();
		}

		@Override
		public int read() {
			return this.bais.read();
		}

		@Override
		public int read(byte[] buf, int off, int len) {
			return this.bais.read(buf, off, len);
		}

		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			// TODO Auto-generated method stub

		}

	}

	public class TeeServletOutputStream extends ServletOutputStream {

		private final TeeOutputStream targetStream;

		public TeeServletOutputStream(OutputStream one, OutputStream two) {
			targetStream = new TeeOutputStream(one, two);
		}

		@Override
		public void write(int arg0) throws IOException {
			this.targetStream.write(arg0);
		}

		public void flush() throws IOException {
			super.flush();
			this.targetStream.flush();
		}

		public void close() throws IOException {
			super.close();
			this.targetStream.close();
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setWriteListener(WriteListener listener) {
			// TODO Auto-generated method stub

		}
	}

	public class BufferedResponseWrapper extends HttpServletResponseWrapper {

		TeeServletOutputStream tee;
		ByteArrayOutputStream bos;

		public BufferedResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		public String getContent() {
			if (bos != null)
				return bos.toString();
			return "";
		}

		public ServletOutputStream getOutputStream() throws IOException {
			if (tee == null) {
				bos = new ByteArrayOutputStream();
				tee = new TeeServletOutputStream(super.getOutputStream(), bos);
			}
			return tee;
		}

		@Override
		public void flushBuffer() throws IOException {
			tee.flush();
		}
	}
}
