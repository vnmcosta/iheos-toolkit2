package gov.nist.toolkit.fhir.server.servlet

import gov.nist.toolkit.simcommon.client.SimId
import gov.nist.toolkit.simcommon.server.SimDb

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Implement request and response logging for FHIR server
 */
public class FhirServletFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest theRequest = (HttpServletRequest) servletRequest

        HttpServletRequest requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest
        // Log request via requestWrapper.getInputStream()
        // uri has form    /fsim/default__fire/Patient/1 for a READ operation
        String uri = httpServletRequest.getRequestURI();
        String method = httpServletRequest.method

        String queryString = httpServletRequest.queryString

        SimId simId = HttpRequestParser.simIdFromURI(uri)
        SimDb simDb
        if (simId) {
            try {
                simDb = new SimDb(simId, SimDb.BASE_TYPE, SimDb.ANY_TRANSACTION, false)
                List<String> headers = theRequest.headerNames.collect { String headerName ->
                    "${headerName}: ${theRequest.getHeader(headerName)}"
                }

                String headerblock = headers.join('\r\n')
                simDb.putRequestHeaderFile(headerblock.bytes)
                simDb.putRequestBodyFile(requestWrapper.inputStream.bytes)
                if (queryString)
                    simDb.putRequestURI("${method} ${uri}?${queryString}")
                else
                    simDb.putRequestURI("${method} ${uri}")
                requestWrapper.setAttribute(Attributes.SIMID, simId)
                requestWrapper.setAttribute(Attributes.SIMDB, simDb)
            } catch (Exception e) {
                // ignored
            }
        }

        CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper(servletResponse as HttpServletResponse)

        // forward on request
        filterChain.doFilter(requestWrapper, responseWrapper)

        if (simDb) {
            // need to do the same magic here to capture the response for logging
            OutputStream outputStream = responseWrapper.outputStream
            File responseBodyFile = simDb.getResponseBodyFile(simId, SimDb.BASE_TYPE, SimDb.ANY_TRANSACTION, simDb.event)
            String response = new String(responseWrapper.toByteArray())
            responseBodyFile.text = response
        }
    }


    @Override
    public void destroy() {

    }
}
