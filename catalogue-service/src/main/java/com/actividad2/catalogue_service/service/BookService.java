package com.actividad2.catalogue_service.service;

import com.actividad2.catalogue_service.dto.BookAvailabilityResponse;
import com.actividad2.catalogue_service.dto.BookRequest;
import com.actividad2.catalogue_service.dto.BookResponse;
import com.actividad2.catalogue_service.entity.Book;
import com.actividad2.catalogue_service.repository.BookRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));

        return toResponse(book);
    }

    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + request.getIsbn());
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .publicationDate(request.getPublicationDate())
                .category(request.getCategory())
                .isbn(request.getIsbn())
                .rating(request.getRating())
                .visible(request.getVisible())
                .stock(request.getStock())
                .price(request.getPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        Book savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublicationDate(request.getPublicationDate());
        book.setCategory(request.getCategory());
        book.setIsbn(request.getIsbn());
        book.setRating(request.getRating());
        book.setVisible(request.getVisible());
        book.setStock(request.getStock());
        book.setPrice(request.getPrice());
        book.setDescription(request.getDescription());
        book.setImageUrl(request.getImageUrl());

        Book updatedBook = bookRepository.save(book);
        return toResponse(updatedBook);
    }

    public BookResponse partialUpdate(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));

        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }

        if (request.getAuthor() != null) {
            book.setAuthor(request.getAuthor());
        }

        if (request.getPublicationDate() != null) {
            book.setPublicationDate(request.getPublicationDate());
        }

        if (request.getCategory() != null) {
            book.setCategory(request.getCategory());
        }

        if (request.getIsbn() != null) {
            book.setIsbn(request.getIsbn());
        }

        if (request.getRating() != null) {
            book.setRating(request.getRating());
        }

        if (request.getVisible() != null) {
            book.setVisible(request.getVisible());
        }

        if (request.getStock() != null) {
            book.setStock(request.getStock());
        }

        if (request.getPrice() != null) {
            book.setPrice(request.getPrice());
        }

        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }

        if (request.getImageUrl() != null) {
            book.setImageUrl(request.getImageUrl());
        }

        Book updatedBook = bookRepository.save(book);
        return toResponse(updatedBook);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }

        bookRepository.deleteById(id);
    }

    public List<BookResponse> search(String title,
                                     String author,
                                     LocalDate publicationDate,
                                     String category,
                                     String isbn,
                                     Double rating,
                                     Boolean visible) {

        Specification<Book> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                ));
            }

            if (author != null && !author.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author")),
                        "%" + author.toLowerCase() + "%"
                ));
            }

            if (publicationDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("publicationDate"), publicationDate));
            }

            if (category != null && !category.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("category")),
                        "%" + category.toLowerCase() + "%"
                ));
            }

            if (isbn != null && !isbn.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), isbn));
            }

            if (rating != null) {
                predicates.add(criteriaBuilder.equal(root.get("rating"), rating));
            }

            if (visible != null) {
                predicates.add(criteriaBuilder.equal(root.get("visible"), visible));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return bookRepository.findAll(specification)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BookAvailabilityResponse checkAvailability(Long id, Integer quantity) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            return new BookAvailabilityResponse(
                    id,
                    null,
                    false,
                    false,
                    0,
                    null,
                    false,
                    "El libro no existe"
            );
        }

        if (Boolean.FALSE.equals(book.getVisible())) {
            return new BookAvailabilityResponse(
                    book.getId(),
                    book.getTitle(),
                    true,
                    false,
                    book.getStock(),
                    book.getPrice(),
                    false,
                    "El libro no está visible"
            );
        }

        Integer requestedQuantity = quantity == null ? 1 : quantity;
        Integer availableStock = book.getStock() == null ? 0 : book.getStock();

        if (availableStock < requestedQuantity) {
            return new BookAvailabilityResponse(
                    book.getId(),
                    book.getTitle(),
                    true,
                    true,
                    availableStock,
                    book.getPrice(),
                    false,
                    "Stock insuficiente"
            );
        }

        return new BookAvailabilityResponse(
                book.getId(),
                book.getTitle(),
                true,
                true,
                availableStock,
                book.getPrice(),
                true,
                "Libro disponible"
        );
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationDate(),
                book.getCategory(),
                book.getIsbn(),
                book.getRating(),
                book.getVisible(),
                book.getStock(),
                book.getPrice(),
                book.getDescription(),
                book.getImageUrl(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}