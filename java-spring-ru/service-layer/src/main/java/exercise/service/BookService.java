package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.BadRequestException;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.model.Book;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private AuthorRepository authorRepository;

    //Получение списка книг
    public List<BookDTO> getAll() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTO = books.stream().map(bookMapper::map).toList();
        return bookDTO;
    }

    //Получение книги по id
    public BookDTO show(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        BookDTO bookDto = bookMapper.map(book);
        return bookDto;
    }

    //Добавление новой книги
    public BookDTO create(BookCreateDTO bookCreateDTO) {
        authorRepository.findById(bookCreateDTO.getAuthorId())
                .orElseThrow(() -> new BadRequestException("Author with id " + bookCreateDTO.getAuthorId() + " not found"));
        Book book = bookMapper.map(bookCreateDTO);
        bookRepository.save(book);
        BookDTO bookDto = bookMapper.map(book);
        return bookDto;
    }

    //Редактирование книги
    public BookDTO update(BookUpdateDTO bookUpdateDTO, Long id) {
        authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        bookMapper.update(bookUpdateDTO, book);
        bookRepository.save(book);
        var bookDto = bookMapper.map(book);
        return bookDto;
    }

    public void delete(Long id) {
        bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        bookRepository.deleteById(id);
    }
    // END
}
