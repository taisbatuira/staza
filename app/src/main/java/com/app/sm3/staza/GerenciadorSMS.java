package com.app.sm3.staza;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by Tis on 31/05/2016.
 */
public class GerenciadorSMS extends BroadcastReceiver{

    public void enviaMensagem (Contato contato,String texto){
        SmsManager.getDefault().sendTextMessage(contato.getTelefone(),null,texto,null,null);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Object[] mensagens = (Object[]) extras.get("pdus");
        //String formato = extras.getString("format");
        byte[] mensagem = (byte[]) mensagens[0];
        SmsMessage sms = SmsMessage.createFromPdu(mensagem);
        if (sms.getMessageBody().contains("##gps##")){
            // ativar o gps para pegar a coordenada atual
        }
        else if (sms.getMessageBody().contains("##localizacao##")){
            //abre o mapa com a localização recebida
        }
    }
}


