package com.bayoumi.cma.service;

import com.bayoumi.cma.dto.ContactDto.ContactRequest;
import com.bayoumi.cma.dto.ContactDto.ContactResponse;
import com.bayoumi.cma.dto.ContactDto.PagedContactResponse;
import com.bayoumi.cma.model.Contact;
import com.bayoumi.cma.model.User;
import com.bayoumi.cma.repository.ContactRepository;
import com.bayoumi.cma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Transactional
    public ContactResponse addContact(String username, ContactRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Contact contact = Contact.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .birthdate(request.getBirthdate())
                .user(user)
                .build();

        Contact savedContact = contactRepository.save(contact);
        return mapToContactResponse(savedContact);
    }

    @Transactional(readOnly = true)
    public List<ContactResponse> getAllContacts(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Contact> contacts = contactRepository.findByUserId(user.getId());
        return contacts.stream()
                .map(this::mapToContactResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagedContactResponse getContactsPage(String username, int page, int size, String sortBy, String direction) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Contact> contactPage = contactRepository.findByUserId(user.getId(), pageable);

        List<ContactResponse> contactResponses = contactPage.getContent().stream()
                .map(this::mapToContactResponse)
                .collect(Collectors.toList());

        return PagedContactResponse.builder()
                .contacts(contactResponses)
                .currentPage(contactPage.getNumber())
                .totalPages(contactPage.getTotalPages())
                .totalElements(contactPage.getTotalElements())
                .build();
    }

    @Transactional(readOnly = true)
    public ContactResponse getContactById(String username, Long contactId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Contact contact = contactRepository.findByIdAndUserId(contactId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        return mapToContactResponse(contact);
    }

    @Transactional
    public ContactResponse updateContact(String username, Long contactId, ContactRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Contact contact = contactRepository.findByIdAndUserId(contactId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setEmail(request.getEmail());
        contact.setBirthdate(request.getBirthdate());

        Contact updatedContact = contactRepository.save(contact);
        return mapToContactResponse(updatedContact);
    }

    @Transactional
    public void deleteContact(String username, Long contactId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!contactRepository.existsByIdAndUserId(contactId, user.getId())) {
            throw new IllegalArgumentException("Contact not found");
        }

        contactRepository.deleteByIdAndUserId(contactId, user.getId());
    }

    private ContactResponse mapToContactResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .phoneNumber(contact.getPhoneNumber())
                .email(contact.getEmail())
                .birthdate(contact.getBirthdate())
                .build();
    }
}