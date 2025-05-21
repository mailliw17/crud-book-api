package com.example.crud_book.controller;

import com.example.crud_book.model.Book;
import com.example.crud_book.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBooks(){
     try {
         List<Book> bookList = new ArrayList<>();
         bookRepo.findAll().forEach(bookList::add);

         if(bookList.isEmpty()){
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         }

         return new ResponseEntity<>(bookList, HttpStatus.OK);
     } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
     }
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> bookOptional = bookRepo.findById(id);

        if(bookOptional.isPresent()) {
            return new ResponseEntity<>(bookOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/book")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        try {
            Book bookObj = bookRepo.save(book);

            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("book/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBookData) {
//        System.out.println(newBookData.getClass().getName());
                try {
            Optional<Book> oldBookData = bookRepo.findById(id);

            if(oldBookData.isPresent()) {
                Book updatedBookData = oldBookData.get();
                updatedBookData.setTitle(newBookData.getTitle());
                updatedBookData.setAuthor(newBookData.getAuthor());

                Book book = bookRepo.save(updatedBookData);
                return new ResponseEntity<>(book, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("book/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id){
        Optional<Book> book = bookRepo.findById(id);

        if(book.isPresent()) {
            bookRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
