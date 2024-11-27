package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.VehicleTypeDao;
import org.f108349.denis.dto.VehicleTypeDto;

import java.util.Scanner;

public class VehicleTypeCc {
    public static void run(Scanner scanner) {
        System.out.println("\nPlease select an option:");
        System.out.println("1. Save Vehicle Type");
        System.out.println("2. Get Vehicle Type");
        System.out.println("3. Get All Vehicle Types");
        System.out.println("4. Update Vehicle Type");
        System.out.println("5. Delete Vehicle Type");
        System.out.println("6. Exit");
        System.out.print("Your choice: ");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                VehicleTypeCc.saveVehicleType(scanner);
                break;
            case "2":
                VehicleTypeCc.getVehicleType(scanner);
                break;
            case "3":
                VehicleTypeCc.getAllVehicleTypes();
                break;
            case "4":
                VehicleTypeCc.updateVehicleType(scanner);
                break;
            case "5":
                VehicleTypeCc.deleteVehicleType(scanner);
                break;
            case "6":
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }   
    }
    
    private static void saveVehicleType(Scanner scanner) {
        System.out.println("\n--- Save Vehicle Type ---");
        System.out.print("Enter type name: ");
        String typeName = scanner.nextLine();

        VehicleTypeDto vehicleType = new VehicleTypeDto(typeName);
        VehicleTypeDao.saveVehicleType(vehicleType);
        System.out.println("Vehicle Type saved successfully.");
    }

    private static void getVehicleType(Scanner scanner) {
        System.out.println("\n--- Get Vehicle Type ---");
        System.out.print("Enter vehicle type ID: ");
        String vehicleTypeId = scanner.nextLine();

        VehicleTypeDto vehicleType = VehicleTypeDao.getVehicleTypeById(vehicleTypeId);
        if (vehicleType != null) {
            System.out.println(vehicleType);
        } else {
            System.out.println("Vehicle Type not found with ID: " + vehicleTypeId);
        }
    }
    
    private static void getAllVehicleTypes() {
        System.out.println("\n--- Get All Vehicle Types ---");
        VehicleTypeDao.getAllVehicleTypes().forEach(System.out::println);
    }
    
    private static void updateVehicleType(Scanner scanner) {
        System.out.println("\n--- Update Vehicle Type ---");
        System.out.print("Enter vehicle type ID: ");
        String vehicleTypeId = scanner.nextLine();

        VehicleTypeDto vehicleType = VehicleTypeDao.getVehicleTypeById(vehicleTypeId);
        if (vehicleType == null) {
            System.out.println("Vehicle Type not found with ID: " + vehicleTypeId);
            return;
        }

        System.out.println("Vehicle Type found: " + vehicleType.getTypeName());
        
        System.out.println("Current Type Name: " + vehicleType.getTypeName());
        System.out.print("Enter new Type Name: ");
        String newValue = scanner.nextLine();
        vehicleType.setTypeName(newValue);

        VehicleTypeDao.updateVehicleType(vehicleType);
        System.out.println("Vehicle Type updated successfully.");
    }
    
    private static void deleteVehicleType(Scanner scanner) {
        System.out.println("\n--- Delete Vehicle Type ---");
        System.out.print("Enter vehicle type ID: ");
        String vehicleTypeId = scanner.nextLine();

        VehicleTypeDto vehicleType = VehicleTypeDao.getVehicleTypeById(vehicleTypeId);
        if (vehicleType == null) {
            System.out.println("Vehicle Type not found with ID: " + vehicleTypeId);
            return;
        }
        
        VehicleTypeDao.deleteVehicleType(vehicleTypeId);
        System.out.println("Vehicle Type deleted successfully.");
    }
}
