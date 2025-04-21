package com.rafael.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int quantidade = 1;
    private final double precoBase = 20.0;

    private TextView textQuantidade, textPrecoTotal, textResumo;
    private EditText editNome;
    private CheckBox checkboxBacon, checkboxQueijo, checkboxOnion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editTextNome);
        textQuantidade = findViewById(R.id.textViewQuantidade);
        textPrecoTotal = findViewById(R.id.textViewPrecoTotal);
        textResumo = findViewById(R.id.textViewResumoPedido);

        checkboxBacon = findViewById(R.id.checkboxBacon);
        checkboxQueijo = findViewById(R.id.checkboxQueijo);
        checkboxOnion = findViewById(R.id.checkboxOnionRings);

        Button botaoAdicionar = findViewById(R.id.buttonAdicionar);
        Button botaoSubtrair = findViewById(R.id.buttonSubtrair);
        Button botaoEnviar = findViewById(R.id.buttonEnviarPedido);

        botaoAdicionar.setOnClickListener(v -> {
            quantidade++;
            atualizarTudo();
        });

        botaoSubtrair.setOnClickListener(v -> {
            if (quantidade > 1) {
                quantidade--;
                atualizarTudo();
            }
        });

        View.OnClickListener listenerCheck = v -> atualizarTudo();
        checkboxBacon.setOnClickListener(listenerCheck);
        checkboxQueijo.setOnClickListener(listenerCheck);
        checkboxOnion.setOnClickListener(listenerCheck);

        botaoEnviar.setOnClickListener(v -> enviarPedido());

        atualizarTudo();
    }

    private void atualizarTudo() {
        textQuantidade.setText(String.valueOf(quantidade));
        double total = calcularTotal();

        String totalFormatado = String.format("Preço Total: R$ %.2f", total);
        textPrecoTotal.setText(totalFormatado);

        atualizarResumoPedido(total);
    }

    private double calcularTotal() {
        double total = precoBase;
        if (checkboxBacon.isChecked()) total += 2.0;
        if (checkboxQueijo.isChecked()) total += 1.5;
        if (checkboxOnion.isChecked()) total += 3.0;
        return total * quantidade;
    }

    private void atualizarResumoPedido(double total) {
        String nome = editNome.getText().toString();
        StringBuilder adicionais = new StringBuilder();

        if (checkboxBacon.isChecked()) adicionais.append("Bacon, ");
        if (checkboxQueijo.isChecked()) adicionais.append("Queijo, ");
        if (checkboxOnion.isChecked()) adicionais.append("Onion Rings, ");

        if (adicionais.length() > 0)
            adicionais.setLength(adicionais.length() - 2);

        String resumo = "Cliente: " + nome + "\n"
                + "Quantidade: " + quantidade + "\n"
                + "Adicionais: " + (adicionais.length() > 0 ? adicionais : "Nenhum") + "\n"
                + String.format("Total: R$ %.2f", total);

        textResumo.setText(resumo);
    }

    private void enviarPedido() {
        String nome = editNome.getText().toString();
        StringBuilder adicionais = new StringBuilder();

        adicionais.append("Tem Bacon: ").append(checkboxBacon.isChecked() ? "Sim" : "Não").append("\n");
        adicionais.append("Tem Queijo: ").append(checkboxQueijo.isChecked() ? "Sim" : "Não").append("\n");
        adicionais.append("Tem Onion Rings: ").append(checkboxOnion.isChecked() ? "Sim" : "Não").append("\n");

        double total = calcularTotal();

        String corpo = "Nome do cliente: " + nome + "\n"
                + adicionais
                + "Quantidade: " + quantidade + "\n"
                + String.format("Preço final: R$ %.2f", total);

        String assunto = "Pedido de " + nome;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822"); //  filtra para apps de e-mail
        intent.putExtra(Intent.EXTRA_SUBJECT, assunto);
        intent.putExtra(Intent.EXTRA_TEXT, corpo);

        try {
            startActivity(Intent.createChooser(intent, "Enviar pedido por e-mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Nenhum app de e-mail encontrado.", Toast.LENGTH_SHORT).show();
        }
    }


}

