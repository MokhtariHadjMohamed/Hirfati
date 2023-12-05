package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private String email;
    private TextView error;
    private EditText emailPassword;
    private Button submitPassword;
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password01);
        emailPassword = findViewById(R.id.emailPasswordChangeActivity);
        submitPassword = findViewById(R.id.submitPasswordChangeActivity);
        error = findViewById(R.id.errorPasswordChangeActivity);

        if (!getIntent().getStringExtra("email").isEmpty()){
            email = getIntent().getStringExtra("email");
            emailPassword.setText(email);
        }
        submitPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submitPassword){
            dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setMessage("هل تريد تغير كلمة سر؟،\n سوف يتم ارسال تغير كلمة سر في ايمايل خاص بك.");
            dialogBuilder.setTitle("الطلب تغير كلمة السر");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("تغير", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (editTest(new EditText[]{emailPassword}))
                        FirebaseAuth.getInstance().sendPasswordResetEmail(emailPassword.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    startActivity(Intent.createChooser(intent, ""));
                }
            });

            dialogBuilder.setNegativeButton("إلغاء", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }
    private boolean editTest(EditText[] editTexts) {
        for (EditText e : editTexts) {
            if (e.getText().toString().isEmpty()) {
                e.setBackgroundResource(R.drawable.custom_input_error);
                error.setText("إملء كل خانات");
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        error.setText("");
        return true;
    }

}