package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.OrderDao;
import org.f108349.denis.dto.OrderDto;
import org.f108349.denis.entity.enums.Status;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Scanner;

public class OrderCc {
    public static void run(Scanner scanner) {
        OrderDao dao = new OrderDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Save Order", () -> saveOrder(scanner, dao));
        handler.addOption("2", "Get Order", () -> getOrder(scanner, dao));
        handler.addOption("3", "Get All Orders", () -> getAllOrders(dao));
        handler.addOption("4", "Update Order", () -> updateOrder(scanner, dao));
        handler.addOption("5", "Delete Order", () -> deleteOrder(scanner, dao));
        handler.addOption("6", "Back", () -> { });
        handler.run();
    }
    
    private static void saveOrder(Scanner scanner, OrderDao dao) {
        System.out.println("\n--- Save Order ---");
        Date orderDate = ConsoleUtils.promptDate(scanner, "Enter order date:");
        Date shipmentDate = ConsoleUtils.promptDate(scanner, "Enter shipment date:");
        String destination = ConsoleUtils.promptString(scanner, "Enter destination: ");
        int totalCost = ConsoleUtils.promptInt(scanner, "Enter cost: ");
        int totalWeight = ConsoleUtils.promptInt(scanner, "Enter weight: ");
        String customerId = ConsoleUtils.promptString(scanner, "Enter customer ID: ");
        String employeeId = ConsoleUtils.promptString(scanner, "Enter employee ID: ");
        String companyId = ConsoleUtils.promptString(scanner, "Enter company ID: ");
        String vehicleId = ConsoleUtils.promptString(scanner, "Enter vehicle ID: ");

        OrderDto order = new OrderDto(orderDate, shipmentDate, destination, new BigDecimal(totalCost), 
                new BigDecimal(totalWeight), customerId, employeeId, companyId, vehicleId, null);
        dao.saveOrder(order);
        System.out.println("Order saved successfully.");
    }

    private static void getOrder(Scanner scanner, OrderDao dao) {
        System.out.println("\n--- Get Order ---");
        String orderId = ConsoleUtils.promptString(scanner, "Enter order ID: ");
        
        OrderDto order = dao.getOrderByIdWhereNotDeleted(orderId);
        if (order != null) {
            System.out.println(order);
        } else {
            System.out.println("Order not found with ID: " + orderId);
        }
    }
    
    private static void getAllOrders(OrderDao dao) {
        System.out.println("\n--- Get All Orders ---");
        dao.getAllOrdersWhereNotDeleted().forEach(System.out::println);
    }
    
    private static void updateOrder(Scanner scanner, OrderDao dao) {
        System.out.println("\n--- Update Order ---");
        String orderId = ConsoleUtils.promptString(scanner, "Enter order ID: ");

        OrderDto order = dao.getOrderByIdWhereNotDeleted(orderId);
        if (order == null) {
            System.out.println("Order not found with ID: " + orderId);
            return;
        }

        System.out.println("Order found: " + order.getId());
        System.out.println("Which field do you want to update?");
        
        MenuHandler menuHandler = new MenuHandler(scanner);
        menuHandler.addOption("1", "Shipment Date", () -> {
            System.out.println("Current date: " + order.getShipmentDate());
            order.setShipmentDate(ConsoleUtils.promptDate(scanner, "Enter new date: "));
        });
        menuHandler.addOption("2", "Destination", () -> {
            System.out.println("Current destination: " + order.getDestination());
            order.setDestination(ConsoleUtils.promptString(scanner, "Enter new destination: "));
        });
        menuHandler.addOption("3", "Cost", () -> {
            System.out.println("Current cost: " + order.getTotalCost());
            order.setTotalCost(new BigDecimal(ConsoleUtils.promptInt(scanner, "Enter new cost: ")));
        });
        menuHandler.addOption("4", "Weight", () -> {
            System.out.println("Current weight: " + order.getTotalWeight());
            order.setTotalWeight(new BigDecimal(ConsoleUtils.promptInt(scanner, "Enter new weight: ")));
        });
        menuHandler.addOption("5", "Status", () -> {
            System.out.println("Current status: " + order.getStatus());
            order.setStatus(Status.valueOf(ConsoleUtils.promptString(scanner, "Enter new status: ")));
        });
        
        menuHandler.run();
        
        dao.updateOrder(order);
        System.out.println("Order updated successfully.");
    }
    
    private static void deleteOrder(Scanner scanner, OrderDao dao) {
        System.out.println("\n--- Delete Order ---");
        String orderId = ConsoleUtils.promptString(scanner, "Enter order ID: ");

        OrderDto order = dao.getOrderByIdWhereNotDeleted(orderId);
        if (order == null) {
            System.out.println("Order not found with ID: " + orderId);
            return;
        }
        
        dao.deleteOrder(orderId);
        System.out.println("Order deleted successfully.");
    }
}
