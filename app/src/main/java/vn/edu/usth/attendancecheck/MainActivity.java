package vn.edu.usth.attendancecheck;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import vn.edu.usth.attendancecheck.databinding.ActivityMainBinding;
import vn.edu.usth.attendancecheck.ui.CameraFragment;
import vn.edu.usth.attendancecheck.ui.CurrentClassesFragment;
import vn.edu.usth.attendancecheck.ui.HistoryFragment;
import vn.edu.usth.attendancecheck.ui.HomeFragment;

public class MainActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // methods
        navigationSetup();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }


    /*
    -------------------------------------------private----------------------------------------
     */

    /**
     * todo: back stack is not deploy,
     */
    private void navigationSetup() {
        BottomNavigationView navigationView = binding.botNav;
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(navigationView, navController);

        // view on click
        navigationView.setOnItemSelectedListener(
                item -> {
                    int id = item.getItemId();
                    int current = navigationView.getSelectedItemId();
                    if (id == R.id.navIcon_home) {
                        switchFragment(new HomeFragment());
                    } else if (id == R.id.navIcon_checkList) {
                        switchFragment(new CurrentClassesFragment());
                    } else if (id == R.id.navIcon_qr) {
                        openCameraRequirePermission();
                    } else if (id == R.id.navIcon_history) {
                        switchFragment(new HistoryFragment());
                    } else if (id == R.id.navIcon_menu) {

                    }
                    return true;
                }
        );
    }

    private void openCameraRequirePermission() {
        String[] perm = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perm)) {
            switchFragment(new CameraFragment());
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "Need permission beacause of something",
                    123,
                    perm
            );
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }


    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {

        }
    }

}