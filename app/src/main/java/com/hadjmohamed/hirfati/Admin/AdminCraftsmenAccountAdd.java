package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.hadjmohamed.hirfati.Crafts;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.HomePageActivity;
import com.hadjmohamed.hirfati.ImageResizer;
import com.hadjmohamed.hirfati.NewRegisterCraftsmanActivity;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminCraftsmenAccountAdd extends AppCompatActivity implements View.OnClickListener {

    private EditText nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin, emailAdmin,
            phoneNumberAdmin, passwordAdmin, password02Admin, descriptionAdmin;
    private EditText[] editTexts;
    private Spinner statesAdmin, cityAdmin, craftsAdmin, levelAdmin, yearsAdmin;
    private Button submitAdmin;
    private TextView errorAdmin;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    // Image
    private static final int PICK_IMAGE_REQUEST = 1;
    private CardView uploadImage;
    private Uri imageUri;
    private ImageView craftsmanImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    // birthday
    private DatePickerDialog datePickerDialog;
    private Craftsman craftsman;
    // crafts list
    private List<String> craftsList;
    private ArrayAdapter<String> adapterCrafts;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account_add);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        craftsman = new Craftsman();

        // toolbar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(this);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageViewToolBar = findViewById(R.id.imageViewToolBar);

        // get all element
        errorAdmin = findViewById(R.id.errorAdminCraftsmenAccountAdd);
        nameAdmin = findViewById(R.id.nameAdminCraftsmenAccountAdd);
        familyNameAdmin = findViewById(R.id.familyNameAdminCraftsmenAccountAdd);
        addressAdmin = findViewById(R.id.addressAdminCraftsmenAccountAdd);
        emailAdmin = findViewById(R.id.emailAdminCraftsmenAccountAdd);
        phoneNumberAdmin = findViewById(R.id.phoneNumberAdminCraftsmenAccountAdd);
        passwordAdmin = findViewById(R.id.passwordAdminCraftsmenAccountAdd);
        password02Admin = findViewById(R.id.password02AdminCraftsmenAccountAdd);
        descriptionAdmin = findViewById(R.id.descriptionAdminCraftsmenAccountAdd);
        statesAdmin = findViewById(R.id.statesAdminCraftsmenAccountAdd);
        cityAdmin = findViewById(R.id.cityAdminCraftsmenAccountAdd);
        craftsAdmin = findViewById(R.id.craftsAdminCraftsmenAccountAdd);
        levelAdmin = findViewById(R.id.levelAdminCraftsmenAccountAdd);
        yearsAdmin = findViewById(R.id.yearsAdminCraftsmenAccountAdd);
        submitAdmin = (Button) findViewById(R.id.submitAdminCraftsmenAccountAdd);
        uploadImage = findViewById(R.id.uploadImageCraftsmanAccountAdd);
        uploadImage.setOnClickListener(this);
        craftsmanImage = findViewById(R.id.uploadImageAdminCraftsmanAccountAdd);

        // birthday selected
        birthdayAdmin = (EditText) findViewById(R.id.birthdayAdminCraftsmenAccountAdd);
        birthdayAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker
                datePickerDialog = new DatePickerDialog(AdminCraftsmenAccountAdd.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthdayAdmin.setText(i2 + "/" + (i1 + 1) + "/" + i);
                                craftsman.setBirthday(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        editTexts = new EditText[]{nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin,
                emailAdmin, phoneNumberAdmin, passwordAdmin, password02Admin};

        // spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.wilaya,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesAdmin.setAdapter(adapter);

        // crafts spinner
        craftsList = new ArrayList();
        adapterCrafts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, craftsList);
        getCrafts();
        // level spinner
        ArrayAdapter<CharSequence> adapterLevel = ArrayAdapter.createFromResource(this,R.array.level,
                android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelAdmin.setAdapter(adapterLevel);
        // exYears spinner
        ArrayAdapter<CharSequence> adapterExYears = ArrayAdapter.createFromResource(this,R.array.exYears,
                android.R.layout.simple_spinner_item);
        adapterExYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdmin.setAdapter(adapterExYears);

        submitAdmin.setOnClickListener(this);
    }

    private void getCrafts(){
        firestore.collection("Crafts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        for(QueryDocumentSnapshot d: task.getResult()){
                            craftsList.add(d.toObject(Crafts.class).getName());
                        }
                        adapterCrafts.notifyDataSetChanged();
                    }
                });

        adapterCrafts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        craftsAdmin.setAdapter(adapterCrafts);
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
                        Toast.makeText(AdminCraftsmenAccountAdd.this, "تم تسجيل", Toast.LENGTH_SHORT).show();
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
        craftsman.setIdUser(uid);
        firestore.collection("Users")
                .document(uid).set(craftsman)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Upload User", "success");
                        uploadImage(craftsman.getIdUser());
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
                craftsmanImage.setImageBitmap(bitmap);
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
                        startActivity(new Intent(AdminCraftsmenAccountAdd.this, AdminCraftsmen.class));
                        finish();
                        Toast.makeText(AdminCraftsmenAccountAdd.this,
                                        "Image Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminCraftsmenAccountAdd.this,
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
            if (editTest(editTexts)){
                if (passwordAdmin.getText().toString().equals(password02Admin.getText().toString())) {
                    // Progress
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();

                    craftsman.setName(nameAdmin.getText().toString());
                    craftsman.setFamilyName(familyNameAdmin.getText().toString());
                    craftsman.setAddress(addressAdmin.getText().toString());
//                    user.setCity(cityAdmin.getSelectedItem().toString());
                    craftsman.setState(statesAdmin.getSelectedItem().toString());
                    craftsman.setEmail(emailAdmin.getText().toString());
                    craftsman.setPhoneNumber(phoneNumberAdmin.getText().toString());
                    craftsman.setCraft(craftsAdmin.getSelectedItem().toString());
                    craftsman.setLevel(levelAdmin.getSelectedItem().toString());
                    craftsman.setExYears(yearsAdmin.getSelectedItem().toString());
                    craftsman.setDescription(descriptionAdmin.getText().toString());
                    craftsman.setUserType("Craftsman");

                    addAuth(emailAdmin.getText().toString(), passwordAdmin.getText().toString());
                }else{
                    errorAdmin.setText("كلمة سر غير متطابقتان");
                    passwordAdmin.setBackgroundResource(R.drawable.custom_input_error);
                    password02Admin.setBackgroundResource(R.drawable.custom_input_error);
                }
            }
        } else if (view == uploadImage) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(
                    intent,
                    "Select Image from here..."), PICK_IMAGE_REQUEST);
        } else if (view == backArrow) {
            startActivity(new Intent(AdminCraftsmenAccountAdd.this, AdminCraftsmen.class));
            finish();
        }
    }

}