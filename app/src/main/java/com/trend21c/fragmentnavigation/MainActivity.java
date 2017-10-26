package com.trend21c.fragmentnavigation;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener, FragmentController.RootFragmentListener {

    TabLayout mTabLayout;
    FragmentController mFragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentController = new FragmentController(this, getSupportFragmentManager(), R.id.container);

        mTabLayout = findViewById(R.id.tablayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("tab1"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab2"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab3"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab4"));
        mTabLayout.addTab(mTabLayout.newTab().setText("tab5"));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragmentController.switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mFragmentController.switchTab(0);
    }

    @Override
    public void onBackPressed() {
        if (mFragmentController.isRootFragment()) {
            super.onBackPressed();
        } else {
            mFragmentController.popFragment();
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        return SubFragment.newInstance(index, 0);
    }

    @Override
    public void onFragmentPush(Fragment fragment) {
        mFragmentController.pushFragment(fragment);
    }
}
