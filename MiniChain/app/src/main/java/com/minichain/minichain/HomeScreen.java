
package com.minichain.minichain;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private SliderAdapter sliderAdapter;

    private Button nxtButton;
    private Button prevButton;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        nxtButton = (Button)findViewById(R.id.nxtButton);
        prevButton = (Button)findViewById(R.id.prevButton);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        //OnClickListeners

        nxtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPage  == mDots.length - 1){
                    Intent launch = new Intent(HomeScreen.this, MainActivity.class);
                    startActivity(launch);
                }
                mSlideViewPager.setCurrentItem(currentPage+1);
            }
        });


        prevButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mSlideViewPager.setCurrentItem(currentPage-1);
            }
      });
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for(int i = 0; i< mDots.length; i++) {


            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226",Html.FROM_HTML_MODE_LEGACY));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);

        }
        if (mDots.length > 0){
            mDots[position].setTextColor(getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentPage = i;
            if(i==0){
                nxtButton.setEnabled(true);
                prevButton.setEnabled(false);
                prevButton.setVisibility(View.INVISIBLE);

                nxtButton.setText("Next");
                prevButton.setText("");
            }
            else if (i == mDots.length - 1){
                nxtButton.setEnabled(true);
                prevButton.setEnabled(true);
                prevButton.setVisibility(View.VISIBLE);

                nxtButton.setText("START");
                prevButton.setText("BACK");
            }
            else{
                nxtButton.setEnabled(true);
                prevButton.setEnabled(true);
                prevButton.setVisibility(View.VISIBLE);

                nxtButton.setText("Next");
                prevButton.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

}
