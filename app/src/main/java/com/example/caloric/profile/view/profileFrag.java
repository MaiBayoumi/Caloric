package com.example.caloric.profile.view;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caloric.R;
import com.example.caloric.database.LocalDataSource;
import com.example.caloric.database.LocalSource;
import com.example.caloric.model.Meal;
import com.example.caloric.model.Repo;
import com.example.caloric.model.RepoInterface;
import com.example.caloric.model.User;
import com.example.caloric.network.ClientService;
import com.example.caloric.network.RemoteSource;
import com.example.caloric.profile.presenter.ProfilePresenter;
import com.example.caloric.profile.presenter.ProfilePresenterInterface;
import com.example.caloric.register.LogIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class profileFrag extends Fragment implements ProfileViewInterface {

    Button logoutBtn;
    ImageView personalImage;
    TextView nameTextView, emailTextView;

    ProfilePresenterInterface profilePresenter;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    List<Meal> favMeals;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);

        RemoteSource remoteSource = ClientService.getInstance(view.getContext());
        LocalSource localSource = LocalDataSource.getInstance(view.getContext());
        RepoInterface repo = Repo.getInstance(remoteSource, localSource);
        profilePresenter = new ProfilePresenter(repo, this);
        profilePresenter.getAllFavouriteMeals();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    updateUserDataInFireStore();
                }
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent();
                intent.setClass(view.getContext(), LogIn.class);
                startActivity(intent);
                profilePresenter.deleteAllFavouriteMeals();
                getActivity().finish();
            }
        });

        if (currentUser != null) {
            nameTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            emailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            Glide.with(getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .apply(new RequestOptions().override(500, 500)
                            .error(R.drawable.person)).into(personalImage);

            personalImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
            });
        } else {
            showMaterialDialog(view.getContext());
            personalImage.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            logoutBtn.setText("SignIn");
        }


    }

    private void initializeViews(View view) {
        logoutBtn = view.findViewById(R.id.logoutButton);
        personalImage = view.findViewById(R.id.personalImgView);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of the selected image
            Uri imageUri = data.getData();

            // Set the image to the ImageView
            personalImage.setImageURI(imageUri);

        }
    }


    private void updateUserDataInFireStore() {
        User updatedUser = new User(currentUser.getDisplayName(), currentUser.getEmail(),
                favMeals, convertDrawableImageToString(personalImage.getDrawable()));
        Map<String, Object> data = new HashMap<>();
        data.put("userPojo", updatedUser);
        db.collection("users")
                .document(currentUser.getUid())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("hey", "User updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("hey", "Error updating user", e);
                    }
                });
    }


    @Override
    public void onGetAllFavouriteList(List<Meal> favMeals) {
        this.favMeals = favMeals;
    }

    private String convertDrawableImageToString(Drawable drawableImg) {
        if (drawableImg instanceof BitmapDrawable) {
            // Handle BitmapDrawable
            Bitmap bitmap = ((BitmapDrawable) drawableImg).getBitmap();

            // Convert the bitmap to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // Encode the byte array as a Base64 string
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else if (drawableImg instanceof VectorDrawable) {
            // Handle VectorDrawable
            Bitmap bitmap = convertVectorDrawableToBitmap((VectorDrawable) drawableImg);

            // Convert the bitmap to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // Encode the byte array as a Base64 string
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {
            throw new IllegalArgumentException("Unsupported drawable type");
        }
    }

    private Bitmap convertVectorDrawableToBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }


    private void showMaterialDialog(Context context) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(getResources().getString(R.string.caloric))
                .setMessage(getResources().getString(R.string.messagePlan))
                .setNegativeButton(getResources().getString(R.string.signIn), (dialog, which) -> {

                    Intent intent = new Intent();
                    intent.setClass(getContext(), LogIn.class);
                    startActivity(intent);
                })
                .setPositiveButton(getResources().getString(R.string.cancel), (dialog, which) -> {


                })
                .show();
    }
}
