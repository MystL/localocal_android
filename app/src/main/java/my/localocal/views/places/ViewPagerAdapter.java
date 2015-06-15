package my.localocal.views.places;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{

	private List<Fragment>	listOfFragments	= new ArrayList<Fragment>();

	public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> listOfFragments) {
		super(fragmentManager);
		this.listOfFragments = listOfFragments;
	}

	@Override
	public Fragment getItem(int position) {

		if (position >= 0 && position < listOfFragments.size()) {
			return listOfFragments.get(position);
		}

		return null;
	}

	@Override
	public int getCount() {
		return listOfFragments.size();
	}

}
