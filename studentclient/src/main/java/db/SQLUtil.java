package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.moxi.studentclient.model.CourseModel;
import com.moxi.studentclient.model.OptionModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class SQLUtil {

    private SQLiteDatabase db;

    private static SQLUtil mInstance = null;

    public static SQLUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SQLUtil(context);
        }
        return mInstance;
    }

    public SQLUtil(Context context) {
        AssetsDatabaseManager.initManager(context.getApplicationContext());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        db = mg.getDatabase("base.db");
    }
//    studySectionTables = DataSupport.findAll(StudySectionTable.class);
////        DataSupport.findBySQL("select * from SemesterTable where secId=")
//    semesterTables = DataSupport.where("secId=?", "1").find(SemesterTable.class);

    /**
     * 获取学段
     *
     * @return
     */
    public List<OptionModel> getStudySectionFromDb() {
        String sql = "select sec_id,sec_name from T_StudySection";
        Cursor cursor = db.rawQuery(sql, new String[]{});

        List<OptionModel> optionModels = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("sec_id"));
                String name = cursor.getString(cursor.getColumnIndex("sec_name"));
                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);
                optionModels.add(cm);
            } while (cursor.moveToNext());
        }
        return optionModels;
    }

    /**
     * 获取学期
     *
     * @param secId 学段id
     * @return
     */
    public List<OptionModel> getSemesterFromDb(String secId) {
        String sql = "select sem_id,sem_name from T_Semester where sem_sec_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{secId});

        List<OptionModel> optionModels = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("sem_id"));
                String name = cursor.getString(cursor.getColumnIndex("sem_name"));
                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);
                optionModels.add(cm);
            } while (cursor.moveToNext());
        }
        return optionModels;
    }


    /**
     * 获取题型
     *
     * @param subjectId      科目id
     * @param chosenPeriodId 学段id
     */
    public List<OptionModel> getTXFromDb(String subjectId, String chosenPeriodId) {
        String sql = "select T_ExerciseType.ext_id,T_ExerciseType.ext_name " +
                "from T_Subject inner join T_StudySection " +
                "inner join T_ExerciseType inner join T_ExerciseTypeStudySectionSubject " +
                " on T_Subject.sub_id=T_ExerciseTypeStudySectionSubject.cts_sub_id" +
                " and T_StudySection.sec_id=T_ExerciseTypeStudySectionSubject.cts_sec_id" +
                " and T_ExerciseType.ext_id=T_ExerciseTypeStudySectionSubject.cts_ext_id" +
                " where T_ExerciseTypeStudySectionSubject.cts_sub_id=? and T_ExerciseTypeStudySectionSubject.cts_sec_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{subjectId, chosenPeriodId});

        List<OptionModel> listTx = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            listTx.clear();
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ext_id"));
                String name = cursor.getString(cursor.getColumnIndex("ext_name"));
                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);
                listTx.add(cm);
            } while (cursor.moveToNext());
        }
        return listTx;
    }

    /**
     * 获取难度
     */
    public List<OptionModel> getLDFromDb() {
        String sql = "select dct_id,dct_name from T_Dict where dct_pid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{"100"});

        List<OptionModel> optionModels = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("dct_id"));
                String name = cursor.getString(cursor.getColumnIndex("dct_name"));
                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);
                optionModels.add(cm);
            } while (cursor.moveToNext());
        }
        return optionModels;
    }

    /**
     * 获取出版社
     *
     * @return
     */
    public List<OptionModel> getCBSFromDb(String secId, String subId) {
        String sql = "select  distinct pub_id, pub_name from T_CourseBook inner join T_Publisher on T_CourseBook.cob_pub_id=T_Publisher.pub_id where cob_sec_id=? and cob_sub_id=?";

//        String sql = "select pub_id,pub_name from T_Publisher";
        Cursor cursor = db.rawQuery(sql, new String[]{secId, subId});

        List<OptionModel> optionModels = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("pub_id"));
                String name = cursor.getString(cursor.getColumnIndex("pub_name"));
                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);
                optionModels.add(cm);
            } while (cursor.moveToNext());
        }
        return optionModels;
    }

    /**
     * 设置书本下载记录
     *
     * @param filePath
     * @param bookId
     */
    public void setBookWithId(String filePath, String bookId) {
        String fileName=bookId+".db";
        String filePathName=filePath + "/" + fileName;
        File file = new File(filePathName);
        String fileCreateTime = null;
        if (file.exists()) {
            fileCreateTime = String.valueOf(file.lastModified());
            String sql = "update T_CourseBook set file_path=?,file_createtime=?,file_isupdate=0  where cob_id=?";
            db.execSQL(sql, new String[]{filePathName, fileCreateTime, bookId});
        }
    }

    /**
     * 获取教材
     *
     * @param cos_sem_id 学期id（引用semester的id）
     * @param cob_pub_id 教材所属出版社id
     * @param cob_sec_id 教材所属学段id
     * @param cob_sub_id 教材所属科目id
     * @return
     */
    public List<OptionModel> getCourseBookFromDb(String cos_sem_id, String cob_pub_id, String cob_sec_id, String cob_sub_id) {
        String sql = " select cob_id,cob_name from T_CourseBook inner join T_CourseBookSemester on T_CourseBook.cob_id=T_CourseBookSemester.cos_cob_id where cos_sem_id=? and cob_pub_id=? and cob_sec_id=? and cob_sub_id=?";
        Cursor cursor = db.rawQuery(sql, new String[]{cos_sem_id, cob_pub_id, cob_sec_id, cob_sub_id});

        List<OptionModel> optionModels = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("cob_id"));
                String name = cursor.getString(cursor.getColumnIndex("cob_name"));

                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);

                optionModels.add(cm);
            } while (cursor.moveToNext());
        } else {
            String sql2 = "select cob_id,cob_name from T_CourseBook where cob_pub_id=? and cob_sec_id=? and cob_sub_id=?";
            Cursor cursor2 = db.rawQuery(sql2, new String[]{cob_pub_id, cob_sec_id, cob_sub_id});
            if (cursor2 != null && cursor2.moveToFirst()) {
                do {
                    int id = cursor2.getInt(cursor2.getColumnIndex("cob_id"));
                    String name = cursor2.getString(cursor2.getColumnIndex("cob_name"));

                    OptionModel cm = new OptionModel();
                    cm.setChosen(false);
                    cm.setOptionName(name);
                    cm.setId(id);

                    optionModels.add(cm);
                } while (cursor2.moveToNext());
            }
        }
        return optionModels;
    }

    /**
     * 获取章节名称父目录
     *
     * @param cobId 教材章节所属教材id（引用courseBook的id）
     * @return
     */
    public List<OptionModel> getCourseChapterMenuFromDb(String cobId) {
        String sql = "select cch_id,cch_name from T_CourseChapter where cch_cob_id=? and cch_pid is null";
        Cursor cursor = db.rawQuery(sql, new String[]{cobId});

        List<OptionModel> optionModels = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("cch_id"));
                String name = cursor.getString(cursor.getColumnIndex("cch_name"));
                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);
                optionModels.add(cm);
            } while (cursor.moveToNext());
        }
        return optionModels;
    }

    /**
     * 获取章节名称子目录
     *
     * @param cobId 教材章节所属教材id（引用courseBook的id）
     * @param cchId 章节的上级章节id
     * @return
     */
    public List<OptionModel> getCourseChapterChildFromDb(String cobId, String cchId) {
        String sql = "select cch_id,cch_name from T_CourseChapter where cch_cob_id=? and cch_pid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{cobId, cchId});

        List<OptionModel> optionModels = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("cch_id"));
                String name = cursor.getString(cursor.getColumnIndex("cch_name"));
                OptionModel cm = new OptionModel();
                cm.setChosen(false);
                cm.setOptionName(name);
                cm.setId(id);
                optionModels.add(cm);
            } while (cursor.moveToNext());
        }
        return optionModels;
    }


    public static String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }
}


