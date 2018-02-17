package maxzonov.shareloc.ui.map;

import com.arellomobile.mvp.MvpView;
import com.google.android.gms.maps.model.Marker;

public interface MapView extends MvpView {

    void showAddressInMarker(String address);

    void showAndRefreshAddressInMarker(String address, Marker marker);
}
