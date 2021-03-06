package gov.nist.toolkit.valregmsg.message;

import gov.nist.toolkit.errorrecording.ErrorRecorder;
import gov.nist.toolkit.errorrecording.client.XdsErrorCode;
import gov.nist.toolkit.errorrecording.factories.ErrorRecorderBuilder;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrymetadata.MetadataParser;
import gov.nist.toolkit.valregmetadata.top.MetadataValidator;
import gov.nist.toolkit.valregmetadata.top.ObjectStructureValidator;
import gov.nist.toolkit.valregmetadata.top.SubmissionStructure;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.message.MessageBody;
import gov.nist.toolkit.valsupport.message.AbstractMessageValidator;
import gov.nist.toolkit.valsupport.registry.RegistryValidationInterface;

public class MetadataMessageValidator extends AbstractMessageValidator {
//	OMElement xml;
	Metadata m = null;
	ErrorRecorderBuilder erBuilder;
	MessageValidatorEngine mvc;
	RegistryValidationInterface rvi;
	MessageBody messageBody;

	public Metadata getMetadata() { return m; }

//	public MetadataMessageValidator(ValidationContext vc, OMElement xml) {
//		super(vc);
//		this.xml = xml;
//	}
	
	public MetadataMessageValidator(ValidationContext vc, MessageBody messageBody, ErrorRecorderBuilder erBuilder, MessageValidatorEngine mvc, RegistryValidationInterface rvi) {
		super(vc);
		this.erBuilder = erBuilder;
		this.mvc = mvc;
		this.messageBody = messageBody;
		this.rvi = rvi;
	}
	
	public void run(ErrorRecorder er, MessageValidatorEngine mvc) {
		this.er = er;
		er.registerValidator(this);
		
		if (messageBody == null) {
			er.err(XdsErrorCode.Code.XDSRegistryError, "MetadataMessageValidator: top element null", this, "");
            er.unRegisterValidator(this);
			return;
		}
		
		

		try {
			m = MetadataParser.parseNonSubmission(messageBody.getBody());
			
			// save on validation stack so others can find it if they need it
			mvc.addMessageValidator("MetadataContainer", new MetadataContainer(vc, m), erBuilder.buildNewErrorRecorder());

			
			contentSummary(er, m);
			
			MetadataValidator mv = new MetadataValidator(m, vc, rvi);
			new ObjectStructureValidator(m, vc, rvi).run(er);
			mv.runCodeValidation(er);
			if (vc.isRequest && vc.isRM) {
				// must be object ref list
				if (!m.isObjectRefsOnly()) {
					er.err(XdsErrorCode.Code.XDSRegistryError, "Request must be ObjectRefs only", null, null);
				}
			}
			else {
				new SubmissionStructure(m, rvi).run(er, vc);
			}

		} catch (Exception e) {
			er.err(XdsErrorCode.Code.XDSRegistryError, e);
		}
        finally {
            er.unRegisterValidator(this);
        }

		er.finish();

	}
	
	static public void contentSummary(ErrorRecorder er, Metadata m) {
		er.sectionHeading("Content Summary");
		er.detail(m.getSubmissionSetIds().size() + " SubmissionSets");
		er.detail(m.getExtrinsicObjectIds().size() + " DocumentEntries");
		er.detail(m.getFolderIds().size() + " Folders");
		er.detail(m.getAssociationIds().size() + " Associations");
		if (m.getSubmissionSetIds().size() == 0 &&
				m.getExtrinsicObjectIds().size() == 0 &&
				m.getFolderIds().size() == 0 &&
				m.getAssociationIds().size() == 0)
			er.detail(m.getObjectRefIds().size() + " ObjectRefs");
	}

}
