package com.example.caloric.profile.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.model.User;
import com.example.caloric.network.RemoteDataSource;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.profile.presenter.ProfilePresenter;
import com.example.caloric.profile.presenter.ProfilePresenterInterface;
import com.example.caloric.register.LogIn;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class profileFrag extends Fragment implements ProfileViewInterface {

    private static final int IMAGE_PICK_CODE = 1;
    private Button logoutBtn;
    private ImageView personalImage;
    private TextView nameTextView, emailTextView;
    private ProfilePresenterInterface profilePresenter;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private List<Meal> favMeals;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logoutBtn = view.findViewById(R.id.logoutButton);
        personalImage = view.findViewById(R.id.personalImgView);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        RemoteSource remoteSource = RemoteDataSource.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        profilePresenter = new ProfilePresenter(repo, this);
        profilePresenter.getAllFavouriteMeals();

        loadUserProfile(); // Load the user's profile information

        logoutBtn.setOnClickListener(v -> {
            if (currentUser != null) {
                updateUserDataInFireStore(null);
            }
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LogIn.class);
            startActivity(intent);
            profilePresenter.deleteAllFavouriteMeals();
            getActivity().finish();
        });
        if (currentUser != null) {
            nameTextView.setText(currentUser.getDisplayName());
            emailTextView.setText(currentUser.getEmail());
            Uri photoUri = currentUser.getPhotoUrl();
            if (photoUri != null) {
                Glide.with(getContext())
                        .load(photoUri)
                        .apply(new RequestOptions().override(500, 500).error(R.drawable.person))
                        .into(personalImage);
            }

            personalImage.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_PICK_CODE);
            });
        } else {
            showMaterialDialog(view.getContext());
            personalImage.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            logoutBtn.setText("Sign In");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            handleImage(imageUri);
        }
    }

    private void handleImage(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            Bitmap resizedBitmap = resizeBitmap(bitmap, 800, 800); // Resize to prevent large data issues
            personalImage.setImageBitmap(resizedBitmap);

            // Upload image to Firebase Storage
            uploadImageToFirebaseStorage(resizedBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImageToFirebaseStorage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileImagesRef = storageRef.child("profileImages/" + currentUser.getUid() + ".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profileImagesRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL
            profileImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Save the download URL to Firestore
                updateUserDataInFireStore(uri.toString());
            });
        }).addOnFailureListener(e -> Log.d("profileFrag", "Error uploading image", e));
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);

        int newWidth = Math.round(scale * width);
        int newHeight = Math.round(scale * height);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private void updateUserDataInFireStore(String imageUrl) {
        User updatedUser = new User(currentUser.getDisplayName(), currentUser.getEmail(), favMeals, imageUrl);
        Map<String, Object> data = new HashMap<>();
        data.put("userPojo", updatedUser);
        db.collection("users")
                .document(currentUser.getUid())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(unused -> Log.d("profileFrag", "User updated successfully"))
                .addOnFailureListener(e -> Log.d("profileFrag", "Error updating user", e));
    }

    private String convertDrawableImageToString(Drawable drawableImg) {
        if (drawableImg instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawableImg).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    @Override
    public void onGetAllFavouriteList(List<Meal> favMeals) {
        this.favMeals = favMeals;
    }

    @Override
    public void onMealsDeleted() {
        Toast.makeText(getContext(), "meal has been successfully added.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String s) {
        Toast.makeText(getContext(), "Error: " + s, Toast.LENGTH_LONG).show();
    }

    private void showMaterialDialog(Context context) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(getResources().getString(R.string.caloric))
                .setMessage(getResources().getString(R.string.messagePlan))
                .setNegativeButton(getResources().getString(R.string.signIn), (dialog, which) -> {
                    Intent intent = new Intent(getContext(), LogIn.class);
                    startActivity(intent);
                })
                .setPositiveButton(getResources().getString(R.string.cancel), null)
                .show();
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null && user.getUserImage() != null) {
                                Glide.with(getContext())
                                        .load(user.getUserImage())
                                        .apply(new RequestOptions().override(500, 500).error(R.drawable.person))
                                        .into(personalImage);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.d("profileFrag", "Error loading user profile", e));
        }
    }
}