package com.example.demo.service;


import com.example.demo.entity.Skill;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    PersonRepository personRepository;


    public void deleteSkills(Long id){
        final String sql = "DELETE FROM skill WHERE person_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void addSkill(Skill skill){
       skillRepository.save(skill);
    }

    public List<Skill> getAllSkill(){
        return skillRepository.findAll();
    }

    public Skill getOneSkill(long id) {
        return skillRepository.findOne(id);
    }


    public void updateSkill(Skill skills){
        Skill skill = skillRepository.findOne(skills.getId());

        skill.setName(skills.getName());
        skill.setLevel(skills.getLevel());
        skill.setPerson(skills.getPerson());

        skillRepository.save(skill);

    }

    public void deleteSkill(Long id) {
        skillRepository.delete(id);
    }


}
