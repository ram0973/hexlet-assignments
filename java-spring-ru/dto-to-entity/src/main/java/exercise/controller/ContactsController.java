package exercise.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import exercise.model.Contact;
import exercise.repository.ContactRepository;
import exercise.dto.ContactDTO;
import exercise.dto.ContactCreateDTO;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    private final ContactRepository contactRepository;

    public ContactsController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    private ContactDTO toContractDTO(Contact contact) {
        var dto = new ContactDTO();
        dto.setId(contact.getId());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setPhone(contact.getPhone());
        dto.setCreatedAt(contact.getCreatedAt());
        dto.setUpdatedAt(contact.getUpdatedAt());
        return dto;
    }

    private Contact toContract(ContactCreateDTO contactCreateDTO) {
        Contact contact = new Contact();
        contact.setFirstName(contactCreateDTO.getFirstName());
        contact.setLastName(contactCreateDTO.getLastName());
        contact.setPhone(contactCreateDTO.getPhone());
        return contact;
    }

    // BEGIN
    // Создайте в контроллере обработчик, который будет обрабатывать POST-запросы на адрес /contacts.
    // Обработчик должен принимать объект класса ContactCreateDTO и возвращать объект класса ContactDTO.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDTO create(@RequestBody ContactCreateDTO contactCreateDTO) {
        Contact contact = toContract(contactCreateDTO);
        Contact savedContact = contactRepository.save(contact);
        return toContractDTO(savedContact);
    }
    // END
}
