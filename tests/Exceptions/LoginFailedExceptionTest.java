package Exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginFailedExceptionTest {


    @Test
    void constructorTests000(){
        Exception exception000 = new LoginFailedException("Message");
        assertTrue(exception000.getMessage().equals("Message"));

        Exception exception001 = new LoginFailedException("Message2",exception000);
        assertTrue(exception001.getMessage().equals("Message2"));
        assertEquals(exception000,exception001.getCause());



    }

}