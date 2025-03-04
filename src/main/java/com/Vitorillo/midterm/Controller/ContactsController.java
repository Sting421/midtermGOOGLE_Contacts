package com.Vitorillo.midterm.Controller;

import com.Vitorillo.midterm.Sevices.GoogleContactsService;
import com.google.api.services.people.v1.model.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/contacts")
public class ContactsController {
    private static final Logger logger = LoggerFactory.getLogger(ContactsController.class);
    private final GoogleContactsService contactsService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public ContactsController(GoogleContactsService contactsService, OAuth2AuthorizedClientService authorizedClientService) {
        this.contactsService = contactsService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping
    public String listContacts(
        Model model, 
        OAuth2AuthenticationToken authentication
    ) {
        try {
            if (authentication == null) {
                logger.error("Authentication is null. User is not authenticated.");
                model.addAttribute("error", "Authentication is null. Please log in again.");
                return "error";
            }
            
            logger.info("Authentication token present: " + authentication.getName());
            logger.info("Authorized client registration ID: " + authentication.getAuthorizedClientRegistrationId());
            
            try {
                OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                    authentication.getAuthorizedClientRegistrationId(), 
                    authentication.getName()
                );
                
                if (client == null) {
                    logger.error("OAuth2AuthorizedClient is null. Token might be invalid or expired.");
                    model.addAttribute("error", "OAuth token is invalid or expired. Please log in again.");
                    return "error";
                }
                
                logger.info("Access token: " + client.getAccessToken().getTokenValue().substring(0, 10) + "...");
                logger.info("Token expires at: " + client.getAccessToken().getExpiresAt());
            } catch (Exception e) {
                logger.error("Error loading authorized client: " + e.getMessage(), e);
                model.addAttribute("error", "Error with OAuth token: " + e.getMessage());
                return "error";
            }
            
            List<Person> contacts = contactsService.listContacts(authentication);
            logger.info("Successfully retrieved " + (contacts != null ? contacts.size() : 0) + " contacts");
            model.addAttribute("contacts", contacts != null ? contacts : Collections.emptyList());
            return "contacts";
        } catch (Exception e) {
            logger.error("Error in listContacts: " + e.getMessage(), e);
            e.printStackTrace();
            model.addAttribute("error", "Error fetching contacts: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/add")
    public String showAddContactForm() {
        return "add-contact";
    }

    @PostMapping("/add")
    public String addContact(
        OAuth2AuthenticationToken authentication,
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phoneNumber
    ) throws IOException {
        contactsService.createContact(
            authentication, 
            firstName, 
            lastName, 
            email, 
            phoneNumber
        );
        return "redirect:/contacts";
    }

    @GetMapping("/edit/{resourceName}")
    public String showEditContactForm(
        @PathVariable String resourceName, 
        Model model
    ) {
        // In a real implementation, fetch the specific contact details
        model.addAttribute("resourceName", resourceName);
        return "edit-contact";
    }

    @PostMapping("/edit/{resourceName}")
    public String updateContact(
        OAuth2AuthenticationToken authentication,
        @PathVariable String resourceName,
        @RequestParam String firstName,
        @RequestParam String lastName,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phoneNumber
    ) throws IOException {
        contactsService.updateContact(
            authentication, 
            resourceName, 
            firstName, 
            lastName, 
            email, 
            phoneNumber
        );
        return "redirect:/contacts";
    }

    @GetMapping("/delete/{resourceName}")
    public String deleteContact(
        OAuth2AuthenticationToken authentication,
        @PathVariable String resourceName
    ) throws IOException {
        contactsService.deleteContact(authentication, resourceName);
        return "redirect:/contacts";
    }
}
