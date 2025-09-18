package com.zenithhh.elibrary.controllers;

import com.zenithhh.elibrary.dto.BooksDTO;
import com.zenithhh.elibrary.dto.PersonDTO;
import com.zenithhh.elibrary.models.Book;
import com.zenithhh.elibrary.models.Person;
import com.zenithhh.elibrary.services.BooksService;
import com.zenithhh.elibrary.util.BadBookException;
import com.zenithhh.elibrary.util.BookNotFoundException;
import com.zenithhh.elibrary.util.ErrorResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "http://localhost:4200")
public class BooksRestController {
    private final BooksService booksService;
    private final ModelMapper modelMapper;

    @Autowired
    public BooksRestController(BooksService booksService, ModelMapper modelMapper) {
        this.booksService = booksService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<BooksDTO> readAll(@RequestParam(name = "page", required = false, defaultValue = "-1") int page,
                                  @RequestParam(name = "books_per_page", required = false, defaultValue = "-1") int books_per_page,
                                  @RequestParam(name = "sort_by_year", required = false, defaultValue = "false") boolean sort_by_year){

        if (page >= 0 && books_per_page > 0 && !sort_by_year) {
            return booksService.findForPage(books_per_page, page).stream().map(this::convertToBooksDTO).collect(Collectors.toList());
        } else if (page == -1 && books_per_page == -1 && sort_by_year) {
            return booksService.findSortBy("year").stream().map(this::convertToBooksDTO).collect(Collectors.toList());
        } else if (page >= 0 && books_per_page > 0 && sort_by_year) {
            return booksService.findForPageAndSortBy(books_per_page, page, "year").stream().map(this::convertToBooksDTO).collect(Collectors.toList());
        } else {
            return booksService.findAll().stream().map(this::convertToBooksDTO).collect(Collectors.toList());
        }
    }

    @GetMapping("/{id}")
    public BooksDTO readOne(@PathVariable("id") int id){
        return convertToBooksDTO(booksService.findOne(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody  @Valid BooksDTO booksDTO,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BadBookException(errorMsgBuilder(bindingResult));
        }
        booksService.createBook(convertToBook(booksDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        booksService.deleteBook(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid BooksDTO booksDTO,
                                             @PathVariable("id") int id,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BadBookException(errorMsgBuilder(bindingResult));
        }

        booksDTO.setId(id);
        booksService.updateBook(convertToBook(booksDTO), id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PatchMapping("/{id}/person")
    public void updateOwner(@RequestBody Map<String, Integer> data,
                            @PathVariable("id") int bookId){
        booksService.setOwner(data.get("personId"), bookId);
    }

    @PatchMapping("/{id}/free")
    public void deleteOwner(@PathVariable("id") int id){
        booksService.freeBook(id);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BookNotFoundException e){
        ErrorResponse bookErrorResponse = new ErrorResponse(
                "Book not found",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(bookErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadBookException e){
        ErrorResponse ErrorResponse = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(ErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private Book convertToBook(BooksDTO booksDTO) {
        return modelMapper.map(booksDTO, Book.class);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private BooksDTO convertToBooksDTO(Book book) {
        return modelMapper.map(book, BooksDTO.class);
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
