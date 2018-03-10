package bid.xiaocha.xxt.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by 55039 on 2017/11/4.
 */

public class ServeStoreForChat {
    private static ServeStoreForChat serveStoreForChat;
    public static final String DB_NAME = "serveForChat";
    private XxtDataBase xxtDataBase;
    public static ServeStoreForChat getInstance() {
        if (serveStoreForChat == null){
            synchronized (ServeStoreForChat.class){
                if (serveStoreForChat == null){
                    serveStoreForChat = new ServeStoreForChat();
                }
            }
        }
        return serveStoreForChat;
    }
    private ServeStoreForChat(){
        xxtDataBase = XxtDataBase.getInstance();
    }
    public void onCreate(final SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + DB_NAME
                + "( " + ServeForChatColumns.REQUESTER_ID + " STRING NOT NULL,"
                + ServeForChatColumns.SERVE_ID + " STRING NOT NULL,"
                + ServeForChatColumns.SERVE_TYPE + " INT NOT NULL);";
        db.execSQL(sql);
    }
    public class ServeForChatColumns {
        public static final String REQUESTER_ID = "requester_id";
        public static final String SERVE_ID = "serve_id";
        public static final String SERVE_TYPE = "serve_type";
        public static final short NEED_SERVE = 0;
        public static final short OFFER_SERVE = 1;
    }

    public ArrayList<String> getRequesterIds(String serveId,int serveType){
        SQLiteDatabase db = xxtDataBase.getWritableDatabase();
        ArrayList<String> result = new ArrayList<>();
        Cursor cursor = db.query(DB_NAME, null, "serve_id=? and serve_type=?", new String[]{serveId,serveType+""}, null, null, null);
        if (cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            String requesterId = cursor.getString(0);
            if (!result.contains(requesterId))
                result.add(requesterId);
        }
        if (cursor != null){
            cursor.close();
        }
        return result;
    }
    public int getServeCount(String requesterId,String serveId,int serveType){
        SQLiteDatabase db = xxtDataBase.getWritableDatabase();

        Cursor cursor = db.query(DB_NAME, null, "requester_id=? and serve_id=? and serve_type=?", new String[]{requesterId,serveId,serveType+""}, null, null, null);
        int result = cursor.getCount();
        if (cursor != null){
            cursor.close();
        }
        return result;
    }

    public void insertServe(SQLiteDatabase db,String requesterId,String serveId,int serveType){
        if (db == null){
            db = xxtDataBase.getWritableDatabase();
        }
        Cursor cursor = db.query(DB_NAME, null, "requester_id=? and serve_id=? and serve_type=?", new String[]{requesterId,serveId,serveType+""}, null, null, null);
        if (cursor != null && cursor.getCount()>0){
            return;
        }else{
            ContentValues values = new ContentValues();
            values.put(ServeForChatColumns.REQUESTER_ID,requesterId);
            values.put(ServeForChatColumns.SERVE_ID,serveId);
            values.put(ServeForChatColumns.SERVE_TYPE,serveType);
            db.insert(DB_NAME,null,values);
        }
    }
    public int deleteServes(String requesterId,String serveId,int serveType){
        SQLiteDatabase db = xxtDataBase.getWritableDatabase();
        return db.delete(DB_NAME, "serve_id=? and serve_type=? and requester_id=?", new String[]{serveId,serveType+"",requesterId});
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (newVersion>1)
            onCreate(db);
    }
}
