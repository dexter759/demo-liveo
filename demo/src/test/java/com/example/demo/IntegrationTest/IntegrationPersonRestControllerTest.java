package com.example.demo.IntegrationTest;


import com.example.demo.DemoApplication;
import com.example.demo.entity.Person;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
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
public class IntegrationPersonRestControllerTest {

    private JSONRead jsonRead = new JSONRead();

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    private String createURLWithPort(String uri) {
        return "http://localhost:"+port+ uri;
    }

    @Before
    public void setUp() {
        restTemplate = new TestRestTemplate();
    }


        @Test
    public void getAllPersons(){

        HttpEntity<String> entity = new HttpEntity<>(null,headers);

        //Getting String response from random port on URL localhost:8080/api/persons
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/persons/")
                ,HttpMethod.GET,entity,String.class);

        //Creating String Array List for purpose to check if the response match
        List<String> expected = jsonRead.JsonArray("person.json");
        //expected("{id:2,name:Venic,surname:Cooper}");
        //expected("{id:1,name:Dexter,surname:Armagedon}");

       //Checking if the response match the Array List
        try {
            JSONAssert.assertEquals(expected.toString(),response.getBody(),false);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testGetOnePersons(){

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        //Getting String response from random port on URL localhost:8080/api/persons/1
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/persons/2"),
                HttpMethod.GET,entity,String.class);

        //Creating a string for purpose to check if the response match
        ArrayList<String> strings = jsonRead.JsonArray("person.json");
        String expected = strings.get(1);

        //Checking if the response match the String
        try {
            JSONAssert.assertEquals(expected,response.getBody(),false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseEntity<String> responseError = restTemplate.exchange(createURLWithPort("/api/persons/123"),
                HttpMethod.GET,entity,String.class);

        String expectedError = "Bad Request there is no user with id:123";

        try {
            Assert.assertEquals(expectedError, responseError.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addPerson(){

        //Creating a person
        Person person = new Person("Bahtija","Tukkar");

        //Parsing the person in body
        HttpEntity<Person> entity = new HttpEntity<>(person,headers);

        //Making a Post request on localhost:8080/api/persons
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/persons"),
                HttpMethod.POST,entity,String.class);

        //Getting Location of the added person(localhost:8080/api/persons/{id})
        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertTrue(actual.contains("/api/persons/"));

        //Deleting the added person because of the the testing functionality
        restTemplate.exchange(actual,
                HttpMethod.DELETE,entity,String.class);
    }

    @Test
    public void deletePerson(){

        //Creating a user
        Person person = new Person("Dexter","Armagedon");

        //Parsing the person in body on Entity for create person
        HttpEntity<Person> entity2 = new HttpEntity<>(person,headers);

        //Making a Post request on localhost:8080/api/persons
        ResponseEntity<String> response= restTemplate.exchange(
                createURLWithPort("api/persons"),
                HttpMethod.POST,entity2,String.class);

        //Entity for delete
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        //Getting Location of the added person(localhost:8080/api/persons/{id})
        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        //Making DELETE request on created persons location
        ResponseEntity<String> responseDelete = restTemplate.exchange(actual,
                HttpMethod.DELETE,entity,String.class);

        //Testing Status Code aka what is expected to get when the user is delete
        String actualStatusCode ="204";

        //Checking the Status Code from response
        try {
            JSONAssert.assertEquals(actualStatusCode,responseDelete.getStatusCode().toString(),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Making a get function on the persons location
        ResponseEntity<String> responseGet = restTemplate.exchange(actual,
                HttpMethod.GET,entity,String.class);

        //Expected status code for the person who is not in the database
        String actual2 ="400";

        //Checking the Status Code from response
        try {
            JSONAssert.assertEquals(actual2,responseGet.getStatusCode().toString(),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ResponseEntity<String> responseError = restTemplate.exchange(createURLWithPort("/api/persons/123"),
                HttpMethod.DELETE,entity,String.class);

        String expectedError = "Bad Request there is no user with id:123 so we can't delete him";

        try {
            Assert.assertEquals(expectedError, responseError.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updatePerson(){

        //Creating a person
        Person person = new Person("Bahtija","Tukkar");
        person.setId(1L);

        //Parsing a person to body
        HttpEntity<Person> entity = new HttpEntity<>(person,headers);

        //Making a PUT request on person with id=1
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("api/persons"),
                HttpMethod.PUT,entity,String.class);

        //Expected status code
        String actual = "200";

        //Checking the status code from response
        try {
            JSONAssert.assertEquals(actual,response.getStatusCode().toString(),true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Creating a Get Entity
        HttpEntity<String> entityGet = new HttpEntity<>(null, headers);

        //Getting the person from the updated Id
        ResponseEntity<String> responseGet = restTemplate.exchange(createURLWithPort("/api/persons/"+person.getId()),
                HttpMethod.GET,entityGet,String.class);

        //Expected person at that ID
        String expected = "{id:1,name:Bahtija,surname:Tukkar}";

        //Checking if the user has been updated
        try {
            JSONAssert.assertEquals(expected,responseGet.getBody(),false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Making a original person
        Person personOriginal = new Person("Dexter","Armagedon");
        personOriginal.setId(1L);

        //Parsing an original person to body
        HttpEntity<Person> entityPut = new HttpEntity<>(personOriginal,headers);

        //Changing the person back to original on id:1 because of testing purposes
        restTemplate.exchange(createURLWithPort("api/persons"),
                HttpMethod.PUT,entityPut,String.class);
    }

}
