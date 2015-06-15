package my.localocal.views.sidemenu;

public class SideMenuItem{

	private final String	sideMenuText;
	private final int	 sideMenuIconId;

	public SideMenuItem(String sideMenuText, int sideMenuIconId) {
		this.sideMenuText = sideMenuText;
		this.sideMenuIconId = sideMenuIconId;
	}

	public String getSideMenuItemText() {
		return sideMenuText;
	}

	public int getSideMenuIconId() {
		return sideMenuIconId;
	}

}
