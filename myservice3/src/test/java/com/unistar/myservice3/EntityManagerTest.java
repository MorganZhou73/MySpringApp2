package com.unistar.myservice3;

import com.unistar.myservice3.model.User;
import com.unistar.myservice3.model.UserDTO;
import com.unistar.myservice3.repos.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@SpringBootTest
public class EntityManagerTest {

    @Autowired
    private UserRepository userRepository;

    //@PersistenceContext(type = PersistenceContextType.TRANSACTION)
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager em;

    private String testEmail = "joe@gmail.com";
    private Integer testUserId = 0;

    @BeforeEach
    public void setup() throws Exception {
        List<UserDTO> users = userRepository.findByEmail(testEmail);
        if(users.isEmpty()){
            User user = new User("Joe", testEmail);
            userRepository.save(user);
            testUserId = user.getId();
        }
        else {
            testUserId = users.get(0).getId();
        }
     }

    @Test
    void entityManagerFindTest() {
        User u1 = em.find(User.class, testUserId);
        User u2 = em.find(User.class, testUserId);

        Assertions.assertEquals(u1.getName(), u2.getName());
        Assertions.assertEquals(u1.getEmail(), u2.getEmail());
        Assertions.assertEquals(u1, u2); // not true if em is (type = PersistenceContextType.TRANSACTION)

        String newName = "Joe 2";
        // since em is already a transaction-synchronized EntityManager,
        // call em.getTransaction().begin(); would throw java.lang.IllegalStateException
        u1.setName(newName);
        em.persist(u1);

        u2 = em.find(User.class, testUserId);
        Assertions.assertEquals(newName, u2.getName());
    }

    @Test
    void entityManagerReferenceTest(){

        // u1,u2 is only a proxy to the User(testUserId)
        User u1 = em.getReference(User.class, testUserId);
        User u2 = em.getReference(User.class, testUserId);

        Assertions.assertEquals(u1, u2); // not true if em is (type = PersistenceContextType.TRANSACTION)

        // when em is NOT set as (type = PersistenceContextType.EXTENDED),
        //  call u1.getName() would throw hibernate.LazyInitializationException
        Assertions.assertEquals(u1.getName(), u2.getName());

        String newName = "Joe 3";
        u1.setName(newName);
        em.persist(u1);

        u2 = em.find(User.class, testUserId);
        Assertions.assertEquals(newName, u2.getName());
    }
}
