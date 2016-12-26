package com.skyeng.app.login.data;

import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.skyeng.app.json.JacksonObservableRequest;
import com.skyeng.app.login.data.json.ErrorCodeResponse;

import rx.Observable;

public abstract class ErrorCodeRequest<T extends ErrorCodeResponse> extends JacksonObservableRequest<T> {

    /**
     * Creates a new request.
     *
     * @param method       the HTTP method to use
     * @param url          URL to fetch the JSON from
     * @param requestData  A {@link Object} to post and convert into json as the request. Null is allowed and indicates no parameters will be posted along with request.
     * @param responseType
     */
    public ErrorCodeRequest(int method, String url, Object requestData, Class<T> responseType) {
        super(method, url, requestData, responseType);
    }

    @Override
    public Observable<T> execute(@NonNull RequestQueue requestQueue) {
        return super.execute(requestQueue)
                .flatMap(result -> {
                    final Integer errorCode = result.getErrorCode();
                    if (errorCode != null) {
                        return Observable.error(parseError(errorCode));
                    } else {
                        return Observable.just(result);
                    }
                });
    }

    private Throwable parseError(@NonNull Integer errorCode) {
        return new Exception("got error code " + errorCode);
    }
}