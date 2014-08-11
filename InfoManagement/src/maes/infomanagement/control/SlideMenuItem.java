package maes.infomanagement.control;

public class SlideMenuItem {
	private int mItemID;
	private String mTitle;
	
	public SlideMenuItem(int pItemID,String pTitle){
		mItemID = pItemID;
		mTitle = pTitle;
	}
	
	public int getmItemID() {
		return mItemID;
	}
	public void setmItemID(int pItemID) {
		this.mItemID = pItemID;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String pTitle) {
		this.mTitle = pTitle;
	}
	
}
