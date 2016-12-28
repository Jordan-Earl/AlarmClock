package snuze.alarmtooth.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import snuze.alarmtooth.R;


public class SoundFragment extends Fragment {

    private static final String M_PARAM = "param";

    private String mParam;
    public SoundFragment(){}

    //Factory Method to create new instance of SettingsFragment with single String Parameter
    public static SoundFragment newInstance(String param){

        SoundFragment fragment = new SoundFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.device_name, container, false);
        TextView textView = (TextView) view;
        textView.setText(mParam);

        //View to inflate when fragment called
        return view;

    }
}
