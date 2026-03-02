package com.example.medibloomappv2.data.repository;

import android.content.Context;

import com.example.medibloomappv2.data.local.AppDatabase;
import com.example.medibloomappv2.data.local.dao.UserDao;
import com.example.medibloomappv2.data.local.entity.UserEntity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthRepository {

    private final UserDao userDao;
    private final ExecutorService executor;

    public AuthRepository(Context context) {
        userDao = AppDatabase.getInstance(context).userDao();
        executor = Executors.newSingleThreadExecutor();
    }

    // ─── Hash password with SHA-256 ──────────────────────────────────────────
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password; // fallback (should never happen)
        }
    }

    // ─── Register ─────────────────────────────────────────────────────────────
    public void register(String fullName, String email, String phone, String password,
                         OnAuthResult callback) {
        executor.execute(() -> {
            try {
                // Check email already exists
                if (userDao.emailExists(email) > 0) {
                    callback.onFailure("An account with this email already exists.");
                    return;
                }
                UserEntity user = new UserEntity(fullName, email, phone, hashPassword(password));
                long id = userDao.insertUser(user);
                UserEntity created = userDao.getUserById(id);
                callback.onSuccess(created);
            } catch (Exception e) {
                callback.onFailure("Registration failed: " + e.getMessage());
            }
        });
    }

    // ─── Login ────────────────────────────────────────────────────────────────
    public void login(String email, String password, OnAuthResult callback) {
        executor.execute(() -> {
            try {
                UserEntity user = userDao.login(email, hashPassword(password));
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    // Check if email exists to give a better message
                    if (userDao.emailExists(email) > 0) {
                        callback.onFailure("Incorrect password. Please try again.");
                    } else {
                        callback.onFailure("No account found with this email. Please sign up first.");
                    }
                }
            } catch (Exception e) {
                callback.onFailure("Login failed: " + e.getMessage());
            }
        });
    }

    // ─── Reset Password (local - just update it directly) ─────────────────────
    public void resetPassword(String email, String newPassword, OnResetResult callback) {
        executor.execute(() -> {
            if (userDao.emailExists(email) == 0) {
                callback.onResult(false, "No account found with this email.");
                return;
            }
            userDao.updatePassword(email, hashPassword(newPassword));
            callback.onResult(true, "Password updated successfully.");
        });
    }

    // ─── Check email exists ────────────────────────────────────────────────────
    public void checkEmailExists(String email, OnEmailCheckResult callback) {
        executor.execute(() -> callback.onResult(userDao.emailExists(email) > 0));
    }

    // ─── Callbacks ────────────────────────────────────────────────────────────
    public interface OnAuthResult {
        void onSuccess(UserEntity user);
        void onFailure(String message);
    }

    public interface OnResetResult {
        void onResult(boolean success, String message);
    }

    public interface OnEmailCheckResult {
        void onResult(boolean exists);
    }
}

