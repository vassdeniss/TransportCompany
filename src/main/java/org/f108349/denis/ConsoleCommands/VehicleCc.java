package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.VehicleDao;
import org.f108349.denis.dto.VehicleDto;

import java.util.Scanner;

public class VehicleCc {
    public static void run(Scanner scanner) {
        System.out.println("\nPlease select an option:");
        System.out.println("1. Save Vehicle");
        System.out.println("2. Get Vehicle");
        System.out.println("3. Get All Vehicles");
        System.out.println("4. Update Vehicle");
        System.out.println("5. Delete Vehicle");
        System.out.println("6. Exit");
        System.out.print("Your choice: ");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                VehicleCc.saveVehicle(scanner);
                break;
            case "2":
                VehicleCc.getVehicle(scanner);
                break;
            case "3":
                VehicleCc.getAllVehicles();
                break;
            case "4":
                VehicleCc.updateVehicle(scanner);
                break;
            case "5":
                VehicleCc.deleteVehicle(scanner);
                break;
            case "6":
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }   
    }
    
    private static void saveVehicle(Scanner scanner) {
        System.out.println("\n--- Save Vehicle ---");
        System.out.print("Enter registration number: ");
        String registrationNumber = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        System.out.print("Enter capacity (kg): ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter vehicle type ID: ");
        String vehicleTypeId = scanner.nextLine();
        
        System.out.print("Enter company ID: ");
        String companyId = scanner.nextLine();

        VehicleDto vehicle = new VehicleDto(registrationNumber, model, capacity, companyId, vehicleTypeId);
        boolean saved = VehicleDao.saveVehicle(vehicle);
        if (saved) {
            System.out.println("Vehicle saved successfully.");
        }
    }

    private static void getVehicle(Scanner scanner) {
        System.out.println("\n--- Get Vehicle ---");
        System.out.print("Enter vehicle ID: ");
        String vehicleId = scanner.nextLine();

        VehicleDto vehicle = VehicleDao.getVehicleById(vehicleId);
        if (vehicle != null) {
            System.out.println(vehicle);
        } else {
            System.out.println("Vehicle not found with ID: " + vehicleId);
        }
    }
    
    private static void getAllVehicles() {
        System.out.println("\n--- Get All Vehicles ---");
        VehicleDao.getAllVehicles().forEach(System.out::println);
    }
    
    private static void updateVehicle(Scanner scanner) {
        System.out.println("\n--- Update Vehicle ---");
        System.out.print("Enter vehicle ID: ");
        String vehicleId = scanner.nextLine();

        VehicleDto vehicle = VehicleDao.getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found with ID: " + vehicleId);
            return;
        }

        System.out.println("Vehicle found: " + vehicle.getLicensePlate());
        System.out.println("Which field do you want to update?");
        System.out.println("1. Registration Plate");
        System.out.println("2. Model");
        System.out.println("3. Capacity");
        System.out.print("Your choice: ");
        String fieldChoice = scanner.nextLine();

        String newValue;
        switch (fieldChoice) {
            case "1":
                System.out.println("Current Registration Number: " + vehicle.getLicensePlate());
                System.out.print("Enter new Registration Number: ");
                newValue = scanner.nextLine();
                vehicle.setLicensePlate(newValue);
                break;
            case "2":
                System.out.println("Current Model: " + vehicle.getModel());
                System.out.print("Enter new Model: ");
                newValue = scanner.nextLine();
                vehicle.setModel(newValue);
                break;
            case "3":
                System.out.println("Current Capacity: " + vehicle.getCapacity());
                System.out.print("Enter new Capacity: ");
                newValue = scanner.nextLine();
                vehicle.setCapacity(Integer.parseInt(newValue));
                break;
            default:
                System.out.println("Invalid choice. No updates made.");
                return;
        }

        VehicleDao.updateVehicle(vehicle);
        System.out.println("Vehicle updated successfully.");
    }
    
    private static void deleteVehicle(Scanner scanner) {
        System.out.println("\n--- Delete Vehicle ---");
        System.out.print("Enter vehicle ID: ");
        String vehicleId = scanner.nextLine();

        VehicleDto vehicle = VehicleDao.getVehicleById(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found with ID: " + vehicleId);
            return;
        }
        
        VehicleDao.deleteVehicle(vehicleId);
        System.out.println("Vehicle deleted successfully.");
    }
}
