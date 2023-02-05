package com.am.bookplus.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    
    public UtilsTest() { }

    @Test
    public void testCheckISBN() 
    {
        //Test of checkISBN method, of class Utils.
        System.out.println("checkISBN");
        
        if (!Utils.checkISBN("9783161484100")) fail();
        if (Utils.checkISBN("9781788399083")) fail();
        if (!Utils.checkISBN("885152159X")) fail();
    }

    @Test
    public void testCheckEmail() 
    {
        //Test of checkEmail method, of class Utils.
        System.out.println("checkEmail");
        
        if (!Utils.checkEmail("mario.rossi@gmail.com")) fail();     
        if (Utils.checkEmail("mariorossi@gmail")) fail();
        if (Utils.checkEmail("abc@gmail.")) fail();
    }
    
    @Test
    public void testCheckStudentId() 
    {
        //Test of checkStudentId method, of class Utils.
        System.out.println("checkStudentId");
        
        if (!Utils.checkStudentId("815564")) fail();     
        if (Utils.checkStudentId("8155675")) fail();
        if (Utils.checkStudentId("815Z66")) fail();
    }
}