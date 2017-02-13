package com.instainsight.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.instainsight.db.tables.LIKEDBYUSER;
import com.instainsight.db.tables.RECENTMEDIA;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.instainsight.db.DatabaseBean.CONSTRAINT_NONE;

/**
 * Created by ashwinir on 23-01-2017.
 */

public class DBQueries {
    static final String TAG = "DBQueries";
    private Context context;
    private DBManager manager;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public DBQueries(Context context) {
        this.context = context;
        getDBInstance();
    }

    private void getDBInstance() {
        manager = DBManager.getInstance(context);
        manager.open();
    }

    public void createTable(String tableName, ArrayList<DatabaseBean> columnDetails) {
        StringBuffer createQuery = new StringBuffer();
        createQuery.append("create table ").append(tableName).append(" ( ");
        if (0 < columnDetails.size()) {
            for (int i = 0; i < columnDetails.size(); i++) {
                createQuery.append(columnDetails.get(i).getColumnName());
                if ("".equals(columnDetails.get(i).getDataType()) || null == columnDetails.get(i).getDataType())
                    columnDetails.get(i).setDataType(DatabaseBean.TYPE_TEXT);

                if ("".equals(columnDetails.get(i).getConstraint()) || null == columnDetails.get(i).getConstraint())
                    columnDetails.get(i).setConstraint(CONSTRAINT_NONE);

                switch (columnDetails.get(i).getDataType()) {
                    case DatabaseBean.TYPE_INTEGER:
                        createQuery.append(" integer ");
                        break;
                    case DatabaseBean.TYPE_TEXT:
                        createQuery.append(" text ");
                        break;
                    default:
                        createQuery.append(" text ");
                        break;
                }

                switch (columnDetails.get(i).getConstraint()) {
                    case DatabaseBean.CONSTRAINT_NOT_NULL:
                        createQuery.append(" not null");
                        break;
                    case DatabaseBean.CONSTRAINT_PRIMARY_KEY:
                        createQuery.append(" primary key");
                        break;
                    default:
                        createQuery.append("");
                        break;
                }
                if (i == columnDetails.size() - 1) {
                    createQuery.append(");");
                } else {
                    createQuery.append(", ");
                }
                Log.d(TAG, "columnDetails: " + columnDetails.get(i).getColumnName()
                        + " " + columnDetails.get(i).getConstraint()
                        + " " + columnDetails.get(i).getDataType());
            }
            if (!isTableExists(tableName)) {
                manager.incrementCount();
                manager.getDB().execSQL(createQuery.toString());
                Toast.makeText(context, "Table created", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(context, "Table already exists", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isTableExists(String tableName) {
        if (tableName == null || manager == null || !manager.getDB().isOpen()) {
            return false;
        }
        Cursor cursor = manager.getDB().rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public synchronized long insertUpdateValuesToRECENTMEDIA(String tableName, ArrayList<Map<String, Object>> columnDetails) {
        long retVal = 0;
        ContentValues initialValues;
        readWriteLock.writeLock().lock();
        for (int i = 0; i < columnDetails.size(); i++) {
            initialValues = new ContentValues();
            for (Map.Entry<String, Object> entry : columnDetails.get(i).entrySet()) {
                if (entry.getValue() instanceof String)
                    initialValues.put(entry.getKey(), (String) entry.getValue());
                else if (entry.getValue() instanceof Integer)
                    initialValues.put(entry.getKey(), (int) entry.getValue());
                else if (entry.getValue() instanceof Long)
                    initialValues.put(entry.getKey(), (long) entry.getValue());
                else if (entry.getValue() instanceof Byte)
                    initialValues.put(entry.getKey(), (byte) entry.getValue());
                else if (entry.getValue() instanceof Short)
                    initialValues.put(entry.getKey(), (short) entry.getValue());
                else if (entry.getValue() instanceof Float)
                    initialValues.put(entry.getKey(), (float) entry.getValue());
                else if (entry.getValue() instanceof Double)
                    initialValues.put(entry.getKey(), (double) entry.getValue());
                else if (entry.getValue() instanceof Boolean)
                    initialValues.put(entry.getKey(), (boolean) entry.getValue());
            }

            Log.d(TAG, "RECENTMEDIA.MEDIAID:" + initialValues.get(RECENTMEDIA.MEDIAID).toString());
            Cursor cursor = manager.getDB().query(tableName, null, RECENTMEDIA.MEDIAID + " = ?"
                    , new String[]{initialValues.get(RECENTMEDIA.MEDIAID).toString()}
                    , null, null, null);
            Log.d(TAG, "RECENTMEDIA.MEDIAID:cursor count:" + cursor.getCount());
            if (cursor.getCount() > 0) {
                retVal = manager.getDB().update(tableName, initialValues
                        , RECENTMEDIA.MEDIAID + " = ?"
                        , new String[]{initialValues.get(RECENTMEDIA.MEDIAID).toString()});
                Log.d(TAG, "insertUpdateValuesToRECENTMEDIA:updated rows:" + retVal);
            } else {
                retVal = manager.getDB().insert(tableName, null, initialValues);
                Log.d(TAG, "insertUpdateValuesToRECENTMEDIA:inserted rows:" + retVal);
            }
            //Toast.makeText(context, "retVal: "+retVal, Toast.LENGTH_SHORT).show();
        }
        readWriteLock.writeLock().unlock();
        return retVal;
    }

    public synchronized long insertUpdateValuesToLIKEDBYUSER(String tableName, ArrayList<Map<String, Object>> columnDetails) {
        long retVal = 0;
        ContentValues initialValues;
        readWriteLock.writeLock().lock();
        for (int i = 0; i < columnDetails.size(); i++) {
            initialValues = new ContentValues();
            for (Map.Entry<String, Object> entry : columnDetails.get(i).entrySet()) {
                if (entry.getValue() instanceof String)
                    initialValues.put(entry.getKey(), (String) entry.getValue());
                else if (entry.getValue() instanceof Integer)
                    initialValues.put(entry.getKey(), (int) entry.getValue());
                else if (entry.getValue() instanceof Long)
                    initialValues.put(entry.getKey(), (long) entry.getValue());
                else if (entry.getValue() instanceof Byte)
                    initialValues.put(entry.getKey(), (byte) entry.getValue());
                else if (entry.getValue() instanceof Short)
                    initialValues.put(entry.getKey(), (short) entry.getValue());
                else if (entry.getValue() instanceof Float)
                    initialValues.put(entry.getKey(), (float) entry.getValue());
                else if (entry.getValue() instanceof Double)
                    initialValues.put(entry.getKey(), (double) entry.getValue());
                else if (entry.getValue() instanceof Boolean)
                    initialValues.put(entry.getKey(), (boolean) entry.getValue());
            }

            Log.d(TAG, "LIKEDBYUSER_MEDIAID:" + initialValues.get(LIKEDBYUSER.LIKEDBYUSER_MEDIAID).toString());
            Cursor cursor = manager.getDB().query(tableName, null, LIKEDBYUSER.LIKEDBYUSER_MEDIAID + " = ?"
                    , new String[]{initialValues.get(LIKEDBYUSER.LIKEDBYUSER_MEDIAID).toString()}
                    , null, null, null);
            Log.d(TAG, "LIKEDBYUSER_MEDIAID:cursor count:" + cursor.getCount());
            if (cursor.getCount() > 0) {
                retVal = manager.getDB().update(tableName, initialValues
                        , LIKEDBYUSER.LIKEDBYUSER_MEDIAID + " = ?"
                        , new String[]{initialValues.get(LIKEDBYUSER.LIKEDBYUSER_MEDIAID).toString()});
                Log.d(TAG, "insertUpdateValues:updated rows:" + retVal);
            } else {
                retVal = manager.getDB().insert(tableName, null, initialValues);
                Log.d(TAG, "insertUpdateValues:inserted rows:" + retVal);
            }
            //Toast.makeText(context, "retVal: "+retVal, Toast.LENGTH_SHORT).show();
        }
        readWriteLock.writeLock().unlock();
        return retVal;
    }

    public synchronized long insertValues(String tableName, ArrayList<Map<String, Object>> columnDetails) {
        long retVal = 0;
        ContentValues initialValues;
        readWriteLock.writeLock().lock();
        for (int i = 0; i < columnDetails.size(); i++) {
            initialValues = new ContentValues();
            for (Map.Entry<String, Object> entry : columnDetails.get(i).entrySet()) {
                if (entry.getValue() instanceof String)
                    initialValues.put(entry.getKey(), (String) entry.getValue());
                else if (entry.getValue() instanceof Integer)
                    initialValues.put(entry.getKey(), (int) entry.getValue());
                else if (entry.getValue() instanceof Long)
                    initialValues.put(entry.getKey(), (long) entry.getValue());
                else if (entry.getValue() instanceof Byte)
                    initialValues.put(entry.getKey(), (byte) entry.getValue());
                else if (entry.getValue() instanceof Short)
                    initialValues.put(entry.getKey(), (short) entry.getValue());
                else if (entry.getValue() instanceof Float)
                    initialValues.put(entry.getKey(), (float) entry.getValue());
                else if (entry.getValue() instanceof Double)
                    initialValues.put(entry.getKey(), (double) entry.getValue());
                else if (entry.getValue() instanceof Boolean)
                    initialValues.put(entry.getKey(), (boolean) entry.getValue());
            }
            retVal = manager.getDB().insert(tableName, null, initialValues);
            //Toast.makeText(context, "retVal: "+retVal, Toast.LENGTH_SHORT).show();
        }
        readWriteLock.writeLock().unlock();
        return retVal;
    }

    public long getRowCount(String tableName, String columnName) {
        long count = 0;
        Cursor cursor = manager.getDB().query(tableName,
                new String[]{"count(" + columnName + ")"},
                null, null, null, null, null);
        cursor.moveToFirst();
        count = cursor.getLong(0);
        Log.d(TAG, "getSATcount Count : " + cursor.getCount());
        //    Cursor cursor= adapter.getDB().rawQuery("SELECT COUNT ("+columnName+") FROM " + tableName,null);
        cursor.close();
        Log.d(TAG, "rowCount: " + count);
        return count;
    }


}
