package bartushk.gentpass.web;

import android.content.Context;

import bartushk.gentpass.data.User;

/**
 * Created by kbartush on 7/6/2014.
 */
public class SyncService implements IWebSyncService {

    private LocalTrustHttpClient client;


    public SyncService(Context context){
        client = new LocalTrustHttpClient(context);
    }

    public void Sync(User toSync){

    }

    public void Register(User toRegister){

    }
}
