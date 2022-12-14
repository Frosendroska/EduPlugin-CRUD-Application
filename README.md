# EduPlugin

A library simulator REST API without frontend.

There is one table "books", which stores a books list.The following data should be stored in the table:
`id`, `title`, `description`, `author` (name and last name), `isbn` – ISBN of the book, `printYear`, `readAlready` (
boolean).

Create a standard CRUD application with a possibility to `add` new book, `update` an existing one
(e.g. mark the book as read or replace a book by a new edition), `delete` and `search` by field.

### Technologies

Gradle; Spring Boot; Java is preferable; Spring Data JPA; Postgres;

### Testing

Start postgresql DB with the name postgres in the port 5432

I used Postman application to deal with requests.

### Torrent-tracker

* Queries:
    * `POST`
        * `/books <Book>` — Add the new book to the database
        * `/books/all List<Book>` — Add new books to the database
    * `GET`
        * `/books` - Get all books from the database
        * `/books/id/<id>` - Get the book with specific id in the database
        * `/books/title/<title>` - Get books with specific title in the database
        * `/books/author/<author>` - Get books with specific author in the database
        * `/books/isbn/<isbn>` - Get books with specific isbn in the database
        * `/books/printYear/<printYear>` - Get books with specific printYear in the database
        * `/books/readAlready/<readAlready>` - Get books with specific readAlready in the database
    * `PUT /books <Book>` — Update the existing book in the database
    * `DELETE`
        * `/books/id/<id>` - Delete the book from the database
        * `/books/all` - Delete all books from the database

### Deadline

* 30.10.2022
