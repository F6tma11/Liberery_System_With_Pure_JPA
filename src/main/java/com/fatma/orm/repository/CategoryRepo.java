package com.fatma.orm.repository;

import com.fatma.orm.entity.Book;
import com.fatma.orm.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CategoryRepo {
    private EntityManagerFactory factory;
    public CategoryRepo(EntityManagerFactory factory){
        this.factory=factory;
    }

    public void saveCategory(Category category){
        try(EntityManager manager=factory.createEntityManager()){
            manager.getTransaction().begin();
            manager.persist(category);
            manager.getTransaction().commit();
        }
    }

    public List<Category> findCategories(){
        try (EntityManager manager=factory.createEntityManager()){
            TypedQuery<Category> categories=manager
                    .createQuery("select c from Category c", Category.class);
            return categories.getResultList();

        }
    }

    public Category getCategoryById(int categoryId){
        try(EntityManager manager=factory.createEntityManager()){
            Category category=  manager.find(Category.class,categoryId);
            return category;
        }
    }
}
