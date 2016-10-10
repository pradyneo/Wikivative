package com.prady.wikivative.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.prady.wikivative.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    OnSearchButtonClickListener searchCallback;

    public interface OnSearchButtonClickListener{
        void onSearchButtonClicked(String query);
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            searchCallback = (OnSearchButtonClickListener) context;
        }catch (Exception e){
            throw new ClassCastException(context.toString() + "must implement OnSearchButtonClickListener");
        }
    }
    /*@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        searchButton = (Button) getView().findViewById(R.id.search_button);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ImageButton searchButton    = (ImageButton) view.findViewById(R.id.search_button);
        final EditText searchQuery        = (EditText) view.findViewById(R.id.search_bar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchQuery.getText().toString();
                searchCallback.onSearchButtonClicked(query);
            }
        });

        return view;
    }

    /*public void onSearchClick(View view){
        switch (view.getId()) {
            case R.id.search_button:
            String query = ((EditText) getView().findViewById(R.id.search_bar)).getText().toString();
            searchCallback.onSearchButtonClicked(query);
        }
    }*/
}
