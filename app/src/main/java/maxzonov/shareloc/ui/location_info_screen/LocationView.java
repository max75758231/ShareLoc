package maxzonov.shareloc.ui.location_info_screen;

import com.arellomobile.mvp.MvpView;

/**
 * Created by Maxim Zonov on 02.11.2017.
 */

public interface LocationView extends MvpView {

    void showInfo(String latitude, String longtitude);

    void showLinks(String latitude, String longtitude);

    void showAddress(String address);
}
