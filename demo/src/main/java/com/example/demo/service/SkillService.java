package com.example.demo.service;


import com.example.demo.entity.Skill;
import com.example.demo.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SkillRepository skillRepository;

    @PreAuthorize("hasAnyAuthority('admin')")
    @Caching(evict = {
            @CacheEvict(value = "person", allEntries = true),
            @CacheEvict(value = "skill", allEntries = true),
            @CacheEvict(value = "skills", allEntries = true),
            @CacheEvict(value = "persons", allEntries = true)})
    public void deleteSkills(Long id){
        final String sql = "DELETE FROM skill WHERE person_id = ?";
        jdbcTemplate.update(sql, id);
    }
    @PreAuthorize("hasAnyAuthority('admin')")
    @Caching(evict = {
            @CacheEvict(value = "person", allEntries = true),
            @CacheEvict(value = "skills", allEntries = true),
            @CacheEvict(value = "persons", allEntries = true)})
    public void addSkill(Skill skill){
       skillRepository.save(skill);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @Cacheable("skills")
    public List<Skill> getAllSkill(){
        return skillRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @Cacheable("skill")
    public Skill getOneSkill(long id) {
        return skillRepository.findOne(id);
    }


    @PreAuthorize("hasAnyAuthority('admin')")
    @Caching(evict = {
            @CacheEvict(value = "person", allEntries = true),
            @CacheEvict(value = "skills", allEntries = true),
            @CacheEvict(value = "skill", allEntries = true),
            @CacheEvict(value = "persons", allEntries = true)})
    public void updateSkill(Skill skills){
        Skill skill = skillRepository.findOne(skills.getId());

        skill.setName(skills.getName());
        skill.setLevel(skills.getLevel());
        skill.setPerson(skills.getPerson());

        skillRepository.save(skill);

    }
    @PreAuthorize("hasAnyAuthority('admin')")
    public void deleteSkill(Long id) {
        skillRepository.delete(id);
    }


}
