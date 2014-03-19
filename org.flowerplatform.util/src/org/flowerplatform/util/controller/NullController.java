package org.flowerplatform.util.controller;

/**
 * Works as an "ignore" controller.
 * 
 * <p>
 * When set as a controller with the lowest <code>orderIndex</code> in his list,
 * type descriptor will consider that it doesn't have any controllers registered for that type
 * and it will return <code>null</code> when calling {@link TypeDescriptor#getSingleController(String, Object)}.
 * 
 * <p>
 * Note: must be added ONLY as a single controller.
 * 
 * @see TypeDescriptor#getCachedSingleController(String, Object, boolean)
 * @author Cristina Constantinescu
 */
public class NullController extends AbstractController {

}
