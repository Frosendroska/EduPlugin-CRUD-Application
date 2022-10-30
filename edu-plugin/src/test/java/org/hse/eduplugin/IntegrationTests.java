package org.hse.eduplugin;

import static java.lang.Long.parseLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONArray;
import org.hse.eduplugin.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
public class IntegrationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(delete("/books")).andExpect(status().isOk());
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("1984", "Very real", "J. Orwell", "12-43432-25", 2005, false));
        books.add(new Book("1984", "Very real", "J. Orwell", "12-43432-23", 2005, false));
        books.add(new Book("451", "Fired", "R. Bradbury", "12-3820-23", 2003, false));
        books.add(new Book("We", "WeWeWeWe", "E. Zamyatin", "12-3820-3232", 2003, false));
        books.add(new Book("Inhabited Island", "English version", "Strugatsky", "121-320-3232", 2001, true));
        books.add(new Book("Brave New World", "English version", "A. Huxley", "121-320-3237", 2001, true));
        return books;
    }

    @Test
    public void postAllBooksAndGetAllBooksOkStatusTest() throws Exception {
        mockMvc.perform(post("/books/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONArray.toJSONString(getBooks())))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/books")).andExpect(status().isOk());
    }

    @Test
    public void postBookReturnOkStatus() throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(getBooks().get(0)))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void postBookReturnOkStatusAndPutBookReturnOkStatusAndChangedValueCheck() throws Exception {
        Book book = getBooks().get(0);
        MvcResult id = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(book))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andReturn();
        book.setAuthor("Undefined");
        book.setId(parseLong(id.getResponse().getContentAsString()));
        mockMvc.perform(put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(book))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    public void postBookReturnOkStatusAndDeleteBookReturnOkStatusGetBookNotFoundStatus() throws Exception {
        Book book = getBooks().get(0);
        MvcResult idResponse = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(book))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andReturn();
        Long id = parseLong(idResponse.getResponse().getContentAsString());
        mockMvc.perform(delete(String.format("/books/id/%d", id))).andExpect(status().isOk());
        mockMvc.perform(get(String.format("/books/id/%d", id))).andExpect(status().isNotFound());
    }

    @Test
    public void postAllBooksAndGetBooksByTitle() throws Exception {
        List<Book> books = getBooks();
        Book testBook = books.get(0);
        mockMvc.perform(post("/books/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSONArray.toJSONString(books)))
                .andExpect(status().isCreated());
        mockMvc.perform(get(String.format("/books/title/%s", testBook.getTitle())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value(testBook.getAuthor()));
    }

    @Test
    public void deleteStatusNotFound() throws Exception {
        mockMvc.perform(delete("/books/id/42")).andExpect(status().isNotFound());
    }
}
