package com.example.demo.service;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    SkillService skillService;


    @PreAuthorize("hasAnyAuthority('admin','user')")
    @Cacheable("persons")
  public List<Person> getAllPersons(){
        return personRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @Cacheable("person")
    public Person getOnePerson(long id) {
        return personRepository.findOne(id);
    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @Caching(evict = {
            @CacheEvict(value = "person", allEntries = true),
            @CacheEvict(value = "persons", allEntries = true)})
    public void updatePerson(Person person){
      Person persona = personRepository.findOne(person.getId());

        persona.setName(person.getName());
        persona.setSurname(person.getSurname());

        personRepository.save(person);


    }
    @PreAuthorize("hasAnyAuthority('admin','user')")
    @Caching(evict = {
            @CacheEvict(value = "person", allEntries = true),
            @CacheEvict(value = "persons", allEntries = true)})
    public void deletePerson(Long id) {

      skillService.deleteSkills(id);
      personRepository.delete(id);

    }

    @PreAuthorize("hasAnyAuthority('admin','user')")
    @CacheEvict(value = "persons", allEntries = true)
    public void createPerson(Person person) {
        personRepository.save(person);
    }


}
