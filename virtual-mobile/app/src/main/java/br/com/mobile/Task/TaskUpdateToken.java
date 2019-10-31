package br.com.mobile.Task;

import android.content.Context;
import android.os.Handler;

import br.com.mobile.Resource.AccessResource;

/**
 * Description: FIXME: Document this type > *Project: virtual-mobile
 *
 * @author breni > _@date: 20/10/2019 > _@version $$ >
 */
public class TaskUpdateToken implements Runnable {
    private static Handler       handler;
    private final AccessResource access;

    public TaskUpdateToken(Context context) {
        handler = new Handler();
        access = new AccessResource(context);
    }

    @Override
    public void run() {
        handler.postDelayed(this, 1700000);
        access.validarToken();
    }
}
