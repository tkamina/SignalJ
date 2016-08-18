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
    private T value = null;
    private T last = null;
    private T sum = null;
    protected Vector<Consumer<Signal<T>>> consumers = new Vector<Consumer<Signal<T>>>();
    private Vector<CompositeSignal<T>> danglingSignals = new Vector<CompositeSignal<T>>();

    public Signal(T value) {
	this.value = value;
	this.last = value;
	this.sum = value;
    }

    protected T computeSumInner(T sum, T value) {
	if (sum instanceof Integer) {
	    Integer num = (Integer)sum;
	    num += (Integer)value;
	    sum = (T)num;
	} else if (sum instanceof Long) {
	    Long num = (Long)sum;
	    num += (Long)value;
	    sum = (T)num;
	} else if (sum instanceof Float) {
	    Float num = (Float)sum;
	    num += (Float)value;
	    sum = (T)num;
	} else if (sum instanceof Double) {
	    Double num = (Double)sum;
	    num += (Double)value;
	    sum = (T)num;
	}
	return sum;
    }

    public void set(T value) {
	last = this.value;
	sum = computeSumInner(sum, value);
	this.value = value;

	for (Consumer<Signal<T>> c : consumers) {
	    c.accept(new Signal<T>(value));
	}

	for (CompositeSignal<T> s : danglingSignals) {
            s.update();
	}
    }
    
    public T __signalj__get() {
	return value;
    }

    public void publish(Consumer<Signal<T>> cs) {
	if (!consumers.contains(cs)) {
	    consumers.add(cs);
	}
    }

    public void publish(CompositeSignal<T> s) {
        if (!danglingSignals.contains(s)) {
	    danglingSignals.add(s);
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
