package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hadjmohamed.hirfati.Admin.AdminUserAccount;
import com.hadjmohamed.hirfati.Admin.AdminUserAccountInfo;
import com.hadjmohamed.hirfati.Admin.Report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UserAccountInfoActivity extends AppCompatActivity implements View.OnClickListener {


    // Element
    private EditText name, familyName, birthday, address, email, phone;
    private Spinner states, city;
    private Button submit;
    private TextView close, error;
    private EditText[] editTexts;
    private String idUser;
    private List<String> stateList, cityList;
    private ArrayAdapter<CharSequence> adapterState, adapterCity;
    // birthday
    private DatePickerDialog datePickerDialog;
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
    //firebase
    private FirebaseFirestore firestore;
    private User user;
    // Dialog delete
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_info);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        user = new User();

        // ToolsBar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageViewToolBar = findViewById(R.id.imageViewToolBar);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("معلومات الحساب");
        backArrow.setOnClickListener(this);
        retrieveImage(imageViewToolBar, idUser);

        // Element
        name = findViewById(R.id.nameUserAccountInfo);
        familyName = findViewById(R.id.famileNameUserAccountInfo);
        birthday = findViewById(R.id.birthdayUserAccountInfo);
        address = findViewById(R.id.addressUserAccountInfo);
        email = findViewById(R.id.emailUserAccountInfo);
        phone = findViewById(R.id.phoneNumberUserAccountInfo);
        userImage = findViewById(R.id.userImageUserAccountInfo);
        error = findViewById(R.id.errorUserAccountInfo);
        uploadImage = findViewById(R.id.editImageUserAccountInfo);
        uploadImage.setOnClickListener(this);

        editTexts = new EditText[]{name, familyName, birthday, address, email, phone};

        states = findViewById(R.id.statesUserAccountInfo);
        city = findViewById(R.id.cityUserAccountInfo);

        // birthday
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker
                datePickerDialog = new DatePickerDialog(UserAccountInfoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthday.setText(i2 + "/" + (i1 + 1) + "/" + i);
                                user.setBirthday(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // adapterState
        stateList = new ArrayList<>();
        adapterState = new ArrayAdapter(this, R.layout.spinner_item, stateList);

        // adapterCity
        cityList = new ArrayList<>();
        adapterCity = new ArrayAdapter(this, R.layout.spinner_item, cityList);


        submit = findViewById(R.id.submitUserAccountInfo);
        submit.setOnClickListener(this);
        close = findViewById(R.id.closeAccountUserAccountInfo);
        close.setOnClickListener(this);

        getUser();
        getStatus();
        itemStatesSelected();
    }

    private void itemStatesSelected(){
        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void getUser(){
        firestore.collection("Users")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.d("get User: ", "failed");
                            return;
                        }
                        User user = task.getResult().toObject(User.class);
                        name.setText(user.getName());
                        familyName.setText(user.getFamilyName());
                        birthday.setText(user.getBirthday());
                        address.setText(user.getAddress());
                        email.setText(user.getEmail());
                        phone.setText("1234567");
                        stateList.add(0, user.getState());
                        cityList.add(0, user.getCity());
                        getCity(user.getState());
                        retrieveImage(userImage, idUser);
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
        states.setAdapter(adapterState);
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
        city.setAdapter(adapterCity);
    }

    private void updateUser(User user){
        HashMap<String, Object> userMap = user.toHashMap();
        firestore.collection("Users")
                .document(idUser).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(UserAccountInfoActivity.this,
                                UserAccountActivity.class);
                        intent.putExtra("idUser", idUser);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Craftsmen update: ", "update failed");
                    }
                });;
    }

    private void retrieveImage(ImageView imageView, String image) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("Image")
                .child(image);

        final File file;
        try {
            file = File.createTempFile("img", "png");

            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                    Log.e("Image " + image, "Failed");
                }
            });
        } catch (IOException e) {
            imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
            throw new RuntimeException(e);
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

    private void deleteUser(String uid){
        firestore.collection("Users").document(uid).delete();
        FirebaseAuth.getInstance().getCurrentUser().delete();
        FirebaseAuth.getInstance().signOut();
        deleteReport();
    }

    private void deleteReport(){
        firestore.collection("Reports")
                .whereEqualTo("reporter", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("Report delete: ", "failed");
                            return;
                        }
                        for (QueryDocumentSnapshot d: task.getResult()){
                            Report report = d.toObject(Report.class);
                            firestore.collection("Reports").document(report.getIdReport()).delete();
                        }
                    }
                });
        startActivity(new Intent(UserAccountInfoActivity.this, LogInActivity.class));
        finish();
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
                        startActivity(new Intent(UserAccountInfoActivity.this, CraftsmanAccountActivity.class));
                        finish();
                        Toast.makeText(UserAccountInfoActivity.this,
                                        "Image Uploaded!!",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserAccountInfoActivity.this,
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
        if (view == submit){
            if (editTest(editTexts) &&
                    !states.getSelectedItem().toString().equals("ولايات") &&
                    !city.getSelectedItem().toString().equals("بلديات")){
                user.setIdUser(idUser);
                user.setName(name.getText().toString());
                user.setFamilyName(familyName.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setEmail(email.getText().toString());
                user.setAddress(address.getText().toString());
                user.setPhoneNumber(phone.getText().toString());
                user.setState(states.getSelectedItem().toString());
                user.setCity(city.getSelectedItem().toString());
                user.setUserType("User");
                updateUser(user);
            }else if(states.getSelectedItem().toString().equals("ولايات")){
                states.setBackgroundResource(R.drawable.custom_input_error);
            }else if(city.getSelectedItem().toString().equals("بلديات")){
                city.setBackgroundResource(R.drawable.custom_input_error);
            }
            uploadImage(idUser);
        } else if (view == close) {
            dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setMessage("هل تريد حذف هذه الحرفة؟");
            dialogBuilder.setTitle("الطلب");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteUser(idUser);
                }
            }).setNegativeButton("لا", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            alertDialog = dialogBuilder.create();
            alertDialog.show();
        } else if (view == backArrow) {
            startActivity(new Intent(UserAccountInfoActivity.this, UserAccountActivity.class));
            finish();
        }else if(view == uploadImage){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(
                    intent,
                    "Select Image from here..."), PICK_IMAGE_REQUEST);
        }
    }
}