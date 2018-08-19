/**
 * Copyright 2016-2018 Iulian Popa (popaiulian@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package net.whais.Client;

/**
 * Used to describe a field of a WHAIS table.
 * <p>
 * For one to create a WHAIS table it needs to first identify its fields' names
 * and types and then to create an array with these objects to be given as a
 * parameter to {@link ValueType#create(TableFieldType[])}.
 *
 * @version 1.0
 *
 * @see Connection
 * @see ValueType
 */
public class TableFieldType implements Comparable<TableFieldType>
{
    /**
     * Instantiate a field description.
     *
     * @param name
     *            Table field's name.
     * @param type
     *            Table field's type..
     *
     * @throws ConnException
     *
     * @since 1.0
     */
    public TableFieldType(String name, ValueType type) throws ConnException
    {
        if ((name == null) || (name.length() == 0))
            throw new ConnException( CmdResult.INVALID_ARGS, "A field type needs to have a name.");
        else if (type == null)
            throw new ConnException( CmdResult.INVALID_ARGS, "Invalid field definition.");
        else if ((type.getBaseType() < ValueType.BOOL) || (type.getBaseType() >= ValueType.TYPE_NOTSET)) {
            throw new ConnException( CmdResult.INVALID_ARGS, "Invalid type for a field descriptor.");
        }

        mName = name;
        mType = type;

        if (mType.isField() || mType.isTable()) {
            throw new ConnException( CmdResult.INVALID_ARGS, "Invalid type used for field definition");
        }
    }

    /**
     * Returns a string representation of a WHAIS field table.
     */
    @Override
    public final String toString()
    {
        return getName() + " AS " + mType;
    }

    /**
     * Retrieve the name of the table field.
     *
     * @since 1.0
     */
    public final String getName()
    {
        return mName;
    }

    /**
     * Retrieve the type of a table field.
     *
     * @return
     *            An object describing the field type.
     *
     * @since 1.0
     */
    public final ValueType getType()
    {
        assert mType != null;

        return mType;
    }

    /*
     * Used internally to normalize the fields of a table.
     *
     * @deprecated Due to its internal usage this method should not be used.
     *
     * @since 1.0
     */
    @Override
    public int compareTo( TableFieldType o)
    {
        return mName.compareTo( o.mName);
    }

    private final String mName;
    private final ValueType mType;

}
