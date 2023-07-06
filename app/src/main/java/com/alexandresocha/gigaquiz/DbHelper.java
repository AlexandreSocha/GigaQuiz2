package com.alexandresocha.gigaquiz;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GigaQuiz.db";
    private static final int DATABASE_VERSION = 2;

    private static DbHelper instance;
    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DbHelper getInstance(Context context){
        if(instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                Table.Categories.TABLE_NAME + "( " +
                Table.Categories._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.Categories.COL_LIBELLE + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                Table.Questions.TABLE_NAME + " ( " +
                Table.Questions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Table.Questions.COL_QUESTION + " TEXT, " +
                Table.Questions.COL_OPTION_1 + " TEXT, " +
                Table.Questions.COL_OPTION_2 + " TEXT, " +
                Table.Questions.COL_OPTION_3 + " TEXT, " +
                Table.Questions.COL_OPTION_4 + " TEXT, " +
                Table.Questions.COL_REPONSE + " INTEGER, " +
                Table.Questions.COL_DIFFICULTE + " TEXT, " +
                Table.Questions.COL_CATEGORIE_ID + " INTEGER, " +
                "FOREIGN KEY(" + Table.Questions.COL_CATEGORIE_ID + ") REFERENCES " +
                Table.Categories.TABLE_NAME + "(" + Table.Categories._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table.Categories.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Table.Questions.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void addCategory(QuestionCategorie category){
        db = getWritableDatabase();
        insertCategory(category);
    }

    public void updateCategory(int id, QuestionCategorie category){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Table.Categories.COL_LIBELLE, category.getName());

        String selection = Table.Categories._ID + " = ? ";
        String[] selectionArgs = new String[]{ String.valueOf(id) };

        db.update(Table.Categories.TABLE_NAME, cv, selection, selectionArgs);
    }

    public void deleteCategory(int id){
        db = getWritableDatabase();

        String selection = Table.Categories._ID + " = ? ";
        String[] selectionArgs = new String[]{ String.valueOf(id) };
        db.delete(Table.Categories.TABLE_NAME, selection, selectionArgs);
    }

    public void addCategories(List<QuestionCategorie> categories){
        db = getWritableDatabase();

        for(QuestionCategorie category : categories){
            insertCategory(category);
        }
    }
    private void insertCategory(QuestionCategorie category){
        ContentValues cv = new ContentValues();
        cv.put(Table.Categories.COL_LIBELLE, category.getName());
        db.insert(Table.Categories.TABLE_NAME, null, cv);
    }

    private void fillCategoriesTable(){
        QuestionCategorie c1 = new QuestionCategorie("Programmation");
        insertCategory(c1);
        QuestionCategorie c2 = new QuestionCategorie("Geographie");
        insertCategory(c2);
        QuestionCategorie c3 = new QuestionCategorie("Mathématiques");
        insertCategory(c3);
    }
    private void fillQuestionsTable(){
        Question q1 = new Question("Microsoft Excel est un ...",
                "Compilateur", "Débugger", "Programme", "Langage", 3,
                Question.DIFFICULTY_EASY, QuestionCategorie.PROGRAMMING);
        insertQuestion(q1);
        Question q2 = new Question("Le langage que l'ordinateur comprend est",
                "Java", "Visual Basic", "Binaire","Les non binaires", 3,
                Question.DIFFICULTY_EASY, QuestionCategorie.PROGRAMMING);
        insertQuestion(q2);
        Question q3 = new Question("L'un de ceux-ci n'est pas un programme :",
                "Le code source", "L'IDE", "Le compilateur","Word", 1,
                Question.DIFFICULTY_EASY, QuestionCategorie.PROGRAMMING);
        insertQuestion(q3);
        Question q4 = new Question("Le C++ est un ...",
                "Debugger", "Compilateur", "Langage haut niveau", "Langage bas niveau",3,
                Question.DIFFICULTY_MEDIUM, QuestionCategorie.PROGRAMMING);
        insertQuestion(q4);
        Question q5 = new Question("Le programme qui fait la traduction en langage binaire est dit ...",
                "Compilateur", "Debugger", "Editeur de texte", "Traducteur",1,
                Question.DIFFICULTY_MEDIUM, QuestionCategorie.PROGRAMMING);
        insertQuestion(q5);
    }

    public void addQuestion(Question question){
        db = getWritableDatabase();
        insertQuestion(question);
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();

        for(Question question : questions){
            insertQuestion(question);
        }
    }

    public void updateQuestion(int id, Question question){
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Table.Questions.COL_QUESTION, question.getQuestion());
        cv.put(Table.Questions.COL_OPTION_1, question.getOption1());
        cv.put(Table.Questions.COL_OPTION_2, question.getOption2());
        cv.put(Table.Questions.COL_OPTION_3, question.getOption3());
        cv.put(Table.Questions.COL_OPTION_4, question.getOption4());
        cv.put(Table.Questions.COL_REPONSE, question.getAnswerNr());
        cv.put(Table.Questions.COL_DIFFICULTE, question.getDifficulty());
        cv.put(Table.Questions.COL_CATEGORIE_ID, question.getCategoryID());

        String selection = Table.Questions._ID + " = ? ";
        String[] selectionArgs = new String[]{ String.valueOf(id) };

        db.update(Table.Questions.TABLE_NAME, cv, selection, selectionArgs);
    }
    public void deleteQuestion(int id){
        db = getWritableDatabase();

        String selection = Table.Questions._ID + " = ? ";
        String[] selectionArgs = new String[]{ String.valueOf(id) };
        db.delete(Table.Questions.TABLE_NAME, selection, selectionArgs);
    }
    private void insertQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(Table.Questions.COL_QUESTION, question.getQuestion());
        cv.put(Table.Questions.COL_OPTION_1, question.getOption1());
        cv.put(Table.Questions.COL_OPTION_2, question.getOption2());
        cv.put(Table.Questions.COL_OPTION_3, question.getOption3());
        cv.put(Table.Questions.COL_OPTION_4, question.getOption4());
        cv.put(Table.Questions.COL_REPONSE, question.getAnswerNr());
        cv.put(Table.Questions.COL_DIFFICULTE, question.getDifficulty());
        cv.put(Table.Questions.COL_CATEGORIE_ID, question.getCategoryID());
        db.insert(Table.Questions.TABLE_NAME, null, cv);
    }

    @SuppressLint("Range")
    public ArrayList<QuestionCategorie> getAllCategories(){
        ArrayList<QuestionCategorie> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Table.Categories.TABLE_NAME, null);

        if(c.moveToFirst()){
            do {
                QuestionCategorie category = new QuestionCategorie();
                category.setId(c.getInt(c.getColumnIndex(Table.Categories._ID)));
                category.setName(c.getString(c.getColumnIndex(Table.Categories.COL_LIBELLE)));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }
    @SuppressLint("Range")
    public QuestionCategorie getCategory(int categoryID){
        ArrayList<QuestionCategorie> categoryList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = Table.Categories._ID + " = ? ";
        String[] selectionArgs = new String[]{ String.valueOf(categoryID) };

        Cursor c = db.query(Table.Categories.TABLE_NAME,null,
                selection, selectionArgs,null,null,null );

        if(c.moveToFirst()){
            do {
                QuestionCategorie category = new QuestionCategorie();
                category.setId(c.getInt(c.getColumnIndex(Table.Categories._ID)));
                category.setName(c.getString(c.getColumnIndex(Table.Categories.COL_LIBELLE)));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return categoryList.get(0);
    }

    @SuppressLint("Range")
    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + Table.Questions.TABLE_NAME, null);

        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Table.Questions._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(Table.Questions.COL_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_1)));
                question.setOption2(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_2)));
                question.setOption3(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_3)));
                question.setOption4(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(Table.Questions.COL_REPONSE)));
                question.setDifficulty(c.getString(c.getColumnIndex(Table.Questions.COL_DIFFICULTE)));
                question.setCategoryID(c.getInt(c.getColumnIndex(Table.Questions.COL_CATEGORIE_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    @SuppressLint("Range")
    public ArrayList<Question> getQuestions(int categoryID, String difficulty){
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = Table.Questions.COL_CATEGORIE_ID + " = ? " +
                " AND " + Table.Questions.COL_DIFFICULTE + " = ? ";
        String[] selectionArgs = new String[]{ String.valueOf(categoryID), difficulty };

        Cursor c = db.query(
                Table.Questions.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(c.moveToFirst()){
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Table.Questions._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(Table.Questions.COL_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_1)));
                question.setOption2(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_2)));
                question.setOption3(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_3)));
                question.setOption4(c.getString(c.getColumnIndex(Table.Questions.COL_OPTION_4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(Table.Questions.COL_REPONSE)));
                question.setDifficulty(c.getString(c.getColumnIndex(Table.Questions.COL_DIFFICULTE)));
                question.setCategoryID(c.getInt(c.getColumnIndex(Table.Questions.COL_CATEGORIE_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public static class Table {
        public static class Categories implements BaseColumns {
            public static final String TABLE_NAME = "questions_categories";
            public static final String COL_LIBELLE = "libelle";
        }

        public static class Questions implements BaseColumns {
            public static final String TABLE_NAME= "questions";
            public static final String COL_QUESTION= "qst_question";
            public static final String COL_OPTION_1= "qst_opt_1";
            public static final String COL_OPTION_2= "qst_opt_2";
            public static final String COL_OPTION_3= "qst_opt_3";
            public static final String COL_OPTION_4= "qst_opt_4";
            public static final String COL_REPONSE= "qst_reponse";
            public static final String COL_DIFFICULTE= "qst_difficulte";
            public static final String COL_CATEGORIE_ID = "qst_cat_id";

        }
    }

}
