package gov.nist.toolkit.xdstools2.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import gov.nist.toolkit.simcommon.client.SimId;
import gov.nist.toolkit.xdstools2.client.tabs.simMsgViewerTab.SimMsgViewer;
import gov.nist.toolkit.xdstools2.client.toolLauncher.NewToolLauncher;

/**
 *
 */
public class SimLogViewerAnchor extends Anchor {

    public SimLogViewerAnchor(final String title, final SimId simid) {
        super(title);

        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                new NewToolLauncher().launch(new SimMsgViewer(simid.toString()));
//                SimulatorMessageViewTab viewTab = new SimulatorMessageViewTab();
//                viewTab.onTabLoad(true, simid.toString());
            }
        });
    }
}
