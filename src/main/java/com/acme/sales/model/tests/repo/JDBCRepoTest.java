package com.acme.sales.model.tests.repo;

import com.acme.infra.postgresql.JDBCBase;

import java.sql.SQLException;

public class JDBCRepoTest extends JDBCBase {

    public static void main(String[] args) throws Exception {
        String personIdJSON = insertPerson("rajan", 22);
        System.out.println("Inserted = " + personIdJSON);

        personIdJSON = insertPerson("jane", 29);
        System.out.println("Inserted = " + personIdJSON);

        String result = selectPerson("jane");
        System.out.println("Selected = " + result);
    }

    private static String insertPerson(String name, int age) throws SQLException {
        String sql = "INSERT INTO PERSON(name,age) VALUES(";
        sql += "'" + name + "', " + age + ") RETURNING person_id AS person_id";

        System.out.println("SQL INSERT statement = " + sql);

        JDBCRepoTest repo = new JDBCRepoTest();
        return repo.executeSQLUpdate(sql);
    }

    private static String selectPerson(String name) throws SQLException {
        String sql = "SELECT * FROM PERSON WHERE name = '" + name + "'";

        System.out.println("SQL SELECT statement = " + sql);

        JDBCRepoTest repo = new JDBCRepoTest();
        return repo.executeSQL(sql);
    }
}
