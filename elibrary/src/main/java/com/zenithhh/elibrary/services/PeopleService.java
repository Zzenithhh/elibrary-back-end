package com.zenithhh.elibrary.services;

import com.zenithhh.elibrary.models.Person;
import com.zenithhh.elibrary.repositories.PeopleRepository;
import com.zenithhh.elibrary.util.PersonNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, ModelMapper modelMapper) {
        this.peopleRepository = peopleRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void createPerson(Person person){
        enrichPerson(person);
        peopleRepository.save(person);
    }

    public Person findById(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    @Transactional
    public void updatePerson(Person updatedPerson, int id){
        Person person = peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);

        modelMapper.map(updatedPerson, person);
        person.setUpdated_at(LocalDateTime.now());

        peopleRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id){
        peopleRepository.deleteById(id);
    }



    private void enrichPerson(Person person){
        person.setCreated_at(LocalDateTime.now());
        person.setUpdated_at(LocalDateTime.now());
        person.setCreated_who("ADMIN");
    }
}
