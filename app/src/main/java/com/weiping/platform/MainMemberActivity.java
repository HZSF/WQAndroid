package com.weiping.platform;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.weiping.credential.activity.UserInfoFormActivity;
import com.weiping.credential.tasks.GetUserInfoImgTask;
import com.weiping.credential.tasks.LogoutTask;
import com.weiping.platform.adapter.NavDrawerListAdapter;
import com.weiping.platform.fragments.AnnFeeMoFragment;
import com.weiping.platform.fragments.DashboardFragment;
import com.weiping.platform.fragments.MemberActFragment;
import com.weiping.platform.fragments.MyAppointFragment;
import com.weiping.platform.fragments.PatentTradeFragment;
import com.weiping.platform.fragments.PersonalInfoFragment;
import com.weiping.platform.fragments.RecommendFragment;
import com.weiping.platform.fragments.TrademarkMonitorFragment;
import com.weiping.platform.fragments.TrademarkTradeFragment;
import com.weiping.platform.model.NavDrawerItem;
import com.weiping.servicecentre.Constants;
import com.weiping.servicecentre.announcement.activity.AnnounceCentreActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import platform.tyk.weping.com.weipingplatform.R;

public class MainMemberActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        setContentView(R.layout.activity_main_member);

        File cacheDir = getBaseContext().getCacheDir();

        getActionBar().setIcon(R.mipmap.ic_portrait);
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_FILE, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.SHARE_PREFERENCE_USERNAME, "");
        File f = new File(cacheDir, "portrait_"+username);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        getActionBar().setIcon(d);

        GetUserInfoImgTask task = new GetUserInfoImgTask(this);
        task.execute(Constants.URL_GET_USER_INFO_IMG);

        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    @Override
    public NavDrawerListAdapter getNavDrawerListAdapter(){
        ArrayList<NavDrawerItem> items = new ArrayList<>();
        for (int i=0; i<navMenuTitles.length; i++) {
            NavDrawerItem item = new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1));
            items.add(item);
        }
        return new NavDrawerListAdapter(this, items);
    }

    public void onSectionAttached(int number) {
        mTitle = navMenuTitles[number-1];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(mTitle);
    }

    public void onClickAnnounce(View view){
        Intent intent_announcement = new Intent(getBaseContext(), AnnounceCentreActivity.class);
        startActivity(intent_announcement);
    }

    public void onClickLogout(View view){
        LogoutTask task = new LogoutTask(this);
        task.setIndicator(1);
        task.execute(Constants.URL_LOGOUT);
    }

    public void onClickPersonalInfo(View view){
        Intent intent = new Intent(getBaseContext(), UserInfoFormActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_member, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
       //     return true;
       // }

        return super.onOptionsItemSelected(item);
    }
*/
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            switch (sectionNumber) {
                case 1:
                    fragment = new DashboardFragment();
                    break;
                case 2:
                    fragment = new MyAppointFragment();
                    break;
                case 3:
                    fragment = new AnnFeeMoFragment();
                    break;
                case 4:
                    fragment = new TrademarkMonitorFragment();
                    break;
                case 5:
                    fragment = new PatentTradeFragment();
                    break;
                case 6:
                    fragment = new TrademarkTradeFragment();
                    break;
                case 8:
                    fragment = new MemberActFragment();
                    break;
                case 9:
                    fragment = new RecommendFragment();
                    break;
                case 10:
                    fragment = new PersonalInfoFragment();
                    break;
            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_member, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainMemberActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }
    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getFragmentManager();
        PlaceholderFragment currentFragment = (PlaceholderFragment)fragmentManager.findFragmentById(R.id.container);
        if(currentFragment instanceof DashboardFragment){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.lbl_click_again_exit), Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(mRunnable, 2000);
        } else {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, PlaceholderFragment.newInstance(1));
            ft.commit();
            //finish();
            //startActivity(getIntent());
        }
    }

}
