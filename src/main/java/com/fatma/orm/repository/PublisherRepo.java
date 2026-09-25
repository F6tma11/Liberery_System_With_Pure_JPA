package com.fatma.orm.repository;

import com.fatma.orm.entity.Category;
import com.fatma.orm.entity.Publisher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublisherRepo {

    private EntityManagerFactory factory;
    public PublisherRepo(EntityManagerFactory factory){
        this.factory=factory;
    }

    public void savePublisher(Publisher publisher){
        try(EntityManager manager=factory.createEntityManager()){
            manager.getTransaction().begin();
            manager.persist(publisher);
            manager.getTransaction().commit();
        }
    }

    public List<Publisher> findPublishers(){
        try (EntityManager manager=factory.createEntityManager()){
            TypedQuery<Publisher> publishers=manager
                    .createQuery("select p from Publisher p", Publisher.class);
            return publishers.getResultList();

        }
    }

    public Publisher getPublisherById(int publisherId){
        try(EntityManager manager=factory.createEntityManager()){
            Publisher publisher=  manager.find(Publisher.class,publisherId);
            return publisher;
        }
    }
}
