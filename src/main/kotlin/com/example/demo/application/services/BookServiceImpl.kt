package com.example.demo.application.services

import com.example.demo.domain.entities.Book
import com.example.demo.domain.repositories.BookRepository
import com.example.demo.domain.services.BookService
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookServiceImpl(private val bookRepository: BookRepository) : BookService {

    override fun getAllBooks(): List<Book> = bookRepository.findAllBooks()

    override fun getBookById(id: UUID): Book? = bookRepository.findBookById(id)
    override fun createBook(book: Book): Book {
        val existingBook = bookRepository.findBookByTitle(book.title)
        if (existingBook != null) {
            throw IllegalArgumentException("A book with the title ${book.title} already exists.")
        }
        return bookRepository.saveBook(book)
    }

    override fun deleteBook(id: UUID): Boolean = bookRepository.deleteBookById(id)

    override fun searchBook(title: String): List<Book> {
        validateSearchBookTitle(title)
        val books = bookRepository.searchBook(title)
        if (books.isEmpty()){
            throw IllegalArgumentException("Sonuç bulunamadı")
        }
        return books
    }

    private fun validateSearchBookTitle(title: String){
        if (title.isBlank()){
            throw IllegalArgumentException("Lütfen bir arama kriteri girin")
        }
        if (title.length < 2 || title.length > 20){
            throw IllegalArgumentException("Lütfen 2 ila 20 karakter uzunluğu arasında bir değer giriniz")
        }
    }
}
