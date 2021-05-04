package com.atisapp.jazbeh.MusicPlayer;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class BinaryRequest extends Request<byte[]> {
    private final Map<String, String> mHeaders;
    private final String mRequestBody;
    private final Response.Listener<byte[]> mListener;
    private final Response.ErrorListener mErrorListener;

    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    public BinaryRequest(int method, String url, Response.Listener<byte[]> listener,  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mHeaders = null;
        this.mRequestBody = null;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public BinaryRequest(String url, String requestBody, Response.Listener<byte[]> listener,  Response.ErrorListener errorListener) {
        super(requestBody == null ? Method.GET : Method.POST, url, errorListener);
        this.mHeaders = null;
        this.mRequestBody = requestBody;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public BinaryRequest(int method, String url, String requestBody, Response.Listener<byte[]> listener,  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mHeaders = null;
        this.mRequestBody = requestBody;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public BinaryRequest(int method, String url, Map<String, String> headers, String requestBody, Response.Listener<byte[]> listener,  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mHeaders = headers;
        this.mRequestBody = requestBody;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}
