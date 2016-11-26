package ar.edu.unc.famaf.redditreader.backend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by federico on 26/11/16.
 */

public class Conected {
    private static Conected ourInstance = new Conected();
    public static Conected getInstance() { return ourInstance;    }
    public boolean isConnected(Context context, int totalItemsCount) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setMessage("No Internet connection!");
            dialogBuilder.setCancelable(true).setTitle("Alert");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            if (totalItemsCount == 0){
                dialogBuilder.create().show();
            }
            return false;
        }
        return true;
    }
}
