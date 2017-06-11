package finnhartshorn.monashlibrary;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Info extends Fragment {

    private static final String TAG = "Info";




    public Info() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }


    // https://stackoverflow.com/questions/33779607/reading-a-txt-file-and-outputing-as-a-textview-in-android
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView textView =  (TextView) getView().findViewById(R.id.info_text_view);
        AssetManager assetManager = getContext().getAssets();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(assetManager.open("licenses_and_attributions.txt")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException error) {
            Log.d(TAG, "Error opening info file", error);
            Toast.makeText(getContext(), "Error opening info file", Toast.LENGTH_LONG).show();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.d(TAG, "Error closing reader", e);
                }
            }
        }

        textView.setText(stringBuilder);
    }
}
