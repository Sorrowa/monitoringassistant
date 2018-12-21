package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private List<Fragment>  mFragments;
    private List<String> tags = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragmentManager = fm;
        this.mFragments = fragments;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        tags.add(makeFragmentName(container.getId(), getItemId(position)));
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        tags.remove(makeFragmentName(container.getId(), getItemId(position)));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    public void update(int position) {
        Fragment fragment = mFragmentManager.findFragmentByTag(tags.get(position));
        if (fragment == null) {
            return;
        }
        if (fragment instanceof UpdateAble) {//这里唯一的要求是Fragment要实现UpdateAble接口
            ((UpdateAble) fragment).update();
        }
    }

    public interface UpdateAble {
        void update();
    }
}
