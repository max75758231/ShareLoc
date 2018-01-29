package maxzonov.shareloc.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import maxzonov.shareloc.R;

public class AppNavigator implements PreviousNavigation {

    private final FragmentManager fragmentManager;

    public AppNavigator(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void navigateToPreviousView() {
        if (hasPreviousView())
            fragmentManager.popBackStack();
    }

    public boolean hasPreviousView() {
        return fragmentManager.getBackStackEntryCount() > 0;
    }

    public void navigateToFragment(Fragment fragment) {
        replaceFragmentWithAddingToBackStack(fragment, "fragment");
    }

    private void replaceFragmentWithAddingToBackStack(Fragment fragment, String name) {
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(name)
                .commit();
    }
}