package com.careerpath;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

final class CareerDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "career_path.db";
    private static final int DB_VERSION = 1;

    CareerDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE careers (id INTEGER PRIMARY KEY, title TEXT NOT NULL, sector TEXT NOT NULL,"
                + " summary TEXT NOT NULL, subjects TEXT NOT NULL, pathway TEXT NOT NULL, salary TEXT NOT NULL)");
        db.execSQL("CREATE TABLE course_history (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL,"
                + " opened_at INTEGER NOT NULL)");

        String[][] careers = {
                {"Software Developer", "Technology", "Build apps, websites and tools that solve real problems.", "Mathematics, Information Technology, English", "NSC → Computer Science / Software Development diploma or degree → portfolio and internships", "R350k–R900k / year"},
                {"Cybersecurity Analyst", "Technology", "Help protect organisations and people from digital threats.", "Mathematics, Information Technology, Physical Sciences", "NSC → IT / cybersecurity qualification → practical labs and entry-level certification", "R350k–R850k / year"},
                {"Data Analyst", "Technology", "Turn information into useful insights for better decisions.", "Mathematics, Statistics, Information Technology", "NSC → Statistics / Data Science / IT study → projects and internships", "R300k–R750k / year"},
                {"AI / Machine Learning Engineer", "Technology", "Create computer systems that learn from data.", "Mathematics, Physical Sciences, Information Technology", "NSC → Computer Science / Engineering degree → programming and maths portfolio", "R500k–R1.2m / year"},
                {"Cloud Engineer", "Technology", "Build and maintain online computing infrastructure.", "Mathematics, Information Technology, Physical Sciences", "NSC → IT / Computer Science study → cloud labs and junior support role", "R400k–R900k / year"},
                {"Renewable Energy Engineer", "Engineering & Energy", "Design cleaner ways to generate and use electricity.", "Mathematics, Physical Sciences, Engineering Graphics and Design", "NSC → accredited electrical / energy engineering degree → candidate engineer path", "R400k–R900k / year"},
                {"Electrician", "Engineering & Energy", "Install, inspect and repair electrical systems safely.", "Mathematics, Physical Sciences, Technical subjects", "NSC → accredited apprenticeship / TVET study → trade test and registration", "R180k–R500k / year"},
                {"Civil Engineer", "Engineering & Energy", "Plan and build roads, water systems and public spaces.", "Mathematics, Physical Sciences, Engineering Graphics and Design", "NSC → accredited civil engineering degree → candidate engineer registration", "R350k–R800k / year"},
                {"Quantity Surveyor", "Engineering & Energy", "Manage construction costs, contracts and project budgets.", "Mathematics, Physical Sciences, Accounting", "NSC → accredited quantity surveying degree → professional candidate route", "R300k–R750k / year"},
                {"Solar PV Technician", "Engineering & Energy", "Install and maintain solar power systems.", "Mathematics, Physical Sciences, Technical subjects", "NSC → electrical / renewable energy TVET training → supervised field experience", "R180k–R450k / year"},
                {"Registered Nurse", "Health", "Care for patients and support healthier communities.", "Life Sciences, Mathematics or Mathematical Literacy, English", "NSC → accredited nursing programme → South African Nursing Council registration", "R220k–R650k / year"},
                {"Medical Doctor", "Health", "Diagnose illness and help people recover and stay well.", "Mathematics, Physical Sciences, Life Sciences, English", "NSC → MBChB at a recognised university → internship and community service", "R500k–R1.2m / year"},
                {"Pharmacist", "Health", "Prepare and explain medicines and support safe treatment.", "Mathematics, Physical Sciences, Life Sciences", "NSC → accredited pharmacy degree → internship and Pharmacy Council registration", "R400k–R850k / year"},
                {"Medical Laboratory Scientist", "Health", "Test samples to help doctors understand and treat disease.", "Mathematics, Physical Sciences, Life Sciences", "NSC → accredited medical laboratory science degree → professional registration", "R300k–R700k / year"},
                {"Agricultural Scientist", "Agriculture & Environment", "Improve crops, soil and food production sustainably.", "Mathematics, Life Sciences, Agricultural Sciences, Physical Sciences", "NSC → agricultural science degree / diploma → research or farm experience", "R280k–R700k / year"},
                {"Environmental Scientist", "Agriculture & Environment", "Study ecosystems and help organisations reduce environmental harm.", "Mathematics, Life Sciences, Geography, Physical Sciences", "NSC → environmental science degree → fieldwork and specialist experience", "R280k–R700k / year"},
                {"Supply Chain Analyst", "Business & Finance", "Help goods move efficiently from producers to customers.", "Mathematics, Accounting, Business Studies, Geography", "NSC → logistics / supply chain study → data skills and work experience", "R280k–R700k / year"},
                {"Financial Analyst", "Business & Finance", "Use financial information to guide business decisions.", "Mathematics, Accounting, Economics, English", "NSC → finance / accounting / economics degree or diploma → practical experience", "R350k–R850k / year"},
                {"UX / Product Designer", "Creative & Digital", "Make digital products easier and more enjoyable to use.", "Visual Arts, Design, Information Technology, English", "NSC → design / digital media study → portfolio, feedback and internships", "R300k–R750k / year"},
                {"Digital Marketing Specialist", "Creative & Digital", "Help organisations reach people through useful online content.", "English, Business Studies, Information Technology, Visual Arts", "NSC → marketing / communications study → campaign portfolio and analytics skills", "R240k–R600k / year"}
        };
        for (String[] row : careers) {
            ContentValues values = new ContentValues();
            values.put("title", row[0]);
            values.put("sector", row[1]);
            values.put("summary", row[2]);
            values.put("subjects", row[3]);
            values.put("pathway", row[4]);
            values.put("salary", row[5]);
            db.insertOrThrow("careers", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            onCreate(db);
        }
    }

    List<Job> searchCareers(String query, String sector) {
        List<Job> jobs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String filter = "%" + query.trim() + "%";
        StringBuilder selection = new StringBuilder(
                "(title LIKE ? OR sector LIKE ? OR summary LIKE ? OR subjects LIKE ? OR pathway LIKE ?)");
        List<String> args = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            args.add(filter);
        }
        if (sector != null && !sector.isEmpty()) {
            selection.append(" AND sector = ?");
            args.add(sector);
        }
        try (Cursor cursor = db.query("careers",
                new String[]{"title", "sector", "summary", "subjects", "pathway", "salary"},
                selection.toString(), args.toArray(new String[0]), null, null, "title COLLATE NOCASE")) {
            while (cursor.moveToNext()) {
                jobs.add(new Job(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            }
        }
        return jobs;
    }

    List<String> getSectors() {
        List<String> sectors = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT DISTINCT sector FROM careers ORDER BY sector COLLATE NOCASE", null)) {
            while (cursor.moveToNext()) {
                sectors.add(cursor.getString(0));
            }
        }
        return sectors;
    }

    void recordCourse(String title) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("opened_at", System.currentTimeMillis());
        getWritableDatabase().insertOrThrow("course_history", null, values);
    }

    List<String> getCourseHistory() {
        List<String> history = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT title FROM course_history ORDER BY opened_at DESC LIMIT 20", null)) {
            while (cursor.moveToNext()) {
                history.add(cursor.getString(0));
            }
        }
        return history;
    }
}
