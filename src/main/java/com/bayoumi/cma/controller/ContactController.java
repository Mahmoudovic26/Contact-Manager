package com.bayoumi.cma.controller;

import com.bayoumi.cma.dto.ContactDto.ContactRequest;
import com.bayoumi.cma.dto.ContactDto.ContactResponse;
import com.bayoumi.cma.dto.ContactDto.PagedContactResponse;
import com.bayoumi.cma.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ContactRequest request) {
        ContactResponse newContact = contactService.addContact(userDetails.getUsername(), request);
        return ResponseEntity.created(URI.create("/api/contacts/" + newContact.getId()))
                .body(newContact);
    }

    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(contactService.getAllContacts(userDetails.getUsername()));
    }

    @GetMapping("/page")
    public ResponseEntity<PagedContactResponse> getContactsPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(contactService.getContactsPage(
                userDetails.getUsername(), page, size, sortBy, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(userDetails.getUsername(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody ContactRequest request) {
        return ResponseEntity.ok(contactService.updateContact(userDetails.getUsername(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        contactService.deleteContact(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}