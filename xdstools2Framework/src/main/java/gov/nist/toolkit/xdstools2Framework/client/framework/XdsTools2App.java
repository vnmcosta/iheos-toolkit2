package gov.nist.toolkit.xdstools2Framework.client.framework;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import gov.nist.toolkit.xdstools2.client.event.Xdstools2EventBus;

/**
 *
 */
public class XdsTools2App implements IsWidget {
    private static final TkGinInjector INJECTOR = TkGinInjector.INSTANCE;
    private final Xdstools2EventBus eventBus = INJECTOR.getEventBus();

    private SimplePanel activityPanel = new SimplePanel();
    private XdsTools2AppView appView;
    private XdsTools2Presenter appPresenter;

    public XdsTools2App() {
        appView = INJECTOR.getXdsTools2AppView();
        appPresenter = INJECTOR.getXdsTools2AppPresenter();
        appPresenter.setView(appView);

        PlaceController placeController = INJECTOR.getPlaceController();

        XdsTools2ActivityMapper activityMapper = new XdsTools2ActivityMapper();
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(activityPanel);

        XdsTools2AppPlaceHistoryMapper historyMapper = GWT.create(XdsTools2AppPlaceHistoryMapper.class);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);

        historyHandler.register(placeController, eventBus, new WelcomePlace("Welcome"));

        activityPanel.add(appView.asWidget());

        historyHandler.handleCurrentHistory();
    }

    @Override
    public Widget asWidget() {
        return activityPanel;
    }

    public XdsTools2AppView getAppView(){
        return appView;
    }

    public static TkGinInjector getInjector() {
        return INJECTOR;
    }

    public Xdstools2EventBus getEventBus() {
        return eventBus;
    }


}