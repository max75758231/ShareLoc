package maxzonov.shareloc.ui.location;

import com.arellomobile.mvp.MvpView;

public interface LocationView extends MvpView {

    void showInfo(String latitude, String longitude, String address);
    void onLocationResponseError();
}