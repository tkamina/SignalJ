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
import io.reactivex.functions.Consumer;

import io.reactivex.Flowable;

public class CompositeSignal<T> extends Signal<T> {

    private Flowable<T> flowable;

    private T or = null;

    private boolean orFlag = false;

    private Signal<?> source = null;

    public CompositeSignal(Signal<?> source, Flowable<T>... mappers) {
	super();
	this.source = source;

	Vector<Flowable<T>> v = new Vector<Flowable<T>>();
	for (Flowable<T> f : mappers) v.add(f);

	if (mappers.length == 1) {
	    flowable = mappers[0];
	} else {
	    flowable = source.getFlowable().merge(v);
	}
    }

    public CompositeSignal(boolean orFlag, Signal<?> source, Flowable<T>... mappers) {
	this(source, mappers);
    }

    public Flowable<T> getFlowable() {
	return flowable;
    }

    public T __signalj__get() {
	return flowable.blockingFirst();
    }

    public T value() {
	return flowable.blockingFirst();
    }

    /*
    public int count() {
	if (counter == null) {
	    counter = new Counter();
	    flowable.subscribe(counter::inc);
	    return 0;
	} else {
	    return counter.getValue();
	}
	//	return flowable.count().blockingGet().intValue();
    }
    */

    public void setEffective() {
	flowable.subscribe(this::setInnerValue);
    }

    public void publish(Consumer<T> cs) {
	flowable.subscribe(cs);
    }

    public void orInner(T arg) {
	System.out.println("OR: " + arg);
	or = arg; }

}
