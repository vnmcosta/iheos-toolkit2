package gov.nist.toolkit.xdstools2.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * This is used by xxx to load the resource file index.html into the home
 * tab of toolkit.
 */
public interface HomePageResources extends ClientBundle {
    HomePageResources INSTANCE = GWT.create(HomePageResources.class);

    @ClientBundle.Source("site/index.html")
    TextResource getIntroHtml();

}
