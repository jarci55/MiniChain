package com.minichain.minichain;

import android.content.Context;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    private static final String TAG ="SliderView";

    public Context context;
    public LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int [] slide_images = {
            R.mipmap.icon_round,
            R.mipmap.icon_round,
            R.mipmap.icon_round,
    };


    public String [] slide_headings ={

            "MiniChain",
            "SIMULATE",
            "OBSERVE"
    };

    public String [] slide_desc ={
            "The  MiniChain application is a mobile device blockchain simulation testing environment. It was created as a part of a Master's degree study conducted by Jaroslav Sak",
            "This Android mobile device application enables to run simulation of blockchain network, with pre-set attributes and compare these scenarios to expected outcome contained in files stored in internal storage directories.",
            "As well as pre-set scenarios we are able to create a custom one and observe the signals flowing through the network, thus observing the blockchain network behaviour and tests its capabilities"

    };



    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout) o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        Log.d(TAG, "instantiateItem: called");

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);

        slideImageView.setImageResource(slide_images[position ]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}
