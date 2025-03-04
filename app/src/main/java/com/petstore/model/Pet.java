package com.petstore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Домашнее животное в магазине
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    private Long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private PetStatus status;
    
    public enum PetStatus {
        AVAILABLE,
        PENDING,
        SOLD
    }
} 