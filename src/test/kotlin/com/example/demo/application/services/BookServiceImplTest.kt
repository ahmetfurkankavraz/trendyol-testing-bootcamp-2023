package com.example.demo.application.services

import com.example.demo.domain.entities.Book
import com.example.demo.domain.repositories.BookRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class BookServiceImplTest {

    @InjectMocks
    lateinit var bookService: BookServiceImpl

    @Mock
    lateinit var bookRepository: BookRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test getAllBooks should return list of books`() {
        val books = listOf(
            Book(UUID.randomUUID(), "Title1", "Author1", 2001),
            Book(UUID.randomUUID(), "Title2", "Author2", 2002),
        )

        Mockito.`when`(bookRepository.findAllBooks()).thenReturn(books)

        val result = bookService.getAllBooks()
        assertEquals(2, result.size)
        assertEquals(books, result)
    }

    @Test
    fun `test getBookById should return a book`() {
        val book = Book(UUID.randomUUID(), "Title1", "Author1", 2001)

        Mockito.`when`(bookRepository.findBookById(book.id)).thenReturn(book)

        val result = bookService.getBookById(book.id)
        assertEquals(book, result)
    }

    @Test
    fun `test createBook with new title should return saved book`() {
        val book = Book(UUID.randomUUID(), "UniqueTitle", "Author1", 2001)

        Mockito.`when`(bookRepository.findBookByTitle(book.title)).thenReturn(null)
        Mockito.`when`(bookRepository.saveBook(book)).thenReturn(book)

        val result = bookService.createBook(book)
        assertEquals(book, result)
    }

    @Test
    fun `test createBook with duplicate title should throw exception`() {
        val book = Book(UUID.randomUUID(), "DuplicateTitle", "Author1", 2001)

        Mockito.`when`(bookRepository.findBookByTitle(book.title)).thenReturn(book)

        val exception = assertThrows<IllegalArgumentException> {
            bookService.createBook(book)
        }

        assertEquals("A book with the title DuplicateTitle already exists.", exception.message)
    }

    @Test
    fun `test searchBook should return a list of books`(){
        val title = "titleBook"
        val books = listOf(
            Book(UUID.randomUUID(), "name$title", "Author1", 2001),
            Book(UUID.randomUUID(), title + "name", "Author2", 2002),
        )

        Mockito.`when`(bookRepository.searchBook(title)).thenReturn(books)

        val result = bookService.searchBook(title)
        assertEquals(2, result.size)
        assertEquals(books, result)
    }

    @Test
    fun `test searchBook should return a book`(){
        val title = "titleBook"
        val books = listOf(
            Book(UUID.randomUUID(), "name$title", "Author1", 2001),
        )

        Mockito.`when`(bookRepository.searchBook(title)).thenReturn(books)

        val result = bookService.searchBook(title)
        assertEquals(1, result.size)
        assertEquals(books, result)
    }

    @Test
    fun `test searchBook should throw exception nothing found`(){
        val title = "titleBook"
        val books : List<Book> = listOf()

        Mockito.`when`(bookRepository.searchBook(title)).thenReturn(books)

        val exception = assertThrows<IllegalArgumentException> {
            bookService.searchBook(title)
        }

        assertEquals("Sonuç bulunamadı", exception.message)
    }

    @Test
    fun `test searchBook should throw exception input title is empty`(){
        val title = ""

        val exception = assertThrows<IllegalArgumentException> {
            bookService.searchBook(title)
        }

        assertEquals("Lütfen bir arama kriteri girin", exception.message)
    }

    @Test
    fun `test searchBook with more than 20 chars search title then throw exception`() {
        val title = "a"

        val exception = assertThrows<IllegalArgumentException> {
            bookService.searchBook(title)
        }

        assertEquals("Lütfen 2 ila 20 karakter uzunluğu arasında bir değer giriniz", exception.message)
    }

    @Test
    fun `test searchBook with less than 2 chars search title then throw exception`() {
        val title = "as".repeat(10) + "a"

        val exception = assertThrows<IllegalArgumentException> {
            bookService.searchBook(title)
        }

        assertEquals("Lütfen 2 ila 20 karakter uzunluğu arasında bir değer giriniz", exception.message)
    }
}
