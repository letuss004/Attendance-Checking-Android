package vn.edu.usth.attendancecheck.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import vn.edu.usth.attendancecheck.MainActivity;
import vn.edu.usth.attendancecheck.databinding.ActivityLoginBinding;
import vn.edu.usth.attendancecheck.network.RemoteDataSource;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private RemoteDataSource remoteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //
        authenticate();
    }

    private void authenticate() {
        String email = binding.username.getText().toString();
        String password = binding.password.getText().toString();
        boolean success = remoteData.login(email, password);
        if (success) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", "put something on here");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}