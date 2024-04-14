package com.example.mankomania.screens;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mankomania.R;
import com.example.mankomania.api.Lobby;
import com.example.mankomania.api.Status;

public class CreateNewLobby extends AppCompatActivity implements Lobby.AddLobbyCallback{

    EditText nameInput;
    Switch privateLobbySwitch;
    EditText passwordInput;
    Spinner maxPlayerSpinner;
    Button createLobbyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // bind elements
        nameInput = findViewById(R.id.nameEditText);
        passwordInput = findViewById(R.id.passwortEditText);
        privateLobbySwitch = findViewById(R.id.privateLobbySwitch);
        maxPlayerSpinner = findViewById(R.id.maxSpielerSpinner);
        createLobbyButton = findViewById(R.id.createLobbyButton);

        // deactivate password at first
        passwordInput.setEnabled(false);
        passwordInput.setFocusable(false);
        passwordInput.setFocusableInTouchMode(false);

        // options for maxPlayerSpinner
        String[] options = { "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxPlayerSpinner.setAdapter(adapter);

        // only allow a password if the lobby is private
        privateLobbySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                passwordInput.setEnabled(isChecked);
                passwordInput.setFocusable(isChecked);
                passwordInput.setFocusableInTouchMode(isChecked);
                if (!isChecked) {
                    passwordInput.setText("");
                }
            }
        });

        createLobbyButton.setOnClickListener(v -> {
            String lobbyName = nameInput.getText().toString();
            String lobbyPassword = passwordInput.getText().toString();
            boolean isLobbyPrivate = privateLobbySwitch.isChecked();
            int maxPlayers = Integer.parseInt((String) maxPlayerSpinner.getSelectedItem());

            // make sure to "send" password = null if password == "" !!!
            if (lobbyPassword.equals("")) {
                lobbyPassword = null;
            }

            // get token from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            // add lobby
            Lobby.addLobby(token, lobbyName, lobbyPassword, isLobbyPrivate, maxPlayers, Status.open, CreateNewLobby.this);
        });
    }

    @Override
    public void onAddLobbySuccess(String message) {
        runOnUiThread(() -> Toast.makeText(CreateNewLobby.this, "Lobby erfolgreich erstellt: " + message, Toast.LENGTH_SHORT).show());

        // go back to login page
        Intent goToChooseYourCharacter = new Intent(CreateNewLobby.this, ChooseYourCharacter.class);
        startActivity(goToChooseYourCharacter);
    }

    @Override
    public void onAddLobbyFailure(String errorMessage) {
        runOnUiThread(() -> Toast.makeText(CreateNewLobby.this, "Lobbyerstellung fehlgeschlagen: " + errorMessage, Toast.LENGTH_SHORT).show());
    }
}