package gov.nist.toolkit.xdstools2.client.tabs;

import gov.nist.toolkit.xdstools2.client.selectors.EnvironmentManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Single instance is created by Xdstools2.java to manage environment choices.
 * Each tab gets a copy of EnvironmentManager.java
 * @author bill
 *
 */
public class EnvironmentStateImpl implements EnvironmentState {
	private String environmentName = "default";
	private List<String> environmentNameChoices = new ArrayList<String>();
	private List<EnvironmentManager> managers = new ArrayList<EnvironmentManager>();

	public boolean isFirstManager() { return managers.size() == 1; }
	
	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
		validate();
	}

	/**
	 * Set environment name without validation - needed to init from GWT Activity
	 * @param environmentName
	 */
	public void initEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public List<String> getEnvironmentNameChoices() {
		return environmentNameChoices;
	}
	
	public void updated(EnvironmentManager source) {
		for (EnvironmentManager m : managers) {
			if (m != source)
				m.needsUpdating();
		}
	}
	
	public int getManagerIndex(EnvironmentManager source) {
		return managers.indexOf(source);
	}

	public void setEnvironmentNameChoices(List<String> environmentNameChoices) {
		this.environmentNameChoices = environmentNameChoices;
		validate();
	}
	
	public void addManager(EnvironmentManager environmentManager) {
		managers.add(environmentManager);
		System.out.println(managers.size() + " EnvironmentManagers");
	}
	
	public void deleteManager(EnvironmentManager environmentManager) {
		managers.remove(environmentManager);
	}
	
	public boolean isValid() { return !isEmpty(environmentName); }
	
	public boolean validate() {
		if (isEmpty(environmentName) || environmentNameChoices == null) {
			environmentName = null;
			return false;
		}
		for (String name : environmentNameChoices) {
			if (environmentName.equals(name))
				return true;
		}
		environmentName = null;
		return false;
	}
	
	boolean isEmpty(String x) { return x == null || x.equals(""); }
	
}