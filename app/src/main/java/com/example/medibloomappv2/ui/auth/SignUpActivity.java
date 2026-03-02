package com.example.medibloomappv2.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
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

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private MaterialButton btnSignUp;
    private CircularProgressIndicator progressIndicator;
    private CheckBox cbTerms;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authRepository = new AuthRepository(this);

        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignUp = findViewById(R.id.btn_signup);
        progressIndicator = findViewById(R.id.progress_indicator);
        cbTerms = findViewById(R.id.cb_terms);
        TextView tvBack = findViewById(R.id.tv_back);

        tvBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });
        btnSignUp.setOnClickListener(v -> attemptSignUp());
    }

    private void attemptSignUp() {
        String name     = getFieldText(etFullName);
        String email    = getFieldText(etEmail);
        String phone    = getFieldText(etPhone);
        String password = getFieldText(etPassword);
        String confirm  = getFieldText(etConfirmPassword);

        if (name.isEmpty())              { etFullName.setError("Name is required");                  etFullName.requestFocus(); return; }
        if (email.isEmpty())             { etEmail.setError("Email is required");                    etEmail.requestFocus(); return; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                           etEmail.setError("Enter a valid email");                  etEmail.requestFocus(); return; }
        if (password.length() < 6)       { etPassword.setError("Minimum 6 characters");             etPassword.requestFocus(); return; }
        if (!password.equals(confirm))   { etConfirmPassword.setError("Passwords do not match");    etConfirmPassword.requestFocus(); return; }
        if (!cbTerms.isChecked())        { Toast.makeText(this, "Please accept the terms", Toast.LENGTH_SHORT).show(); return; }

        setLoading(true);

        authRepository.register(name, email, phone, password, new AuthRepository.OnAuthResult() {
            @Override
            public void onSuccess(com.example.medibloomappv2.data.local.entity.UserEntity user) {
                runOnUiThread(() -> {
                    setLoading(false);
                    PreferenceManager prefs = new PreferenceManager(SignUpActivity.this);
                    prefs.setUserId(String.valueOf(user.id));
                    prefs.setUserName(user.fullName);
                    prefs.setUserEmail(user.email);
                    Toast.makeText(SignUpActivity.this, "Account created! Welcome 🌸", Toast.LENGTH_SHORT).show();
                    goToMain();
                });
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> {
                    setLoading(false);
                    Toast.makeText(SignUpActivity.this, "❌ " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private String getFieldText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void setLoading(boolean loading) {
        btnSignUp.setEnabled(!loading);
        progressIndicator.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSignUp.setText(loading ? "Creating account…" : "Create Account");
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
