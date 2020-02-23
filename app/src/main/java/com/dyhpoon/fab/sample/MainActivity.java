package com.dyhpoon.fab.sample;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.text.method.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.dyhpoon.fab.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

public class MainActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    @SuppressWarnings("deprecation")
    private void initActionBar()
	{
        if (getSupportActionBar() != null)
		{
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab()
							 .setText("ListView")
							 .setTabListener(new ActionBar.TabListener() {
									 @Override
									 public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
										 fragmentTransaction.replace(android.R.id.content, new ListViewFragment());
									 }

									 @Override
									 public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
									 }

									 @Override
									 public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
									 }
								 }));
            actionBar.addTab(actionBar.newTab()
							 .setText("RecyclerView")
							 .setTabListener(new ActionBar.TabListener() {
									 @Override
									 public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
										 fragmentTransaction.replace(android.R.id.content, new RecyclerViewFragment());
									 }

									 @Override
									 public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
									 }

									 @Override
									 public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
									 }
								 }));
            actionBar.addTab(actionBar.newTab()
							 .setText("ScrollView")
							 .setTabListener(new ActionBar.TabListener() {
									 @Override
									 public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
										 fragmentTransaction.replace(android.R.id.content, new ScrollViewFragment());
									 }

									 @Override
									 public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
									 }

									 @Override
									 public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
									 {
									 }
								 }));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.about:
				TextView content = (TextView) getLayoutInflater().inflate(R.layout.about_view, null);
				content.setMovementMethod(LinkMovementMethod.getInstance());
				content.setText(Html.fromHtml(getString(R.string.about_body)));
				new AlertDialog.Builder(this)
					.setTitle(R.string.about)
					.setView(content)
					.setInverseBackgroundForced(true)
					.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					}).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

	private static final long delay = 2000L;
    private boolean mRecentlyBackPressed = false;
    private Handler mExitHandler = new Handler();
    private Runnable mExitRunnable = new Runnable() {

        @Override
        public void run()
		{
            mRecentlyBackPressed = false;   
        }
    };

	@Override
	public void onBackPressed()
	{
		// TODO: Implement this method
		// 16 JUN 2019 https://stackoverflow.com/questions/8430805/clicking-the-back-button-twice-to-exit-an-activity
		//You may also add condition if (doubleBackToExitPressedOnce || fragmentManager.getBackStackEntryCount() != 0) // in case of Fragment-based add
		if (mRecentlyBackPressed)
		{
			mExitHandler.removeCallbacks(mExitRunnable);
			mExitHandler = null;
			super.onBackPressed();
		}
		else
		{
			mRecentlyBackPressed = true;
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
			mExitHandler.postDelayed(mExitRunnable, delay);
		}
	}
	
    public static class ListViewFragment extends Fragment
	{

        @SuppressLint("InflateParams")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
            View root = inflater.inflate(R.layout.fragment_listview, container, false);

            ListView list = (ListView) root.findViewById(android.R.id.list);
            ListViewAdapter listAdapter = new ListViewAdapter(getActivity(),
															  getResources().getStringArray(R.array.countries));
            list.setAdapter(listAdapter);

            FloatingActionsMenu menu = (FloatingActionsMenu) root.findViewById(R.id.floating_menu);
            menu.attachToListView(list, new ScrollDirectionListener() {
					@Override
					public void onScrollDown()
					{
						Log.d("ListViewFragment", "onScrollDown()");
					}

					@Override
					public void onScrollUp()
					{
						Log.d("ListViewFragment", "onScrollUp()");
					}
				}, new AbsListView.OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState)
					{
						Log.d("ListViewFragment", "onScrollStateChanged()");
					}

					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
					{
						Log.d("ListViewFragment", "onScroll()");
					}
				});

            return root;
        }
    }

    public static class RecyclerViewFragment extends Fragment
	{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
            View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);

            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), getResources()
																  .getStringArray(R.array.countries));
            recyclerView.setAdapter(adapter);

            FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
            fab.attachToRecyclerView(recyclerView);

            return root;
        }
    }

    public static class ScrollViewFragment extends Fragment
	{
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
            View root = inflater.inflate(R.layout.fragment_scrollview, container, false);

            ObservableScrollView scrollView = (ObservableScrollView) root.findViewById(R.id.scroll_view);
            LinearLayout list = (LinearLayout) root.findViewById(R.id.list);

            String[] countries = getResources().getStringArray(R.array.countries);
            for (String country : countries)
			{
                TextView textView = (TextView) inflater.inflate(R.layout.list_item, container, false);
                String[] values = country.split(",");
                String countryName = values[0];
                int flagResId = getResources().getIdentifier(values[1], "drawable", getActivity().getPackageName());
                textView.setText(countryName);
                textView.setCompoundDrawablesWithIntrinsicBounds(flagResId, 0, 0, 0);

                list.addView(textView);
            }

            FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
            fab.attachToScrollView(scrollView);

            return root;
        }
    }
}
