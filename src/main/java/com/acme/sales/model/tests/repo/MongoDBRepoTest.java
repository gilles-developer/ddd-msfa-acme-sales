package com.acme.sales.model.tests.repo;

import com.acme.infra.mongodb.MongoDBBase;
import com.mongodb.client.model.Filters;

public class MongoDBRepoTest extends MongoDBBase {

    public static void main(String[] args) {

        MongoDBRepoTest mongoDBRepoTest = new MongoDBRepoTest();

        String json = "{'name': 'bobette', 'age': 16}";

        mongoDBRepoTest.executeInsert("test", json);
        System.out.println("1. inserted into collection");

        json = mongoDBRepoTest.find("test", Filters.eq("name", "bob")).iterator().next().toString();

        System.out.println("2. Retrieved from mongodb: " + json);

    }
}
