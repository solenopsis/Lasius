/*
 * Copyright (C) 2017 Scot P. Floess
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.solenopsis.lasius.wsimport.common.util;

import java.lang.reflect.Method;

/**
 *
 * @author Scot P. Floess
 */
public interface PortObserver {
    Object prepare(Object proxy, Method method, Object[] args) throws Throwable;

    Object preInvoke(int totalCalls, Object proxy, Method method, Object[] args) throws Throwable;

    Object postInvoke(int totalCalls, Object proxy, Method method, Object[] args) throws Throwable;

    void failedInvoke(int totalCalls, Throwable failure, Object proxy, Method method, Object[] args) throws Throwable;
}
