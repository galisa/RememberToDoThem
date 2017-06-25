package com.burakgalisa.android.remembertodothem.database;

/**
 * Created by Burak on 8.6.2017.
 */

public class ChoreDbSchema {
    public static final class ChoreTable {
        public static final String NAME = "chores";

        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
