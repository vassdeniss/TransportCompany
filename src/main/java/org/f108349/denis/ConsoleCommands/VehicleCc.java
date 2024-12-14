package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.VehicleDao;
import org.f108349.denis.dto.VehicleDto;

import java.util.Scanner;

public class VehicleCc {
    public static void run(Scanner scanner) {
        VehicleDao dao = new VehicleDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Save Vehicle", () -> saveVehicle(scanner, dao));
        handler.addOption("2", "Get Vehicle", () -> getVehicle(scanner, dao));
        handler.addOption("3", "Get All Vehicles", () -> getAllVehicles(dao));
        handler.addOption("4", "Update Vehicle", () -> updateVehicle(scanner, dao));
        handler.addOption("5", "Delete Vehicle", () -> deleteVehicle(scanner, dao));
        handler.addOption("6", "Back", () -> { });
        handler.run();
    }
    
    private static void saveVehicle(Scanner scanner, VehicleDao dao) {
        System.out.println("\n--- Save Vehicle ---");
        String licensePlate = ConsoleUtils.promptString(scanner, "Enter license plate: ");
        String model = ConsoleUtils.promptString(scanner, "Enter model: ");
        int capacity = ConsoleUtils.promptInt(scanner, "Enter capacity (kg): ");
        String vehicleTypeId = ConsoleUtils.promptString(scanner, "Enter vehicle type ID: ");
        String companyId = ConsoleUtils.promptString(scanner, "Enter company ID: ");

        VehicleDto vehicle = new VehicleDto(licensePlate, model, capacity, companyId, vehicleTypeId);
        boolean saved = dao.saveVehicle(vehicle);
        if (saved) {
            System.out.println("Vehicle saved successfully.");
        }
    }

    private static void getVehicle(Scanner scanner, VehicleDao dao) {
        System.out.println("\n--- Get Vehicle ---");
        String vehicleId = ConsoleUtils.promptString(scanner, "Enter vehicle ID: ");

        VehicleDto vehicle = dao.getVehicleByIdWhereNotDeleted(vehicleId);
        if (vehicle != null) {
            System.out.println(vehicle);
        } else {
            System.out.println("Vehicle not found with ID: " + vehicleId);
        }
    }
    
    private static void getAllVehicles(VehicleDao dao) {
        System.out.println("\n--- Get All Vehicles ---");
        dao.getAllVehiclesWhereNotDeleted().forEach(System.out::println);
    }
    
    private static void updateVehicle(Scanner scanner, VehicleDao dao) {
        System.out.println("\n--- Update Vehicle ---");
        String vehicleId = ConsoleUtils.promptString(scanner, "Enter vehicle ID: ");

        VehicleDto vehicle = dao.getVehicleByIdWhereNotDeleted(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found with ID: " + vehicleId);
            return;
        }
        
        System.out.println("Vehicle found: " + vehicle.getLicensePlate());
        System.out.println("Which field do you want to update?");
        
        MenuHandler menuHandler = new MenuHandler(scanner);
        menuHandler.addOption("1", "license plate", () -> {
            System.out.println("Current license plate: " + vehicle.getLicensePlate());
            vehicle.setLicensePlate(ConsoleUtils.promptString(scanner, "Enter new license plate: "));
        });
        menuHandler.addOption("2", "model", () -> {
            System.out.println("Current model: " + vehicle.getModel());
            vehicle.setModel(ConsoleUtils.promptString(scanner, "Enter new model: "));
        });
        menuHandler.addOption("3", "Capacity", () -> {
            System.out.println("Current capacity: " + vehicle.getCapacity());
            vehicle.setCapacity(ConsoleUtils.promptInt(scanner, "Enter new capacity: "));
        });
        menuHandler.addOption("4", "Company", () -> {
            System.out.println("Current company: " + vehicle.getCompany().getName());
            vehicle.setCompanyId(ConsoleUtils.promptString(scanner, "Enter new company ID: "));
        });
        menuHandler.addOption("5", "Vehicle Type", () -> {
            System.out.println("Current vehicle type: " + vehicle.getVehicleType().getTypeName());
            vehicle.setVehicleTypeId(ConsoleUtils.promptString(scanner, "Enter new vehicle type ID: "));
        });
        
        menuHandler.run();

        dao.updateVehicle(vehicle);
        System.out.println("Vehicle updated successfully.");
    }
    
    private static void deleteVehicle(Scanner scanner, VehicleDao dao) {
        System.out.println("\n--- Delete Vehicle ---");
        String vehicleId = ConsoleUtils.promptString(scanner, "Enter vehicle ID: ");

        VehicleDto vehicle = dao.getVehicleByIdWhereNotDeleted(vehicleId);
        if (vehicle == null) {
            System.out.println("Vehicle not found with ID: " + vehicleId);
            return;
        }
        
        dao.deleteVehicle(vehicleId);
        System.out.println("Vehicle deleted successfully.");
    }
}
