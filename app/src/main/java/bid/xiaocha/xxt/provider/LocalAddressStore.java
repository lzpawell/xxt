package bid.xiaocha.xxt.provider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.StringBuilderPrinter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bid.xiaocha.xxt.model.AddressEntity;

/**
 * Created by 55039 on 2017/11/4.
 */

public class LocalAddressStore {
    private static LocalAddressStore localAddressStore;
    public static final String DB_NAME = "address";
    private XxtDataBase xxtDataBase;
    public static LocalAddressStore getInstance() {
        if (localAddressStore == null){
            synchronized (LocalAddressStore.class){
                if (localAddressStore == null){
                    localAddressStore = new LocalAddressStore();
                }
            }
        }
        return localAddressStore;
    }
    private LocalAddressStore(){
        xxtDataBase = XxtDataBase.getInstance();
    }
    public void onCreate(final SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + DB_NAME
                + "(" + LocalAddressColumns.ADDRESS_ID + " LONG NOT NULL PRIMARY KEY,"
                + LocalAddressColumns.USER_ID + " STRING NOT NULL,"
                + LocalAddressColumns.USER_NAME + " STRING NOT NULL,"
                + LocalAddressColumns.PHONE + " STRING NOT NULL,"
                + LocalAddressColumns.PLACE + " STRING NOT NULL,"
                + LocalAddressColumns.LONGITUDE +" DOUBLE NOT NULL,"
                + LocalAddressColumns.LATITUDE + " DOUBLE NOT NULL);";


        db.execSQL(sql);
    }
    public class LocalAddressColumns {
        public static final String ADDRESS_ID = "address_id";
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String PHONE = "phone";
        public static final String PLACE = "place";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
    }

    public List<AddressEntity> getAllAddress(String userId){
        List<AddressEntity> addressList = null;
        SQLiteDatabase db = xxtDataBase.getWritableDatabase();
        String sql="Select * from" + DB_NAME + " where user_id = '" + userId + "';";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount()>0){
            addressList = new ArrayList<>(cursor.getCount());
            do {
                AddressEntity addressEntity = parseAddressEntity(cursor);
                addressList.add(addressEntity);
            }while(cursor.moveToNext());
        }

        cursor.close();
        return addressList;
    }

    private AddressEntity parseAddressEntity(Cursor cursor) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddressId(cursor.getLong(0));
        addressEntity.setUserId(cursor.getString(1));
        addressEntity.setUserName(cursor.getString(2));
        addressEntity.setPhone(cursor.getString(3));
        addressEntity.setPlace(cursor.getString(4));
        addressEntity.setLongitude(cursor.getDouble(5));
        addressEntity.setLatitude(cursor.getDouble(6));
        return addressEntity;
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (newVersion>1)
            onCreate(db);
    }
}
