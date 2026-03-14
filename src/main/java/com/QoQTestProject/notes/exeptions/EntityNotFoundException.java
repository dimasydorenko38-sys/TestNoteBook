package com.QoQTestProject.notes.exeptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName, String id) {
        super(String.format("%s this ID :%s not found in data base", entityName, id));
    }
}
