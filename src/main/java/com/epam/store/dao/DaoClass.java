package com.epam.store.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which marks Dao class and contains
 * the array of the entity classes that can be given
 * by this dao class.
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
@interface DaoClass {
    Class[] entityClasses();
}
