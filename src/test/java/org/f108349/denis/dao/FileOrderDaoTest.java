package org.f108349.denis.dao;

import org.f108349.denis.dto.OrderDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

public class FileOrderDaoTest {
    @TempDir
    private Path tempDir;
    
    @Test
    void testFileOrderDao_ShouldExportAndImportCorrectly() {
        // 1) Arrange: define some sample orders
        OrderDto order1 = new OrderDto();
        order1.setId("ID-001");
        order1.setItem("Office Supplies");
        order1.setDestination("Sofia");
        order1.setOrderDate(Date.valueOf(LocalDate.now()));
        order1.setTotalCost(BigDecimal.valueOf(300.00));

        OrderDto order2 = new OrderDto();
        order2.setId("ID-002");
        order2.setItem("Electronics");
        order2.setDestination("Plovdiv");
        order2.setOrderDate(Date.valueOf(LocalDate.now().plusDays(1)));
        order2.setTotalCost(BigDecimal.valueOf(550.75));

        List<OrderDto> originalOrders = Arrays.asList(order1, order2);

        // 2) Create a custom file path in the temp directory
        File tempFile = this.tempDir.resolve("test-orders.json").toFile();

        // 3) Create our DAO that uses the temp file path
        FileOrderDao fileOrderDao = new FileOrderDao(tempFile.getAbsolutePath());

        // 4) Act: Export
        fileOrderDao.exportOrdersToFile(originalOrders);

        // 5) Import
        List<OrderDto> importedOrders = fileOrderDao.importOrdersFromFile();

        // 6) Assert: The imported list should match the original
        assertNotNull(importedOrders, "Imported list should not be null");
        assertEquals(2, importedOrders.size(), "Should have 2 orders after import");

        // Check the first order
        OrderDto o1 = importedOrders.get(0);
        assertEquals("ID-001", o1.getId());
        assertEquals("Office Supplies", o1.getItem());
        assertEquals("Sofia", o1.getDestination());
        assertEquals(Date.valueOf(LocalDate.now()), o1.getOrderDate());
        assertEquals(0, BigDecimal.valueOf(300.00).compareTo(o1.getTotalCost()));

        // Check the second order
        OrderDto o2 = importedOrders.get(1);
        assertEquals("ID-002", o2.getId());
        assertEquals("Electronics", o2.getItem());
        assertEquals("Plovdiv", o2.getDestination());
        assertEquals(Date.valueOf(LocalDate.now().plusDays(1)), o2.getOrderDate());
        assertEquals(0, BigDecimal.valueOf(550.75).compareTo(o2.getTotalCost()));
    }
    
    @Test
    void testImportOrdersFromFile_whenFileDoesNotExist_shouldThrowRuntimeException() {
        // Arrange
        File nonExistentFile = this.tempDir.resolve("does-not-exist.json").toFile();
        FileOrderDao fileOrderDao = new FileOrderDao(nonExistentFile.getAbsolutePath());

        // Act & Assert
        assertThrows(RuntimeException.class, fileOrderDao::importOrdersFromFile);
    }
}
