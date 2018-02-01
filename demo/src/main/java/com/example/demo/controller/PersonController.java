package com.example.demo.controller;


import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import com.example.demo.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    SkillService skillService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllPersons(){
      List<Person> personList = personService.getAllPersons();
      if(personList.isEmpty()){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no users in our database");
      }
      return ResponseEntity.ok().body(personList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable(value = "id") Long personId) {
        Person person = personService.getOnePerson(personId);
        if(person==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad Request there is no user with id:"+ personId);
        }
        return ResponseEntity.ok().body(person);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> editPerson(@RequestBody Person person){

        personService.updatePerson(person);
        return ResponseEntity.ok(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTaskById(@PathVariable("id") long id){
        Person person = personService.getOnePerson(id);
        if(person==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad Request there is no user with id:"+ id+" so we can't delete him");
        }

        skillService.deleteSkills(person.getId());
        personService.deletePerson(person.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("You have deleted the user successfully");
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> insertPerson(@RequestBody @Valid Person person, UriComponentsBuilder builder){

        personService.createPerson(person);
        UriComponents uriComponents =
                builder.path("/api/persons/{id}").buildAndExpand(person.getId());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }

}
