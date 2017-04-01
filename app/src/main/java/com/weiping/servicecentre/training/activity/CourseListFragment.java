package com.weiping.servicecentre.training.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.weiping.servicecentre.training.course.activity.CourseActivity;
import com.weiping.servicecentre.training.view.CourseGridAdapter;

import platform.tyk.weping.com.weipingplatform.R;

public class CourseListFragment extends Fragment {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String[] grid_name = mContext.getResources().getStringArray(R.array.cs_course_grid_name);
        final int[] grid_drawable = new int[]{
                R.drawable.cs_course_grid1,
                R.drawable.cs_course_grid2,
                R.drawable.cs_course_grid3,
                R.drawable.cs_course_grid4,
                R.drawable.cs_course_grid5,
                R.drawable.cs_course_grid6,
                R.drawable.cs_course_grid7,
                R.drawable.cs_course_grid8,
                R.drawable.cs_course_grid9,
                R.drawable.cs_course_grid10,
                R.drawable.cs_course_grid11,
                R.drawable.cs_course_grid12
        };
        // Inflate the layout for this fragment
        FrameLayout frameLayout = (FrameLayout)inflater.inflate(R.layout.fragment_course_list, container, false);

        GridView gridview = (GridView)frameLayout.findViewById(R.id.gridView_courseList);
        gridview.setAdapter(new CourseGridAdapter(mContext, grid_name, grid_drawable));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent courseCategoryIntent = new Intent(getActivity().getBaseContext(), CourseActivity.class);
                courseCategoryIntent.putExtra("categoryID", position);
                startActivity(courseCategoryIntent);
            }
        });

        return frameLayout;
    }

    public void setMContext(Context context){
        mContext = context;
    }
}
