package vn.edu.usth.attendancecheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.databinding.ActivityLoginBinding;
import vn.edu.usth.attendancecheck.databinding.ActivityMainBinding;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


    }

}