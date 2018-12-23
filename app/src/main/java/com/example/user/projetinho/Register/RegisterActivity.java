
package com.example.user.projetinho.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.user.projetinho.Home.HomeActivity;
import com.example.user.projetinho.R;

public class RegisterActivity extends AppCompatActivity {

    private Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnCadastrar = findViewById(R.id.btn_cadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
            }
        });
    }
}
