package tranthanh.dmt.gamecaro.pvp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import tranthanh.dmt.gamecaro.R;

public class dialogEnd extends DialogFragment {
    ImageView img;
    private View rootView;
    private playgame activity;
    private int anh;
    public static dialogEnd newInstance(playgame activity, int anh) {
        dialogEnd dialog= new dialogEnd();
        dialog.activity = activity;

        dialog.anh = anh;
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
                .setNegativeButton("Exits",null)
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
        ((ImageView) rootView.findViewById(R.id.imgdiaglog)).setImageResource(anh);
    }
}
