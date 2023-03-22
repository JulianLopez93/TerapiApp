package com.example.imc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Math;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText et_peso, et_estatura;
    TextView tv_res, tv_mes, tv_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_peso = findViewById(R.id.txt_peso);
        et_estatura = findViewById(R.id.txt_estatura);
        tv_res = findViewById(R.id.txt_res);
        tv_mes = findViewById(R.id.tv_mes);
        tv_desc = findViewById(R.id.tv_desc);
    }

    public float [] convertir(EditText n1, EditText n2)
    {
        String valor1 = n1.getText().toString();
        String valor2 = n2.getText().toString();
        float nmb1 = Float.parseFloat(valor1);
        int nmb2 = Integer.parseInt(valor2);

        return new float[] {nmb1,nmb2};

    }

    //Método que realiza el cálculo del IMC
    public float operar() {
        float[] numeros = convertir(et_peso, et_estatura);

        float est_mts = (numeros[1]) / 100;
        float elev = (float) Math.pow(est_mts, 2);

        float imc = numeros[0] / elev;

        DecimalFormat numformat = new DecimalFormat("#.00");

        //String result = String.valueOf(imc);
        //StringBuilder text = new StringBuilder();

        //("Su índice de masa corporal es: "+numformat.format(imc));
        tv_res.setText(getString(R.string.Your_bmi_is, String.valueOf(numformat.format(imc))));
        return imc;
    }

    public boolean validar1 ()
    {
        if (et_peso.getText().toString().length()==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean validar2 ()
    {
        if (et_estatura.getText().toString().length()==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void calcular(View view)
    {


        if (validar1()==false)
        {
            Toast.makeText(this,getString(R.string.weight_alert),Toast.LENGTH_SHORT).show();
        }

        if (validar2()==false)
        {
            Toast.makeText(this,getString(R.string.height_alert),Toast.LENGTH_SHORT).show();
        }
        if (validar1()==true && validar2()==true)
        {
            String msj [] = mensaje();

            operar();
            //Toast.makeText(this, msj,Toast.LENGTH_LONG).show();
            tv_mes.setText(msj[0]);
            tv_desc.setText(msj[1]);




        }
    }

    public String [] mensaje()
    {
        String msj="";
        String msj2="";

        StringBuilder msjA=new StringBuilder();
        StringBuilder msjB=new StringBuilder();

        String LOW = getString(R.string.Low);
        String NORMAL = getString(R.string.Normal);

        String OVERWEIGHT = getString(R.string.Overweight);
        String OBESE = getString(R.string.Obese);

        String MODERATE = getString(R.string.Moderate);
        String SEVERE = getString(R.string.Severe);
        String MILD = getString(R.string.Mild);

        String MORBID = getString(R.string.Morbid);


        float imc = operar();

        if (imc <18.5)
        {
            msjA.append(getString(R.string.Your_weight_is, LOW.toLowerCase()));
            msj=msjA.toString();
            tv_mes.setTextColor(Color.RED);


            if (imc < 16)
            {
                msjB.append(getString(R.string.Your_thinness_is, SEVERE.toLowerCase()));
                msj2=msjB.toString();
            }

            else if (imc >= 16.00 && imc < 16.99)
            {
                msjB.append(getString(R.string.Your_thinness_is, MODERATE.toLowerCase()));
                msj2=msjB.toString();
            }

            else
            {
                msjB.append(getString(R.string.Your_thinness_is, MILD.toLowerCase()));
                msj2=msjB.toString();
            }
        }

        else if (imc >= 18.5 && imc < 25)
        {
            msjA.append(getString(R.string.Your_weight_is, NORMAL.toLowerCase()));
            msj=msjA.toString();
            tv_mes.setTextColor(Color.GREEN);
        }

        else if (imc >=25 && imc < 30)
        {
            msjA.append(getString(R.string.You_are, OVERWEIGHT.toLowerCase()));
            msj=msjA.toString();
            tv_mes.setTextColor(Color.rgb(255,188,0));


        }

        else
        {
            msjA.append(getString(R.string.You_are, OBESE.toLowerCase()));
            msj=msjA.toString();
            tv_mes.setTextColor(Color.RED);

            if (imc >= 30 && imc <34.99)
            {
                msjB.append(getString(R.string.Your_obesity_is, MILD.toLowerCase()));
                msj2=msjB.toString();
            }

            else if (imc >=35 && imc <39.99)
            {
                msjB.append(getString(R.string.Your_obesity_is, MODERATE.toLowerCase()));
                msj2=msjB.toString();
            }

            else
            {
                msjB.append(getString(R.string.Your_obesity_is, MORBID.toLowerCase()));
                msj2=msjB.toString();
            }
        }
        String [] arr_msj = {msj,msj2};

        return arr_msj;
    }
}