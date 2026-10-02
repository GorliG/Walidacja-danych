package com.example.walidacjadanych;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText Imie, Nazwisko, Email, Haslo;
    private Button Rejestracja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Imie = findViewById(R.id.Imie);
        Nazwisko = findViewById(R.id.Nazwisko);
        Email = findViewById(R.id.Email);
        Haslo = findViewById(R.id.Haslo);
        Rejestracja = findViewById(R.id.Rejestracja);

        Rejestracja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walidujIZapisz();
            }
        });
    }

    private void walidujIZapisz() {
        String imie = Imie.getText().toString().trim();
        String nazwisko = Nazwisko.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String haslo = Haslo.getText().toString().trim();

        if (imie.isEmpty() || nazwisko.isEmpty() || email.isEmpty() || haslo.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Podaj poprawny adres email", Toast.LENGTH_SHORT).show();
            return;
        }

        String komunikatHasla = sprawdzHaslo(haslo);
        if (!komunikatHasla.isEmpty()) {
            Toast.makeText(this, komunikatHasla, Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Dane są poprawne", Toast.LENGTH_SHORT).show();
        zapiszDoPliku(imie, nazwisko, email, haslo);
    }

    private String sprawdzHaslo(String haslo) {
        ArrayList<String> braki = new ArrayList<>();

        if (haslo.length() < 8) {
            braki.add("co najmniej 8 znaków");
        }

        boolean maMala = false;
        boolean maDuza = false;
        boolean maZnakSpecjalny = false;

        for (char c : haslo.toCharArray()) {
            if (Character.isLowerCase(c)) maMala = true;
            else if (Character.isUpperCase(c)) maDuza = true;
            else if (!Character.isLetterOrDigit(c)) maZnakSpecjalny = true;
        }

        if (!maDuza) braki.add("Dużą literę");
        if (!maMala) braki.add("Małą literę");
        if (!maZnakSpecjalny) braki.add("znak specjalny");

        if (!braki.isEmpty()) {
            StringBuilder sb = new StringBuilder("Hasło musi mieć ");
            for (int i = 0; i < braki.size(); i++) {
                sb.append(braki.get(i));
                if (i < braki.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        return "";
    }

    private void zapiszDoPliku(String imie, String nazwisko, String email, String haslo) {
        String dane = "Imię: " + imie + "\nNazwisko: " + nazwisko + "\nEmail: " + email + "\nHasło: " + haslo + "\n---\n";
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("uzytkownicy.txt", MODE_APPEND);
            fos.write(dane.getBytes());

            Imie.setText("");
            Nazwisko.setText("");
            Email.setText("");
            Haslo.setText("");

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd zapisu do pliku!", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}