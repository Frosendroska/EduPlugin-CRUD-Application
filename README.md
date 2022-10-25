# Запуск

Start postgresql DB with the name postgres in the port 5432

# Testing


# EduPlugin

A library simulator REST API without frontend.

There is one table "books", which stores a books list.The following data should be stored in the table:
`id`, `title`, `description`, `author` (name and last name), `isbn` – ISBN of the book, `printYear`, `readAlready` (boolean).

Create a standard CRUD application with a possibility to `add` new book, `update` an existing one 
(e.g. mark the book as read or replace a book by a new edition), `delete` and `search` by field.

### Torrent

  Gradle;
  Spring Boot;
  Java is preferable;
  Spring Data JPA;
  Postgres;

# Torrent-tracker

* Queries:
    * `POST` **/books** <Book> —- Add the new book to the database 
    * `GET` —- Get books with specific title/id in the database or all books
      *   **/books** -- Get all books from the database
      *   **/books/id/<id>** -- Get the book with specific id in the database
      *   **/books/title/<title>** -- Get books with specific title in the database
      *   **/books/author/<author>**-- Get books with specific author in the database
      *   **/books/isbn/<isbn>** -- Get books with specific isbn in the database
      *   **/books/printYear/<printYear>** -- Get books with specific printYear in the database
      *   **/books/readAlready/<readAlready>** -- Get books with specific readAlready in the database
    * `PUT`**/books** <Book> —- Update the existing book in the database
    * `DELETE` **/books/<id>** —- Delete the  book from the database

# Deadline 

* 30.10.2022
