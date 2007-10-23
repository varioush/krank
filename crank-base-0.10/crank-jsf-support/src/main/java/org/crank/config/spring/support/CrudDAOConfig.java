package org.crank.config.spring.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.crank.crud.GenericDao;
import org.crank.crud.GenericDaoFactory;
import org.crank.crud.controller.CrudManagedObject;
import org.crank.crud.jsf.support.JsfCrudAdapter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.ExternalBean;
import org.springframework.config.java.util.DefaultScopes;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public abstract class CrudDAOConfig implements InitializingBean {

	private DataSource dataSource;

	private Properties jpaProperties;

	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@ExternalBean
	public abstract List<CrudManagedObject> managedObjects();

	@SuppressWarnings("unchecked")
	@ExternalBean
	public abstract Map<String, GenericDao> repos();

	@SuppressWarnings("unchecked")
	@ExternalBean
	public abstract Map<String, JsfCrudAdapter> cruds();

	/**
	 * Create gaggle of DAO objects.
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Bean(scope = DefaultScopes.SINGLETON, aliases = "repos")
	public Map<String, GenericDao> repositories() throws Exception {

		Map<String, GenericDao> repositories = new HashMap<String, GenericDao>();

		for (CrudManagedObject mo : managedObjects()) {
			GenericDao dao = createDao(mo.getDaoInterface(), mo.getEntityType());
			repositories.put(mo.getName(), dao);
		}

		return repositories;
	}

	/**
	 * Register EntityManager
	 * 
	 * @return
	 * @throws Exception
	 */
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryFactory()
			throws Exception {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryFactory
				.setPersistenceUnitName(persistenceUnitName());
		entityManagerFactoryFactory.setJpaVendorAdapter(jpaVendorAdapter());
		if (jpaProperties != null) {
			entityManagerFactoryFactory.setJpaProperties(jpaProperties);
		} else {
			entityManagerFactoryFactory.setJpaProperties(new Properties());
		}
		if (dataSource != null) {
			entityManagerFactoryFactory.setDataSource(dataSource);
		}

		entityManagerFactoryFactory.afterPropertiesSet();
		return entityManagerFactoryFactory;
	}

	@ExternalBean
	public abstract String persistenceUnitName();

	@ExternalBean
	public abstract JpaVendorAdapter jpaVendorAdapter();

	/**
	 * Create an entity manager factory.
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean(scope = DefaultScopes.SINGLETON)
	public EntityManagerFactory entityManagerFactory() throws Exception {
		return entityManagerFactoryFactory().getObject();
	}

	@Bean(scope = DefaultScopes.SINGLETON)
	public PlatformTransactionManager transactionManager() throws Exception {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory());
		return transactionManager;
	}

	@Bean(scope = DefaultScopes.SINGLETON)
	public TransactionInterceptor transactionInterceptor() throws Exception {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor(
				(PlatformTransactionManager) transactionManager(),
				new AnnotationTransactionAttributeSource());
		return transactionInterceptor;
	}

	@SuppressWarnings("unchecked")
	public GenericDao createDao(Class daoClass, Class entityClass)
			throws Exception {
		GenericDaoFactory genericDaoFactory = new GenericDaoFactory(
				transactionInterceptor());
		if (daoClass == null) {
			genericDaoFactory.setInterface(GenericDao.class);
		} else {
			genericDaoFactory.setInterface(daoClass);
		}
		genericDaoFactory.setBo(entityClass);
		genericDaoFactory.setEntityManagerFactory(entityManagerFactory());
		genericDaoFactory.afterPropertiesSet();
		return (GenericDao) genericDaoFactory.getObject();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setJpaProperties(Properties jpaProperties) {
		this.jpaProperties = jpaProperties;
	}

}
