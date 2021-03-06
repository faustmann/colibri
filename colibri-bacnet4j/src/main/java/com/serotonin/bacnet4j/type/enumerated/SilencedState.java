/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
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
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class SilencedState extends Enumerated {
    private static final long serialVersionUID = -9140683573905523375L;
    public static final SilencedState unsilenced = new SilencedState(0);
    public static final SilencedState audibleSilenced = new SilencedState(1);
    public static final SilencedState visibleSilenced = new SilencedState(2);
    public static final SilencedState allSilenced = new SilencedState(3);

    public SilencedState(int value) {
        super(value);
    }

    public SilencedState(ByteQueue queue) {
        super(queue);
    }
}
