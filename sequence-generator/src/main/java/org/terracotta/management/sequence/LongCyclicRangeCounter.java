/*
 * Copyright Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.terracotta.management.sequence;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Counter which only goes from positives min to max values and cycle back to min value when max is reached
 *
 * @author Mathieu Carbou
 */
class LongCyclicRangeCounter {

  private final AtomicLong counter;
  private final long min;
  private final long max;

  LongCyclicRangeCounter(long min, long max) {
    this.min = min;
    this.max = max;
    if (min >= max) {
      throw new IllegalArgumentException("min >= maxValue");
    }
    if (min < 0) {
      throw new IllegalArgumentException("min < 0");
    }
    if (max <= 0) {
      throw new IllegalArgumentException("max <= 0");
    }
    counter = new AtomicLong(min);
  }

  long getAndIncrement() {
    for (; ; ) {
      long current = counter.get();
      long next = current == max ? min : current + 1;
      if (counter.compareAndSet(current, next)) {
        return current;
      }
    }
  }

}
