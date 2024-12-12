package exercise.mapper;

import exercise.dto.*;
import exercise.model.Author;
import exercise.model.Book;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class AuthorMapper {

    // BEGIN
    public abstract Author map(AuthorCreateDTO authorCreateDTO);

    public abstract AuthorDTO map(Author author);
    // END

    public abstract void update(AuthorUpdateDTO dto, @MappingTarget Author model);
}
