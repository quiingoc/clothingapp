package edu.huflit.shoppingonl.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.huflit.shoppingonl.R;

public class Login extends AppCompatActivity {
    LinearLayout loginGG;
    FirebaseDatabase db;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    Button login;
    EditText username, pwd;
    TextView register;

    int RC_SIGN_IN = 40;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //anh xa
        Mapping();


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        //Đăng ký
        register.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lay thong tin tu Edit Text
                String getUsername = username.getText().toString().trim();
                String getPwd = pwd.getText().toString().trim();

                db = FirebaseDatabase.getInstance();
                reference = db.getReference("User");

                if (getUsername.isEmpty() || getPwd.isEmpty()){
                    Toast.makeText(Login.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(getUsername)){
                                // username is exist in firebase database
                                // now get password of user from firebase data and match it with user entered password

                                final String confirmPw = snapshot.child(getUsername).child("passWord").getValue(String.class);

                                if(confirmPw.equals(getPwd)) {
                                    //
                                    Toast.makeText(Login.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                                    String phoneFromDB = snapshot.child(getUsername).child("phoneNumber").getValue(String.class);
                                    String fullNameFromDB = snapshot.child(getUsername).child("fullName").getValue(String.class);
                                    String passwordFromBD = snapshot.child(getUsername).child("passWord").getValue(String.class);


                                    Intent i = new Intent(getApplicationContext(), Home.class);
                                    Bundle b = new Bundle();
                                    b.putString("userName",getUsername);
                                    b.putString("phoneNumber",phoneFromDB);
                                    b.putString("fullName",fullNameFromDB);
                                    b.putString("passWord",passwordFromBD);

                                    i.putExtra("login_status", true);
                                    i.putExtras(b);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(Login.this,"Sai mật khẩu",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(Login.this,"Tài khoản không tồn tại",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });

        //processRequest();

        //Đăng nhập bằng gmail
//        loginGG.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                processLogin();
//            }
//        });

    }

    private void Mapping() {
        register = (TextView)  findViewById(R.id.tvRegister);
        loginGG = (LinearLayout) findViewById(R.id.login_GG);
        username = findViewById(R.id.edUserName);
        pwd = findViewById(R.id.edPassword);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.tvRegister);
    }

    private void processRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void processLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference reference = db.getReference("TKKH");

                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                // Lấy UID của người dùng hiện tại
                                String uid = currentUser.getUid();
                                String gmail = currentUser.getEmail();
                                String accountName = currentUser.getDisplayName();

//                                User user = new User(uid, null, accountName, "", "", gmail,
//                                        "", "Active");

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(uid)) {
                                            Intent i = new Intent(getApplicationContext(), Home.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("cusID", uid);
                                            i.putExtras(bundle);
                                            startActivity(i);
                                        }
//                                        else {
//                                            reference.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                                                    goToHome(currentUser.getEmail(), currentUser.getDisplayName());
//                                                }
//                                            });
//                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else {
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void goToHome(String gmail, String accountName) {
        Intent i = new Intent(getApplicationContext(), Home.class);
        i.putExtra("email", gmail);
        i.putExtra("accountName", accountName);
        startActivity(i);
    }
}