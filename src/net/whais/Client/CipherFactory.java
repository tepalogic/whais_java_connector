package net.whais.Client;

import net.whais.Client.PlainCipher;

final class CipherFactory
{
    static PlainCipher plainCipher ()
    {

        if (cipher == null) {
            cipher = new PlainCipher ();
        }

        return cipher;
    }

    static PlainCipher cipher;
}
