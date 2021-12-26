package vn.edu.usth.attendancecheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.ExecutionException;

import vn.edu.usth.attendancecheck.MainActivity;
import vn.edu.usth.attendancecheck.databinding.ActivityLoginBinding;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final RemoteDataSource remote = RemoteDataSource.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //
        setItemEvent();
    }

    private void setItemEvent() {
        binding.loginbtn.setOnClickListener(v -> authenticate());

    }

    private void authenticate() {
        String email = "letuss004@gmail.com";
        String password = "letuss004";
//        String email = binding.username.getText().toString();
//        String password = binding.password.getText().toString();
        boolean success = remote.login(email, password);

        if (success) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            binding.username.setText("fail");
        }
    }

    //todo: perform this
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", "put something on here");
    }

    //todo: perform this
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}