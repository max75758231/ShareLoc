package maxzonov.shareloc.ui.settings_screen;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

/**
 * Created by Maxim Zonov on 09.11.2017.
 */

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    public static final String MESSAGE_TO_SEND = "message";

}
