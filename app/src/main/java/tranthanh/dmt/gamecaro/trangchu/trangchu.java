package tranthanh.dmt.gamecaro.trangchu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tranthanh.dmt.gamecaro.R;
import tranthanh.dmt.gamecaro.pvcomput.nguoivsnguoi;
import tranthanh.dmt.gamecaro.pvp.playgame;

public class trangchu extends Fragment {
    Button btnpvc,btnpvp;

    public static trangchu newInstance() {

        Bundle args = new Bundle();

        trangchu fragment = new trangchu();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.trangchu,container,false);
        btnpvc=view.findViewById(R.id.btnpvc);
        btnpvp=view.findViewById(R.id.btnpvp);
        btnpvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, playgame.newInstance()).commit();
            }
        });
        btnpvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, nguoivsnguoi.newInstance()).commit();

            }
        });
        return view;
    }
}
