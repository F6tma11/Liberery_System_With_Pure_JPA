package com.fatma.orm.repository;

import com.fatma.orm.entity.Author;
import com.fatma.orm.entity.Book;
import com.fatma.orm.entity.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class BookRepo {

    private EntityManagerFactory managerFactory;

    public BookRepo(EntityManagerFactory managerFactory){
        this.managerFactory=managerFactory;
    }

    public void saveBook(Book book){
        try (EntityManager entityManager=managerFactory.createEntityManager()){
            entityManager.getTransaction().begin();
            entityManager.persist(book);
            entityManager.getTransaction().commit();
        }
    }

    public List<Book> findBooks(){
        try (EntityManager entityManager=managerFactory.createEntityManager()){
            TypedQuery<Book> books=entityManager.createQuery("select b from Book b ", Book.class);
            return books.getResultList();
        }
    }

    public List<Book> findBooksByAuthorName(String authorName){
        try(EntityManager entityManager=managerFactory.createEntityManager()){
            TypedQuery<Book> books=  entityManager.
                    createQuery("select b from  Book b where b.author.name=:name", Book.class);
            books.setParameter("name",authorName);
            return books.getResultList();
        }catch (Exception e){
          throw   new RuntimeException();
        }

    }

    public List<Book> findBooksByPublisherName(String publisherName){
        try(EntityManager entityManager=managerFactory.createEntityManager()){
            TypedQuery<Book> books=entityManager.
                    createQuery("select b from Book b where b.publisher.name=:name", Book.class);
            books.setParameter("name",publisherName);
            return books.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Book findBookById(int bookID){
        try(EntityManager entityManager=managerFactory.createEntityManager()){
            TypedQuery<Book> book=entityManager.
                    createQuery("select b from Book b where b.id =?1", Book.class);
            book.setParameter(1,bookID);
            return book.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Book> getBookByTitle(String bookTitle){
        try(EntityManager entityManager=managerFactory.createEntityManager()){
            CriteriaBuilder builder=entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> query=builder.createQuery(Book.class);
            Root<Book> bookByTitle=query.from(Book.class);
            query.select(bookByTitle).where(builder.equal(bookByTitle.get("title"), bookTitle));
            List<Book> book=entityManager.createQuery(query).getResultList();
            return book;
        }
    }

    public List<Book> findBooks(String title, String authorName) {
        try(EntityManager entityManager=managerFactory.createEntityManager()){
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> cq = cb.createQuery(Book.class);
            Root<Book> book = cq.from(Book.class);

            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(book.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (authorName != null && !authorName.isBlank()) {

                Join<Book, Author> author = book.join("author", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(author.get("name")), "%" + authorName.toLowerCase() + "%"));
            }

            cq.select(book).where(predicates.toArray(new Predicate[0]));

            return entityManager.createQuery(cq).getResultList();
        }

    }
}