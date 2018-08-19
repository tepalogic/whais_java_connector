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

import java.nio.ByteBuffer;
import java.security.Key;

import javax.crypto.spec.SecretKeySpec;



public class DESCipher extends net.whais.Client.PlainCipher
{
    @Override
    public byte type()
    {
        return _c.FRAME_ENCTYPE_DES;
    }

    @Override
    public int metadataSize()
    {
        return _c.FRAME_HDR_SIZE + _c.ENC_HDR_SIZE + _c.PLAIN_HDR_SIZE;
    }

    @Override
    public void encodeFrame( CommunicationFrame frame, Object key)
    {
        final ByteBuffer buffer = frame.getCmdBuffer();
        int bufferSize = frame.getLastPosition();
        int plainSize = bufferSize;

        buffer.position( bufferSize);
        while (bufferSize % 8 != 0) {
            buffer.put( (byte) (Math.random() * 1024));
            ++bufferSize;
            frame.markBufferPositionValid();
        }

        try {
            final javax.crypto.Cipher cipher = this.getChiper ();
            cipher.init (javax.crypto.Cipher.ENCRYPT_MODE, (Key) key);

            buffer.putShort( _c.FRAME_HDR_SIZE + _c.ENC_PLAIN_SIZE_OFF, (short) plainSize);

            final byte[] buff = buffer.array();
            cipher.doFinal (buff, _c.FRAME_HDR_SIZE, bufferSize - _c.FRAME_HDR_SIZE, buff, _c.FRAME_HDR_SIZE);

        } catch (Exception e) {
            e.printStackTrace ();
        }

        buffer.putShort (_c.FRAME_SIZE_OFF, (short) bufferSize);
    }

    @Override
    public void decodeFrame( CommunicationFrame frame, Object key)
    {
        final ByteBuffer buffer = frame.getCmdBuffer();

        assert (frame.getLastPosition () % 8) == 0;

        try {
            final javax.crypto.Cipher cipher = this.getChiper ();
            cipher.init (javax.crypto.Cipher.DECRYPT_MODE, (Key) key);

            final byte[] buff = buffer.array();
            cipher.doFinal (buff,
                            _c.FRAME_HDR_SIZE,
                            frame.getLastPosition() - _c.FRAME_HDR_SIZE, buff, _c.FRAME_HDR_SIZE);

        } catch (Exception e) {
            e.printStackTrace ();
        }

        int plainSize = buffer.getShort( _c.FRAME_HDR_SIZE + _c.ENC_PLAIN_SIZE_OFF);
        plainSize &= 0x0000FFFF;

        buffer.position( plainSize);
        frame.markBufferPositionValid();
    }

    public byte[] encode (byte[] key, byte[] buffer, int offset, int count)
    {
        try {
            final Object k = prepareKey (key);

            final javax.crypto.Cipher cipher = this.getChiper ();
            cipher.init (javax.crypto.Cipher.ENCRYPT_MODE, (SecretKeySpec) k);

            return cipher.doFinal (buffer, offset, count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] decode (byte[] key, byte[] buffer, int offset, int count)
    {
        try {
            final Object k = prepareKey (key);

            final javax.crypto.Cipher cipher = this.getChiper ();
            cipher.init (javax.crypto.Cipher.DECRYPT_MODE, (SecretKeySpec) k);

            return cipher.doFinal (buffer, offset, count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
