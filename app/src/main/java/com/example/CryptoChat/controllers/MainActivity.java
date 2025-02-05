package com.example.CryptoChat.controllers;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.CryptoChat.R;
import com.example.CryptoChat.common.data.models.DaoSession;
import com.example.CryptoChat.common.data.provider.SQLiteDialogProvider;
import com.example.CryptoChat.common.data.provider.SQLiteMessageProvider;
import com.example.CryptoChat.common.data.provider.SQLiteUserProvider;
import com.example.CryptoChat.services.AuthenticationManager;
import com.example.CryptoChat.utils.DBUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogController.OnFragmentInteractionListener,
        ContactListController.OnFragmentInteractionListener, SettingsController.OnFragmentInteractionListener, Authenticatable {


    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.NFC,
                        Manifest.permission.USE_BIOMETRIC,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        // Check permission

        initUI();
        hide();
        verifyAuth();

        mDaoSession = DBUtils.getDaoSession(this);
        DBUtils.initDB(this);
        SQLiteMessageProvider.getInstance(mDaoSession);
        SQLiteDialogProvider.getInstance(mDaoSession);
        SQLiteUserProvider.getInstance(mDaoSession);

        /*
         * Navigating by setting same ID for menu items and nav items
         * */
    }

    @Override
    public BiometricPrompt.AuthenticationCallback getCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e("Auth", "Auth error");
                //TODO: In prod env, should exit the program
                show();

                //System.exit(0);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                AuthenticationManager.unlock();
                show();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                AuthenticationManager.lock();

                Log.e("Auth", "Auth failed");
            }
        };
    }

    @Override
    public void verifyAuth() {
        if (!AuthenticationManager.getAuthState()) {
            showBiometricPrompt();
        } else {
            show();
        }
    }

    @Override
    public void initUI() {
        setContentView(R.layout.activity_main);

        BottomNavigationView nav = findViewById(R.id.main_nav);
        nav.setVisibility(View.VISIBLE);
        nav.setSelectedItemId(R.id.nav_destination_contactController);
        NavController navCtl = Navigation.findNavController(this, R.id.nav_controller);
        NavigationUI.setupWithNavController(nav, navCtl);
    }

    @Override
    public FragmentActivity getBioActivity() {
        return MainActivity.this;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        //verifyAuth();

    }

    @Override
    public void onStart() {

        super.onStart();
        verifyAuth();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void hide() {
        findViewById(R.id.activity_main_layout).setVisibility(View.INVISIBLE);

    }

    private void show() {
        findViewById(R.id.activity_main_layout).setVisibility(View.VISIBLE);
    }
}
