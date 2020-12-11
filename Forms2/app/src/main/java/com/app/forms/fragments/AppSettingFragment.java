package com.app.forms.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.forms.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AppSettingFragment extends Fragment implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    ImageView userprofile;
    TextView displayname, mailid, apptheme, signup;
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
        apptheme = v.findViewById(R.id.apptheme);
        signup = v.findViewById(R.id.signup);

        signup.setOnClickListener(this);
        apptheme.setOnClickListener(this);

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
                        //Snackbar.make(this, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
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
            signup.setText("Sign Up");
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
                //Log.e("123", signup.getText().toString());
                if (((TextView) v).getText().equals("Sign Up"))
                    signIn();
                else
                    signOut();
                break;
            case R.id.apptheme:
                //ToDo: change app theme here
                break;

        }
    }
}