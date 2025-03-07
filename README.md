# Google Contacts Management System

## Overview
This is a Spring Boot application that integrates with the Google People API to manage contacts. It provides CRUD (Create, Read, Update, Delete) operations for contacts stored in Google accounts.

## Features
- **Contact Management**:
  - Add new contacts
  - View contact list
  - Edit existing contacts
  - Delete contacts
- **Google Integration**:
  - OAuth2 authentication
  - Integration with Google People API
  - Supports both regular contacts and Google 'people/' prefixed contacts
- **User Interface**:
  - Thymeleaf templates for web interface
  - Form validation and error handling
  - Pre-filled edit forms with placeholders

## System Architecture

The system consists of several key components:

1. **Controller Layer** (`ContactsController`):
   - Handles HTTP requests and responses
   - Manages routing and view rendering
   - Implements security checks

2. **Service Layer** (`GoogleContactsService`):
   - Interacts with Google People API
   - Implements business logic
   - Handles error cases and logging

3. **Security Configuration** (`SecurityConfig`):
   - Manages OAuth2 authentication
   - Configures access control
   - Handles user sessions

4. **Templates** (Thymeleaf):
   - `add-contact.html`: Form for adding new contacts
   - `edit-contact.html`: Form for editing existing contacts
   - `contacts.html`: Displays list of contacts

## API Integration Details

The system uses the Google People API with the following key endpoints:

- **Get Contacts**: `people.connections.list`
- **Create Contact**: `people.createContact`
- **Update Contact**: `people.updateContact`
- **Delete Contact**: `people.deleteContact`

## Error Handling
The system implements comprehensive error handling:

- **API Errors**:
  - Invalid requests
  - Authentication failures
  - Rate limiting
- **Application Errors**:
  - Missing required fields
  - Invalid data formats
  - Database connection issues

## Security Features

- OAuth2 authentication
- CSRF protection
- Secure session management
- Input validation
- Error logging

## Setup Instructions

1. Clone the repository
2. Configure OAuth2 credentials in `application.properties`
3. Build the project using Maven
4. Run the application
5. Access through web browser

## Usage

1. Authenticate with Google account
2. Use the web interface to manage contacts
3. View, add, edit, and delete contacts as needed

## Dependencies

- Spring Boot
- Google API Client Library
- Thymeleaf
- Spring Security OAuth2

## Contact

For support or questions, please contact [your email]
