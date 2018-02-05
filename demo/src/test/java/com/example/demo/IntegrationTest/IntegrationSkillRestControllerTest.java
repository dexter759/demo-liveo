package com.example.demo.IntegrationTest;


import com.example.demo.DemoApplication;
import com.example.demo.entity.Level;
import com.example.demo.entity.Person;
import com.example.demo.entity.Skill;
import com.example.demo.service.SkillService;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationSkillRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    SkillService skillService;

    JSONRead jsonRead = new JSONRead();

    private TestRestTemplate restTemplate;
    private HttpHeaders headers = new HttpHeaders();


    @Before
    public void setUp() {
        restTemplate = new TestRestTemplate();
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:"+port+ uri;
    }

    @Test
    public void getAllSkills(){
        HttpEntity<String> entity = new HttpEntity<>(null,headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/skills/")
                ,HttpMethod.GET,entity,String.class);
        System.out.println(response.getBody());
        List<String> expected = jsonRead.JsonArray("skill.json");

//        expected.add("{id:1,name:Programing,level:GOOD}");
//        expected.add("{id:2,name:Testing,level:GODLIKE}");

        try {
            JSONAssert.assertEquals(expected.toString(),response.getBody(),false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOneSkill(){
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/skills/2"),
                HttpMethod.GET,entity,String.class);
        ArrayList<String> strings = jsonRead.JsonArray("skill.json");
        String expected = strings.get(1);

        try {
            JSONAssert.assertEquals(expected,response.getBody(),false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseEntity<String> responseError = restTemplate.exchange(createURLWithPort("/api/skills/123"),
                HttpMethod.GET,entity,String.class);

        String expectedError = "Bad Request there is no skill with id:123";

        try {
            Assert.assertEquals(expectedError, responseError.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Test
    public void addSkill(){

        Skill skill = new Skill("Flying", Level.AWESOME);

        HttpEntity<Skill> entity = new HttpEntity<>(skill,headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/skills"),
                HttpMethod.POST,entity,String.class);

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        assertTrue(actual.contains("/api/skills/"));

        restTemplate.exchange(actual,HttpMethod.DELETE,entity,String.class);
    }


    @Test
    public void deleteSkill(){

        Skill skill = new Skill("Flying", Level.AWESOME);

        HttpEntity<Skill> entityCreate = new HttpEntity<>(skill,headers);

        ResponseEntity<String> responseCreate = restTemplate.exchange(
                createURLWithPort("api/skills"),
                HttpMethod.POST,entityCreate,String.class);

        String actualLocation = responseCreate.getHeaders().get(HttpHeaders.LOCATION).get(0);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(actualLocation,
                HttpMethod.DELETE,entity,String.class);

        String actual ="204";
        try {
            JSONAssert.assertEquals(actual,response.getStatusCode().toString(),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ResponseEntity<String> response2 = restTemplate.exchange(actualLocation,
                HttpMethod.GET,entity,String.class);

        String actual2 ="400";
        try {
            JSONAssert.assertEquals(actual2,response2.getStatusCode().toString(),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseEntity<String> responseError = restTemplate.exchange(createURLWithPort("/api/skills/123"),
                HttpMethod.DELETE,entity,String.class);

        String expectedError = "Bad Request there is no skill with id:123 so we can't delete him";

        try {
            Assert.assertEquals(expectedError, responseError.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateSkill(){
        Person person = new Person();
        person.setId(1L);
        Skill skill = new Skill("Mining",Level.GODLIKE,person);
        skill.setId(1L);

        HttpEntity<Skill> entity = new HttpEntity<>(skill,headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/skills"),
                HttpMethod.PUT,entity,String.class);

        String actual ="200";
        try {
            JSONAssert.assertEquals(actual,response.getStatusCode().toString(),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity<String> entity2 = new HttpEntity<>(null, headers);

        ResponseEntity<String> response2 = restTemplate.exchange(createURLWithPort("/api/skills/"+skill.getId()),
                HttpMethod.GET,entity2,String.class);
        String expected = "{id:1,name:Mining,level:GODLIKE}";

        try {
            JSONAssert.assertEquals(expected,response2.getBody(),false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Skill skillOriginal = new Skill("Programing",Level.GOOD,person);
        skillOriginal.setId(1L);


        HttpEntity<Skill> entity3 = new HttpEntity<>(skillOriginal,headers);
        restTemplate.exchange(createURLWithPort("api/skills"),
                HttpMethod.PUT,entity3,String.class);
    }





}
