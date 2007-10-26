package org.crank.crud.jsf.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.crank.crud.controller.CrudController;
import org.crank.crud.controller.CrudControllerBase;
import org.crank.crud.controller.CrudControllerListener;
import org.crank.crud.controller.CrudEvent;
import org.crank.crud.controller.CrudOperations;
import org.crank.crud.controller.EntityLocator;
import org.crank.crud.controller.FilterablePageable;
import org.crank.crud.controller.FilteringEvent;
import org.crank.crud.controller.FilteringListener;
import org.crank.crud.controller.PaginationEvent;
import org.crank.crud.controller.PaginationListener;
import org.crank.crud.controller.Row;
import org.crank.crud.controller.ToggleEvent;
import org.crank.crud.controller.ToggleListener;
import org.crank.crud.controller.Toggleable;

/**
 * This class adpats a CrudController to the JSF world.
 * @author Rick Hightower
 *
 * @param <T> Type of entity that we are providing CRUD for.
 * @param <PK> Primary key type.
 */
public class JsfCrudAdapter<T, PK extends Serializable> implements EntityLocator, Serializable {
	private static final long serialVersionUID = 1L;
	private FilterablePageable paginator;
    private DataModel model = new ListDataModel();
    private CrudControllerBase<T, PK> controller;
    private List page;

    public JsfCrudAdapter() {
        
    }

    public JsfCrudAdapter(FilterablePageable filterablePageable, CrudController<T, PK > crudController) {
        this.paginator = filterablePageable;
        this.controller = crudController;
        crudController.setEntityLocator( this );
        
        
        
        /* Registers for events. */
        ((Toggleable)crudController).addToggleListener( new ToggleListener(){
            public void toggle( ToggleEvent event ) {
                JsfCrudAdapter.this.crudChanged();
            }} );
        
        this.paginator.addFilteringListener(new FilteringListener(){

			public void afterFilter(FilteringEvent fe) {
				getPage();
			}

			public void beforeFilter(FilteringEvent fe) {
			}});
        

        this.controller.addCrudControllerListener(new CrudControllerListener(){

			public void afterCancel(CrudEvent event) {
			}

			public void afterCreate(CrudEvent event) {
				getPage();
			}

			public void afterDelete(CrudEvent event) {
				getPage();
			}

			public void afterLoadCreate(CrudEvent event) {
			}

			public void afterLoadListing(CrudEvent event) {
			}

			public void afterRead(CrudEvent event) {
			}

			public void afterUpdate(CrudEvent event) {
				getPage();
			}

			public void beforeCancel(CrudEvent event) {
				FacesContext.getCurrentInstance().renderResponse();
			}

			public void beforeCreate(CrudEvent event) {
			}

			public void beforeDelete(CrudEvent event) {
			}

			public void beforeLoadCreate(CrudEvent event) {
			}

			public void beforeLoadListing(CrudEvent event) {
			}

			public void beforeRead(CrudEvent event) {
			}

			public void beforeUpdate(CrudEvent event) {
			}});
        
        this.paginator.addPaginationListener(new PaginationListener(){
			public void pagination(PaginationEvent pe) {
				getPage();
			}});
    }
    
    protected void getPage() {
    	page = paginator.getPage();
    }
    
    /**
     * @see EntityLocator#getEntity()
     */
    public Serializable getEntity() {
       return (Serializable) ((Row)model.getRowData()).getObject();
    }

    private void crudChanged() {
        paginator.reset();
    }

    public DataModel getModel() {
    	if (page == null) {
    		page = paginator.getPage();
    	}
        /* Note if you wire in events from paginators, you will only have to change this
         * when there is a next page event.
         */
        List<Row> wrappedList = new ArrayList<Row>(page.size());
        for (Object rowData : page) {
            Row row = new Row();
            row.setObject( rowData );
            wrappedList.add(row);
        }
        model.setWrappedData( wrappedList );
        return model;
    }

    public void setModel( DataModel model ) {
        this.model = model;
    }

    public CrudOperations getController() {
        return controller;
    }

    public FilterablePageable getPaginator() {
        return paginator;
    }

    @SuppressWarnings("unchecked")
    public List getSelectedEntities() {
        List<Row> list = (List<Row>) model.getWrappedData();
        List selectedList = new ArrayList(10);
        for (Row row : list){
            if (row.isSelected()) {
                selectedList.add( row.getObject() );
            }
        }
        return selectedList;
    }

}
