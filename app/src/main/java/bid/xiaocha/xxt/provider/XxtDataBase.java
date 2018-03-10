package bid.xiaocha.xxt.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bid.xiaocha.xxt.util.App;

/**
 * Created by 55039 on 2017/11/4.
 */

public class XxtDataBase extends SQLiteOpenHelper{
    private static String dataBaseName = "xxtDB";
    private static int version = 1;
    private static XxtDataBase xxtDataBase;

    public static XxtDataBase getInstance() {
        if (xxtDataBase == null){
            synchronized (xxtDataBase){
                if (xxtDataBase == null){
                    xxtDataBase = new XxtDataBase(App.getAppContext());
                }
            }
        }
        return xxtDataBase;
    }

    public XxtDataBase(Context context){
        super(context,dataBaseName,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ServeStoreForChat.getInstance().onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ServeStoreForChat.getInstance().onUpgrade(db,oldVersion,newVersion);
    }
}
