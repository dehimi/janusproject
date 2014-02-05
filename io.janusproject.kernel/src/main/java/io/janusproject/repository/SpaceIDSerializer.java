/*
 * Copyright 2014 Sebastian RODRIGUEZ, Nicolas GAUD, Stéphane GALLAND
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.janusproject.repository;

import io.sarl.lang.core.SpaceID;
import io.sarl.lang.core.SpaceSpecification;

import java.io.IOException;
import java.util.UUID;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

/**
 * @author $Author: srodriguez$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpaceIDSerializer implements StreamSerializer<SpaceID> {

	public static final int SPACE_ID_CLASS_TYPE = 19118;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTypeId() {
		return SPACE_ID_CLASS_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(ObjectDataOutput out, SpaceID object) throws IOException {
		out.writeObject(object.getContextID());
		out.writeObject(object.getID());
		out.writeUTF(object.getSpaceSpecification().getCanonicalName());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SpaceID read(ObjectDataInput in) throws IOException {
		try {
			UUID cid = in.readObject();
			UUID id = in.readObject();
			String specCls = in.readUTF();
			if (cid == null || id == null || specCls == null) {
				throw new IOException(String.format(
						"Cannot build SpaceID object with contextID=%s, ID=%s, SpecClass=%s", cid, id, specCls));
			}
			Class<? extends SpaceSpecification> spec = (Class<? extends SpaceSpecification>) Class.forName(specCls);
			SpaceID s = new SpaceID(cid, id, spec);
			return s;
		} catch (ClassNotFoundException e) {
			throw new IOException("Specification Class not found.", e);
		}

	}

}