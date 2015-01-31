package net.whais.Client;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DESedeChiper extends DESCipher
{
    @Override
    public byte type()
    {
        return _c.FRAME_ENCTYPE_3DES;
    }

    @Override
    public Object prepareKey( byte[] key)
    {
        byte[] _key = new byte[24];
        for (int i = 0; i < _key.length; ++i)
            _key[i] = (i < key.length) ? key[i] : 0;

        try {
            return new SecretKeySpec (_key, "DESede");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected javax.crypto.Cipher getChiper ()
    {
        try {
            return Cipher.getInstance("DESede/ECB/NoPadding");
        }catch (Exception e) {
            e.printStackTrace ();
        }

        return null;
    }
}
