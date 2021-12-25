package vn.edu.usth.attendancecheck;

import androidx.appcompat.app.AppCompatActivity;

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
    private static final RemoteDataSource remote = RemoteDataSource.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.tv.setText(remote.getUser().getValue().toString());

        binding.logout.setOnClickListener(v -> {
            boolean b = remote.logout();
            Toast.makeText(this, "--" + b, Toast.LENGTH_SHORT).show();
        });
    }

}