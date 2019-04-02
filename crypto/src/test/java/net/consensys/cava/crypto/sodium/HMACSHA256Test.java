/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.consensys.cava.crypto.sodium;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import net.consensys.cava.bytes.Bytes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HMACSHA256Test {
  @BeforeAll
  static void checkAvailable() {
    assumeTrue(Sodium.isAvailable(), "Sodium native library is not available");
  }

  @Test
  void testHmacsha256() {
    HMACSHA256.Key key = HMACSHA256.Key.random();
    Bytes authenticator = HMACSHA256.authenticate(Bytes.fromHexString("deadbeef"), key);
    assertTrue(HMACSHA256.verify(authenticator, Bytes.fromHexString("deadbeef"), key));
  }

  @Test
  void testHmacsha256InvalidAuthenticator() {
    HMACSHA256.Key key = HMACSHA256.Key.random();
    Bytes authenticator = HMACSHA256.authenticate(Bytes.fromHexString("deadbeef"), key);
    assertThrows(
        IllegalArgumentException.class,
        () -> HMACSHA256
            .verify(Bytes.concatenate(authenticator, Bytes.of(1, 2, 3)), Bytes.fromHexString("deadbeef"), key));
  }

  @Test
  void testHmacsha512NoMatch() {
    HMACSHA256.Key key = HMACSHA256.Key.random();
    Bytes authenticator = HMACSHA256.authenticate(Bytes.fromHexString("deadbeef"), key);
    assertFalse(HMACSHA256.verify(authenticator.reverse(), Bytes.fromHexString("deadbeef"), key));
  }
}
