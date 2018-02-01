package com.example.demo.service;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Service
public class PersonService  {


    @Autowired
    PersonRepository personRepository;

  public List<Person> getAllPersons(){
        return personRepository.findAll();
    }

    public Person getOnePerson(long id) {
        return personRepository.findOne(id);
    }


    public void updatePerson(Person person){
      Person persona = personRepository.findOne(person.getId());

        persona.setName(person.getName());
        persona.setSurname(person.getSurname());

        personRepository.save(person);


    }

    public void deletePerson(Long id) {
      personRepository.delete(id);
    }

    public void createPerson(Person person) {
        personRepository.save(person);
    }


}
