package finnhartshorn.monashlibrary.Books;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import finnhartshorn.monashlibrary.R;

public class BookDetailsActivity extends AppCompatActivity {

    Book displayBook;

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genreTextView;
    private TextView ISBNTextView;
    private TextView yearTextView;
    private ImageView coverImageView;
//    private TextView TextView;

    private StorageReference thumbnailsReference = FirebaseStorage.getInstance().getReference().child("cover-images");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Get references to textViews
        titleTextView = (TextView) findViewById(R.id.detail_title_textView);
        authorTextView = (TextView) findViewById(R.id.detail_author_textView);
        genreTextView = (TextView) findViewById(R.id.detail_genre_textView);
        ISBNTextView = (TextView) findViewById(R.id.detail_ISBN_textView);
        yearTextView = (TextView) findViewById(R.id.detail_year_textView);
        // And imageView
        coverImageView = (ImageView) findViewById(R.id.book_cover_imageView);

        Intent intent = getIntent();
        displayBook = intent.getParcelableExtra("Book");

        titleTextView.setText(displayBook.getTitle());
        authorTextView.setText(displayBook.getAuthor());
        genreTextView.setText("PLACEHOLDER");
        ISBNTextView.setText(displayBook.getISBN());
        yearTextView.setText(displayBook.getPublicationDate());             // TODO: Get year from publication date; use date format to store?

        StorageReference coverReference = thumbnailsReference.child(displayBook.getThumbnail());          //TODO: This could fail, handle that

        Glide.with(this)        // Glide caches images, which will reduce data footprint
                .using(new FirebaseImageLoader())
                .load(coverReference)
                .into(coverImageView);

        // Gets reference to action bar and enables up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
