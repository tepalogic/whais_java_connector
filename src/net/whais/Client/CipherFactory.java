package net.whais.Client;


final class CipherFactory
{
    static PlainCipher plainCipher ()
    {

        if (cipherPlain == null) {
            cipherPlain = new PlainCipher ();
        }

        return cipherPlain;
    }

    static ThreeKingsCipher threeKingsCipher ()
    {

        if (cipher3K == null) {
            cipher3K = new ThreeKingsCipher ();
        }

        return cipher3K;
    }


    static PlainCipher      cipherPlain;
    static ThreeKingsCipher cipher3K;
}
