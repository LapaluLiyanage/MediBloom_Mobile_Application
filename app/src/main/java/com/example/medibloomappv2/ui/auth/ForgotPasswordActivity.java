package com.example.medibloomappv2.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medibloomappv2.R;
import com.example.medibloomappv2.data.repository.AuthRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {

    private LinearLayout stepEmail, stepReset, stepSuccess;
    private TextInputEditText etEmail, etNewPassword, etConfirmPassword;
    private MaterialButton btnCheckEmail, btnResetPassword;
    private CircularProgressIndicator progressIndicator;
    private AuthRepository authRepository;
    private String verifiedEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        authRepository = new AuthRepository(this);

        stepEmail    = findViewById(R.id.step_email);
        stepReset    = findViewById(R.id.step_reset);
        stepSuccess  = findViewById(R.id.step_success);

        etEmail          = findViewById(R.id.et_email);
        etNewPassword    = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        btnCheckEmail    = findViewById(R.id.btn_send_reset);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        progressIndicator = findViewById(R.id.progress_indicator);

        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        btnCheckEmail.setOnClickListener(v -> checkEmail());
        btnResetPassword.setOnClickListener(v -> resetPassword());
        findViewById(R.id.btn_back_to_login).setOnClickListener(v -> finish());
    }

    private void checkEmail() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        if (email.isEmpty()) { etEmail.setError("Email is required"); return; }

        setLoading(true);
        authRepository.checkEmailExists(email, exists -> runOnUiThread(() -> {
            setLoading(false);
            if (exists) {
                verifiedEmail = email;
                showStep(stepReset);
            } else {
                Toast.makeText(this, "❌ No account found with this email.", Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void resetPassword() {
        String newPass  = etNewPassword.getText() != null ? etNewPassword.getText().toString().trim() : "";
        String confirm  = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        if (newPass.length() < 6)       { etNewPassword.setError("Minimum 6 characters"); return; }
        if (!newPass.equals(confirm))   { etConfirmPassword.setError("Passwords do not match"); return; }

        setLoading(true);
        authRepository.resetPassword(verifiedEmail, newPass, (success, message) -> runOnUiThread(() -> {
            setLoading(false);
            if (success) {
                showStep(stepSuccess);
            } else {
                Toast.makeText(this, "❌ " + message, Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void showStep(LinearLayout nextStep) {
        // Find current visible step and fade it out, then fade in the next
        LinearLayout[] steps = { stepEmail, stepReset, stepSuccess };
        for (LinearLayout step : steps) {
            if (step.getVisibility() == View.VISIBLE && step != nextStep) {
                step.animate().alpha(0f).setDuration(250).withEndAction(() -> {
                    step.setVisibility(View.GONE);
                    nextStep.setVisibility(View.VISIBLE);
                    nextStep.setAlpha(0f);
                    nextStep.animate().alpha(1f).setDuration(250).start();
                }).start();
                return;
            }
        }
        nextStep.setVisibility(View.VISIBLE);
    }

    private void setLoading(boolean loading) {
        btnCheckEmail.setEnabled(!loading);
        if (btnResetPassword != null) btnResetPassword.setEnabled(!loading);
        progressIndicator.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
