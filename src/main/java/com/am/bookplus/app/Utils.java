package com.am.bookplus.app;

public class Utils 
{
    private Utils() { }
    
    public static boolean checkISBN(String isbn)
    {
        if (!isbn.matches("^[0-9]{13}|[0-9]{10}|[0-9]{9}X$")) return false;
        
        switch (isbn.length()) 
        {
            case 10:
            {
                int count = 0;
                for (int i = 0; i < 9; i++)
                {
                    int digit = isbn.charAt(i) - 48;
                    count += digit * (10 - i);
                }
                
                if (isbn.charAt(9) == 'X') count += 10;
                else count += isbn.charAt(9) - 48;
                
                return count % 11 == 0;
            }
            case 13:
            {
                int count = 0;
                for (int i = 0; i < 13; i++)
                {
                    int digit = isbn.charAt(i) - 48;

                    if (i % 2 == 0) count += digit;
                    else count += digit * 3;
                }
                
                return count % 10 == 0;
            }
        }
        
        return false;
    }
    
    public static boolean checkEmail(String email)
    {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    
    public static boolean checkStudentId(String id)
    {
        if (id.length() != 6) return false;
        for (int i = 0; i < 6; i++) 
            if (id.charAt(i) < '0' || id.charAt(i) > '9') 
                return false;
        return true;
    }
}
