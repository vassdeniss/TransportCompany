package org.f108349.denis.ConsoleCommands;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dao.FileOrderDao;
import org.f108349.denis.dao.OrderDao;
import org.f108349.denis.dto.OrderDto;

import java.util.List;
import java.util.Scanner;

public class FileOrderCc {
    public static void run(Scanner scanner) {
        FileOrderDao fileDao = new FileOrderDao();
        OrderDao orderDao = new OrderDao(SessionFactoryUtil.getSessionFactory());
        MenuHandler handler = new MenuHandler(scanner);
        handler.addOption("1", "Export Orders", () -> exportOrdersToFile(fileDao, orderDao));
        handler.addOption("2", "Import Orders", () -> importOrdersFromFile(fileDao));
        handler.addOption("5", "Back", () -> { });
        handler.run();
    }
    
    private static void exportOrdersToFile(FileOrderDao fileDao, OrderDao orderDao) {
        List<OrderDto> orders = orderDao.getAllOrdersEagerWhereNotDeleted();
        fileDao.exportOrdersToFile(orders);
    }
    
    private static void importOrdersFromFile(FileOrderDao fileDao) {
        fileDao.importOrdersFromFile().forEach(System.out::println);
    }
}
