package com.zenithhh.elibrary.services;

import com.zenithhh.elibrary.models.Book;
import com.zenithhh.elibrary.models.Person;
import com.zenithhh.elibrary.repositories.BooksRepository;
import com.zenithhh.elibrary.util.BookNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService{
    private final BooksRepository booksRepository;
    private final ModelMapper modelMapper;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, ModelMapper modelMapper, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.modelMapper = modelMapper;
        this.peopleService = peopleService;
    }

    @Transactional
    public void createBook(Book book){
        book.setCreated_at(LocalDateTime.now());
        book.setUpdated_at(LocalDateTime.now());
        book.setCreated_who("ADMIN");
        booksRepository.save(book);
    }

    public List<Book> findAll(){
        return booksRepository.findAll();
    }

    public List<Book> findForPage(int size, int page){
        return booksRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public List<Book> findSortBy(String param){
        return booksRepository.findAll(Sort.by(param));
    }

    public List<Book> findForPageAndSortBy(int size, int page, String param){
        return booksRepository.findAll(PageRequest.of(page, size, Sort.by(param))).getContent();
    }

    public Book findOne(int id){
        return booksRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @Transactional
    public void deleteBook(int id){
        booksRepository.deleteById(id);
    }

    @Transactional
    public void updateBook(Book updatedBook, int id){
        Book book = booksRepository.findById(id).orElseThrow(BookNotFoundException::new);
        modelMapper.map(updatedBook, book);
        book.setUpdated_at(LocalDateTime.now());
        booksRepository.save(book);
    }

    @Transactional
    public void setOwner(int ownerId, int bookId){
        Book book = booksRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        book.setOwner(peopleService.findById(ownerId));
        book.setTaken_at(LocalDateTime.now());
        booksRepository.save(book);
    }

    @Transactional
    public void freeBook(int id){
        Book book = booksRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setTaken_at(null);
        book.setOwner(null);
        booksRepository.save(book);
    }

}
