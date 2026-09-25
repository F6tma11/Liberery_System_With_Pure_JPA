package com.fatma.orm;

import com.fatma.orm.config.AppConfig;
import com.fatma.orm.entity.Author;
import com.fatma.orm.entity.Book;
import com.fatma.orm.entity.Category;
import com.fatma.orm.entity.Publisher;
import com.fatma.orm.repository.AuthorRepo;
import com.fatma.orm.repository.BookRepo;
import com.fatma.orm.repository.CategoryRepo;
import com.fatma.orm.repository.PublisherRepo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        AuthorRepo authorRepo =
                context.getBean(AuthorRepo.class);

        BookRepo bookRepo =
                context.getBean(BookRepo.class);

        PublisherRepo publisherRepo =
                context.getBean(PublisherRepo.class);

        CategoryRepo categoryRepo =
                context.getBean(CategoryRepo.class);


        // ============================================================
        // 1. CREATE CATEGORIES
        // ============================================================

        Category java = new Category("Java");
        Category spring = new Category("Spring");

        categoryRepo.saveCategory(java);
        categoryRepo.saveCategory(spring);

        List<Category> categories = new ArrayList<>();
        categories.add(java);
        categories.add(spring);


        // ============================================================
        // 2. CREATE PUBLISHER
        // ============================================================

        Publisher publisher =
                new Publisher("JPA Publishing");

        publisherRepo.savePublisher(publisher);


        // ============================================================
        // 3. CREATE AUTHOR
        // ============================================================

        Author author =
                new Author("Fatma Ahmed");


        // ============================================================
        // 4. CREATE BOOKS
        // ============================================================

        Book book1 = new Book(
                "Java Head First",
                "The core Java concepts",
                publisher,
                categories
        );

        Book book2 = new Book(
                "Effective Java",
                "Advanced Java programming",
                publisher,
                categories
        );


        // ============================================================
        // 5. SYNCHRONIZE BOTH SIDES
        // ============================================================

        author.addBook(book1);
        author.addBook(book2);


        System.out.println("\n========== BEFORE CASCADE ==========");
        System.out.println("Author: " + author);
        System.out.println("Books: " + author.getBooks());


        // ============================================================
        // 6. CASCADE DEMONSTRATION
        // ============================================================

        System.out.println("\n========== CASCADE DEMONSTRATION ==========");

        authorRepo.addAuthor(author);

        System.out.println(
                "Author and Books persisted using CascadeType.ALL"
        );


        // ============================================================
        // 7. VERIFY AUTHORS
        // ============================================================

        System.out.println("\n========== AUTHORS ==========");

        System.out.println(authorRepo.findAuthors());


        // ============================================================
        // 8. VERIFY BOOKS
        // ============================================================

        System.out.println("\n========== BOOKS ==========");

        System.out.println(bookRepo.findBooks());


        // ============================================================
        // 9. JPQL - BOOKS BY AUTHOR
        // ============================================================

        System.out.println("\n========== BOOKS BY AUTHOR ==========");

        System.out.println(
                bookRepo.findBooksByAuthorName("Fatma Ahmed")
        );


        // ============================================================
        // 10. JPQL - BOOKS BY PUBLISHER
        // ============================================================

        System.out.println("\n========== BOOKS BY PUBLISHER ==========");

        System.out.println(
                bookRepo.findBooksByPublisherName("JPA Publishing")
        );


        // ============================================================
        // 11. POSITIONAL PARAMETER
        // ============================================================

        System.out.println("\n========== BOOK BY ID ==========");

        int bookId = book1.getId();

        System.out.println(
                bookRepo.findBookById(bookId)
        );


        // ============================================================
        // 12. JOIN FETCH
        // ============================================================

        System.out.println("\n========== JOIN FETCH ==========");

        Author fetchedAuthor =
                authorRepo.getAuthorAndHisBooks("Fatma Ahmed");

        System.out.println("Author: " + fetchedAuthor);
        System.out.println("Books: " + fetchedAuthor.getBooks());


        // ============================================================
        // 13. AGGREGATION
        // ============================================================

        System.out.println("\n========== BOOK COUNT PER AUTHOR ==========");

        System.out.println(
                authorRepo.getAuthorsAndNumBooks()
        );


        // ============================================================
        // 14. CRITERIA API - TITLE
        // ============================================================

        System.out.println("\n========== CRITERIA - TITLE ==========");

        System.out.println(
                bookRepo.getBookByTitle("Java Head First")
        );


        // ============================================================
        // 15. DYNAMIC CRITERIA - TITLE ONLY
        // ============================================================

        System.out.println("\n========== DYNAMIC CRITERIA - TITLE ONLY ==========");

        System.out.println(
                bookRepo.findBooks("Java", null)
        );


        // ============================================================
        // 16. DYNAMIC CRITERIA - AUTHOR ONLY
        // ============================================================

        System.out.println("\n========== DYNAMIC CRITERIA - AUTHOR ONLY ==========");

        System.out.println(
                bookRepo.findBooks(null, "Fatma")
        );


        // ============================================================
        // 17. DYNAMIC CRITERIA - BOTH
        // ============================================================

        System.out.println("\n========== DYNAMIC CRITERIA - BOTH ==========");

        System.out.println(
                bookRepo.findBooks("Java", "Fatma")
        );


        // ============================================================
        // 18. ORPHAN REMOVAL DEMONSTRATION
        // ============================================================

        System.out.println("\n========== ORPHAN REMOVAL ==========");

        System.out.println(
                "Books before removing book1:"
        );

        System.out.println(
                bookRepo.findBooks()
        );


        authorRepo.removeBookFromAuthor(
                author.getId(),
                book1.getId()
        );


        System.out.println(
                "Books after removing book1 from Author.books:"
        );

        System.out.println(
                bookRepo.findBooks()
        );


        // ============================================================
        // 19. LAZY INITIALIZATION EXCEPTION
        // ============================================================

        System.out.println("\n========== LAZY INITIALIZATION DEMO ==========");

        try {

            Author lazyAuthor =
                    authorRepo.getAuthorById(author.getId());

            System.out.println(
                    "Author loaded successfully: "
                            + lazyAuthor
            );

            System.out.println(
                    "Trying to access LAZY books..."
            );

            System.out.println(
                    lazyAuthor.getBooks()
            );

        } catch (Exception e) {

            System.out.println(
                    "Expected exception: "
                            + e.getClass().getSimpleName()
            );

            System.out.println(
                    "Reason: books is LAZY and the EntityManager "
                            + "was already closed."
            );
        }
    }
}