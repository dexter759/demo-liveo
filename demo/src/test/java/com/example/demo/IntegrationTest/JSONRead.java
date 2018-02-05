package com.example.demo.IntegrationTest;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileReader;
import java.util.ArrayList;

public class JSONRead {

    public ArrayList<String> JsonArray(String jsonFile) {


        JSONParser parser = new JSONParser();

        Resource resource = new ClassPathResource(jsonFile);

        JSONArray jsonArray = null;
        try {

            Object obje = parser.parse(new FileReader(resource.getFile()));

            jsonArray = (JSONArray) obje;

        } catch (Exception e) {
            System.out.println(e);
        }
        ArrayList<String> arrayList = null;
        if (jsonArray != null) {

            arrayList = new ArrayList<>();
            for (Object obj : jsonArray) {
                arrayList.add(obj.toString());
            }
        }

        return arrayList;
    }


}
