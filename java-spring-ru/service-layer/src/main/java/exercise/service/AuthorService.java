package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.model.Author;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorMapper authorMapper;

    //Получение списка авторов
    public List<AuthorDTO> getAll() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorDTO> authorsDTO = authors.stream().map(authorMapper::map).toList();
        return authorsDTO;
    }

    //Получение автора по id
    public AuthorDTO authorById(Long id) {
        Author authorEntity = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        AuthorDTO authorDto = authorMapper.map(authorEntity);
        return authorDto;
    }

    //Добавление нового автора
    public AuthorDTO create(AuthorCreateDTO authorCreateDTO) {
        var author = authorMapper.map(authorCreateDTO);
        authorRepository.save(author);
        AuthorDTO authorDto = authorMapper.map(author);
        return authorDto;
    }

    //Редактирование автора
    public AuthorDTO update(AuthorUpdateDTO authorUpdateDTO, Long id) {
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        authorMapper.update(authorUpdateDTO, author);
        authorRepository.save(author);
        AuthorDTO authorDto = authorMapper.map(author);
        return authorDto;
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found"));
        authorRepository.delete(author);
    }

    // END
}
