package com.app.sm3.staza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private String telefoneDoDono;
    private GoogleApiClient googleClient;

    public void enviaMensagem (Contato contato,String texto){
        SmsManager.getDefault().sendTextMessage(contato.getTelefone(),null,texto,null,null);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Object[] mensagens = (Object[]) extras.get("pdus");
        byte[] mensagem = (byte[]) mensagens[0];
        SmsMessage sms = SmsMessage.createFromPdu(mensagem);
        if (sms.getMessageBody().contains("##gps##")){
            // ativar o gps para pegar a coordenada atual

            this.googleClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();

            googleClient.connect();

            this.telefoneDoDono = sms.getDisplayOriginatingAddress();
            System.out.println(this.telefoneDoDono);
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

    @Override
    public void onLocationChanged(Location location) {
        String coordenada = "##localizacao## " + location.getLatitude() + "," + location.getLongitude();
        SmsManager.getDefault().sendTextMessage(telefoneDoDono,null,coordenada,null,null);
        LocationServices.FusedLocationApi.removeLocationUpdates(this.googleClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleClient,request,this);
    }

    @Override
    public void onConnectionSuspended(int i) {}
}


