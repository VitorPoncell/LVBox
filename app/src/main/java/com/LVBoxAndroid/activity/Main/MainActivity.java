package com.LVBoxAndroid.activity.Main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.activity.Login.LoginActivity;
import com.LVBoxAndroid.adapter.TabAdapter;
import com.LVBoxAndroid.model.AuthCustom;
import com.LVBoxAndroid.model.SlidingTabLayout;
import com.LVBoxAndroid.util.Connection;

public class MainActivity extends AppCompatActivity {

    private MainControler controler;
    private Toolbar toolbar;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(!Connection.verifyNet(MainActivity.this)){
            Toast.makeText(MainActivity.this,"Sem conecxao com internet",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controler = new MainControler(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager)findViewById(R.id.vp_page);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_logout:
                openLogin();
                return true;
            case R.id.item_settings:
                return true;
            case R.id.item_add:
                openContactRegister();
                return true;
                default:
                    return super.onOptionsItemSelected(item);


        }
    }

    private void openLogin(){
        AuthCustom authCustom = new AuthCustom(this);
        authCustom.logout();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openContactRegister(){

        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogAddContact);

        aleBuilder.setTitle("Novo contato");
        aleBuilder.setMessage("E-mail do usuario");
        aleBuilder.setCancelable(false);

        final EditText contactEmail = new EditText(MainActivity.this);
        aleBuilder.setView(contactEmail);

        aleBuilder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String contactEmailValue = contactEmail.getText().toString();

                if(contactEmailValue.isEmpty()){
                    Toast.makeText(MainActivity.this,"Email vazio",Toast.LENGTH_LONG).show();
                }else {
                    controler.addContact(contactEmailValue);

                }
            }
        });

        aleBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        aleBuilder.create();
        aleBuilder.show();

    }

    public void showError(String erro){
        Toast.makeText(MainActivity.this,erro,Toast.LENGTH_LONG).show();
    }
}
