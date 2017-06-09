package finnhartshorn.monashlibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import finnhartshorn.monashlibrary.books.BookSearchActivity;
import finnhartshorn.monashlibrary.books.BooksTabFragment;
import layout.Info;
import finnhartshorn.monashlibrary.locations.LocationsTabFragment;

public class MainMenuActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        OnCompleteListener {

    private static final String TAG = "MainActivity";

    // Identifiers for drawer actions
    private static final int SIGN_IN = 100;
    private static final int CHANGE_PROFILE= 101;
    private static final int SIGN_OUT = 102;
    private static final int LOANS = 103;

    private static final int GOOGLE_SIGNIN = 1001;

    private FirebaseAuth mAuth;

    //Store Google signin api
    GoogleApiClient mGoogleApiClient;


    // Store drawer and drawer account header, allowing this to be changed when the current account changes
    private AccountHeader mAccountHeader;
    private IProfile mAnonAccount;
    private IProfile mChangeAccount;
    private IProfile mSignOut;

    private Drawer mDrawer;
    PrimaryDrawerItem mLogin;
    PrimaryDrawerItem mLoans;
//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//    private DatabaseReference mThumbnailRef = mRootRef.child("thumbnails");


//    private DatabaseReference mDatabase;

    private ViewPager mViewPager;

    // Stores a reference to menu so the search icon can be hidden when not in the books tab
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        PagerAdapter pagerAdapter =
                new PagerAdapter(getSupportFragmentManager(), MainMenuActivity.this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {                // Adds a Page listener to react whenever the active tab changes

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {                      // If the current tab is not the book tab, hide the search icon
                if (position == 0) {
                    setSearchVisibility(true);
                } else {
                    setSearchVisibility(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth.signInAnonymously().addOnCompleteListener(this);

        // Setup account drawer
        mAnonAccount = new ProfileDrawerItem().withName("Anonymous Account").withEmail("Anonymous Account").withIcon(R.drawable.ic_account_circle_24dp);
        mChangeAccount = new ProfileSettingDrawerItem().withName("Change Account").withIcon(R.drawable.ic_account_box_24dp).withIdentifier(CHANGE_PROFILE);
        mSignOut = new ProfileSettingDrawerItem().withName("Logout").withIcon(R.drawable.ic_logout_24dp).withIdentifier(SIGN_OUT);


        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        mAnonAccount,
                        new ProfileSettingDrawerItem().withName("Change Account").withIcon(R.drawable.ic_account_box_24dp).withIdentifier(CHANGE_PROFILE),
                        new ProfileSettingDrawerItem().withName("Logout").withIcon(R.drawable.ic_logout_24dp).withIdentifier(SIGN_OUT)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        if (profile instanceof IDrawerItem) {
                            switch ((int) profile.getIdentifier()) {
                                case CHANGE_PROFILE:
                                    changeAccount();
                                    break;
                                case SIGN_OUT:
                                    signOut();
                            }
                        }
                        return false;
                    }
                })
                .build();


        mLogin = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.login_option).withIcon(R.drawable.ic_account_box_24dp).withIdentifier(SIGN_IN);
        mLoans = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.loans_option).withIcon(R.drawable.ic_loans_24dp).withIdentifier(LOANS);
//        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.logout_option);

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withAccountHeader(mAccountHeader)
                .addDrawerItems(
                       mLogin
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case SIGN_IN:
                                signIn();
                                break;
                            case LOANS:
                                // TODO: Implement loans view
                        }
                        return false;
                    }
                })
                .build();

//        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Attempt to signin (will succeed if user has signed in previously
        OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (pendingResult.isDone()) {
            updateAccount(pendingResult.get().getSignInAccount());
        } else {   // If there isn't a result immediately, wait until there is
//            showProgressIndicator();          // TODO: Progress indicator
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    updateAccount(googleSignInResult.getSignInAccount());
                }
            });
        }

        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            mAuth.signInAnonymously();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        this.menu = menu;
        return true;
    }

    private void setSearchVisibility(boolean visibility) {
        MenuItem search = menu.findItem(R.id.action_search);
        search.setVisible(visibility);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Log.d(TAG, "Search attempted");
            Intent searchIntent = new Intent(this, BookSearchActivity.class);
            startActivity(searchIntent);
        } else if (id == R.id.action_settings) {
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

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this);
    }

    // Signs out and then signs back in
    private void changeAccount() {
        signOut();
        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGNIN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateAccount(null);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "ActivityResult: " + requestCode);

        if (requestCode == GOOGLE_SIGNIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                updateAccount(account);
            } else {
                Log.d(TAG, "Unsuccessful Login: " + result.getStatus().getStatusMessage());
            }
        }
    }

    private void updateAccount(GoogleSignInAccount account) {
        mAccountHeader.clear();             // Clear accounts and items in drawer
        mDrawer.removeAllItems();
        if (account != null) {
            Log.d(TAG, "New profile: " + account.getEmail());
            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName(account.getDisplayName()).withEmail(account.getEmail()).withIcon(account.getPhotoUrl()).withIdentifier(100);
            mAccountHeader.addProfiles(newProfile, mChangeAccount, mSignOut);
            mDrawer.addItems(mLoans);
        } else {
            mAccountHeader.addProfiles(mAnonAccount);
            mDrawer.addItems(mLogin);

        }

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "signInAnonymously:success =:= " + user.getEmail());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    }

    // Pager adapter is used to instantiate pages inside a ViewPager
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
