package gov.nist.toolkit.xdsexception;

import gov.nist.toolkit.xdsexception.client.XdsException;

public class XDSRepositoryMetadataException extends XdsException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XDSRepositoryMetadataException(String message, String resource) {
		super(message, resource);
	}

	public XDSRepositoryMetadataException(String msg, String resource, Throwable cause) {
		super(msg, resource, cause);
	}

}
