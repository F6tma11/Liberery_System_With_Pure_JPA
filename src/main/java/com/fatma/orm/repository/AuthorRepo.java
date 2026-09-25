package com.fatma.orm.repository;

import com.fatma.orm.dto.AuthorBooks;
import com.fatma.orm.entity.Author;
import com.fatma.orm.entity.Book;
import com.fatma.orm.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepo {

    private EntityManagerFactory managerFactory;

    public  AuthorRepo(EntityManagerFactory managerFactory){
        this.managerFactory=managerFactory;
    }

    public void addAuthor(Author author){
        try(EntityManager entityManager=managerFactory.createEntityManager()){
            entityManager.getTransaction().begin();
            entityManager.persist(author);
            entityManager.getTransaction().commit();
        }

    }

    public List<Author> findAuthors(){
        try(EntityManager manager=managerFactory.createEntityManager()){
            TypedQuery<Author> authors=manager.createQuery("select a from Author a",Author.class);
            return authors.getResultList();
        }
    }

    public Author getAuthorById(int authorId){
        try(EntityManager manager=managerFactory.createEntityManager()){
          Author author=  manager.find(Author.class,authorId);
          return author;
        }
    }
    public Author getAuthorAndHisBooks(String authorName){
        try (EntityManager entityManager=managerFactory.createEntityManager()){
            TypedQuery<Author> author=entityManager.
                    createQuery("select distinct a from Author a Join Fetch a.books where a.name=:name",Author.class);
            author.setParameter("name" , authorName);
            return author.getSingleResult();
        }
    }

    public List<AuthorBooks> getAuthorsAndNumBooks(){
        try (EntityManager entityManager=managerFactory.createEntityManager()){
            TypedQuery<AuthorBooks> authorBooks=entityManager.
                    createQuery("select new com.fatma.orm.dto.AuthorBooks(a.name, COUNT(b)) " +
                            "from Author a Left join a.books b Group By a.name", AuthorBooks.class);
            return authorBooks.getResultList();
        }
    }


    public void removeBookFromAuthor(int authorId, int bookId) {

        try (EntityManager entityManager = managerFactory.createEntityManager()) {

            entityManager.getTransaction().begin();

            Author author = entityManager.find(Author.class, authorId);
            Book book = entityManager.find(Book.class, bookId);

            if (author != null && book != null) {
                author.removeBook(book);
            }

            entityManager.getTransaction().commit();
        }
    }


}
