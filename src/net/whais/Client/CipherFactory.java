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
