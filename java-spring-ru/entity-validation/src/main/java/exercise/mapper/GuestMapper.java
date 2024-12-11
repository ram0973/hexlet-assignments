package exercise.mapper;

import exercise.dto.GuestCreateDTO;
import exercise.dto.GuestDTO;
import org.mapstruct.*;

import exercise.model.Guest;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class GuestMapper {

    public abstract Guest map(GuestCreateDTO dto);
    public abstract GuestDTO map(Guest model);
}
