package com.juanuni.terapiappfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ServicesListActivity extends AppCompatActivity {

    // Lista de productos
    private List<Product> productList;

    // Vista del RecyclerView
    private RecyclerView recyclerView;

    // Adaptador del RecyclerView
    private ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);

        // Inicializar la lista de productos
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", "10.00", R.drawable.ic_launcher_background));
        productList.add(new Product("Product 2", "20.00", R.drawable.ic_launcher_background));
        productList.add(new Product("Product 3", "30.00", R.drawable.ic_launcher_background));
        productList.add(new Product("Product 4", "40.00", R.drawable.ic_launcher_background));
        productList.add(new Product("Product 5", "50.00", R.drawable.ic_launcher_background));

        // Inicializar el RecyclerView y el adaptador
        recyclerView = findViewById(R.id.recyclerViewProductList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListAdapter = new ProductListAdapter(this, productList);
        recyclerView.setAdapter(productListAdapter);
    }

    // Clase interna para el adaptador personalizado
    private class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

        private Context context;
        private List<Product> productList;

        public ProductListAdapter(ServicesListActivity context, List<Product> productList) {
            this.context = context;
            this.productList = productList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_service_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Product product = productList.get(position);
            holder.imageViewProduct.setImageResource(product.getImageResourceId());
            holder.textViewProductTitle.setText(product.getTitle());
            holder.textViewProductPrice.setText("$" + product.getPrice());
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageViewProduct;
            TextView textViewProductTitle;
            TextView textViewProductPrice;

            public ViewHolder(View itemView) {
                super(itemView);
                imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
                textViewProductTitle = itemView.findViewById(R.id.textViewProductTitle);
                textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            }
        }
    }
}