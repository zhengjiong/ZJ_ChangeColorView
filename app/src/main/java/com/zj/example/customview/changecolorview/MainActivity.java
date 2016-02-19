package com.zj.example.customview.changecolorview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        setViewPager();
    }

    private void setViewPager() {
        mFragments.add(TestFragment.newInstance());
        mFragments.add(TestFragment.newInstance());
        mFragments.add(TestFragment.newInstance());
        mFragments.add(TestFragment.newInstance());

        mAdapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);
    }

    class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
