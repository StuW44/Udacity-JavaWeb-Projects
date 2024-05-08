package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.TestAnnotationUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder=mock(BCryptPasswordEncoder.class);
    @Before
    public void SetUp()
    {
        userController=new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepo);
        TestUtils.injectObjects(userController,"cartRepository",cartRepo);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
    }
    @Test
    public void create_user_happy_path() throws Exception
    {
        when(encoder.encode("testPassword")).thenReturn("thisishashashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        User u =  response.getBody();
        assertNotNull(u);
        assertEquals(0,u.getId());
        assertEquals("thisishashashed",u.getPassword());

    }
    @Test
    public void findUserFailure()
    {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("bob");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");
        ResponseEntity<User> response = userController.findByUserName("bob");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }
    @Test
    public void createUserBadPassword()
    {
        when(encoder.encode("testPassword")).thenReturn("thisishashashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword1");
        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(400,response.getStatusCodeValue());
        assertNotNull(response);

    }
    @Test
    public void findUser()
    {
        User r = new User();
        r.setId(1L);
        r.setUsername("test");
        r.setPassword("testPassword");

        when(userRepo.findByUsername("test")).thenReturn(r);
        ResponseEntity<User> response =   userController.findByUserName("test")    ;
        assertEquals(200,response.getStatusCodeValue());
        User u =  response.getBody();
        assertNotNull(u);
        assertEquals(1,u.getId());
        assertEquals("test",u.getUsername());
    }
}
