package bartushk.gentpass.web;

import bartushk.gentpass.data.User;

/**
 * Created by kbartush on 7/6/2014.
 */
public interface IWebSyncService {

    public void Sync(User toSync);

    public void Register(User toRegister);

}
