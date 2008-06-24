package org.crank.crud.controller;

import java.io.Serializable;
import java.util.List;

import org.crank.crud.QueryHint;

@SuppressWarnings("serial")
public class CrudManagedObject implements Serializable {
    private Class idType = Long.class;
    private Class entityType;
    private Class daoInterface;
    private String name;
    private boolean needsConverter;
    private boolean needsDropDownSupport;
    private boolean transactionalController;
    private String newSelect = null;
	private List<QueryHint<?>> queryHints;
    

    public List<QueryHint<?>> getQueryHints() {
		return queryHints;
	}
	public void setQueryHints(List<QueryHint<?>> queryHints) {
		this.queryHints = queryHints;
	}
	public String getNewSelect() {
		return newSelect;
	}
	public void setNewSelect(String newSelect) {
		this.newSelect = newSelect;
	}
	public CrudManagedObject () {
        
    }
    public CrudManagedObject (final Class entityType) {
    	this(entityType, null);
    }
	
    public CrudManagedObject (final Class entityType, final Class daoInterface) {
        this.entityType = entityType;
        this.daoInterface = daoInterface;
    }
    public CrudManagedObject (final Class entityType, final String name, final Class daoInterface) {
        this.entityType = entityType;
        this.name = name;
        this.daoInterface = daoInterface;
    }
    public CrudManagedObject (final Class entityType, final Class idType, final String name, final Class daoInterface) {
        this.entityType = entityType;
        this.idType = idType;
        this.name = name;
        this.daoInterface = daoInterface;
    }
    public String getName() {
        return name == null ? CrudUtils.getClassEntityName(entityType) : name; 
    }
    public void setName( String name ) {
        this.name = name;
    }
    public Class getEntityType() {
        return entityType;
    }
    public void setEntityType( Class entityType ) {
        this.entityType = entityType;
    }
    public Class getIdType() {
        return idType;
    }
    public void setIdType( Class idType ) {
        this.idType = idType;
    }
    public Class getDaoInterface() {
        return daoInterface;
    }
    public void setDaoInterface( Class daoInterface ) {
        this.daoInterface = daoInterface;
    }
    public boolean isNeedsConverter() {
        return needsConverter;
    }
    public void setNeedsConverter( boolean needsConverter ) {
        this.needsConverter = needsConverter;
    }
    public boolean isNeedsDropDownSupport() {
        return needsDropDownSupport;
    }
    public void setNeedsDropDownSupport( boolean needsDropDownSupport ) {
        this.needsDropDownSupport = needsDropDownSupport;
    }
	public boolean isTransactionalController() {
		return transactionalController;
	}
	public void setTransactionalController(boolean transactionalController) {
		this.transactionalController = transactionalController;
	}
}