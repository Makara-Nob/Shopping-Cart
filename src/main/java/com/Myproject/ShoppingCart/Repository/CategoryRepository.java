package com.Myproject.ShoppingCart.Repository;

import com.Myproject.ShoppingCart.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
    List<Category> findByParentIsNull();
    List<Category> findByParent(Category parent);
}
