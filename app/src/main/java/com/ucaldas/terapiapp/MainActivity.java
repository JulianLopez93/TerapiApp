package com.ucaldas.terapiapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.ucaldas.terapiapp.fragmentos.CrearReservaFragment;
import com.ucaldas.terapiapp.fragmentos.CrearServicioFragment;
import com.ucaldas.terapiapp.fragmentos.ListarReservasFragment;
import com.ucaldas.terapiapp.fragmentos.ServiciosFragment;
import com.ucaldas.terapiapp.fragmentos.sobreNosotrosFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav){

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                toolbar.setTranslationX(slideX);
            }
        };
        toggle.getDrawerArrowDrawable().setColor(Color.rgb(97,5,24));

        toggle.getDrawerArrowDrawable().setBarLength(100f);
        toggle.getDrawerArrowDrawable().setBarThickness(8f);
        toggle.getDrawerArrowDrawable().setGapSize(30f);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ServiciosFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_crearServicio:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CrearServicioFragment()).commit();
                break;
            case R.id.nav_crearReserva:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CrearReservaFragment()).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ServiciosFragment()).commit();
                break;
            case R.id.nav_listarReserva:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListarReservasFragment()).commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new sobreNosotrosFragment()).commit();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}