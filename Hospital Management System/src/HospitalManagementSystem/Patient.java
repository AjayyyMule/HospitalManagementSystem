package HospitalManagementSystem;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner sc;


    public Patient(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

    public void addPatient(){
        System.out.println("Enter patient name: ");
        String name = sc.next();

        System.out.println("Enter patients age: ");
        int age = sc.nextInt();

        System.out.println("Enter patients gender: ");
        String gender = sc.next();

        try{
            String query = "INSERT INTO patients(name, age, gender)VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient added successfully!!!");

            }else{
                System.out.println("Failed to add patient");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatient(){
        String query = "SELECT * FROM patients;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("Patients: ");
            System.out.println("----------------------------------------------------------------");
            System.out.println("| Patient Id  | Name           |  Age         | Gender          ");
            System.out.println("----------------------------------------------------------------");

            while(rs.next()){
                int id = rs.getInt("id" );
                String name = rs.getString("name");
                String age = rs.getString("age");
                String gender = rs.getString("gender");
                System.out.printf("| %-11s | %-14s | %-12s | %-15s |", id,name,age,gender);
                System.out.println();

                System.out.println("----------------------------------------------------------------");

            }




        }catch (SQLException e){
            e.printStackTrace();
        }
    }






    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()){
                return true;

            }
            else{
                return false;
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }



}
