package maxzonov.shareloc.ui.location_info_screen;

import com.arellomobile.mvp.MvpView;

public interface LocationView extends MvpView {

    void showInfo(String latitude, String longitude, String address);
}