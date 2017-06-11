package finnhartshorn.monashlibrary.books;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import finnhartshorn.monashlibrary.R;
import finnhartshorn.monashlibrary.model.Book;
import finnhartshorn.monashlibrary.model.Loan;

public class LoanViewActivity extends AppCompatActivity implements ValueEventListener {

    private static final String TAG = "LoanViewActivity";

    private RecyclerView mRecyclerView;
    private LoanViewAdapter mAdapter;

    private FirebaseAuth mAuth;

    // Stores reference to current query
    private Query mLoanQuery;

    // Database references
    private DatabaseReference mRootRef;
    private DatabaseReference mBookRef;

    private ArrayList<Book> mBooklist = new ArrayList<>();
    private HashMap<Book, Loan> mLoanMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure recyclerview
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.loans_recycler_view);
        recyclerView.setHasFixedSize(true);

        // Setup recyclerview adapter
        mAdapter = new LoanViewAdapter(this, mBooklist, mLoanMap);
        recyclerView.setAdapter(mAdapter);

        // Set the layout of the recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Get reference to firebase authentication
        mAuth = FirebaseAuth.getInstance();

        // Get reference to firebase database and set default query
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mBookRef = mRootRef.child("books");
        mLoanQuery = mRootRef.child("users").child(mAuth.getCurrentUser().getUid());
        mLoanQuery.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {               // Updates the loan view if the loan data changes
        mBooklist = new ArrayList<>();
        for (DataSnapshot loanSnapShot : dataSnapshot.getChildren()) {

            Loan loan = loanSnapShot.getValue(Loan.class);
            Book book = loanSnapShot.child("book").getValue(Book.class);
            mBooklist.add(book);
            mLoanMap.put(book, loan);
        }

        mAdapter.updateBookData(mBooklist, mLoanMap);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "Failed loading loans", databaseError.toException());
        Toast.makeText(this, "Error loading loans from database", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
