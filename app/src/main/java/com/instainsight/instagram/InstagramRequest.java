package com.instainsight.instagram;

import android.os.AsyncTask;

import com.instainsight.instagram.util.Cons;
import com.instainsight.instagram.util.Debug;
import com.instainsight.instagram.util.StringUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Http request to instagram api.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 */
public class InstagramRequest {
    private String mAccessToken;

    /**
     * Instantiate new object.
     */
    public InstagramRequest() {
        mAccessToken = "";
    }

    /**
     * Instantiate new object.
     *
     * @param accessToken Access token.
     */
    public InstagramRequest(String accessToken) {
        mAccessToken = accessToken;
    }

    /**
     * Create http request to an instagram api endpoint.
     * This is a synchronus method, so you have to call it on a separate thread.
     *
     * @param method   Http method, can be GET or POST
     * @param endpoint Api endpoint.
     * @param params   Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    public String createRequest(String method, String endpoint, List<NameValuePair> params) throws Exception {
        if (method.equals("POST")) {
            return requestPost(endpoint, params);
        } else {
            return requestGet(endpoint, params);
        }
    }

    /**
     * Create http request to an instagram api endpoint.
     * This is an asynchronous method, so you have to define a listener to handle the result.
     *
     * @param method   Http method, can be GET or POST
     * @param endpoint Api endpoint.
     * @param params   Request parameters
     * @param listener Request listener
     */
    public void createRequest(String method, String endpoint, List<NameValuePair> params, InstagramRequestListener listener) {
        new RequestTask(method, endpoint, params, listener).execute();
    }

    /**
     * Create http GET request to an instagram api endpoint.
     *
     * @param endpoint Api endpoint.
     * @param params   Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    private String requestGet(String endpoint, List<NameValuePair> params) throws Exception {
        String requestUri = Cons.API_BASE_URL + ((endpoint.indexOf("/") == 0) ? endpoint : "/" + endpoint);

        return get(requestUri, params);
    }

    /**
     * Create http POST request to an instagram api endpoint.
     *
     * @param endpoint Api endpoint.
     * @param params   Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    private String requestPost(String endpoint, List<NameValuePair> params) throws Exception {
        String requestUri = Cons.API_BASE_URL + ((endpoint.indexOf("/") == 0) ? endpoint : "/" + endpoint);

        return post(requestUri, params);
    }

    /**
     * Create http GET request to an instagram api endpoint.
     *
     * @param requestUri Api url
     * @param params     Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    public String get(String requestUri, List<NameValuePair> params) throws Exception {
        InputStream stream = null;
        String response = "";

        try {
            String requestUrl = requestUri;

            if (!mAccessToken.equals("")) {
                if (params == null) {
                    params = new ArrayList<NameValuePair>(1);

                    params.add(new BasicNameValuePair("access_token", mAccessToken));
                } else {
                    params.add(new BasicNameValuePair("access_token", mAccessToken));
                }
            }

            if (params != null) {
                StringBuilder requestParamSb = new StringBuilder();
                int size = params.size();

                for (int i = 0; i < size; i++) {
                    BasicNameValuePair param = (BasicNameValuePair) params.get(i);

                    requestParamSb.append(param.getName() + "=" + param.getValue() + ((i != size - 1) ? "&" : ""));
                }

                String requestParam = requestParamSb.toString();

                requestUrl = requestUri + ((requestUri.contains("?")) ? "&" + requestParam : "?" + requestParam);
            }

            Debug.i("GET " + requestUrl);

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(requestUrl);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity == null) {
                throw new Exception("Request returns empty result");
            }

            stream = httpEntity.getContent();
            response = StringUtil.streamToString(stream);

            Debug.i("Response " + response);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return response;
    }

    /**
     * Create http POST request to an instagram api endpoint.
     *
     * @param requestUrl Api url
     * @param params     Request parameters
     * @return Api response in json format.
     * @throws Exception If error occured.
     */
    public String post(String requestUrl, List<NameValuePair> params) {
        InputStream stream = null;
        String response = "";

        try {
            if (!mAccessToken.equals("")) {
                if (params == null) {
                    params = new ArrayList<NameValuePair>(1);

                    params.add(new BasicNameValuePair("access_token", mAccessToken));
                } else {
                    params.add(new BasicNameValuePair("access_token", mAccessToken));
                }
            }

            Debug.i("POST " + requestUrl);


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(requestUrl);

            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity == null) {
                throw new Exception("Request returns empty result");
            }

            stream = httpEntity.getContent();
            response = StringUtil.streamToString(stream);

            Debug.i("Response " + response);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String sendPost(String _url, Map<String, String> parameter) {
        StringBuilder params = new StringBuilder("");
        String result = "";
        try {
            for (String s : parameter.keySet()) {
                params.append("&" + s + "=");

                params.append(URLEncoder.encode(parameter.get(s), "UTF-8"));
            }
            params.append("?access_token").append("=" + mAccessToken);

            String url = _url;
            URL obj = new URL(_url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
//            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "UTF-8");

            con.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            outputStreamWriter.write(params.toString());
            outputStreamWriter.flush();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + params);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            result = response.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }

    }

    public void createPaginationRequest(String url, InstagramRequestListener listener) {
        new PaginationRequestTask(url, listener).execute();
    }

    private String requestPagination(String url) throws Exception {

        return paginationGet(url);
    }

    public String paginationGet(String requestUri) throws Exception {
        InputStream stream = null;
        String response = "";

        try {
            String requestUrl = requestUri;

            Debug.i("GET " + requestUrl);

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(requestUrl);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity == null) {
                throw new Exception("Request returns empty result");
            }

            stream = httpEntity.getContent();
            response = StringUtil.streamToString(stream);

            Debug.i("Response " + response);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return response;
    }

    //Request listener
    public interface InstagramRequestListener {
        public abstract void onSuccess(String response);

        public abstract void onError(String error);
    }

    private class RequestTask extends AsyncTask<URL, Integer, Long> {
        String method, endpoint, response = "";

        List<NameValuePair> params;

        InstagramRequestListener listener;

        public RequestTask(String method, String endpoint, List<NameValuePair> params, InstagramRequestListener listener) {
            this.method = method;
            this.endpoint = endpoint;
            this.params = params;
            this.listener = listener;
        }

        protected void onCancelled() {
        }

        protected void onPreExecute() {
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                if (method.equals("POST")) {
                    response = requestPost(endpoint, params);
                } else {
                    response = requestGet(endpoint, params);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            if (!response.equals("")) {
                listener.onSuccess(response);
            } else {
                listener.onError("Failed to process api request");
            }
        }
    }

    private class PaginationRequestTask extends AsyncTask<URL, Integer, Long> {
        String response = "";
        String url;
        InstagramRequestListener listener;

        public PaginationRequestTask(String url, InstagramRequestListener listener) {
            this.url = url;
            this.listener = listener;
        }

        protected void onCancelled() {
        }

        protected void onPreExecute() {
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            try {
                response = requestPagination(url);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            if (!response.equals("")) {
                listener.onSuccess(response);
            } else {
                listener.onError("Failed to process api request");
            }
        }
    }

}