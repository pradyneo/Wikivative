package com.prady.wikivative.activity;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.prady.wikivative.R;
import com.prady.wikivative.fragment.ArticleFragment;
import com.prady.wikivative.fragment.SearchFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements
        SearchFragment.OnSearchButtonClickListener{


    private static final int internet_permission = 1;

    private String wikiWebserviceUri = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=json&titles=";
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static ArrayList<String> permission_request_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            permission_request_list.add(Manifest.permission.READ_CONTACTS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            permission_request_list.add(Manifest.permission.INTERNET);
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},internet_permission);
        }

        if(permission_request_list.size()>0){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, internet_permission);
        }

        if (findViewById(R.id.search_fragment_container)!= null){
            SearchFragment searchFragment = new SearchFragment();

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction
                    .add(R.id.search_fragment_container, searchFragment)
                    .commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch (requestCode){
            case internet_permission:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Contacts permission not graned", Toast.LENGTH_LONG).show();
                }

                /*if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Internet Permission not granted", Toast.LENGTH_SHORT).show();
                }*/
        }
    }

    @Override
    public void onSearchButtonClicked(String query) {
//        String query = ((EditText)findViewById(R.id.search_bar)).getText().toString();
        Log.d(TAG, "onSearchButtonClicked: Query: " + query);
        wikiWebserviceUri = wikiWebserviceUri + query;
        new AsyncTask<String, Void, String>(){

            @Override
            protected String doInBackground(String... strings) {
                Log.d(TAG, "doInBackground: calling the wikipedia api");
                String uri = strings[0];
                try {
                    Log.d(TAG, "doInBackground: URI = " + uri);
                    URL url = new URL(uri);
                    URLConnection urlConnection = url.openConnection();

                    String line = "";
                    InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
                    Log.d(TAG, "doInBackground: inputStream: " + new BufferedReader(inputStream).readLine());
                    JsonReader reader = new JsonReader(inputStream);
                    reader.beginArray();
                    while (reader.hasNext()){
                        Log.d(TAG, "doInBackground: Tag Name: " + reader.nextName());
                    }
                    reader.endArray();
                    Log.d(TAG, "doInBackground: Reading result complete");
//                    if (bufferedReader.readLine() == null)
//                        Log.d(TAG, "doInBackground: Null value");
//                    while(bufferedReader.readLine() != null){
//                        line = bufferedReader.readLine();
//                        Log.d(TAG, "doInBackground: Line: " + line);
//                    }

                } catch (MalformedURLException e) {
                    Log.d(TAG, "doInBackground: MalformedURLException caught");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(TAG, "doInBackground: IOException caught");
                    e.printStackTrace();
                }
                return "Test Result";
            }

            @Override
            protected void onPostExecute(String result){
                Log.d(TAG, "onPostExecute: Result: " + result);
                displayResult(result);
            }
        }.execute(wikiWebserviceUri);

    }



    public void displayResult(String search_result){
        Log.d(TAG, "displayResult: Initializing Fragment to display result" );
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle result_bundle = new Bundle();
        result_bundle.putString("result", search_result);
        articleFragment.setArguments(result_bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction
                .add(R.id.article_fragment_container, articleFragment)
                .commit();
    }

}
