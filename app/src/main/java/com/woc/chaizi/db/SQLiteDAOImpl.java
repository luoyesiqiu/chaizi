package com.woc.chaizi.db;

/**
 * Created by zyw on 2016/4/9.
 */

    import java.util.ArrayList;
    import java.util.List;

    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;

    import com.woc.chaizi.MainActivity;
    import com.woc.chaizi.entity.Words;

    public class SQLiteDAOImpl {
        private DBOpenHandler dbOpenHandler;
        private Words words;

        public SQLiteDAOImpl(Context context) {
            this.dbOpenHandler = new DBOpenHandler(context, MainActivity.SD_PATH+MainActivity.DB_NAME, null, 1);
        }

        /**
         * 往表中添加元素
         * @param words
         */
        public void add(Words words) {
            SQLiteDatabase db = dbOpenHandler.getWritableDatabase();// 取得数据库操作
            db.execSQL("insert into words (source,chai) values(?,?)", new Object[] { words.getSource(), words.getChai() });
            db.close();// 记得关闭数据库
        }

        /**
         * 删除记录
         * @param id
         */
        public void remove(Integer id) {
            SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
            db.execSQL("delete from words where id=?", new Object[] { id.toString() });
            db.close();
        }

        public void update(Words words) {// 修改纪录
            SQLiteDatabase db = dbOpenHandler.getWritableDatabase();
            db.execSQL("update words set source=?,chai=? where" + " id=?", new Object[] {words.getSource(), words.getChai(), words.getId() });
            db.close();
        }

        /**
         * 根据source查寻纪录
         * @param source
         * @return
         */
        public String find(String source) {
            String chai=null;
            SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
            // 用游标Cursor接收从数据库检索到的数据
            Cursor cursor = db.rawQuery("select * from words where source=?", new String[] { source.toString() });
            if (cursor.moveToFirst()) {// 依次取出数据

                chai=cursor.getString(cursor.getColumnIndex("chai"));

            }
            cursor.close();
            db.close();
            if(chai==null)
            {
                return source;
            }
            return chai;
        }

        /**
         * 获取所有记录
         * @return
         */
        public List<Words> findAll() {
            List<Words> lists = new ArrayList<Words>();
            Words words = null;
            SQLiteDatabase db = dbOpenHandler.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from words ", null);
            while (cursor.moveToNext()) {
                words = new Words();
                words.setId(cursor.getInt(cursor.getColumnIndex("id")));
                words.setSource(cursor.getString(cursor.getColumnIndex("source")));
                words.setChai(cursor.getString(cursor.getColumnIndex("chai")));
                lists.add(words);
            }
            db.close();
            return lists;
        }

    /**
     * 获取所有条目
     * @return
     */
        public long getCount() {
            SQLiteDatabase db = dbOpenHandler.getReadableDatabase();
            Cursor cursor = db.rawQuery("select count(*) from words ", null);
            cursor.moveToFirst();
            db.close();
            return cursor.getLong(0);
        }
    }

