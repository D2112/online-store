package com.epam.store.dao;

import com.epam.store.model.BaseEntity;

import java.util.List;
import java.util.Map;

public interface Dao<T extends BaseEntity> {

    /**
     * Inserts object into database
     *
     * @param object to insert
     * @return The same object, but with id from database
     */
    public T insert(T object);

    /**
     * Tries to find record with specified id
     *
     * @param id of the record
     * @return found object or null if nothing was found
     */
    public T find(long id);

    /**
     * Updates record in database with data from object
     *
     * @param object to be updated
     * @return true if record was successfully updated or false if not
     */
    public boolean update(T object);

    /**
     * Deletes record in database with specified id,
     * not deletes any related records
     *
     * @param id of the record to be deleted
     * @return true if record was successfully deleted or false if not
     */
    public boolean delete(long id);

    /**
     * Gets all records
     *
     * @return List of the found objects
     */
    public List<T> getAll();

    /**
     * Finds list of records with specified parameters.
     *
     * @param parameters Map of parameters to search, the string key is name of parameter,
     *                   and the Object is a value.
     * @return List of objects corresponding specified parameters.
     */
    public List<T> findByParameters(Map<String, Object> parameters);

    /**
     * Finds list of records with only one specified parameter
     *
     * @return List of the found objects
     */
    public List<T> findByParameter(String paramName, Object paramValue);

    /**
     * Finds list of records by corresponding specified parameter
     * and return only first object from list
     *
     * @return first found object or null if nothing was found
     */
    public T findFirstByParameter(String paramName, Object paramValue);
}
