package finnhartshorn.monashlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import finnhartshorn.monashlibrary.Books.InnerBooks.BooksTabFragment;
import layout.Info;
import finnhartshorn.monashlibrary.Locations.LocationsTabFragment;

public class MainMenuActivity extends AppCompatActivity implements OnCompleteListener {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mThumbnailRef = mRootRef.child("thumbnails");


    private DatabaseReference mDatabase;

//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), MainMenuActivity.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }


//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);

//        mAuth = FirebaseAuth.getInstance();
//
//        mAuth.signInAnonymously().addOnCompleteListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check is user is signed in
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser == null) {
//            mAuth.signInAnonymously();
//        }

    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "signInAnonymously:success");            // Log successful sign in
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        } else {
            Log.w(TAG, "signInAnonymously:failure", task.getException());
            updateUI(null);
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "signInAnonymously:success =:= " + user.getEmail());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            if(getArguments().getInt(ARG_SECTION_NUMBER)== 1) {
//                View rootView = inflater.inflate(R.layout.fragment_books, container, false);
//                return rootView;
//            } else if(getArguments().getInt(ARG_SECTION_NUMBER)== 2) {
//                View rootView = inflater.inflate(R.layout.fragment_locations, container, false);
//                return rootView;
//            } else if(getArguments().getInt(ARG_SECTION_NUMBER)== 3) {
//                View rootView = inflater.inflate(R.layout.fragment_info, container, false);
//                return rootView;
//            } else {
//                throw new RuntimeException("Invalid tab");
//            }
//        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[] {"Books", "Locations", "Info"};
        Context context;

        public PagerAdapter(FragmentManager fragmentManager, Context context){
            super(fragmentManager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new BooksTabFragment();
                case 1:
                    return new LocationsTabFragment();
                case 2:
                    return new Info();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(MainMenuActivity.this).inflate(R.layout.blank_tab, null);
            TextView textView = (TextView) tab.findViewById(R.id.testTextView);
            textView.setText(tabTitles[position]);
            return tab;
        }
    }
}
