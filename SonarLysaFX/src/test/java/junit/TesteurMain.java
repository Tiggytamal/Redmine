package junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import com.ibm.team.repository.common.TeamRepositoryException;

/**
 * Calsse permetant de faire des tests directement avec une entrée JAVA classique
 * 
 * @author ETP8137 - Grégoire Mathon
 * @since 1.0
 */
public class TesteurMain
{

    public static void main(String[] args) throws TeamRepositoryException, IllegalArgumentException, IllegalAccessException, IOException
    {
        StringBuilder builder = new StringBuilder("ETP8137");
        builder.append(":");
        builder.append("28H02m8903,;:!");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        builder = new StringBuilder("admin");
        builder.append(":");
        builder.append("admin");
        System.out.println(Base64.getEncoder().encodeToString(builder.toString().getBytes()));
        
        ProcessBuilder pb = new ProcessBuilder("D:\\mysql\\bin\\mysqld.exe", "--console"); //"D:\\mysql\\bin\\mysqladmin.exe", "-u", "root", "-pAQPadmin01", "ping"); // "D:\\mysql\\bin\\mysqladmin.exe", "-u", "root", "-pAQPadmin01", "shutdown");  //"D:\\mysql\\bin\\mysqld.exe", "--console"); // 
        
        try {
            Process pr = pb.start();
            BufferedReader errors = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            BufferedReader output = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            errors.lines().forEach(System.out::println);
            output.lines().forEach(System.out::println);
            System.out.println("exit " + pr.exitValue());

        } catch (IOException e) {
            System.err.println("Error running command: " + e.getMessage());
        }
        
    }
}
