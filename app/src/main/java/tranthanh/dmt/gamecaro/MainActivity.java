package tranthanh.dmt.gamecaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import tranthanh.dmt.gamecaro.trangchu.trangchu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().addToBackStack("trangchu").replace(R.id.frame, trangchu.newInstance()).commit();
    }
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().beginTransaction().addToBackStack("trangchu").replace(R.id.frame, trangchu.newInstance()).commit();
        }
    }
}
