package gov.nist.toolkit.valregmsg.message;

import gov.nist.toolkit.errorrecording.ErrorRecorder;
import gov.nist.toolkit.errorrecording.client.XdsErrorCode;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.DefaultValidationContextFactory;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.message.MessageBodyContainer;
import gov.nist.toolkit.valsupport.message.AbstractMessageValidator;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;

import java.util.*;

/**
 * Validate wrappers of a message.  Wrappers are the XML elements that wrap the metadata
 * contents of a message.
 * @author bill
 *
 */
public class WrapperValidator extends AbstractMessageValidator {

	class Mapper {
		List<String> wrapperList = new ArrayList<>();
		String transaction;
		String profile;
		ValidationContext vc;

		Mapper(String profile, String transaction) {
			this.profile = profile;
			this.transaction = transaction;
		}

		void addWrapper(String wrapper) {
			wrapperList.add(wrapper);
		}
	}
	private List<Mapper> mappers = new ArrayList<>();

	private void newMapper(String profile, ValidationContext vc, List<String> wrapperList) {
		Mapper m = new Mapper(profile, vc.getTransactionName());
		m.vc = vc;
		m.wrapperList = wrapperList;
		mappers.add(m);
	}

	private List<Mapper> findMapper(String transaction) {
		List<Mapper> mappers = new ArrayList<>();
		for (Mapper m : mappers) {
			if (transaction != null && !transaction.equals(m.transaction)) continue;
		}
		return mappers;
	}

	OMElement xml;
	private List<List<String>> wrapperList = new ArrayList<>();
	private List<String> elementOrder = new ArrayList<String>();

	public WrapperValidator(ValidationContext vc) {
		super(vc);
	}
	
	void err(String msg, String ref) {
		er.err(XdsErrorCode.Code.XDSRegistryMetadataError, msg, this, ref);
	}

	private final String XDR = "XDR";
	private final String XDS = "XDS";

	String getProfileName(ValidationContext vd) {
		return (vc.isXDR) ? XDR : XDS;
	}

	public void run(ErrorRecorder er, MessageValidatorEngine mvc) {
		this.er = er;
		er.registerValidator(this);

		MessageBodyContainer cont = (MessageBodyContainer) mvc.findMessageValidator("MessageBodyContainer");
		xml = cont.getBody();
		init();
		String transaction = vc.getTransactionName();

		List<String> expectedWrappers = findWrapperList(vc);

		if (xml == null) {
			err("No content present", "");
            er.unRegisterValidator(this);
			return;
		}

		if (expectedWrappers == null) {
//			er.err("Do not have expected wrappers for validation type of " + vc.getTransactionName(), "Internal Error");
            er.unRegisterValidator(this);
			return;
		}

		validateWrappers(xml, expectedWrappers);

//		checkElementOrder(xml);
        er.unRegisterValidator(this);

	}

	private List<String> findWrapperList(ValidationContext vc) {
		String transactionName = vc.getTransactionName();
		String profileName = getProfileName(vc);

		List<Mapper> choices = new ArrayList<>();
		for (Mapper m : mappers) {
			if (transactionName.equals(m.transaction))
				choices.add(m);
		}
		if (choices.size() == 1) return choices.get(0).wrapperList;
		if (choices.size() > 1 && profileName != null) {
			for (Mapper choice : choices) {
				if (profileName.equals(choice.profile)) return choice.wrapperList;
			}
		}
		return null;
	}

	void initElementOrder() {
		elementOrder.add("Slot");
		elementOrder.add("Name");
		elementOrder.add("Description");
		elementOrder.add("VersionInfo");
		elementOrder.add("Classification");
		elementOrder.add("ExternalIdentifier");
	}

	void initWrapperList() {
		List<String> x;
		ValidationContext v;

		v = DefaultValidationContextFactory.validationContext();
		x = new ArrayList<>();
		x.add("ProvideAndRegisterDocumentSetRequest");
		x.add("SubmitObjectsRequest");
		x.add("RegistryObjectList");
		v.isXDR = true;
		v.isR = true;
		v.isRequest = true;
		newMapper(XDR, v, x);

		v = DefaultValidationContextFactory.validationContext();
		x = new ArrayList<String>();
		x.add("ProvideAndRegisterDocumentSetRequest");
		x.add("SubmitObjectsRequest");
		x.add("RegistryObjectList");
		v.isPnR = true;
		v.isRequest = true;
		newMapper(XDS, v, x);

		v = DefaultValidationContextFactory.validationContext();
		x = new ArrayList<String>();
		x.add("SubmitObjectsRequest");
		x.add("RegistryObjectList");
		v.isR = true;
		v.isRequest = true;
		newMapper(null, v, x);
		v.isR = false;
		v.isXDM = true;
		newMapper(null, v, x);
		v.isXDM = false;

		v = DefaultValidationContextFactory.validationContext();
		x = new ArrayList<String>();
		x.add("AdhocQueryRequest");
		v.isSQ = true;
		v.isRequest = true;
		newMapper(null, v, x);
		v.isXC = true;
		newMapper(null, v, x);

		v = DefaultValidationContextFactory.validationContext();
		x = new ArrayList<String>();
		x.add("AdhocQueryResponse");
		x.add("RegistryObjectList");
		v.isSQ = true;
		v.isResponse = true;
		newMapper(null, v, x);
		v.isXC = true;
		newMapper(null, v, x);

		v = DefaultValidationContextFactory.validationContext();
		x = new ArrayList<String>();
		x.add("RetrieveDocumentSetRequest");
		x.add("DocumentRequest");
		v.isRet = true;
		v.isRequest = true;
		newMapper(null, v, x);
		v.isXC = true;
		newMapper(null, v, x);
	}

//	@SuppressWarnings("unchecked")
//	void checkElementOrder(OMElement ele) {
//		if (ele == null)
//			return;
//		for (Iterator<OMElement> it = ele.getChildElements(); it.hasNext(); ) {
//			OMElement ele1 = it.next();
//			String ele1Name = ele1.getLocalName();
//			OMElement ele2 = getNextOMElementSibling(ele1);
//			if (ele2 != null) {
//				String ele2Name = ele2.getLocalName();
//				if (!canFollow(ele1Name, ele2Name))
//					err(
//							"Child elements of " + ele.getLocalName() + "(id=" + new Metadata().getId(ele) + ")" +
//							" are out of Schema required order:   " +
//							" element " + ele2.getLocalName() + " cannot follow element " + ele1.getLocalName() +
//							". Elements must be in this order " + elementOrder
//							," ebRIM 3.0 Schema");
//			}
//			checkElementOrder(ele1);
//		}
//	}

//	boolean canFollow(String element, String nextElement) {
//		if (element == null || element.equals(""))
//			return false;
//		if (nextElement == null || nextElement.equals(""))
//			return false;
//		int elementI = elementOrder.indexOf(element);
//		int nextElementI = elementOrder.indexOf(nextElement);
//		return elementI == -1 || nextElementI == -1 || elementI <= nextElementI;
//	}

	void init() {
		initWrapperList();
		initElementOrder();
	}

//	OMElement getNextOMElementSibling(OMElement ele) {
//		OMNode n = null;
//		for (n = ele.getNextOMSibling(); n != null && !(n instanceof OMElement); n = n.getNextOMSibling())
//			;
//		return (OMElement) n;
//	}

//	String schema_validate(OMElement ahqr, int metadata_type) throws XdsInternalException {
//		String schema_messages = null;
//		try {
//			schema_messages = SchemaValidation.validate(ahqr, metadata_type);
//			return schema_messages;
//		} catch (Exception e) {
//			throw new XdsInternalException("Schema Validation threw internal error: " + e.getMessage());
//		}
//	}

	boolean isSubmission() {
		return vc.isSubmit();
	}

//	boolean hasPeerElement(OMElement ele) {
//		OMNode next = ele.getNextOMSibling();
//		if (next != null && (next instanceof OMElement))
//			return true;
//		OMNode prev = ele.getPreviousOMSibling();
//		if (prev != null && (prev instanceof OMElement))
//			return true;
//		return false;
//	}
	
	OMElement findPeerWithName(OMElement ele, String name) {
		if (name == null)
			return null;
		
		OMNode focus = ele;
		
		while (focus != null) {
			if ((focus instanceof OMElement)) {
				OMElement focusEle = (OMElement) focus;
				if (focusEle.getLocalName().equals(name))
				return focusEle;
			}
			focus = focus.getNextOMSibling();
		}
		return null;
	}

    private OMElement findElement(Iterator<OMNode> it) {
        while(it.hasNext()) {
            OMNode n = it.next();
            if (n instanceof OMElement)
                return (OMElement) n;
        }
        return null;
    }

	private List<String> firstNestedElements(OMElement ele, int depth) {
        List<String> names = new ArrayList<>();
        while(ele != null && names.size() < depth) {
            String name = ele.getLocalName();
            names.add(name);
            ele = findElement(ele.getChildren());
        }
        return names;
    }

	private void validateWrappers(OMElement ele, List<String> expectedWrappers) {
		List<String> foundWrappers = new ArrayList<String>();
		for (String wrapper : expectedWrappers) {
			OMElement focus = findPeerWithName(ele, wrapper);
			if (focus == null) {
				err("Expected metadata wrappers " + expectedWrappers +
						" but found " + firstNestedElements(ele, 4) + " but not " + wrapper, "ebRS 3.0");
				return;
			}
			foundWrappers.add(wrapper);
			ele = focus.getFirstElement();
		}
	}



}
