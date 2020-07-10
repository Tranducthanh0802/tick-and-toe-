package tranthanh.dmt.gamecaro.pvcomput;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import tranthanh.dmt.gamecaro.R;
import tranthanh.dmt.gamecaro.trangchu.trangchu;

public class dialogEnd1 extends DialogFragment {
    ImageView img;
    TextView txt;
    private View rootView;
    private nguoivsnguoi activity;
    private String player;
    public static dialogEnd1 newInstance(nguoivsnguoi activity, String player) {
        dialogEnd1 dialog= new dialogEnd1();
        dialog.activity = activity;

        dialog.player = player;
        return dialog;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initViews();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setCancelable(false)
                .setPositiveButton("Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onNewGame();
                    }
                })
                .setNegativeButton("Exits", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("trangchu").replace(R.id.frame, trangchu.newInstance()).commit();

                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;

    }
    private void onNewGame() {
        dismiss();
        activity.newgame();
    }
    private void initViews() {
        rootView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_dialog, null, false);
        ((ImageView) rootView.findViewById(R.id.imgdiaglog)).setImageResource(R.drawable.youwin);
        ((TextView) rootView.findViewById(R.id.txtdialog)).setText(player+" win");
    }
}
