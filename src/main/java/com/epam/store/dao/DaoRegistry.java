package com.epam.store.dao;

import com.epam.store.metadata.AnnotationManager;
import com.epam.store.model.BaseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class which registry all classes marked
 * {@link com.epam.store.dao.DaoClass} annotation
 * and distributes them, if requested entity class which not registered
 * when default universal realization {@link com.epam.store.dao.JdbcDao}
 * will be given
 */
class DaoRegistry {
    private static final Class DEFAULT_DAO_IMPLEMENTATION_CLASS = JdbcDao.class;
    private Map<Class, DaoCreator> daoRegistry = new HashMap<>();

    public DaoRegistry() {
        Set<Class<?>> annotatedClasses = AnnotationManager.getAnnotatedClasses("com.epam.store.dao", DaoClass.class);
        for (Class<?> annotatedClass : annotatedClasses) {
            if (!Dao.class.isAssignableFrom(annotatedClass)) {
                throw new DaoException(
                        DaoClass.class.getSimpleName()
                                + " annotation used with class which is not implement interface " + Dao.class.getSimpleName());
            }
            DaoClass annotation = annotatedClass.getAnnotation(DaoClass.class);
            Class[] entityClasses = annotation.entityClasses();
            for (Class entityClass : entityClasses) {
                daoRegistry.put(entityClass, new DaoCreator(annotatedClass));
            }
        }
    }

    public DaoCreator get(Class<? extends BaseEntity> key) {
        DaoCreator daoCreator = daoRegistry.get(key); //get special dao if such present in registry
        if (daoCreator == null) {
            daoCreator = new DaoCreator(DEFAULT_DAO_IMPLEMENTATION_CLASS); //if not present, then use default dao
        }
        return daoCreator;
    }
}


