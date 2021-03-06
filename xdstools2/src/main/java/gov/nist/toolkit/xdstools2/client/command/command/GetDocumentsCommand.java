package gov.nist.toolkit.xdstools2.client.command.command;

import gov.nist.toolkit.results.client.Result;
import gov.nist.toolkit.xdstools2.client.util.ClientUtils;
import gov.nist.toolkit.xdstools2.shared.command.request.GetDocumentsRequest;

import java.util.List;

/**
 * Created by onh2 on 11/3/16.
 */
public abstract class GetDocumentsCommand extends GenericCommand<GetDocumentsRequest,List<Result>>{
    @Override
    public void run(GetDocumentsRequest var1) {
        ClientUtils.INSTANCE.getToolkitServices().getDocuments(var1,this);
    }
}
