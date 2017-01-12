package snuze.alarmtooth.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewGroupCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import snuze.alarmtooth.R;

/**
 * Created by ericksong on 1/8/2017.
 */

public class ColorFragment extends Fragment {

    private static final String M_PARAM = "param1";

    private String mParam;


    public ColorFragment(){
    }

    public static ColorFragment newInstance(String param){
        ColorFragment fragment = new ColorFragment();
        Bundle args = new Bundle();
        args.putString(M_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam = getArguments().getString(M_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color, container, false);
        TextView textView = (TextView) view;
        textView.setText(mParam);

        //View to inflate when fragment called
        return view;

    }
}