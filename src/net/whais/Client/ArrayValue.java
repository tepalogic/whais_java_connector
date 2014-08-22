package net.whais.Client;

import java.util.Vector;

public class ArrayValue extends Value
{
    public ArrayValue (Value[] values) throws ConnException
    {
        if (values == null)
        {
            this.values = null;
            return ;
        }
        
        this.values = new Vector<Value> ();
        for (int i = 0; i < values.length; ++i)
        {
            if ((values[i] == null) || values[i].isNull ())
                throw new ConnException (CmdResult.INVALID_ARGS, "An array should not have a null value.");
            
            else if ((i > 0) && ! values[i].type ().equals (values[i - 1].type ()))
                throw new ConnException (CmdResult.INVALID_ARGS, "All values of an array should be of the same type.");

            this.values.add (values[i]);
        }
    }
    
    public ArrayValue () throws ConnException
    {
        this (null);
    }
    
    public void add (Value v) throws ConnException
    {
        if (v.isNull ())
            throw new ConnException (CmdResult.INVALID_ARGS, "An array should not have a null value.");
        
        if (! v.type ().isBasic ()
            || v.type ().equals (ValueType.textType ()))
        {
            throw new ConnException (CmdResult.INVALID_ARGS, "An array can hold only basic values.");
        }
        
        if (this.values == null)
        {
            this.values = new Vector<Value> ();
            this.values.add (v);
            
            return ;
        }
        
        if ( ! this.values.get (0).type ().equals (v.type ()))
            throw new ConnException (CmdResult.INVALID_ARGS, "An array can hold only basic values.");
        
        this.values.add (v);
    }
    
    public Value get (int i)
    {
        if ((this.values == null) || (this.values.size () <= i))
            throw new ArrayIndexOutOfBoundsException (i);
        
        return values.get (i);
    }
    
    public Value remove (int i)
    {
        if ((this.values == null) || (this.values.size () <= i))
            throw new ArrayIndexOutOfBoundsException (i);
        
        return values.remove (i);
    }

    public Value[] toArray ()
    {
        if (isNull ())
            return null;
        
        return (Value[]) values.toArray ();
    }
    
    @Override
    public String toString ()
    {
        if (isNull ())
            return "";
        
        return values.toString ();
    }

    @Override
    public boolean isNull ()
    {
        return (this.values == null) || (this.values.size () == 0);
    }

    @Override
    public ValueType type () throws ConnException
    {
        if (this.isNull ())
            return ValueType.create (ValueType.ARRAY_MASK | ValueType.TYPE_NOTSET);
        
        return ValueType.create (this.values.get (0).type ().getTypeId() & ValueType.ARRAY_MASK);
    }
    
    private Vector<Value> values;
}
