package bid.xiaocha.xxt.presenter;

import android.content.Context;

import com.amap.api.maps.model.LatLng;

import bid.xiaocha.xxt.iview.IMapChooseAddressView;
import bid.xiaocha.xxt.util.MapUtil;

/**
 * Created by 55039 on 2017/11/4.
 */

public class MapChooseAddressPresenter implements IMapChooseAddressPresenter {
    private IMapChooseAddressView view;
    private Context context;

    public MapChooseAddressPresenter(IMapChooseAddressView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void getPlace(LatLng latLng) {
        view.showLoading();
        MapUtil.getPlaceByLatlng(context, latLng, new MapUtil.OnGetPlaceByLatlngResult() {
            @Override
            public void getPlaceByLatlngResult(boolean isSuccess, String place) {
                view.dismissLoading();
                view.showGetPlaceResult(isSuccess,place);
            }
        });
    }
}
