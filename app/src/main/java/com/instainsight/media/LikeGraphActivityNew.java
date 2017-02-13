package com.instainsight.media;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.instainsight.InstaInsightApp;
import com.instainsight.R;
import com.instainsight.ViewModelActivity;
import com.instainsight.databinding.ActivityLikeGraphNewBinding;
import com.instainsight.media.models.RecentMediaBean;
import com.instainsight.media.viewmodel.RecentMediaViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

public class LikeGraphActivityNew extends ViewModelActivity {

    @Inject
    public RecentMediaViewModel recentMediaViewModel;
    private String TAG = LikeGraphActivityNew.class.getSimpleName();
    private ActivityLikeGraphNewBinding activityLikeGraphNewBinding;
    private int[] mColors = new int[]{
            Color.rgb(137, 230, 81)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((InstaInsightApp) getApplication()).getComponent().inject(LikeGraphActivityNew.this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.lbl_likegraphs);
        initActionbar();
        activityLikeGraphNewBinding = DataBindingUtil.setContentView(this, R.layout.activity_like_graph_new);
        activityLikeGraphNewBinding.setRecentMedia(recentMediaViewModel);
        recentMediaViewModel.getRecentMediaLikes();
    }


    private void initActionbar() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.base));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void createViewModel() {
        mViewModel = recentMediaViewModel;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RecentMediaEvent recentMediaEvent) {
        if (recentMediaEvent != null && recentMediaEvent.getAryLstRecentMedia() != null) {
            ArrayList<RecentMediaBean> arylstRecentMedia = recentMediaEvent.getAryLstRecentMedia();
            Log.d(TAG, "onEvent::arylstRecentMedia:" + arylstRecentMedia.size());
            init(arylstRecentMedia);
        } else {

        }
    }

    private void init(ArrayList<RecentMediaBean> aryLstLikes) {
        activityLikeGraphNewBinding.linechart.setDrawGridBackground(false);

        // no description text
        activityLikeGraphNewBinding.linechart.getDescription().setEnabled(false);

        // enable touch gestures
        activityLikeGraphNewBinding.linechart.setTouchEnabled(true);

        // enable scaling and dragging
        activityLikeGraphNewBinding.linechart.setDragEnabled(false);
        activityLikeGraphNewBinding.linechart.setScaleEnabled(false);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        activityLikeGraphNewBinding.linechart.setPinchZoom(false);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = activityLikeGraphNewBinding.linechart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        LimitLine ll1 = new LimitLine(20f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
//        ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(0f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
//        ll2.setTypeface(tf);

        YAxis leftAxis = activityLikeGraphNewBinding.linechart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);

        ArrayList<RecentMediaBean> aryLstMedia = aryLstLikes;

//        MediaBean mediaBean = Collections.max(aryLstMedia, new Comparator<MediaBean>() {
//            @Override
//            public int compare(MediaBean mediaBean, MediaBean t1) {
//                return t1.getLikes().compareTo(mediaBean.getLikes());
//            }
//        });


        float xMax = 0f;
        if (aryLstMedia != null && aryLstMedia.size() > 0) {
            xMax = Float.parseFloat(aryLstMedia.get(0).getLikesBean().getCount());
        }
        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        activityLikeGraphNewBinding.linechart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(aryLstMedia);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        activityLikeGraphNewBinding.linechart.animateX(1000);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = activityLikeGraphNewBinding.linechart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }

    private void setData(ArrayList<RecentMediaBean> aryLstLikes) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < aryLstLikes.size(); i++) {
            if (i >= 7)
                break;
            values.add(new Entry(i, Integer.parseInt(aryLstLikes.get(i).getLikesBean().getCount())));
        }

//        values.add(new Entry(0, 5));
//        values.add(new Entry(1, 10));
//        values.add(new Entry(2, 7));
//        values.add(new Entry(3, 20));

//        for (int i = 0; i < count; i++) {
//
//            float val = (float) (Math.random() * range) + 3;
//            values.add(new Entry(i, val));
//        }

        LineDataSet set1;

        if (activityLikeGraphNewBinding.linechart.getData() != null &&
                activityLikeGraphNewBinding.linechart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) activityLikeGraphNewBinding.linechart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            activityLikeGraphNewBinding.linechart.getData().notifyDataChanged();
            activityLikeGraphNewBinding.linechart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            activityLikeGraphNewBinding.linechart.setData(data);
        }
    }
}
