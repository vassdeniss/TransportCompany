package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.dao.VehicleTypeDao;
import org.f108349.denis.dto.VehicleTypeDto;

import java.util.Scanner;

public class VehicleTypeCc {
    public static void run(Scanner scanner) {
        VehicleTypeDao dao = new VehicleTypeDao();
        
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Save Vehicle Type", () -> saveVehicleType(scanner, dao));
        handler.addOption("2", "Get Vehicle Type", () -> getVehicleType(scanner, dao));
        handler.addOption("3", "Get All Vehicle Types", () -> getAllVehicleTypes(dao));
        handler.addOption("4", "Delete Vehicle Type", () -> deleteVehicleType(scanner, dao));
        handler.addOption("5", "Back", () -> { });
        
        handler.run();
    }
    
    private static void saveVehicleType(Scanner scanner, VehicleTypeDao dao) {
        System.out.println("\n--- Save Vehicle Type ---");
        ConsoleUtils utils = new ConsoleUtils(scanner);
        String typeName = utils.promptString("Enter type name: ");

        VehicleTypeDto vehicleType = new VehicleTypeDto(typeName);
        dao.saveVehicleType(vehicleType);
        System.out.println("Vehicle Type saved successfully.");
    }

    private static void getVehicleType(Scanner scanner, VehicleTypeDao dao) {
        System.out.println("\n--- Get Vehicle Type ---");
        ConsoleUtils utils = new ConsoleUtils(scanner);
        String vehicleTypeId = utils.promptString("Enter vehicle type ID: ");

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
        ConsoleUtils utils = new ConsoleUtils(scanner);
        String vehicleTypeId = utils.promptString("Enter vehicle type ID: ");

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
