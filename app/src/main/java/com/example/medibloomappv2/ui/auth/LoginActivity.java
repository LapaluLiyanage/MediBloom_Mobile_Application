package com.example.medibloomappv2.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.repository.AuthRepository;
import com.example.medibloomappv2.ui.main.MainActivity;
import com.example.medibloomappv2.utils.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private CircularProgressIndicator progressIndicator;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        progressIndicator = findViewById(R.id.progress_indicator);
        TextView tvForgotPassword = findViewById(R.id.tv_forgot_password);
        TextView tvSignUp = findViewById(R.id.tv_sign_up);

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
    }

    private void attemptLogin() {
        String email = getFieldText(etEmail);
        String password = getFieldText(etPassword);

        if (email.isEmpty()) { etEmail.setError("Email is required"); etEmail.requestFocus(); return; }
        if (password.isEmpty()) { etPassword.setError("Password is required"); etPassword.requestFocus(); return; }
        if (password.length() < 6) { etPassword.setError("Minimum 6 characters"); etPassword.requestFocus(); return; }

        setLoading(true);

        authRepository.login(email, password, new AuthRepository.OnAuthResult() {
            @Override
            public void onSuccess(com.example.medibloomappv2.data.local.entity.UserEntity user) {
                runOnUiThread(() -> {
                    setLoading(false);
                    PreferenceManager prefs = new PreferenceManager(LoginActivity.this);
                    prefs.setUserId(String.valueOf(user.id));
                    prefs.setUserName(user.fullName);
                    prefs.setUserEmail(user.email);
                    goToMain();
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(LoginActivity.this, "❌ " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private String getFieldText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        progressIndicator.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setText(loading ? "Logging in…" : "Login");
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
