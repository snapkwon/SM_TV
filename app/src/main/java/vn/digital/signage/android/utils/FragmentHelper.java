package vn.digital.signage.android.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import vn.digital.signage.android.R;

/**
 * The type Fragment helper.
 */
@Singleton
public class FragmentHelper {
    /**
     * The constant TAG.
     */
    public static String TAG = FragmentHelper.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private String mCurrentFragmentTag;

    /**
     * Instantiates a new Fragment helper.
     */
    @Inject
    public FragmentHelper() {
    }

    /**
     * Gets fragment manager.
     *
     * @return the fragment manager
     */
    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    /**
     * Sets fragment manager.
     *
     * @param fragmentManager the fragment manager
     */
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    /**
     * Sets back stack changed listener.
     *
     * @param onBackStackListener the on back stack listener
     */
    public void setBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackListener) {
        mFragmentManager.addOnBackStackChangedListener(onBackStackListener);
    }

    // ---------------------------------------------------------------------------------

    /**
     * Attach fragment.
     *
     * @param menuId           the menu id
     * @param fragment         the fragment
     * @param fragmentTag      the fragment tag
     * @param isAddToBackStack the is add to back stack
     */
    public void attachFragment(int menuId, Fragment fragment,
                               String fragmentTag, boolean isAddToBackStack) {
        attFragment(menuId, fragment, fragmentTag, isAddToBackStack, true);
        commitTransactions();
    }

    /**
     * Attach fragment.
     *
     * @param menuId           the menu id
     * @param fragment         the fragment
     * @param fragmentTag      the fragment tag
     * @param isAddToBackStack the is add to back stack
     * @param isShouldAnimate  the is should animate
     */
    public void attachFragment(int menuId, Fragment fragment,
                               String fragmentTag, boolean isAddToBackStack, boolean isShouldAnimate) {
        attFragment(menuId, fragment, fragmentTag, isAddToBackStack, isShouldAnimate);
        commitTransactions();
    }

    /**
     * Gets fragment by tag.
     *
     * @param tag the tag
     * @return the fragment by tag
     */
    public Fragment getFragmentByTag(String tag) {
        Fragment f = mFragmentManager.findFragmentByTag(tag);
        return f;
    }

    /**
     * Clear all fragments in stack.
     */
    public void clearAllFragmentsInStack() {
        mFragmentManager.popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Gets fragments.
     *
     * @return the fragments
     */
    public List<Fragment> getFragments() {
        return mFragmentManager.getFragments();
    }

    /**
     * Gets active fragment.
     *
     * @param clsList the cls list
     * @return the active fragment
     */
    public Fragment getActiveFragment(List<Class<?>> clsList) {
        List<Fragment> fs = mFragmentManager.getFragments();
        if (fs != null && fs.size() > 0) {
            for (int i = fs.size() - 1; i >= 0; i--) {
                Fragment f = fs.get(i);
                if (f != null && f.isAdded() && f.isVisible()
                        && !f.isDetached()) {
                    if (clsList == null) {
                        return f;
                    } else {
                        for (Class<?> cls : clsList) {
                            if (cls.isInstance(f))
                                return f;
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * Close current fragment.
     */
    public void closeCurrentFragment() {
        mFragmentManager.popBackStack();
    }

    /**
     * Remove current fragment.
     *
     * @param f the f
     */
    public void removeCurrentFragment(Fragment f) {
        detachFragment(f, true);
    }

    // ---------------------------------------------------------------------------------

    private void attFragment(int layout, Fragment f, String tag,
                             boolean isAddToBackStack, boolean shouldAnimate) {
        mFragmentManager.executePendingTransactions();
        if (f != null) {
            if (f.isDetached()) {
                ensureTransaction(shouldAnimate);
                mFragmentTransaction.attach(f);
                if (isAddToBackStack)
                    mFragmentTransaction.addToBackStack(tag);
            } else {
                ensureTransaction(shouldAnimate);
                mFragmentTransaction.replace(layout, f, tag);
                if (isAddToBackStack)
                    mFragmentTransaction.addToBackStack(tag);
            }
        }
    }

    private void detachFragment(Fragment f, boolean shouldAnimate) {
        if (f != null && !f.isDetached()) {
            ensureTransaction(shouldAnimate);
            mFragmentTransaction.detach(f);
        }
    }

    private FragmentTransaction ensureTransaction(boolean shouldAnimate) {
        if (mFragmentTransaction == null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            if (shouldAnimate) {
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right);
            } else {
                mFragmentTransaction.setCustomAnimations(0, 0, 0, 0);
            }
        }
        return mFragmentTransaction;
    }

    private void commitTransactions() {
        if (mFragmentTransaction != null && !mFragmentTransaction.isEmpty()) {
            mFragmentTransaction.commitAllowingStateLoss();
            mFragmentTransaction = null;
        }
    }

    /**
     * Pop back stack.
     */
    public void popBackStack() {
        mFragmentManager.popBackStack();
    }

}
