/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.color.HighlightingCompositeConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;

/**
 * Contains simple utility methods used to log audit messages.
 * 
 * <p>
 * The methods that perform the same tasks but have different signatures, 
 * are copy/paste of one another on purpose / for performance reasons. 
 * 
 * @author Cristi
 */
public final class LogUtil {
	
	private LogUtil() {
		
	}
	public static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
	/**
	 *@author Cristina Constantinescu
	 **/
	public static class FlowerHighlightingCompositeConverter extends HighlightingCompositeConverter {

		@Override
		protected String getForegroundColorCode(ILoggingEvent event) {
			if (event.getLevel().toInt() == Level.TRACE_INT) {
				return ANSIConstants.CYAN_FG;
			} else {
				return super.getForegroundColorCode(event);				
			}
		}
		
	}
	
	/**
	 *@author Cristina Constantinescu
	 **/
	public static class FlowerHighlightingCompositeConverterDefaultYellow extends HighlightingCompositeConverter {

		@Override
		protected String getForegroundColorCode(ILoggingEvent event) {
			// in XOPS, yellow is not visible on white background; although there is something fishy: the colors become vivide after init;
			// with the default "non-vivid" version, yellow would have been visible as well I guess
			return ANSIConstants.MAGENTA_FG;
		}
		
	}	
	
	private static IAuditAppender auditAppender;
	
	public static IAuditAppender getAuditAppender() {
		return auditAppender;
	}

	/**
	 * @author Mariana
	 */
	public static void setAuditAppender(IAuditAppender appender) {
		auditAppender = appender;
	}
	
	/**
	 * Append an audit message, using the {@link #auditAppender}. Also measures the duration since the
	 * moment the <code>auditDetails</code>.
	 * 
	 * @author Mariana
	 */
	public static void audit(AuditDetails auditDetails) {
		if (auditAppender != null) {
			auditDetails.measureDuration();
			auditAppender.append(auditDetails);
		}
	}
}