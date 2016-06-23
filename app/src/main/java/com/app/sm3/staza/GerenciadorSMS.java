package com.app.sm3.staza;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

/**
 * Created by Tis on 31/05/2016.
 */
public class GerenciadorSMS extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener{

    private static String telefoneDoDono;
    private GoogleApiClient googleClient;
    private Context context;

    public GerenciadorSMS(Context context) {
        this.context = context;
    }

    //O Android precisa desse construtor padrão pra ligar o Receiver
    public GerenciadorSMS() {}

    public void enviaMensagem (Contato contato,String texto){
        SmsManager.getDefault().sendTextMessage(contato.getTelefone(),null,texto,null,null);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle extras = intent.getExtras();
        Object[] mensagens = (Object[]) extras.get("pdus");
        byte[] mensagem = (byte[]) mensagens[0];
        SmsMessage sms = SmsMessage.createFromPdu(mensagem);
        if (sms.getMessageBody().contains("##gps##")){
            // ativar o gps para pegar a coordenada atual

            telefoneDoDono = sms.getDisplayOriginatingAddress();
            System.out.println(telefoneDoDono);

            Contato contatoDoDB = new ContatoDB(context).buscaContatoComTelefone(telefoneDoDono);
            if(contatoDoDB == null) {
                Intent irParaMainActivity = new Intent(context, MainActivity.class);
                irParaMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                irParaMainActivity.putExtra("chegouSMS", true);
                irParaMainActivity.putExtra("telefoneDoSMS", telefoneDoDono);
                context.startActivity(irParaMainActivity);
            } else {
                if(contatoDoDB.temPermissao()) {
                    //manda sms;
                    iniciaGPS();
                }
            }
        }
        else if (sms.getMessageBody().contains("##localizacao##")){
            String[] strings = sms.getMessageBody().split(" ");
            String coordenadaRecebida = strings[1];

            String uri = String.format(Locale.ENGLISH, "geo:%s?q=%s(Localizacao)", coordenadaRecebida, coordenadaRecebida);
            Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            maps.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(maps);
        }
    }

    public void iniciaGPS(){
        this.googleClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleClient.connect(); //(1)
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) { //(2)
        LocationRequest request = new LocationRequest();
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleClient,request,this); //(3)
        //Linha que faz a ativação do gps
    }

    @Override
    public void onLocationChanged(Location location) { //(4)
        String coordenada = "##localizacao## " + location.getLatitude() + "," + location.getLongitude();
        System.out.println(coordenada);
        SmsManager.getDefault().sendTextMessage(telefoneDoDono,null,coordenada,null,null);
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {}
}