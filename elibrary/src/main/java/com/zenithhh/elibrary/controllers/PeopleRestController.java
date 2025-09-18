package com.zenithhh.elibrary.controllers;

import com.zenithhh.elibrary.dto.PersonDTO;
import com.zenithhh.elibrary.models.Person;
import com.zenithhh.elibrary.services.PeopleService;
import com.zenithhh.elibrary.util.BadPersonException;
import com.zenithhh.elibrary.util.ErrorResponse;
import com.zenithhh.elibrary.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
@CrossOrigin(origins = "http://localhost:4200")
public class PeopleRestController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleRestController(PeopleService peopleService,
                                ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public List<PersonDTO> readAll(){
        return peopleService.findAll().stream().map(this::convertToPersonDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO readOne(@PathVariable("id") int id){
        return convertToPersonDTO(peopleService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BadPersonException(errorMsgBuilder(bindingResult));
        }
        peopleService.createPerson(convertToPerson(personDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        peopleService.deletePerson(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid PersonDTO personDTO,
                       @PathVariable("id") int id,
                       BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            throw new BadPersonException(errorMsgBuilder(bindingResult));
        }
        personDTO.setId(id);
        peopleService.updatePerson(convertToPerson(personDTO), id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e){
        ErrorResponse personErrorResponse = new ErrorResponse(
                "Person not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadPersonException e){
        ErrorResponse ErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }

    private String errorMsgBuilder(BindingResult bindingResult){
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for(FieldError error : errors){
            errorMsg.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return errorMsg.toString();
    }
}
