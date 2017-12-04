package com.example.bryan_2.geekout_sqltest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by narjitsingh on 12/3/17.
 */
public class MainActivity extends Activity {
    private ViewPager pageNumber;
    private ViewPagerAdapter pageAdapter;
    private int[] allSlides;
    private Button skipButton;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.tutorial_slide);

        pageNumber = (ViewPager) findViewById(R.id.view_pager);
        skipButton = (Button) findViewById(R.id.btn_skip);
        nextButton = (Button) findViewById(R.id.btn_next);

        allSlides = new int[]{R.layout.slide1, R.layout.slide2, R.layout.slide3, R.layout.slide6, R.layout.slide4, R.layout.slide5, R.layout.slide7};

        pageAdapter = new ViewPagerAdapter();
        pageNumber.setAdapter(pageAdapter);
        pageNumber.addOnPageChangeListener(pagelistner);
    }

    public  void skipButtonClick(View v) {
        goHome();
    }

    public  void moveToNextSlide(View v) {
        int i = getItem(1);
        if (i < allSlides.length) {
            pageNumber.setCurrentItem(i);
        } else {
            goHome();
        }
    }

    ViewPager.OnPageChangeListener pagelistner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int index) {
            if (index == allSlides.length - 1) {
                nextButton.setText(getString(R.string.start));
                skipButton.setVisibility(View.GONE);
            } else {
                nextButton.setText(getString(R.string.next));
                skipButton.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    private int getItem(int i) {
        return pageNumber.getCurrentItem() + i;
    }

    private void goHome() {
        startActivity(new Intent(this, StartMenu.class));
        finish();
    }
    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        public ViewPagerAdapter() {

        }
        @Override
        public Object instantiateItem(ViewGroup group, int pos) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(allSlides[pos], group, false);
            group.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return allSlides.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
