package com.instainsight.instagram;

import android.content.Context;

import com.instainsight.instagram.util.Cons;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;


/**
 * Instragam main class.
 *
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 */
public class Instagram {

    @Inject
    InstagramServices instagramServices;
    private String TAG = Instagram.class.getSimpleName();
    private Context mContext;
    private InstagramDialog mDialog;
    private InstagramAuthListener mListener;
    private InstagramSession mSession;
    private String mClientId;
    private String mClientSecret;
    private String mRedirectUri;

    /**
     * Instantiate new object of this class.
     *
     * @param context      Context
     * @param clientId     Client id
     * @param clientSecret Client secret
     * @param redirectUri  Redirect uri
     */
    public Instagram(Context context, String clientId, String clientSecret, String redirectUri) {
        mContext = context;

        mClientId = clientId;
        mClientSecret = clientSecret;
        mRedirectUri = redirectUri;

        String authUrl = Cons.AUTH_URL + "client_id=" + mClientId + "&redirect_uri=" + mRedirectUri + "&response_type=code&scope=basic+public_content+follower_list+comments+relationships+likes";

        mSession = new InstagramSession(context);

        mDialog = new InstagramDialog(context, authUrl, redirectUri, new InstagramDialog.InstagramDialogListener() {

            @Override
            public void onSuccess(String code) {
                retreiveAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onError(error);
                resetSession();
            }

            @Override
            public void onCancel() {
                mListener.onCancel();
                resetSession();

            }
        });
    }

    /**
     * Authorize user.
     *
     * @param listener          Auth listner
     * @param instagramServices
     */
    public void authorize(InstagramAuthListener listener, InstagramServices instagramServices) {
        mListener = listener;
        this.instagramServices = instagramServices;
        mDialog.show();
    }

    /**
     * Reset session.
     */
    public void resetSession() {
        mSession.reset();

        mDialog.clearCache();
    }

    /**
     * Get session.
     *
     * @return Instagram session.
     */
    public InstagramSession getSession() {
        return mSession;
    }

    /**
     * Retreive access token.
     *
     * @param code
     */
    private void retreiveAccessToken(String code) {
        instagramServices.getInstagramUser(mClientId, mClientSecret, mRedirectUri, code)
                .subscribe(new Consumer<InstagramUser>() {
                    @Override
                    public void accept(InstagramUser instagramUser) throws Exception {
                        mSession.store(instagramUser);

                        mListener.onSuccess(instagramUser);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

//        new AccessTokenTask(code).execute();
    }

    public interface InstagramAuthListener {
        public abstract void onSuccess(InstagramUser user);

        public abstract void onError(String error);

        public abstract void onCancel();
    }

//    public class AccessTokenTask extends AsyncTask<URL, Integer, Long> {
//        ProgressDialog progressDlg;
//        InstagramUser user;
//        String code;
//
//        public AccessTokenTask(String code) {
//            this.code = code;
//
//            progressDlg = new ProgressDialog(mContext);
//
//            progressDlg.setMessage("Getting access token...");
//        }
//
//        protected void onCancelled() {
//            progressDlg.cancel();
//        }
//
//        protected void onPreExecute() {
//            progressDlg.show();
//        }
//
//        protected Long doInBackground(URL... urls) {
//            long result = 0;
//
//            try {
//                List<NameValuePair> params = new ArrayList<NameValuePair>(5);
//
//                params.add(new BasicNameValuePair("client_id", mClientId));
//                params.add(new BasicNameValuePair("client_secret", mClientSecret));
//                params.add(new BasicNameValuePair("grant_type", "authorization_code"));
//                params.add(new BasicNameValuePair("redirect_uri", mRedirectUri));
//                params.add(new BasicNameValuePair("code", code));
//
//                InstagramRequest request = new InstagramRequest();
//                String response = request.post(Cons.ACCESS_TOKEN_URL, params);
//
//                if (!response.equals("")) {
//                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
//                    JSONObject jsonUser = jsonObj.getJSONObject("user");
//
//                    user = new InstagramUser();
//
//                    user.accessToken = jsonObj.getString("access_token");
//
//                    user.id = jsonUser.getString("id");
//                    user.username = jsonUser.getString("username");
//                    user.fullName = jsonUser.getString("full_name");
//                    user.profilPicture = jsonUser.getString("profile_picture");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return result;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//        }
//
//        protected void onPostExecute(Long result) {
//            progressDlg.dismiss();
//
//            if (user != null) {
//                mSession.store(user);
//
//                mListener.onSuccess(user);
//            } else {
//                mListener.onError("Failed to get access token");
//            }
//        }
//    }
}