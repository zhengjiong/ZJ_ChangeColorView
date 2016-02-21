package com.zj.example.customview.changecolorview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();

    private List<ChangeColorView> mTabIndicators = new ArrayList<>();

    public void onClick(View view) {
        restoreChangeColorViewAlpha();

        switch (view.getId()) {
            case R.id.a:
                mTabIndicators.get(0).setAlpha(1.0f);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.b:
                mTabIndicators.get(1).setAlpha(1.0f);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.c:
                mTabIndicators.get(2).setAlpha(1.0f);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.d:
                mTabIndicators.get(3).setAlpha(1.0f);
                mViewPager.setCurrentItem(3);
                break;
        }

    }

    private void restoreChangeColorViewAlpha() {
        for (int i = 0; i < mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setAlpha(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        setViewPager();

        ChangeColorView changeColorViewA = (ChangeColorView) findViewById(R.id.a);
        ChangeColorView changeColorViewB = (ChangeColorView) findViewById(R.id.b);
        ChangeColorView changeColorViewC = (ChangeColorView) findViewById(R.id.c);
        ChangeColorView changeColorViewD = (ChangeColorView) findViewById(R.id.d);

        mTabIndicators.add(changeColorViewA);
        mTabIndicators.add(changeColorViewB);
        mTabIndicators.add(changeColorViewC);
        mTabIndicators.add(changeColorViewD);

        changeColorViewA.setAlpha(1.0f);

    }

    private void setViewPager() {
        mFragments.add(TestFragment.newInstance());
        mFragments.add(TestFragment.newInstance());
        mFragments.add(TestFragment.newInstance());
        mFragments.add(TestFragment.newInstance());

        mAdapter = new PagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0)
                {
                    ChangeColorView left = mTabIndicators.get(position);
                    ChangeColorView right = mTabIndicators.get(position + 1);
                    left.setAlpha(1 - positionOffset);
                    right.setAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
