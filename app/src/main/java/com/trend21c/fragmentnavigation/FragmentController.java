package com.trend21c.fragmentnavigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kevin on 2017. 10. 26..
 */

public class FragmentController {

    private RootFragmentListener mRootFragmentListener;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private SparseArray<Stack<Fragment>> mFragmentStacks;
    private AtomicInteger mCounter;
    private int mSelectedTabIndex;

    public FragmentController(RootFragmentListener rootFragmentListener, FragmentManager fragmentManager, int containerId) {
        this.mRootFragmentListener = rootFragmentListener;
        this.mFragmentManager = fragmentManager;
        this.mContainerId = containerId;
        this.mFragmentStacks = new SparseArray<>();
        this.mCounter = new AtomicInteger(0);
        this.mSelectedTabIndex = 0;
    }

    public void switchTab(int index) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        detachCurrentFragment(fragmentTransaction);

        if (mFragmentStacks.get(index) != null && !mFragmentStacks.get(index).isEmpty()) {

            Fragment attachFragment = mFragmentManager.findFragmentByTag(mFragmentStacks.get(index).peek().getTag());
            fragmentTransaction.attach(attachFragment);
        } else {
            Fragment attachFragment = mRootFragmentListener.getRootFragment(index);

            if (mFragmentStacks.get(index) != null) {
                mFragmentStacks.get(index).add(attachFragment);
            } else {
                Stack<Fragment> stack = new Stack<>();
                stack.add(attachFragment);
                mFragmentStacks.put(index, stack);
            }

            fragmentTransaction.add(mContainerId, attachFragment, generateTag(attachFragment));
        }

        fragmentTransaction.commit();

        mSelectedTabIndex = index;
    }

    public boolean isRootFragment() {
        if (mFragmentStacks.get(mSelectedTabIndex) != null && mFragmentStacks.get(mSelectedTabIndex).size() > 1) {
            return false;
        }

        return true;
    }

    public String generateTag(Fragment fragment) {
        return fragment.getClass().getSimpleName() + mCounter.incrementAndGet();
    }

    public void detachCurrentFragment(FragmentTransaction ft) {
        if (mFragmentStacks.get(mSelectedTabIndex) != null && !mFragmentStacks.get(mSelectedTabIndex).isEmpty()) {
            Fragment currentFragment = mFragmentManager.findFragmentByTag(mFragmentStacks.get(mSelectedTabIndex).peek().getTag());

            ft.detach(currentFragment);
        }
    }

    public void pushFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        detachCurrentFragment(fragmentTransaction);

        if (mFragmentStacks.get(mSelectedTabIndex) != null) {
            mFragmentStacks.get(mSelectedTabIndex).add(fragment);
        } else {
            Stack<Fragment> stack = new Stack<>();
            stack.add(fragment);
            mFragmentStacks.put(mSelectedTabIndex, stack);
        }

        fragmentTransaction.add(mContainerId, fragment, generateTag(fragment));
        fragmentTransaction.commit();
    }

    public void popFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (mFragmentStacks.get(mSelectedTabIndex) != null && !mFragmentStacks.get(mSelectedTabIndex).isEmpty()) {

            Fragment removeFragment = mFragmentManager.findFragmentByTag(mFragmentStacks.get(mSelectedTabIndex).pop().getTag());

            fragmentTransaction.remove(removeFragment);

            Fragment newFragment = mFragmentManager.findFragmentByTag(mFragmentStacks.get(mSelectedTabIndex).peek().getTag());
            fragmentTransaction.attach(newFragment);

            fragmentTransaction.commit();
        }
    }

    interface RootFragmentListener {
        Fragment getRootFragment(int index);
    }
}
