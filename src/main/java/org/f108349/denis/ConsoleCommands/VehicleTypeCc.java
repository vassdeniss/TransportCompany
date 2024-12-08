package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.VehicleTypeDao;
import org.f108349.denis.dto.VehicleTypeDto;

import java.util.Scanner;

public class VehicleTypeCc {
    public static void run(Scanner scanner) {
        VehicleTypeDao dao = new VehicleTypeDao();
        
        System.out.println("\nPlease select an option:");
        System.out.println("1. Save Vehicle Type");
        System.out.println("2. Get Vehicle Type");
        System.out.println("3. Get All Vehicle Types");
        System.out.println("4. Delete Vehicle Type");
        System.out.println("5. Exit");
        System.out.print("Your choice: ");
        
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                VehicleTypeCc.saveVehicleType(scanner, dao);
                break;
            case "2":
                VehicleTypeCc.getVehicleType(scanner, dao);
                break;
            case "3":
                VehicleTypeCc.getAllVehicleTypes(dao);
                break;
            case "4":
                VehicleTypeCc.deleteVehicleType(scanner, dao);
                break;
            case "5":
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }   
    }
    
    private static void saveVehicleType(Scanner scanner, VehicleTypeDao dao) {
        System.out.println("\n--- Save Vehicle Type ---");
        System.out.print("Enter type name: ");
        String typeName = scanner.nextLine();

        VehicleTypeDto vehicleType = new VehicleTypeDto(typeName);
        dao.saveVehicleType(vehicleType);
        System.out.println("Vehicle Type saved successfully.");
    }

    private static void getVehicleType(Scanner scanner, VehicleTypeDao dao) {
        System.out.println("\n--- Get Vehicle Type ---");
        System.out.print("Enter vehicle type ID: ");
        String vehicleTypeId = scanner.nextLine();

        VehicleTypeDto vehicleType = dao.getVehicleTypeByIdWhereNotDeleted(vehicleTypeId);
        if (vehicleType != null) {
            System.out.println(vehicleType);
        } else {
            System.out.println("Vehicle Type not found with ID: " + vehicleTypeId);
        }
    }
    
    private static void getAllVehicleTypes(VehicleTypeDao dao) {
        System.out.println("\n--- Get All Vehicle Types ---");
        dao.getAllVehicleTypesWhereNotDeleted().forEach(System.out::println);
    }
    
    private static void deleteVehicleType(Scanner scanner, VehicleTypeDao dao) {
        System.out.println("\n--- Delete Vehicle Type ---");
        System.out.print("Enter vehicle type ID: ");
        String vehicleTypeId = scanner.nextLine();

        VehicleTypeDto vehicleType = dao.getVehicleTypeByIdWhereNotDeleted(vehicleTypeId);
        if (vehicleType == null) {
            System.out.println("Vehicle Type not found with ID: " + vehicleTypeId);
            return;
        }
        
        try {
            dao.deleteVehicleType(vehicleTypeId);
            System.out.println("Vehicle Type deleted successfully.");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
