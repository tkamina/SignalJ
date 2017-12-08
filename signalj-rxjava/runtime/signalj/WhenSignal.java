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

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.Flowable;

public class WhenSignal<T> extends CompositeSignal<T> {

    private Flowable<T> flowable;
    private Predicate<T> pred;

    public WhenSignal(Predicate<T> pred, T init, Signal<?> source, Flowable<T>... mappers) {
	super(source, mappers);
	this.pred = pred;
	super.setEffective();
        flowable = super.getFlowable().map(data->pred.test(data) ? data : super.last(init));
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

    public void publish(Consumer<T> cs) {
	flowable.subscribe(data -> { if (pred.test(data)) cs.accept(data); });
    }

}
