package com.Vitorillo.midterm.Sevices;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.PhoneNumber;
import com.google.api.services.people.v1.model.EmailAddress;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class GoogleContactsService {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public GoogleContactsService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    private PeopleService getPeopleService(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
            authentication.getAuthorizedClientRegistrationId(), 
            authentication.getName()
        );

        GoogleCredential credential = new GoogleCredential().setAccessToken(
            client.getAccessToken().getTokenValue()
        );

        return new PeopleService.Builder(
            new NetHttpTransport(), 
            new GsonFactory(), 
            credential
        ).setApplicationName("Google Contacts Manager").build();
    }

    public List<Person> listContacts(OAuth2AuthenticationToken authentication) throws IOException {
        PeopleService peopleService = getPeopleService(authentication);
        
        return peopleService.people().connections()
            .list("people/me")
            .setPersonFields("names,emailAddresses,phoneNumbers")
            .execute()
            .getConnections();
    }

    public Person createContact(
        OAuth2AuthenticationToken authentication, 
        String firstName, 
        String lastName, 
        String email, 
        String phoneNumber
    ) throws IOException {
        PeopleService peopleService = getPeopleService(authentication);
        
        Person person = new Person();
        
        // Add Names
        person.setNames(List.of(new Name()
            .setGivenName(firstName)
            .setFamilyName(lastName)
        ));
        
        // Add Email
        if (email != null) {
            person.setEmailAddresses(List.of(new EmailAddress()
                .setValue(email)
            ));
        }
        
        // Add Phone Number
        if (phoneNumber != null) {
            person.setPhoneNumbers(List.of(new PhoneNumber()
                .setValue(phoneNumber)
            ));
        }
        
        return peopleService.people().createContact(person).execute();
    }

    public Person updateContact(
        OAuth2AuthenticationToken authentication, 
        String resourceName,
        String firstName, 
        String lastName, 
        String email, 
        String phoneNumber
    ) throws IOException {
        PeopleService peopleService = getPeopleService(authentication);
        
        Person person = new Person();
        
        // Add Names
        person.setNames(List.of(new Name()
            .setGivenName(firstName)
            .setFamilyName(lastName)
        ));
        
        // Add Email
        if (email != null) {
            person.setEmailAddresses(List.of(new EmailAddress()
                .setValue(email)
            ));
        }
        
        // Add Phone Number
        if (phoneNumber != null) {
            person.setPhoneNumbers(List.of(new PhoneNumber()
                .setValue(phoneNumber)
            ));
        }
        
        return peopleService.people().updateContact(resourceName, person).execute();
    }

    public void deleteContact(
        OAuth2AuthenticationToken authentication, 
        String resourceName
    ) throws IOException {
        PeopleService peopleService = getPeopleService(authentication);
        peopleService.people().deleteContact(resourceName).execute();
    }
}
