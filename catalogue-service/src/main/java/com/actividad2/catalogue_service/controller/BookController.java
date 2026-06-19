package com.actividad2.catalogue_service.controller;

import com.actividad2.catalogue_service.dto.BookAvailabilityResponse;
import com.actividad2.catalogue_service.dto.BookRequest;
import com.actividad2.catalogue_service.dto.BookResponse;
import com.actividad2.catalogue_service.service.BookService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> findAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponse> partialUpdate(@PathVariable Long id,
                                                      @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.partialUpdate(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publicationDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Boolean visible
    ) {
        return ResponseEntity.ok(
                bookService.search(title, author, publicationDate, category, isbn, rating, visible)
        );
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<BookAvailabilityResponse> checkAvailability(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Integer quantity
    ) {
        return ResponseEntity.ok(bookService.checkAvailability(id, quantity));
    }
}