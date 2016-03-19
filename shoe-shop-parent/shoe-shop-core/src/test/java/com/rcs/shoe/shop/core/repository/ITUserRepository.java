package com.rcs.shoe.shop.core.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.rcs.shoe.shop.core.config.CoreConfig;
import com.rcs.shoe.shop.core.entity.impl.User;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:dbunit/user-entries.xml")
@DatabaseTearDown("classpath:dbunit/clear-user-entries.xml")
public class ITUserRepository {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testFindByUserNameAndPassword(){
        User user = userRepository.findByUserNameAndPassword("leo.messi@test.com", "654321");
        
        assertThat(user).isNotNull();
    }
}
