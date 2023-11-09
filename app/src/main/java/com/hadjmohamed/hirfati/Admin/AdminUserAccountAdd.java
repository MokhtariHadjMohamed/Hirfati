package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hadjmohamed.hirfati.City;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.ImageResizer;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.State;
import com.hadjmohamed.hirfati.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminUserAccountAdd extends AppCompatActivity implements View.OnClickListener {

    private EditText nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin, emailAdmin,
            phoneNumberAdmin, passwordAdmin, password02Admin;
    private EditText[] editTexts;
    private Spinner statesAdmin, cityAdmin;
    private Button submitAdmin;
    private TextView errorAdmin;
    private List<String> stateList, cityList;
    private ArrayAdapter<CharSequence> adapterState, adapterCity;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    // Image
    private static final int PICK_IMAGE_REQUEST = 1;
    private CardView uploadImage;
    private Uri imageUri;
    private ImageView userImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    // birthday
    private DatePickerDialog datePickerDialog;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private User user;
    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_account_add);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = new User();

        // toolbar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(this);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("اضافة مستخدم");
        imageViewToolBar = findViewById(R.id.imageViewToolBar);

        // get all element
        errorAdmin = findViewById(R.id.errorAdminUserAccountAdd);
        nameAdmin = findViewById(R.id.nameAdminUserAccountAdd);
        familyNameAdmin = findViewById(R.id.famileNameAdminUserAccountAdd);
        addressAdmin = findViewById(R.id.addressAdminUserAccountAdd);
        emailAdmin = findViewById(R.id.emailAdminUserAccountAdd);
        phoneNumberAdmin = findViewById(R.id.phoneAdminUserAccountAdd);
        passwordAdmin = findViewById(R.id.passwordAdminUserAccountAdd);
        password02Admin = findViewById(R.id.password02AdminUserAccountAdd);
        statesAdmin = findViewById(R.id.statesAdminUserAccountAdd);
        cityAdmin = findViewById(R.id.cityAdminUserAccountAdd);
        submitAdmin = (Button) findViewById(R.id.submitAdminUserAccountAdd);
        submitAdmin.setOnClickListener(this);
        uploadImage = findViewById(R.id.uploadImageAdminUserAccountAdd);
        uploadImage.setOnClickListener(this);
        userImage = findViewById(R.id.userImageAdminUserAccountAdd);

        // birthday selected
        birthdayAdmin = (EditText) findViewById(R.id.birthdayAdminUserAccountAdd);
        birthdayAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker
                datePickerDialog = new DatePickerDialog(AdminUserAccountAdd.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthdayAdmin.setText(i2 + "/" + (i1 + 1) + "/" + i);
                                user.setBirthday(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        editTexts = new EditText[]{nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin,
                emailAdmin, phoneNumberAdmin, passwordAdmin, password02Admin};

        // adapterState
                stateList = new ArrayList<>();
        adapterState = new ArrayAdapter(this,R.layout.spinner_item, stateList);

        // adapterCity
        cityList = new ArrayList<>();
        adapterCity = new ArrayAdapter(this,R.layout.spinner_item, cityList);

        getStatus();
        itemStatesSelected();
    }

    private void itemStatesSelected(){
        statesAdmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityList.clear();
                cityList.add(0, "بلديات");
                getCity(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getStatus(){
        stateList.add("ولايات");
        firestore.collection("States")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        for(QueryDocumentSnapshot d: task.getResult()){
                            stateList.add(d.toObject(State.class).getAr_name());
                        }
                        adapterState.notifyDataSetChanged();
                    }
                });
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesAdmin.setAdapter(adapterState);
    }

    private void getCity(String uid){
        firestore.collection("City")
                .whereEqualTo("state_name", uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        for(QueryDocumentSnapshot d: task.getResult()){
                            cityList.add(d.toObject(City.class).getCommune_name());
                        }
                        adapterCity.notifyDataSetChanged();
                    }
                });
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityAdmin.setAdapter(adapterCity);
    }


    private boolean editTest(EditText[] editTexts){
        for (EditText e:editTexts) {
            if (e.getText().toString().isEmpty()){
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorAdmin.setText("إملء كل خانات");
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorAdmin.setText("");
        return true;
    }

    private void addAuth(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addUserFirestor(authResult.getUser().getUid());
                        Toast.makeText(AdminUserAccountAdd.this, "تم تسجيل", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Register", e.getMessage());
                    }
                });
    }

    private void addUserFirestor(String uid){
        user.setIdUser(uid);
        firestore.collection("Users")
                .document(uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Upload User", "success");
                        uploadImage(user.getIdUser());
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Upload User", e.getMessage());
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmapOrigin = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Bitmap bitmap = ImageResizer.reduceBitmapSize(bitmapOrigin, 307200);
                userImage.setImageBitmap(bitmap);
                Toast.makeText(this, "Upload", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Upload", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Filed", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(String udi) {
        if (imageUri == null)
            return;

        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference ref = storageReference.child("Image/"
                + udi);
        Bitmap bitmapOrigin = null;
        try {
            bitmapOrigin = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //convert uri to byteArray
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bitmap bitmap = ImageResizer.reduceBitmapSize(bitmapOrigin, 307200);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        ref.putBytes(bytes.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        startActivity(new Intent(AdminUserAccountAdd.this, AdminUser.class));
                        finish();
                        Toast.makeText(AdminUserAccountAdd.this,
                                        "Image Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminUserAccountAdd.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    // Progress Listener for loading
                    // percentage on the dialog box
                    @Override
                    public void onProgress(
                            UploadTask.TaskSnapshot taskSnapshot) {
                        double progress
                                = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage(
                                "Uploaded "
                                        + (int) progress + "%");
                        if (progress == 100)
                            progressDialog.cancel();
                    }
                });
    }
    @Override
    public void onClick(View view) {
        if (view == submitAdmin){
            if (editTest(editTexts) &&
                    !statesAdmin.getSelectedItem().toString().equals("ولايات") &&
                    !cityAdmin.getSelectedItem().toString().equals("بلديات")) {
                if (passwordAdmin.getText().toString().equals(password02Admin.getText().toString())) {
                    // Progress
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();

                    user.setName(nameAdmin.getText().toString());
                    user.setFamilyName(familyNameAdmin.getText().toString());
                    user.setAddress(addressAdmin.getText().toString());
                    user.setCity(cityAdmin.getSelectedItem().toString());
                    user.setState(statesAdmin.getSelectedItem().toString());
                    user.setEmail(emailAdmin.getText().toString());
                    user.setPhoneNumber(phoneNumberAdmin.getText().toString());
                    user.setUserType("User");

                    addAuth(emailAdmin.getText().toString(), passwordAdmin.getText().toString());
                } else {
                    errorAdmin.setText("كلمة سر غير متطابقتان");
                    passwordAdmin.setBackgroundResource(R.drawable.custom_input_error);
                    password02Admin.setBackgroundResource(R.drawable.custom_input_error);
                }
            }else if(statesAdmin.getSelectedItem().toString().equals("ولايات")){
                statesAdmin.setBackgroundResource(R.drawable.custom_input_error);
            }else if(cityAdmin.getSelectedItem().toString().equals("بلديات")){
                cityAdmin.setBackgroundResource(R.drawable.custom_input_error);
            }
        } else if (view == uploadImage) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(
                    intent,
                    "Select Image from here..."), PICK_IMAGE_REQUEST);
        } else if (view == backArrow) {
            startActivity(new Intent(AdminUserAccountAdd.this, AdminCraftsmen.class));
            finish();
        }

    }
}