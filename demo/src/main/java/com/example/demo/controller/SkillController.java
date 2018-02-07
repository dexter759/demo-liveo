package com.example.demo.controller;


import com.example.demo.entity.Skill;
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
@RequestMapping("api/admin/skills")
public class SkillController {

    @Autowired
    SkillService skillService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllSkills(){
        List<Skill> skillList = skillService.getAllSkill();
        if(skillList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no skills in our database");
        }
        return ResponseEntity.ok().body(skillList);
    }


    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Skill> insertSkill(@RequestBody @Valid Skill skill, UriComponentsBuilder builder) {

        skillService.addSkill(skill);
        UriComponents uriComponents =
                builder.path("/api/skills/{id}").buildAndExpand(skill.getId());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getSkillById(@PathVariable(value = "id") Long skillId) {
        Skill skill = skillService.getOneSkill(skillId);
        if(skill==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad Request there is no skill with id:"+ skillId);
        }
        return ResponseEntity.ok().body(skill);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Skill> editSkill(@RequestBody Skill skill){

        skillService.updateSkill(skill);

        return ResponseEntity.ok(skill);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTaskById(@PathVariable("id") long id){
        Skill skill = skillService.getOneSkill(id);
        if(skill==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Bad Request there is no skill with id:"+ id+" so we can't delete him");
        }

        skillService.deleteSkill(skill.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("You have deleted the user successfully");
    }

}
