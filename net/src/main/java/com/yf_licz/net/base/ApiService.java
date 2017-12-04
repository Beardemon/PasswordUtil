package com.yf_licz.net.base;

import com.yf_licz.net.response.DoubanUserResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by yfzx-sh-licz on 2017/11/10.
 */

public interface ApiService {


    @GET("https://api.douban.com/v2/user/lewis614")
    Observable<DoubanUserResponse>
    getDouBanApi();
}
