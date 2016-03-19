/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.repository;

import com.rcs.shoe.shop.core.entity.impl.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rajko
 */
public interface UserRepository extends CrudRepository<User, Long> {
    
    public User findByUserNameAndPassword(String userName, String password);
    
    public User findByUserName(String userName);
}
