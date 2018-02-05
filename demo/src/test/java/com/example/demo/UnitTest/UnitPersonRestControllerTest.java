package com.example.demo.UnitTest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.controller.PersonController;
import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import com.example.demo.service.SkillService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class UnitPersonRestControllerTest {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonService personService;

    List<Person> mockPersonList;

    @Test
    public void getAllPersons() throws Exception{

        mockPersonList = Arrays.asList(new Person("Dexter","Armagedon"), new Person("Venic","Cooper"));

        mockPersonList.get(0).setId(1L);
        mockPersonList.get(1).setId(2L);

        when(personService.getAllPersons()).thenReturn(mockPersonList);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/persons").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String expected ="[{id:1,name:Dexter,surname:Armagedon},{id:2,name:Venic,surname:Cooper}]";
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void getOnePerson() throws Exception{

     Person person =new Person("Dexter","Armagedon");

     person.setId(1L);

        when(personService.getOnePerson(1L)).thenReturn(person);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/persons/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        String expected ="{id:1,name:Dexter,surname:Armagedon}";
        JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void deleteOnePerson() throws  Exception{

        Person person =new Person("Dexter","Armagedon");

        person.setId(1L);

        when(personService.getOnePerson(1L)).thenReturn(person);
        doNothing().when(personService).deletePerson(person.getId());
            mvc.perform(MockMvcRequestBuilders.delete("/api/persons/{id}",person.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    public void  updateOnePerson() throws Exception{
        Person person =new Person("Dexter","Armagedon");

        person.setId(1L);

        when(personService.getOnePerson(person.getId())).thenReturn(person);
        doNothing().when(personService).updatePerson(person);

        mvc.perform(MockMvcRequestBuilders.put("/api/persons/").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(person)))
                .andExpect(status().isOk());
    }

    @Test
    public void createOnePerson()throws Exception{
        Person person =new Person("Dexter","Armagedon");
        doNothing().when(personService).createPerson(person);

        mvc.perform(MockMvcRequestBuilders.post("/api/persons/").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(person))).andExpect(status().isCreated());
    }


}
