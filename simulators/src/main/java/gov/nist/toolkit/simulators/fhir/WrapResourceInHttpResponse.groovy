package gov.nist.toolkit.simulators.fhir

import ca.uhn.fhir.context.FhirContext
import gov.nist.toolkit.installation.ResourceCache
import gov.nist.toolkit.simulators.proxy.util.SimProxyBase
import org.apache.http.ProtocolVersion
import org.apache.http.entity.StringEntity
import org.apache.http.impl.EnglishReasonPhraseCatalog
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine
import org.apache.http.protocol.HttpDateGenerator
import org.hl7.fhir.instance.model.api.IBaseResource
/**
 *
 */
class WrapResourceInHttpResponse {

    static BasicHttpResponse wrap(SimProxyBase base, IBaseResource resource, int httpCode) {
        FhirContext ctx = ResourceCache.ctx
        String httpCodeString = EnglishReasonPhraseCatalog.INSTANCE.getReason(httpCode, Locale.ENGLISH)
        BasicHttpResponse outcome = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion('HTTP', 1, 1), httpCode, httpCodeString))
        outcome.addHeader('Content-Type', base.clientContentType)
        outcome.addHeader('Date', new HttpDateGenerator().currentDate)
        String content
        if (base.clientContentType.contains('json')) {
            content = ctx.newJsonParser().encodeResourceToString(resource)
        } else {
            content = ctx.newXmlParser().encodeResourceToString(resource)
        }
        outcome.setEntity(new StringEntity(content))
        return outcome

    }
}