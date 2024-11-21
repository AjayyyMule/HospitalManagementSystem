package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {



    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "root";

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner sc){
        System.out.println("Enter patient Id: ");
        int patientId = sc.nextInt();

        System.out.println("Enter Doctor Id:");
        int doctorId = sc.nextInt();

        System.out.println("Enter appointment date (YYYY-MM-DD)");
        String appointmentDate = sc.next();

        if(patient.getPatientById(patientId)&& doctor.getDoctorById(doctorId)){

            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {

                String appointmentQuery = "INSERT INTO appointments(patient_id, doctors_id, appointment_date) VALUES (?, ?, ?);";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);

                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows>0){
                        System.out.println("Approved appointment !!!");
                    }else {
                        System.out.println("Failed to book appointment ");
                    }

                }catch (SQLException e){
                    e.printStackTrace();
                }

            }else {
                System.out.println("Doctor is not available on this date ");
            }

        } else {
            System.out.println("Either doctor or patient doesnt exists!!!");
        }


    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctors_id = ? AND appointment_date = ? ;";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                int count = rs.getInt(1);
                if (count==0){
                    return true;
                }else {
                    return false;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }





    public static void main(String[] args) {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }


        Scanner sc = new Scanner(System.in);


        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection,sc );
            Doctor doctor = new Doctor(connection);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit" );
                System.out.println("Enter your choice: ");

                int choice = sc.nextInt();

                switch (choice){
                    case 1:
                        // Add Patient
                        patient.addPatient();
                        System.out.println();
                        break;


                    case 2:
                        //View Patients
                        patient.viewPatient();
                        System.out.println();
                        break;

                    case 3:
                        // View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;

                    case 4:
                        // Book Appointment
                        bookAppointment(patient, doctor, connection, sc);
                        System.out.println();
                        break;

                    case 5:
                        // Exit
                        System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM!!!!!!!");
                        return;
                    default:
                        //Default case
                        System.out.println("Please Enter valid choice ");
                        break;


                }


            }


        }catch (SQLException e){
            e.printStackTrace();
        }

    }


}
