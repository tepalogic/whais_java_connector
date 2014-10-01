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

    static PlainCipher sCipherPlain;
    static ThreeKingsCipher sCipher3K;
}
