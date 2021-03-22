package com.app.forms.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.forms.R;
import com.app.forms.constants.Constants;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AppSettingFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    ImageView userprofile;
    TextView displayname, mailid, contactus, signup, savedres;
    MaterialCardView signupcard, contactuscard, savedrescard;
    ProgressDialog dialog;

    public AppSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_app_setting, container, false);
        userprofile = v.findViewById(R.id.userprofile);
        displayname = v.findViewById(R.id.displayname);
        mailid = v.findViewById(R.id.mailid);
        signup = v.findViewById(R.id.signup);
        contactus = v.findViewById(R.id.contactus);
        signupcard = v.findViewById(R.id.signupcard);
        contactuscard = v.findViewById(R.id.contactuscard);
        savedres = v.findViewById(R.id.savedres);
        savedrescard = v.findViewById(R.id.savedrescard);


        signup.setOnClickListener(this);
        contactus.setOnClickListener(this);
        signupcard.setOnClickListener(this);
        contactuscard.setOnClickListener(this);
        savedrescard.setOnClickListener(this);
        savedres.setOnClickListener(this);

        updateUI();

        return v;
    }

    public void signIn() {
        GoogleSignInClient googleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getContext(), "result " + e.getMessage() + e.toString(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        updateUI();
                    } else {
                        Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "failed" + e.getMessage() + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void updateUI() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Glide.with(getContext())
                    .load(user.getPhotoUrl())
                    .into(userprofile);
            displayname.setText(user.getDisplayName());
            mailid.setText(user.getEmail());
            signup.setText("Sign Out");
        } else {
            userprofile.setImageDrawable(getResources().getDrawable(R.drawable.anonymous));
            mailid.setText("");
            displayname.setText("Anonymous");
            signup.setText("Login/SignUp");
        }
    }

    public void signOut() {
        firebaseAuth.signOut();
        updateUI();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signup:
            case R.id.signupcard:
                if (signup.getText().equals("Login/SignUp"))
                    signIn();
                else
                    signOut();
                break;
            case R.id.contactuscard:
            case R.id.contactus:
                Intent i = new Intent(Intent.ACTION_SEND);
                String[] recipients = {Constants.emailID};
                i.setData(Uri.parse("mailto:"));
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, recipients);
                i.putExtra(Intent.EXTRA_SUBJECT, "Forms:support");
                startActivity(Intent.createChooser(i, "Send mail"));
                break;
            case R.id.savedres:
            case R.id.savedrescard:
                Intent _i = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivity(_i);
                break;
        }
    }
}