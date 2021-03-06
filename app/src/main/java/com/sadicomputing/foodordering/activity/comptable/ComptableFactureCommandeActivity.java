package com.sadicomputing.foodordering.activity.comptable;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sadicomputing.foodordering.R;
import com.sadicomputing.foodordering.activity.accueil.LoginActivity;
import com.sadicomputing.foodordering.adapter.ComptableFactureCommandeAdapter;
import com.sadicomputing.foodordering.entity.Commande;
import com.sadicomputing.foodordering.entity.CommandeArticle;
import com.sadicomputing.foodordering.service.RetrofitService;
import com.sadicomputing.foodordering.service.RetrofitUtlis;
import com.sadicomputing.foodordering.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sadicomputing.foodordering.adapter.ComptableCommandeAdapter.commande;

public class ComptableFactureCommandeActivity extends AppCompatActivity {

    private RetrofitService retrofitService;
    private ComptableFactureCommandeAdapter mAdapter;
    private List<CommandeArticle> commandeArticles = new ArrayList<>();
    private TextView textViewTotal;
    private Double prixTotal= 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serveur_details_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrofitService = RetrofitUtlis.getRetrofitService();
        RecyclerView recyclerView = findViewById(R.id.reclycerViewPlatsdujour);

        ImageView imageView = findViewById(R.id.logoresto);
        TextView textView = findViewById(R.id.designation);
        TextView textView2 = findViewById(R.id.adresse);
        TextView textView3 = findViewById(R.id.telephone);
        TextView textView4 = findViewById(R.id.commande);
        TextView textView5 = findViewById(R.id.table);
        TextView textView6 = findViewById(R.id.date);
        textViewTotal = findViewById(R.id.totalefacture);
        Button btnImprimerFacture = findViewById(R.id.passerlacommande);

        Constantes.loadImage(getApplicationContext(),"resto.jpg", imageView);
        textView.setText(commande.getServeur().getRestaurant().getDesignation());
        textView2.setText("Adresse: "+commande.getServeur().getRestaurant().getAdresse());
        textView3.setText("Téléphone: "+commande.getServeur().getRestaurant().getTelephone());
        textView4.setText("N° Cmde: "+commande.getNumero());
        textView5.setText("N° Table: "+commande.getTable().getNumero());
        textView6.setText("Date: "+commande.getDate());

        mAdapter = new ComptableFactureCommandeAdapter(this, commandeArticles);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        getCommandeArticlesByCommande();

        btnImprimerFacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCommande();
            }
        });


    }

    private void getCommandeArticlesByCommande() {
        retrofitService.getCommandeArticlesByCommande(commande.getNumero()).enqueue(new Callback<List<CommandeArticle>>() {
            @Override
            public void onResponse(Call<List<CommandeArticle>> call, Response<List<CommandeArticle>> response) {
                if (response.isSuccessful()){
                    commandeArticles = response.body();
                    mAdapter.updateAnswers(commandeArticles);
                    for (CommandeArticle commandeArticle : commandeArticles) {
                        prixTotal += commandeArticle.getPrixTotal();
                    }
                    textViewTotal.setText(prixTotal+" FCFA");
                }
            }

            @Override
            public void onFailure(Call<List<CommandeArticle>> call, Throwable t) {
                Log.e("ERREUR", t.getMessage());
            }
        });
    }

private void updateCommande() {
    commande.setStatut(3);
    commande.setComptableid(LoginActivity.compte.getEmploye().getIdEmploye());
    retrofitService.updateCommande(commande.getIdCommande(), commande).enqueue(new Callback<Commande>() {
        @Override
        public void onResponse(Call<Commande> call, Response<Commande> response) {
            if (response.isSuccessful()){
                Toast.makeText(getApplicationContext(), response.body().getNumero()+" est en cours d'impression", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), ComptableCommandeActivity.class));
                finish();

            }
        }

        @Override
        public void onFailure(Call<Commande> call, Throwable t) {
            Log.e("ERROR",t.getMessage());
            Toast.makeText(getApplicationContext(),"Erreur!", Toast.LENGTH_SHORT).show();
        }
    });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_bis, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_panier) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
