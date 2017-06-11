package finnhartshorn.monashlibrary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import finnhartshorn.monashlibrary.books.BookSearchActivity;
import finnhartshorn.monashlibrary.books.BooksTabFragment;
import finnhartshorn.monashlibrary.books.LoanViewActivity;
import finnhartshorn.monashlibrary.locations.LocationsTabFragment;

public class MainMenuActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

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
//    private IProfile mSignOut;

    private Drawer mDrawer;
    PrimaryDrawerItem mLogin;
    PrimaryDrawerItem mLoans;

    BooksTabFragment bookFragment;

    private ProgressDialog mProgressDialog;

    private ViewPager mViewPager;

    // Stores a reference to menu so the search icon can be hidden when not in the books tab
    private Menu menu = null;

    // Book fragment is stored so it can be updated if the logged in user changes
    private BooksTabFragment mBookTabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Logging In");
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);

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
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Setup account drawer
        mAnonAccount = new ProfileDrawerItem().withName("Anonymous Account").withEmail("Anonymous Account").withIcon(R.drawable.ic_account_circle_24dp);
        mChangeAccount = new ProfileSettingDrawerItem().withName("Change Account").withIcon(R.drawable.ic_account_box_24dp).withIdentifier(CHANGE_PROFILE);


        mAccountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        mAnonAccount,
                        mChangeAccount
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        if (profile instanceof IDrawerItem) {
                            switch ((int) profile.getIdentifier()) {
                                case CHANGE_PROFILE:
                                    changeAccount();
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .build();


        mLogin = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.login_option).withIcon(R.drawable.ic_account_box_24dp).withSelectable(false).withIdentifier(SIGN_IN);
        mLoans = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.loans_option).withIcon(R.drawable.ic_loans_24dp).withIconTintingEnabled(true).withIdentifier(LOANS);

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
                                openLoans();
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    private void openLoans() {
        Intent newIntent = new Intent(this, LoanViewActivity.class);
        startActivity(newIntent);
    }


    @Override
    public void onStart() {
        super.onStart();

//        mAuth.signOut();            // Just for testing

        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            signInAnonymously();
        } else if (currentUser.isAnonymous()) {
            updateUI(null);
        } else {
            updateUI(currentUser);
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
        if (menu != null) {
            MenuItem search = menu.findItem(R.id.action_search);
            search.setVisible(visibility);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Log.d(TAG, "Search attempted");
            Intent searchIntent = new Intent(this, BookSearchActivity.class);
            startActivity(searchIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Users are signed in anonymously the first time they use the app, if they sign up later their new and anonymous accounts are linked together.
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInAnonymously:success");            // Log successful sign in
                    updateUI(null);
                } else {
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    updateUI(null);
                }
            }
        });
    }

    // Signs out and then signs back in
    private void changeAccount() {
        signOut();
        signIn();
    }

    private void signIn() {
        Log.d(TAG, "Signing In");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGNIN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        mAuth.signOut();
                        updateUI(null);
                        Toast.makeText(MainMenuActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Handles result of google signin activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mProgressDialog.show();

        if (requestCode == GOOGLE_SIGNIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                final GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

                OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(MainMenuActivity.this, "Signed in to " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            updateUI(user);
                            bookFragment.addLoanRange();
                        } else {
                            Log.d(TAG, "Fail: ", task.getException());
                            updateUI(null);
                        }
                    }
                };
                if (mAuth.getCurrentUser() != null) {
                    mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(this, listener);            // If a user exists, they are anonymous
                } else {
                    mAuth.signInWithCredential(credential).addOnCompleteListener(this, listener);                           // If the user is null, they are switching accounts
                }
            } else {
                Log.d(TAG, "Unsuccessful Login: " + result.getStatus().getStatusMessage());
            }

        }
    }

    private void updateUI(FirebaseUser account) {
        mAccountHeader.clear();             // Clear accounts and items in drawer
        mDrawer.removeAllItems();
        if (account != null) {
            Log.d(TAG, "New profile: " + account.getEmail());
            IProfile newProfile = new ProfileDrawerItem()
                    .withNameShown(true)
                    .withName(account.getDisplayName())
                    .withEmail(account.getEmail())
                    .withIdentifier(100);
            mAccountHeader.addProfiles(newProfile, mChangeAccount);
            mDrawer.addItems(mLoans);
        } else {
            mAccountHeader.addProfiles(mAnonAccount);
            mDrawer.addItems(mLogin);

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
    }

    // Pager adapter is used to instantiate pages inside a ViewPager
    private class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[] {"Books", "Locations", "Info"};
        Context context;

        PagerAdapter(FragmentManager fragmentManager, Context context){
            super(fragmentManager);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    bookFragment = new BooksTabFragment();
                    return bookFragment;
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

        View getTabView(int position) {
            View tab = LayoutInflater.from(MainMenuActivity.this).inflate(R.layout.blank_tab, null);
            TextView textView = (TextView) tab.findViewById(R.id.testTextView);
            textView.setText(tabTitles[position]);
            return tab;
        }
    }
}
