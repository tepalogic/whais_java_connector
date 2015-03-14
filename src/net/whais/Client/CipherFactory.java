package net.whais.Client;

final class CipherFactory
{
    static PlainCipher plainCipher()
    {
        if (sCipherPlain == null)
            sCipherPlain = new PlainCipher();

        return sCipherPlain;
    }

    static ThreeKingsCipher threeKingsCipher()
    {
        if (sCipher3K == null)
            sCipher3K = new ThreeKingsCipher();

        return sCipher3K;
    }

    static DESCipher desCipher () {
        if (sCipherDES == null)
            sCipherDES = new DESCipher();

        return sCipherDES;
    }

    static DESCipher desedeCipher () {
        if (sCipher3DES == null)
            sCipher3DES = new DESedeChiper();

        return sCipher3DES;
    }

    static PlainCipher          sCipherPlain;
    static ThreeKingsCipher     sCipher3K;
    static DESCipher            sCipherDES;
    static DESedeChiper         sCipher3DES;
}
