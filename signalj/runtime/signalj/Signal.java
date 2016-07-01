/* Copyright (c) 2016, Tetsuo Kamina
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package signalj;

import java.util.Vector;
import java.util.function.Consumer;

public class Signal<T> {
    T value = null;
    T last = null;
    T sum = null;
    Vector<Consumer<T>> consumers = new Vector<Consumer<T>>();

    public Signal(T value) {
	this.value = value;
	this.last = value;
	this.sum = value;
    }

    public void set(T value) {
	last = this.value;
	if (sum instanceof Integer) {
	    Integer num = (Integer)sum;
	    num += (Integer)this.value;
	    sum = (T)num;
	} else if (sum instanceof Long) {
	    Long num = (Long)sum;
	    num += (Long)this.value;
	    sum = (T)num;
	} else if (sum instanceof Double) {
	    Double num = (Double)sum;
	    num += (Double)this.value;
	    sum = (T)num;
	} else if (sum instanceof Float) {
	    Float num = (Float)sum;
	    num += (Float)this.value;
	    sum = (T)num;
	}
	this.value = value;
	for (Consumer<T> c : consumers) {
	    c.accept(value);
	}
    }
    
    public T __signalj__get() {
	return value;
    }
    
    public void publish(Consumer<T> listener) {
	if (!consumers.contains(listener)) {
	    consumers.add(listener);
	}
    }
    
    public T last() {
	return last;
    }

    public T sum() {
	if (sum instanceof Number) return sum;
	else return value;
    }
}
