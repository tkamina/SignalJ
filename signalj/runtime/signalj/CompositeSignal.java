/* Copyright (c) 2016-2017, Tetsuo Kamina
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
import java.util.Iterator;
import java.util.function.Consumer;

public class CompositeSignal<T> extends Signal<T> {
    private SignalInterface<T> object;
    private Vector<Signal<T>> sources = new Vector<Signal<T>>();

    public CompositeSignal(SignalInterface<T> object) {
	super(object.method());
	this.object = object;
	T memo = object.method();
	this.last = memo;
	this.sum = memo;
    }

    public CompositeSignal(SignalInterface<T> object, Signal<?>... signals) {
	this(object);
        for (Signal<?> s : signals) {
	    if (s instanceof CompositeSignal) {
		CompositeSignal cs = (CompositeSignal)s;
		for (Iterator<Signal<T>> iter = cs.sources.iterator();
		     iter.hasNext(); ) {
		    Signal<T> s1 = iter.next();
		    s1.publish(this);
		    sources.add(s1);
		}
	    } else {
		((Signal<T>)s).publish(this);
		sources.add((Signal<T>)s);		
	    }
        }
    }

    public T last() { return last; }
    public T sum() { return sum; }

    public void update(Signal<T> source) {
	last = value;
	value = object.method();
	sum = computeSumInner(sum, value);
	count++;
	for (Consumer<Signal<T>> c : consumers) {
	    c.accept(this);
	}
    }
}
