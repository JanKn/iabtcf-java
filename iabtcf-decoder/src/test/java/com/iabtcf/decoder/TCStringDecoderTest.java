package com.iabtcf.decoder;

/*-
 * #%L
 * IAB TCF Core Library
 * %%
 * Copyright (C) 2020 IAB Technology Laboratory, Inc
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import static org.junit.Assert.assertNotNull;

import com.iabtcf.utils.Base64;

import org.junit.Test;

import com.iabtcf.exceptions.ByteParseException;
import com.iabtcf.exceptions.UnsupportedVersionException;

public class TCStringDecoderTest {

    @Test
    public void testCanCreateModelFromTwoPartsString() {
        String tcString =
                "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA.IFoEUQQgAIQwgIwQABAEAAAAOIAACAIAAAAQAIAgEAACEAAAAAgAQBAAAAAAAGBAAgAAAAAAAFAAECAAAgAAQARAEQAAAAAJAAIAAgAAAYQEAAAQmAgBC3ZAYzUw";
        assertNotNull(TCStringDecoder.decode(tcString));
    }

    @Test
    public void testCanCreateModelOnePartString() {
        String tcString = "COtybn4PA_zT4KjACBENAPCIAEBAAECAAIAAAAAAAAAA";
        assertNotNull(TCStringDecoder.decode(tcString));
    }

    @Test(expected = UnsupportedVersionException.class)
    public void shouldFailIfANonSupportedVersionIsPassed() {
        String tcString = Base64.getUrlEncoder().encodeToString(new byte[] { 13 });
        TCStringDecoder.decode(tcString);
    }

    @Test(expected = ByteParseException.class)
    public void testReadBeyondBuffer() {
        // bit pattern: 0000011
        TCString tcString = TCString.decode("Bg");
        tcString.getCreated();
    }

    @Test(expected = ByteParseException.class)
    public void testStrict() {
        TCString.decode("CA==");
    }

    @Test
    public void testLazy() {
        TCString.decode("CA==", DecoderOption.LAZY);
    }

    @Test(expected = ByteParseException.class)
    public void testLazyFailure() {
        TCString.decode("CA==", DecoderOption.LAZY).getCmpId();
    }
}
