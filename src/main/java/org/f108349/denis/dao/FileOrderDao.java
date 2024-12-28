package org.f108349.denis.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.f108349.denis.dto.OrderDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileOrderDao {
    private final String filePath;
    private final ObjectMapper objectMapper;
    
    public FileOrderDao() {
        this("orders.json");
    }
    
    public FileOrderDao(String filePath) {
        this.objectMapper = new ObjectMapper();
        this.filePath = filePath;
    }

    public void exportOrdersToFile(List<OrderDto> orders) {
        try {
            this.objectMapper.writeValue(new File(this.filePath), orders);
            System.out.println("Orders exported successfully to " + this.filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to export orders to file", e);
        }
    }
    
    public List<OrderDto> importOrdersFromFile() {
        try {
            File file = new File(this.filePath);
            if (!file.exists()) {
                throw new RuntimeException("No backup file found at " + this.filePath);
            }
            
            return this.objectMapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to import orders from file", e);
        }
    }
}
