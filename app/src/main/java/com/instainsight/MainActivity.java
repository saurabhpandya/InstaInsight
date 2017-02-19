//package com.instainsight;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.instainsight.instagram.Instagram;
//import com.instainsight.instagram.InstagramRequest;
//import com.instainsight.instagram.InstagramSession;
//import com.instainsight.instagram.InstagramUser;
//import com.instainsight.instagram.util.Debug;
//
///**
// * Instagram authentication.
// *
// * @author Lorensius W. L. T
// */
//public class MainActivity extends Activity {
//    private InstagramSession mInstagramSession;
//    private Instagram mInstagram;
//
//    private ProgressBar mLoadingPb;
//    private GridView mGridView;
//
//    private static final String CLIENT_ID = "a1521f53d33b4dca82947358c75940da";
//    private static final String CLIENT_SECRET = "8282f6ef07fb4f2cb47caf135053a11c";
//    private static final String REDIRECT_URI = "http://yourcallback.com/";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mInstagram = new Instagram(this, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
//
//        mInstagramSession = mInstagram.getSession();
//
//        if (mInstagramSession.isActive()) {
//            setContentView(R.layout.activity_user);
//
//            InstagramUser instagramUser = mInstagramSession.getUser();
//
//            mLoadingPb = (ProgressBar) findViewById(R.id.pb_loading);
//            mGridView = (GridView) findViewById(R.id.gridView);
//
//            ((TextView) findViewById(R.id.tv_name)).setText(instagramUser.fullName);
//            ((TextView) findViewById(R.id.tv_username)).setText(instagramUser.username);
//
//            ((Button) findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    mInstagramSession.reset();
//
//                    startActivity(new Intent(MainActivity.this, MainActivity.class));
//
//                    finish();
//                }
//            });
//
//            ImageView userIv = (ImageView) findViewById(R.id.iv_user);
//
////            AnimateFirstDisplayListener animate = new AnimateFirstDisplayListener();
//            Glide.with(getApplicationContext()).load(instagramUser.profilPicture).placeholder(R.drawable.ic_user)
//                    .crossFade().into(userIv);
//
//            new DownloadTask().execute();
//
//        } else {
//            setContentView(R.layout.activity_main);
//
////            ((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View arg0) {
////                    mInstagram.authorize(mAuthListener, instagramServices);
////                }
////            });
//        }
//    }
//
//    private void showToast(String text) {
//        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
//    }
//
//    private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {
//        @Override
//        public void onSuccess(InstagramUser user) {
//            finish();
//
//            startActivity(new Intent(MainActivity.this, MainActivity.class));
//        }
//
//        @Override
//        public void onError(String error) {
//            showToast(error);
//        }
//
//        @Override
//        public void onCancel() {
//            showToast("OK. Maybe later?");
//
//        }
//    };
//
////    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
////
////        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
////
////        @Override
////        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
////            if (loadedImage != null) {
////                ImageView imageView = (ImageView) view;
////                boolean firstDisplay = !displayedImages.contains(imageUri);
////                if (firstDisplay) {
////                    FadeInBitmapDisplayer.animate(imageView, 500);
////                    displayedImages.add(imageUri);
////                }
////            }
////        }
////    }
//
//    public class DownloadTask extends AsyncTask<URL, Integer, Long> {
//        ArrayList<String> photoList;
//
//        protected void onCancelled() {
//
//        }
//
//        protected void onPreExecute() {
//
//        }
//
//        protected Long doInBackground(URL... urls) {
//            long result = 0;
//
//            try {
//                List<NameValuePair> params = new ArrayList<NameValuePair>(1);
//
//                params.add(new BasicNameValuePair("count", "10"));
//
//                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
//                String response = request.createRequest("GET", "/users/self/media/recent", params);
//                Debug.i(response);
//                if (!response.equals("")) {
//                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
//                    JSONArray jsonData = jsonObj.getJSONArray("data");
//
//                    int length = jsonData.length();
//
//                    if (length > 0) {
//                        photoList = new ArrayList<String>();
//
//                        for (int i = 0; i < length; i++) {
//                            JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("images").getJSONObject("low_resolution");
//
//                            photoList.add(jsonPhoto.getString("url"));
//                        }
//                    }
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
//            mLoadingPb.setVisibility(View.GONE);
//
//            if (photoList == null) {
//                Toast.makeText(getApplicationContext(), "No Photos Available", Toast.LENGTH_LONG).show();
//            } else {
//                DisplayMetrics dm = new DisplayMetrics();
//
//                getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//                int width = (int) Math.ceil((double) dm.widthPixels / 2);
//                width = width - 50;
//                int height = width;
//
//                PhotoListAdapter adapter = new PhotoListAdapter(MainActivity.this);
//
//                adapter.setData(photoList);
//                adapter.setLayoutParam(width, height);
//
//                mGridView.setAdapter(adapter);
//            }
//        }
//    }
//}