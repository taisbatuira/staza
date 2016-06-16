package com.app.sm3.staza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Tis on 31/05/2016.
 */
public class GerenciadorSMS extends BroadcastReceiver implements LocationListener{

    private Context context;
    private String telefoneDoDono;

    public void enviaMensagem (Contato contato,String texto){
        SmsManager.getDefault().sendTextMessage(contato.getTelefone(),null,texto,null,null);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle extras = intent.getExtras();
        Object[] mensagens = (Object[]) extras.get("pdus");
        //String formato = extras.getString("format");
        byte[] mensagem = (byte[]) mensagens[0];
        SmsMessage sms = SmsMessage.createFromPdu(mensagem);
        if (sms.getMessageBody().contains("##gps##")){
            // ativar o gps para pegar a coordenada atual
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            manager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
            this.telefoneDoDono = sms.getDisplayOriginatingAddress();
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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


