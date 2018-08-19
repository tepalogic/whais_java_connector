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
